package org.irdresearch.smstarseel.handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.irdresearch.smstarseel.EmailEngine;
import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelGlobals;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.Device;
import org.irdresearch.smstarseel.data.InboundMessage;
import org.irdresearch.smstarseel.data.InboundMessage.InboundStatus;
import org.irdresearch.smstarseel.data.InboundMessage.InboundType;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.InboundSmsParams;
import org.irdresearch.smstarseel.global.RequestParam.OuboundSmsParams;
import org.irdresearch.smstarseel.global.RequestParam.RequestMendatoryParam;
import org.irdresearch.smstarseel.global.RequestParam.ResponseCode;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.global.SmsTarseelGlobal;
import org.irdresearch.smstarseel.global.SmsTarseelResponse;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;
import org.irdresearch.smstarseel.service.utils.ExceptionUtil;
import org.irdresearch.smstarseel.web.util.WebGlobals.OutBoundDeliveryError;
import org.irdresearch.smstarseel.web.util.WebGlobals.TarseelSetting;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmsHandler 
{
private static Date lastSmsCollectedErrorEmailDate = new Date(120000000000L);
private static Date lastSmsFetchedErrorEmailDate = new Date(120000000000L);

public static synchronized void revertUnknownSmses(String response){
	TarseelServices sc = TarseelContext.getServices();
	try{
		TarseelSetting maxTriesSet = TarseelSetting.OUTBOUND_MAX_RETRIES;
		int maxTries = Integer.parseInt(TarseelContext.getSetting(maxTriesSet.NAME(), maxTriesSet.DEFAULT().toString()));
		
		JSONObject json = new JSONObject(response);
		final JSONArray smslist = (JSONArray)json.get(OuboundSmsParams.LIST_ID.KEY());
		
		for (int i = 0; i < smslist.length(); i++)
		{
			final JSONObject sms = smslist.getJSONObject(i);
			OutboundMessage ob = sc.getSmsService().findOutboundById(sms.getLong(OuboundSmsParams.SMSID.KEY()));
			
			if(ob.getTries() == null || ob.getTries() <= maxTries){
				ob.setStatus(OutboundStatus.PENDING);
				ob.setErrormessage((ob.getErrormessage()==null?"":ob.getErrormessage())+OutBoundDeliveryError.REVERTED.ERROR_MESSAGE()+";");

				try{
					EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread("SMS REVERTED "+ob.getOutboundId(), "SMS REVERTED "+ob.getOutboundId());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{
				try{
					EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread("SMS NOT REVERTED "+ob.getOutboundId(), "SMS NOT REVERTED "+ob.getOutboundId()+" . Tries exceeded than limit "+maxTries);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			sc.getSmsService().updateOutbound(ob);
		}
		
		sc.commitTransaction();
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	finally{
		sc.closeSession();
	}
}
	public static synchronized void getPendingSmsTillNow(JSONObject request, HttpServletResponse resp) throws IOException, JSONException
	{
		String imei 	= (String) request.get(RequestMendatoryParam.IMEI.KEY());
		String sim 	= (String) request.get(RequestMendatoryParam.SIM.KEY());
		String projectName 	= (String) request.get(RequestMendatoryParam.PROJECT_REGISTERED.KEY());

		TarseelServices sc = TarseelContext.getServices();

		try{
		Device prjRegDev = SmsTarseelUtil.verifyDeviceProject(imei, sim, projectName, request.getString(App_Service.NAME), sc);
		
		if(prjRegDev == null){
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,"Error while finding device registered").jsonToString());
			return;
		}
		
		TarseelSetting maxTriesSet = TarseelSetting.OUTBOUND_MAX_RETRIES;
		int maxTries = Integer.parseInt(TarseelContext.getSetting(maxTriesSet.NAME(), maxTriesSet.DEFAULT().toString()));

		TarseelSetting maxDuplicateSet = TarseelSetting.SPAM_OUTBOUND_MAX_DUPLICATE_PER_RECP;
		int maxDuplicatesPerRecpPerDay = Integer.parseInt(TarseelContext.getSetting(maxDuplicateSet.NAME(), maxDuplicateSet.DEFAULT().toString()));

		TarseelSetting maxSmsSet = TarseelSetting.SPAM_OUTBOUND_MAX_SMS_PER_RECP;
		int maxSmsPerRecpPerDay = Integer.parseInt(TarseelContext.getSetting(maxSmsSet.NAME(), maxSmsSet.DEFAULT().toString()));

		TarseelSetting lostRetryIntervalMinSet = TarseelSetting.OUTBOUND_LOST_RETRY_INTERVAL_MIN;
		int lostRetryIntervalMin = Integer.parseInt(TarseelContext.getSetting(lostRetryIntervalMinSet.NAME(), lostRetryIntervalMinSet.DEFAULT().toString()));
		
		TarseelSetting failedRetryIntervalSet = TarseelSetting.OUTBOUND_FAILED_RETRY_INTERVAL_MIN;
		int failedRetryInterval = Integer.parseInt(TarseelContext.getSetting(failedRetryIntervalSet.NAME(), failedRetryIntervalSet.DEFAULT().toString()));
		
		////check if any recipient have been scheduled to receive more than 3 exactly similar smses for the day
		Session tempsc = TarseelContext.getNewSession();
		try{
			int runProc = tempsc.createSQLQuery("CALL OutboundCleanup("+prjRegDev.getProject().getProjectId()+","+maxDuplicatesPerRecpPerDay+","+maxSmsPerRecpPerDay+","+maxTries+","+lostRetryIntervalMin+","+failedRetryInterval+")").executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
			if(DateUtils.differenceBetweenIntervals(new Date(), lastSmsFetchedErrorEmailDate, TIME_INTERVAL.HOUR) > 1){
				EmailEngine.getInstance().emailErrorReportToAdmin(projectName+" : SmsTarseel: Error calling clean up procedure", projectName+" : SmsTarseel: Error calling clean up procedure: \n"+ExceptionUtil.getStackTrace(e));
				lastSmsFetchedErrorEmailDate = new Date();
			}
		}
		finally{
			try{
				tempsc.close();
			}
			catch (Exception e) {
				System.out.println("CLOSING MANUALLY RETRIEVED SESSION THREW EXCEPTION");
				e.printStackTrace();
			}
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		try
		{
			JSONArray smsList = new JSONArray();

			TarseelSetting maxFetchSet = TarseelSetting.OUTBOUND_FETCH_PER_GO;
			int maxFetch = Integer.parseInt(TarseelContext.getSetting(maxFetchSet.NAME(), maxFetchSet.DEFAULT().toString()));

			List<OutboundMessage> oml = sc.getSmsService().findPendingOutboundTillNow(projectName.trim(), true, maxFetch);
			for (OutboundMessage om : oml) {
				om.setSystemProcessingStartDate(new Date());

				TIME_INTERVAL interv = null;
				switch (om.getPeriodType()) {
				case DAY:
					interv = TIME_INTERVAL.DAY;
					break;
				case HOUR:
					interv = TIME_INTERVAL.HOUR;
					break;

				case WEEK:
					interv = TIME_INTERVAL.WEEK;
					break;
					
				default:
					break;
				}
				Date validDate = DateUtils.addInterval(om.getDueDate(), om.getValidityPeriod(), interv);
				if(validDate.before(new Date()))
				{
					om.setStatus(OutboundStatus.MISSED);
					om.setFailureCause((om.getFailureCause() == null ? "" : om.getFailureCause().trim())+"VALIDITY PERIOD PASSED;");
					om.setErrormessage((om.getErrormessage() == null ? "" : om.getErrormessage().trim())+"VALIDITY PERIOD PASSED;");
					om.setSystemProcessingEndDate(new Date());
				}
				else
				{
					om.setStatus(OutboundStatus.UNKNOWN);
					om.setImei(imei);
					om.setTries((om.getTries()==null?0:om.getTries())+1);

					JSONObject smsMap = new JSONObject();
					smsMap.put(OuboundSmsParams.CELL.KEY(), om.getRecipient());
					smsMap.put(OuboundSmsParams.SMSID.KEY(), Long.toString(om.getOutboundId()));
					smsMap.put(OuboundSmsParams.TEXT.KEY(), om.getText());
					smsMap.put(OuboundSmsParams.IS_SENT.KEY(), JSONObject.NULL);
					smsMap.put(OuboundSmsParams.SIM.KEY(), JSONObject.NULL);
					smsMap.put(OuboundSmsParams.SENT_DATE.KEY(), JSONObject.NULL);
					smsMap.put(OuboundSmsParams.DESC.KEY(), om.getDescription());
					smsMap.put(OuboundSmsParams.ERR_MSG.KEY(), om.getErrormessage());
					smsMap.put(OuboundSmsParams.FAIL_CAUSE.KEY(), om.getFailureCause());

					smsList.put(smsMap);
				}
				
				sc.getSmsService().updateOutbound(om);
			}
			
			sc.commitTransaction();

			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			json.addObjectList(OuboundSmsParams.LIST_ID.KEY(),smsList);
			
			try{
				if(!SmsTarseelUtil.sendResponse(resp, json.jsonToString())){
					throw new IOException("Sending response returned FALSE");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				SmsTarseelUtil.DBLOGGER.error("REVERTING SMSES STATUS"+ExceptionUtil.getStackTrace(e));
				System.out.println("REVERTING SMSES STATUS");
				if(smsList.length() > 0) SmsHandler.revertUnknownSmses(json.jsonToString());
			}
		}
		catch (DataException e)
		{
			e.printStackTrace();
			if(DateUtils.differenceBetweenIntervals(new Date(), lastSmsFetchedErrorEmailDate, TIME_INTERVAL.HOUR) > 1){
				EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error fetching sms", "SmsTarseel: Error fetching sms:"+e.getMessage());
				lastSmsFetchedErrorEmailDate = new Date();
			}
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,e.getMessage()).jsonToString());
		}
		}
		finally{
			sc.closeSession();
		}
	}
	public static synchronized void submitSmsSendAttemptResult(JSONObject request, HttpServletResponse resp) throws IOException, JSONException
	{
		TarseelServices sc = TarseelContext.getServices();

		try{
			JSONArray list = request.getJSONArray(OuboundSmsParams.LIST_ID.KEY());

			for (int i = 0; i < list.length(); i++)
			{
				JSONObject sms = new JSONObject();
				try{
					sms = list.optJSONObject(i);
					OutboundMessage ob = sc.getSmsService().findOutboundById(sms.getLong(OuboundSmsParams.SMSID.KEY()));
					if(ob == null){//ideally will never happen
						String respsms = "SMS: "+sms.toString();
						SmsTarseelUtil.DBLOGGER.error("SMS LOST FROM DB\n"+respsms);
					}
					else{
						try{
						ob.setErrormessage((ob.getErrormessage() == null ? "" : ob.getErrormessage().trim())+sms.getString(OuboundSmsParams.ERR_MSG.KEY())+";");
						}catch (Exception e) {
						}
						try{
						ob.setFailureCause((ob.getFailureCause() == null ? "" : ob.getFailureCause().trim()+"\n")+sms.getString(OuboundSmsParams.FAIL_CAUSE.KEY()));
						}catch (Exception e) {
						}
						ob.setOriginator(sms.getString(OuboundSmsParams.SIM.KEY()));
						try{
						ob.setSentdate(new SimpleDateFormat(SmsTarseelGlobal.DEFAULT_DATE_FORMAT).parse(sms.getString(OuboundSmsParams.SENT_DATE.KEY())));
						}catch(Exception e){
							e.printStackTrace();
						}
						ob.setStatus(sms.getBoolean(OuboundSmsParams.IS_SENT.KEY())?OutboundStatus.SENT:OutboundStatus.FAILED);
						ob.setSystemProcessingEndDate(new Date());
						
						sc.getSmsService().updateOutbound(ob);
					}
					
				}
				catch (Exception e) {
					e.printStackTrace();
					EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error logging device`s sms sent", "SmsTarseel: Error logging device`s sms sent:"+e.getMessage()+",SMS:"+sms.toString());
					SmsTarseelUtil.DBLOGGER.error(ExceptionUtil.getStackTrace(e));
				}
			}

			sc.commitTransaction();

			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			SmsTarseelUtil.sendResponse(resp, json.jsonToString());
		}
		finally{
			sc.closeSession();
		}
	}
	public static synchronized void submitRecivedSms(JSONObject request, HttpServletResponse resp) throws JSONException, IOException  {

		String imei 	= (String) request.get(RequestMendatoryParam.IMEI.KEY());
		String sim 	= (String) request.get(RequestMendatoryParam.SIM.KEY());
		String projectName 	= (String) request.get(RequestMendatoryParam.PROJECT_REGISTERED.KEY());

		TarseelServices sc = TarseelContext.getServices();
		try{
			Device prjRegDev = SmsTarseelUtil.verifyDeviceProject(imei, sim, projectName, request.getString(App_Service.NAME), sc);
			
			if(prjRegDev == null){
				SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,"Error while finding device registered").jsonToString());
				return;
			}
			
			JSONArray list = request.getJSONArray(InboundSmsParams.LIST_ID.KEY());
		
			for (int i = 0; i < list.length(); i++)
			{
				JSONObject sms = new JSONObject();
				try{
					sms = list.optJSONObject(i);
					InboundMessage ib = new InboundMessage();
					ib.setImei(imei);
					ib.setOriginator((String) sms.get(InboundSmsParams.SENDER_NUM.KEY()));
					
					ib.setProjectId(prjRegDev.getProject().getProjectId());
					ib.setReferenceNumber(TarseelGlobals.getUniqueSmsId(prjRegDev.getProject().getProjectId()));
					
					Date recvDate = org.irdresearch.smstarseel.global.DateUtils.parseRequestDate((String) sms.get(InboundSmsParams.RECIEVED_DATE.KEY()));
					ib.setRecieveDate(recvDate);
					
					ib.setRecipient(sim);
					ib.setStatus(InboundStatus.UNREAD);//should be unread until the application (whome it is meant for) doesnot mark it read
					
					ib.setSystemProcessingStartDate(org.irdresearch.smstarseel.global.DateUtils.parseRequestDate((String) sms.get(InboundSmsParams.SYSTEM_PROCESS_START_DATE.KEY())));
					
					//ib.setSystemRecieveDate(null);
					ib.setText((String) sms.get(InboundSmsParams.TEXT.KEY()));
					
					ib.setType(InboundType.SMS);
	
					ib.setSystemProcessingEndDate(new Date());
	
					sms.remove(InboundSmsParams.SYSTEM_PROCESS_START_DATE.KEY());
					sms.remove(InboundSmsParams.TEXT.KEY());
					sms.remove(InboundSmsParams.SENDER_NUM.KEY());
					
					sc.getSmsService().saveInbound(ib);
	
					/// never delete it : it is notify that inbound sms has been saved
					sms.put(InboundSmsParams.IS_SAVED.KEY(), true);
				}
				catch (Exception e) {
					e.printStackTrace();
					if(DateUtils.differenceBetweenIntervals(new Date(), lastSmsCollectedErrorEmailDate, TIME_INTERVAL.HOUR) > 1){
						EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error logging device`s sms collected", "SmsTarseel: Error logging device`s sms collected:"+e.getMessage()+", EX:"+sms.toString());
						lastSmsCollectedErrorEmailDate = new Date();
					}
				}
			}
			
			sc.commitTransaction();

			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			json.addObjectList(InboundSmsParams.LIST_ID.KEY(), list);
			
			SmsTarseelUtil.sendResponse(resp, json.jsonToString());
		}
		finally{
			sc.closeSession();
		}
	}
}
