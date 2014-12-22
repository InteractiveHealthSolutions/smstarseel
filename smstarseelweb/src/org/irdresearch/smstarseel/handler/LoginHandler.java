package org.irdresearch.smstarseel.handler;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.SystemPermissions;
import org.irdresearch.smstarseel.context.ActiveDevice;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.Device;
import org.irdresearch.smstarseel.data.Device.DeviceStatus;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.global.RequestParam.DeviceRegisterParam;
import org.irdresearch.smstarseel.global.RequestParam.LoginRequest;
import org.irdresearch.smstarseel.global.RequestParam.LoginResponse;
import org.irdresearch.smstarseel.global.RequestParam.RequestMendatoryParam;
import org.irdresearch.smstarseel.global.RequestParam.ResponseCode;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.global.SmsTarseelGlobal;
import org.irdresearch.smstarseel.global.SmsTarseelResponse;
import org.irdresearch.smstarseel.service.UserServiceException;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginHandler {

	public LoginHandler() {

	}
	
	public static void handleDeviceLogin(JSONObject request, HttpServletResponse resp) throws JSONException, IOException {
		String username = (String) request.get(LoginRequest.USERNAME.KEY());
		String password = (String) request.get(LoginRequest.PASSWORD.KEY());
		String imei 	= (String) request.get(RequestMendatoryParam.IMEI.KEY());
		String date 	= (String) request.get(RequestMendatoryParam.DATE.KEY());
		
		try
		{
			int datediff = DateUtils.differenceBetweenIntervals(new Date(), new SimpleDateFormat(SmsTarseelGlobal.DEFAULT_DATE_FORMAT).parse(date), TIME_INTERVAL.DATE);
			if(datediff < -3 || datediff > 3){
				SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.INVALID_PHONE_DATE,"").jsonToString());
				return;
			}
		}
		catch (ParseException e2)
		{
			e2.printStackTrace();
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.INVALID_PHONE_DATE_ERROR,e2.getMessage()).jsonToString());
			return;
		}

		try
		{
			String message = "";
			
			User user = TarseelContext.getAuthenticatedUser(username, password) ;

			if(!user.hasPermission(SystemPermissions.DEVICE_OPEARTIONS.toString())){
				throw new UserServiceException(UserServiceException.PERMISSION_UNGRANTED, UserServiceException.PERMISSION_UNGRANTED);
			}
			
			message += "\nUser : " + username;
			message += "\nImei : " + imei;

			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			json.addElement(LoginResponse.DETAILS.KEY(), message);
			
			SmsTarseelUtil.sendResponse(resp, json.jsonToString());
		}
		catch (UserServiceException e)
		{
			e.printStackTrace();
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.INVALID_USER,e.getMessage()).jsonToString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,e.getMessage()).jsonToString());
		}
	}
	
	public static void registerDevice(JSONObject request, HttpServletResponse resp) throws JSONException, IOException 
	{
		String imei = (String) request.get(RequestMendatoryParam.IMEI.KEY());
		String sim = (String) request.get(RequestMendatoryParam.SIM.KEY());
		String project = (String) request.get(DeviceRegisterParam.PROJECT_NAME.KEY());
		
		Project prj = null;
		TarseelServices sc = TarseelContext.getServices();

		try{
			String message = "";
			
			for (Device dd : sc.getDeviceService().findDevice(imei, DeviceStatus.ACTIVE, false, null, null, 0, Integer.MAX_VALUE)) {
				dd.setStatus(DeviceStatus.DISCARDED);
				
				sc.getDeviceService().updateDevice(dd);
			}
			
			prj = sc.getDeviceService().findProject(project).get(0);
			
			Device d = new Device();
			d.setDateAdded(new Date());
			d.setDeviceName(imei);
			d.setImei(imei);
			d.setProject(prj);
			d.setSim(sim);
			d.setStatus(DeviceStatus.ACTIVE);
			
			sc.getDeviceService().saveDevice(d);
			
			sc.commitTransaction();
			
			message += "\nImei    : " + imei;
			message += "\nSim     : " + sim;
			message += "\nProject : " + project;
			
			TarseelContext.ACTIVE_DEVICES.put(imei, new ActiveDevice(imei));
			
			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			json.addElement("details", message);
			
			SmsTarseelUtil.sendResponse(resp, json.jsonToString());
		}
		catch (DataException e)
		{
			e.printStackTrace();
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,e.getMessage()).jsonToString());
		}
		finally{
			sc.closeSession();
		}
	}
}
