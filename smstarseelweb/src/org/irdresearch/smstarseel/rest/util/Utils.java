package org.irdresearch.smstarseel.rest.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.data.ServiceLog;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundType;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;
import org.irdresearch.smstarseel.data.ServiceLog.ServiceLogStatus;
import org.irdresearch.smstarseel.rest.telenor.TelenorContext;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;
import com.sun.swing.internal.plaf.synth.resources.synth;

public class Utils {
	public static final String GLOBAL_DATE_FORMAT = "dd-MM-yyyy";
	public static final SimpleDateFormat GLOBAL_SDF = new SimpleDateFormat("dd-MM-yyyy");
	public static final String GLOBAL_DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
	public static final SimpleDateFormat GLOBAL_SDTF = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	
	public static void createErrorResponse(String error, Map<String, Object> responseMap) {
		responseMap.put("ERROR", true);
		responseMap.put("ERROR_MESSAGE", error);
	}
	
	public static void createErrorResponse(String error, HttpServletResponse response) {
		try {
			response.getWriter().write(new JSONObject()
								.put("ERROR", true)
								.put("ERROR_MESSAGE", error).toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static Map<String, Object> createMissingParamErrorResponse(String param, Map<String, Object> responseMap) {
		if(responseMap == null){
			responseMap = new HashMap<String, Object>();
		}
		responseMap.put("ERROR", true);
		responseMap.put("ERROR_MESSAGE", "No param named as '"+param+"' found");
		return responseMap;
	}
	
	public static Map<String, Object> createMissingSettingErrorResponse(String setting, Map<String, Object> responseMap) {
		if(responseMap == null){
			responseMap = new HashMap<String, Object>();
		}
		responseMap.put("ERROR", true);
		responseMap.put("ERROR_MESSAGE", "No setting named as '"+setting+"' configured");
		return responseMap;
	}
	
	public static String createUrl(String... urlParts) {
		String url = "";
		for (String u : urlParts) {
			//append ending and trailing slashes in all parts and append manually
			url += "/"+HttpUtil.removeTrailingSlash(HttpUtil.removeEndingSlash(u))+"/";
		}
		//remove ending and trailing slashes in very start and end
		url = HttpUtil.removeTrailingSlash(HttpUtil.removeEndingSlash(url));
		return url;
	}
	
	public static void createTelenorResponse(HttpResponse response, Map<String, Object> responseMap) {
		try {
			Map<String, Object> mapResp = new Gson().fromJson(XML.toJSONObject(response.body()).getJSONObject("corpsms").toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
		
			if(mapResp.get("response").toString().equalsIgnoreCase("ok")){
				responseMap.put("SUCCESS", true);
			}
			else {
				responseMap.put("ERROR", true);
				responseMap.put("ERROR_MESSAGE", mapResp.get("data"));
			}
			
			responseMap.putAll(mapResp);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
	
	public static void createITSResponse(HttpResponse response, Map<String, Object> responseMap) {
		try {
			Map<String, Object> mapResp = new Gson().fromJson(XML.toJSONObject(response.body())
					.getJSONObject("response").getJSONObject("data").getJSONObject("acceptreport").toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
		
			if(new Double(mapResp.get("statuscode").toString()).intValue() == 0){
				responseMap.put("SUCCESS", true);
			}
			else {
				responseMap.put("ERROR", true);
				responseMap.put("ERROR_MESSAGE", mapResp.get("statusmessage"));
			}
			
			responseMap.putAll(mapResp);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
	
	public static ServiceLog createServiceLog(int delayInMin, boolean nativeService, int projectId, 
			String serviceUrl, String serviceMethod, String authKey, Map<String, String> extras) {
		ServiceLog sl = new ServiceLog();
		sl.setDateAdded(new Date());
		sl.setDateDue(DateTime.now().plusMinutes(delayInMin).toDate());
		sl.setRetries(0);
		//sl.setDescription(description);
		sl.setNativeService(nativeService);
		sl.setAuthKey(authKey);
		sl.setProject(new Project(projectId));
		sl.setServiceUrl(serviceUrl);
		sl.setServiceMethod(serviceMethod);
		sl.setStatus(ServiceLogStatus.PENDING);
		sl.setExtras(extras);
		return sl;
	}
	
	public static synchronized OutboundMessage createOutbound(Date duedate, String imei, String from, String to, String text, Date sentdate,
			int validityInDays, int projectId, Date processingStartDate, OutboundStatus status, 
			String referenceNumber, Map<String, String> extras) {
		OutboundMessage om = new OutboundMessage();
		om.setCreatedDate(new Date());
		om.setDueDate(duedate);
		//om.setErrormessage(errormessage);
		//om.setFailureCause(failureCause);
		om.setImei(imei);
		om.setOriginator(from);
		om.setRecipient(to);
		om.setText(text);
		om.setTries(0);
		om.setSentdate(sentdate);
		om.setStatus(status);
		om.setPeriodType(PeriodType.DAY);
		om.setValidityPeriod(validityInDays);
		om.setPriority(Priority.HIGH);
		om.setProjectId(projectId);
		om.setReferenceNumber(referenceNumber);
		//om.setSystemProcessingEndDate(systemProcessingEndDate);
		om.setSystemProcessingStartDate(processingStartDate);
		//om.setDescription(description);
		om.setType(OutboundType.SMS);
		om.setExtras(extras);
		return om;
	}
	
	public static String getStringFilter(String filter, HttpServletRequest req)
	{
	  return StringUtils.isEmptyOrWhitespaceOnly(req.getParameter(filter)) ? null : req.getParameter(filter);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Enum getEnumFilter(String filter, Class cls, HttpServletRequest req)
	{
	  String filterVal = getStringFilter(filter, req);
	  if (filterVal != null) {
	    return Enum.valueOf(cls, filterVal);
	  }
	  return null;
	}
	
	public static Boolean getBooleanFilter(String filter, HttpServletRequest req)
	{
	  String strval = getStringFilter(filter, req);
	  return strval == null ? null : Boolean.parseBoolean(strval);
	}
	
	public static Integer getIntegerFilter(String filter, HttpServletRequest req)
	{
	  String strval = getStringFilter(filter, req);
	  return strval == null ? null : Integer.parseInt(strval);
	}
	
	public static Float getFloatFilter(String filter, HttpServletRequest req)
	{
	  String strval = getStringFilter(filter, req);
	  return strval == null ? null : Float.parseFloat(strval);
	}
	
	public static Date getDateFilter(String filter, HttpServletRequest req) throws ParseException
	{
	  String strval = getStringFilter(filter, req);
	  return strval == null ? null : GLOBAL_SDF.parse(strval);
	}
	
	public static String setDateFilter(Date date) throws ParseException
	{
	  return date == null ? null : GLOBAL_SDF.format(date);
	}
	
	public static <T> void verifyRequiredProperties(List<String> properties, T entity) {
		if(properties != null)
		for (String p : properties) {
			Field[] aaa = entity.getClass().getDeclaredFields();
			for (Field field : aaa) {
				if(field.getName().equals(p)){
					field.setAccessible(true);
					try {
						if(field.get(entity) == null){
							throw new RuntimeException("A required field "+p+" was found empty");
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						throw new RuntimeException("A required field "+p+" was not found in resource class");
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
