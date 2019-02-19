package org.irdresearch.smstarseel.autosys;

import java.util.List;

import org.irdresearch.smstarseel.TarseelWebGlobals.SmsServiceConstants;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.ServiceLog;
import org.irdresearch.smstarseel.data.ServiceLog.ServiceLogStatus;
import org.irdresearch.smstarseel.rest.util.HttpResponse;
import org.irdresearch.smstarseel.rest.util.HttpUtil;
import org.irdresearch.smstarseel.rest.util.HttpUtil.AuthType;

import com.mysql.jdbc.StringUtils;

public class ServiceLogExecuterJob implements Runnable{	
	@Override
	public void run() {
		TarseelServices tsc = TarseelContext.getServices();
		try{
			List<ServiceLog> sll = tsc.getCustomQueryService().getDataByHQL("FROM ServiceLog WHERE status IN ('"+ServiceLogStatus.ERROR+"','"+ServiceLogStatus.PENDING+"')");
			System.out.println("SMS TARSEEL EXECUTING SERVICE LOGS LIST WITH SIZE "+sll.size());
			for (ServiceLog sl : sll) {
				String method = StringUtils.isEmptyOrWhitespaceOnly(sl.getServiceMethod()) || sl.getServiceMethod().equalsIgnoreCase("post")?"POST":"GET";
				String authType = sl.getExtras().get("authentication")==null?"none":sl.getExtras().get("authentication");
				
				String serviceUrl = sl.getServiceUrl();
				if(serviceUrl.contains("?")){
					serviceUrl = serviceUrl +"&";
				}
				else {
					serviceUrl = serviceUrl +"?";
				}
				
				serviceUrl = serviceUrl + SmsServiceConstants.API_KEY.name()+"="+sl.getAuthKey()
					+"&"+SmsServiceConstants.SERVICE_LOG_ID.name()+"="+sl.getServiceLogId();
				
				HttpResponse resp = null;
				if(method.equalsIgnoreCase("post")){
					if(authType.equalsIgnoreCase("token")){
						resp  = HttpUtil.postWithToken(serviceUrl, "", sl.getExtras().get("content"), sl.getExtras().get("token"));
					}
					else if(authType.equalsIgnoreCase("basic")){
						resp = HttpUtil.post(serviceUrl, "", sl.getExtras().get("content"), null, AuthType.BASIC, sl.getExtras().get("token"));
					}
					else {
						resp = HttpUtil.post(serviceUrl, "", sl.getExtras().get("content"));
					}
				}
				else {
					//TODO resp = HttpUtil.get(serviceUrl, "", "", "");
				}
				
				if(resp != null){
					sl.addExtra(SmsServiceConstants.SERVICE_LOG_LAST_RESPONSE.name(), resp.body());
				}
				
				tsc.getCustomQueryService().update(sl);
				tsc.flushSession();
			}
			
			tsc.commitTransaction();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			tsc.closeSession();
		}
	}

}
