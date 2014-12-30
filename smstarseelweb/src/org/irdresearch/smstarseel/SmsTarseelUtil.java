package org.irdresearch.smstarseel;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.Device;
import org.irdresearch.smstarseel.data.Device.DeviceStatus;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.mysql.jdbc.StringUtils;

public class SmsTarseelUtil {
	public static final Logger DBLOGGER = (Logger) LoggerFactory.getLogger("dbAppender");
	public static final Logger FILELOGGER = (Logger) LoggerFactory.getLogger("fileAppender");
	public static final Logger PHONECOMMLOGGER = (Logger) LoggerFactory.getLogger("phoneCommfileAppender");
	public static final Logger PHONELOGLOGGER = (Logger) LoggerFactory.getLogger("phonefileAppender");

	private static Date lastDeviceErrorEmailDate = new Date(120000000000L);

	public static boolean sendResponse(HttpServletResponse response , String responseToSend) throws IOException 
	{
		SmsTarseelUtil.PHONECOMMLOGGER.info("response:"+responseToSend);
		response.setCharacterEncoding("UTF-8");
		PrintWriter wrtr = response.getWriter();
		wrtr.println(responseToSend);
		return !wrtr.checkError(); // returns true if there is any error false otherwise, but we need to send true if there is no error
		/*ServletOutputStream out = response.getOutputStream();
		out.println(responseToSend);
		out.close();*/
	}
	
	public static Device verifyDeviceProject(String imei, String sim, String projectName, String serviceType, TarseelServices tsc){
		Device prjRegDev = null;
		StringBuffer errormsg = new StringBuffer("Error while finding device.\n");
		try{
			prjRegDev = tsc.getDeviceService().findDevice(imei, DeviceStatus.ACTIVE, false, projectName, sim, 0, 2).get(0);
			if(serviceType.equals(App_Service.FETCH_PENDING_SMS.VALUE())){
				prjRegDev.setDateLastOutboundPing(new Date());
			}
			else if(serviceType.equals(App_Service.SUBMIT_RECIEVED_SMS.VALUE())){
				prjRegDev.setDateLastInboundPing(new Date());
			}
			else if(serviceType.equals(App_Service.SUBMIT_CALL_LOG.VALUE())){
				prjRegDev.setDateLastCalllogPing(new Date());
			}
			
			tsc.getDeviceService().updateDevice(prjRegDev);
		}
		catch (Exception e) {
			e.printStackTrace();
			errormsg.append("Trace is:"+e.getMessage()+".\n");
		}
		
		if(prjRegDev == null){
			errormsg.append("Error handling device for imei:"+imei+", sim:"+sim+", project:"+projectName+", service:"+serviceType);
			if(DateUtils.differenceBetweenIntervals(new Date(), lastDeviceErrorEmailDate, TIME_INTERVAL.HOUR) > 1){
				EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error handling device ", errormsg.toString());
				lastDeviceErrorEmailDate = new Date();
			}
		}
		return prjRegDev;
	}
	
	public static String getReguestParameter(Enum enumVal, HttpServletRequest req){
		String val = req.getParameter(enumVal.name());
		if(StringUtils.isEmptyOrWhitespaceOnly(val)){
			return null;
		}
		
		return val;
	}
	
	public static String getSingleParamFromRequestMap(Enum enumVal, Map qMap, boolean returnWhitespaceVals){
		String[] val = (String[]) qMap.get(enumVal.name());
		if(val != null && val.length > 0){
			return (!returnWhitespaceVals&&StringUtils.isEmptyOrWhitespaceOnly(val[0])?null:val[0]);
		}
		
		return null;
	}
	
	public static String[] getArrayParamFromRequestMap(Enum enumVal, Map qMap){
		Object oo = qMap.get(enumVal.name()+"[]");

		String[] val = (String[]) qMap.get(enumVal.name()+"[]");
		return val;
	}
	
	public static Map convertEntrySetToMap(Set<Entry<Object, Object>> entrySet){
	    Map<Object, Object> mapFromSet = new HashMap<Object, Object>();
	    for(Entry<Object, Object> entry : entrySet)
	    {
	        mapFromSet.put(entry.getKey(), entry.getValue());
	    }
		return mapFromSet;
	}
}
