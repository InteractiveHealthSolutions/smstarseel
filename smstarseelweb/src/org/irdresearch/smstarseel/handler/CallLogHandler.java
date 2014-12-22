package org.irdresearch.smstarseel.handler;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.EmailEngine;
import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelGlobals;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.CallLog;
import org.irdresearch.smstarseel.data.CallLog.CallStatus;
import org.irdresearch.smstarseel.data.CallLog.CallType;
import org.irdresearch.smstarseel.data.Device;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.CallLogParams;
import org.irdresearch.smstarseel.global.RequestParam.RequestMendatoryParam;
import org.irdresearch.smstarseel.global.RequestParam.ResponseCode;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.global.SmsTarseelResponse;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CallLogHandler {
	private static Date lastCallSubmitErrorEmailDate = new Date(120000000000L);

	public static synchronized void submitCallLog(JSONObject request, HttpServletResponse resp) throws JSONException, IOException {
		String imei 	= (String) request.get(RequestMendatoryParam.IMEI.KEY());
		String sim 	= (String) request.get(RequestMendatoryParam.SIM.KEY());
		String projectName 	= (String) request.get(RequestMendatoryParam.PROJECT_REGISTERED.KEY());
		JSONArray list = request.getJSONArray(CallLogParams.LIST_ID.KEY());

		TarseelServices sc = TarseelContext.getServices();

		Device prjRegDev = SmsTarseelUtil.verifyDeviceProject(imei, sim, projectName, request.getString(App_Service.NAME), sc);
		
		if(prjRegDev == null){
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,"Error while finding device registered").jsonToString());
			return;
		}
		
		try{
			for (int i = 0; i < list.length(); i++)
			{
				JSONObject calljson = new JSONObject();
				try{
					calljson = list.optJSONObject(i);
					
					CallLog call = new CallLog();
					call.setImei(imei);
					call.setCallerNumber((String) calljson.get(CallLogParams.CALLER_NUM.KEY()));
					call.setProjectId(prjRegDev.getProject().getProjectId());
					
					Date callDate = org.irdresearch.smstarseel.global.DateUtils.parseRequestDate((String) calljson.get(CallLogParams.CALL_DATE.KEY()));
					call.setCallDate(callDate);
					
					call.setRecipient(sim);
					call.setReferenceNumber(TarseelGlobals.getUniqueSmsId(prjRegDev.getProject().getProjectId()));
					call.setCallStatus(CallStatus.UNREAD);
					call.setSystemProcessingStartDate(org.irdresearch.smstarseel.global.DateUtils.parseRequestDate((String) calljson.get(CallLogParams.SYSTEM_PROCESS_START_DATE.KEY())));
					
					call.setDurationInSec(Integer.parseInt((String) calljson.get(CallLogParams.DURATION.KEY())));
					
					String type = (String) calljson.get(CallLogParams.TYPE.KEY());
					if(type.toLowerCase().indexOf("missed") != -1){
						call.setCallType(CallType.MISSED);
					}
					else if(type.toLowerCase().indexOf("incoming") != -1){
						call.setCallType(CallType.INCOMING);
					}
					else if(type.toLowerCase().indexOf("outgoing") != -1){
						call.setCallType(CallType.OUTGOING);
					}
					call.setSystemProcessingEndDate(new Date());
					
					calljson.remove(CallLogParams.CALLER_NUM.KEY());
					calljson.remove(CallLogParams.CALL_DATE.KEY());
					calljson.remove(CallLogParams.SYSTEM_PROCESS_START_DATE.KEY());
					calljson.remove(CallLogParams.DURATION.KEY());
	
					sc.getCallService().saveCallLog(call);
	
					///TODO never delete it : it is notify that call log has been saved
					calljson.put(CallLogParams.IS_SAVED.KEY(), true);
				}
				catch (Exception e) {
					e.printStackTrace();
					if(DateUtils.differenceBetweenIntervals(new Date(), lastCallSubmitErrorEmailDate, TIME_INTERVAL.HOUR) > 1){
						EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error logging device`s calls collected", "SmsTarseel: Error logging device`s calls collected:"+e.getMessage()+", EX:"+calljson.toString());
						lastCallSubmitErrorEmailDate = new Date();
					}
					SmsTarseelUtil.FILELOGGER.error(CallLogHandler.class.getName(), e);
				}
			}
			
			sc.commitTransaction();

			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			json.addObjectList(CallLogParams.LIST_ID.KEY(), list);
			
			SmsTarseelUtil.sendResponse(resp, json.jsonToString());
		}
		finally{
			sc.closeSession();
		}
	}
}
