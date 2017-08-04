package org.irdresearch.smstarseel.rest.its;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.irdresearch.smstarseel.TarseelWebGlobals.SmsServiceConstants;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.data.Service;
import org.irdresearch.smstarseel.data.ServiceLog;
import org.irdresearch.smstarseel.data.ServiceLog.ServiceLogStatus;
import org.irdresearch.smstarseel.rest.ServiceResource;
import org.irdresearch.smstarseel.rest.its.ITSContext.ITSConfig;
import static org.irdresearch.smstarseel.rest.its.ITSContext.*;
import org.irdresearch.smstarseel.rest.util.HttpResponse;
import org.irdresearch.smstarseel.rest.util.HttpUtil;
import org.irdresearch.smstarseel.rest.util.Utils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonSyntaxException;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/its/outbound")
public class ITSOutboundResource {
	private ServiceResource serviceResource;
	
	@Autowired
	public ITSOutboundResource(ServiceResource serviceResource) {
		this.serviceResource = serviceResource;
	}
	
	public static void main(String[] args) {
		String s = "136483;";
		System.out.println(s.substring(0, s.length()-1));
		System.out.println(":136483".substring(1));
		System.out.println("id:2363432432432424234324324324324232738;tel:923371283239".matches("^id[:=]?.{3,20}[;&]?tel[:=]?(923[0-9]{9})"));
	
		String to = "id:2363432432432424234324324324324232738;tel:923371283239";
		String[] vals = to.split("id|tel");
		String recipient = vals[1];
		// remove any separator from start
		recipient = recipient.matches("^[:=]\\d+")?recipient.substring(1):recipient;
		
		String contactId = vals[0];
		contactId = contactId.matches("^[:=]\\d+")?contactId.substring(1):contactId;
		contactId = contactId.matches("^\\d+[;&,]")?contactId.substring(0, contactId.length()-1):contactId;
	}
	
	@RequestMapping(value = "send")
	public @ResponseBody Map<String, Object> sendOutbound(HttpServletRequest request) throws JsonSyntaxException, JSONException, UnsupportedEncodingException {
		Map<String, Object> resp = new HashMap<String, Object>();

		Date systemProcessingStartDate = new Date();

		Service s = serviceResource.authenticatedService(request);
		if(s == null){
			Utils.createErrorResponse("Unhandled Error. No authenticated service found. Make sure you sent Basic Auth or "+SmsServiceConstants.API_KEY.name()+" with request", resp);
			return resp;
		}
		
		String tempUrl = request.getRequestURL().toString();
		String requestPath = tempUrl.substring(0,tempUrl.lastIndexOf("/"));

		String to = Utils.getStringFilter("to", request);
		String contactId = Utils.getStringFilter("contactId", request);
		String from = Utils.getStringFilter("from", request);
		String text = Utils.getStringFilter("text", request);
		String mask = Utils.getStringFilter("mask", request);
		String pp = Utils.getStringFilter("project", request);
		String externalSystemMessageId = Utils.getStringFilter("id", request);
		
		TarseelServices tsc = TarseelContext.getServices();
		Integer projectId = null;
		String recipient = null;
		try{
			if(pp != null){
				List<Project> p = tsc.getDeviceService().findProject(pp);
				if(p.size() > 0){
					projectId = p.get(0).getProjectId();
				}
			}

			if(projectId == null) {
				projectId = s.getProject().getProjectId();
			}

			if(to == null){
				return Utils.createMissingParamErrorResponse("to", resp);
			}
			to = to.replaceAll(" ", "").replaceAll("\\+", "");
			
			if(!to.matches("^(923[0-9]{9})")){
				Utils.createErrorResponse("Recipient(s) must be a single number or in format 923xxxxxxxxx", resp);
				return resp;
			}
			
			recipient = to;

			if(text == null){
				return Utils.createMissingParamErrorResponse("text", resp);
			}
			
			if(mask == null){
				return Utils.createMissingParamErrorResponse("mask", resp);
			}
	
			ITSConfig obdu = ITSConfig.OUTBOUND_DISPATCH_URL;
			if(!ITSConfig.isConfigured(obdu)){
				return Utils.createMissingSettingErrorResponse(obdu.property(), resp);
			}
			
			HttpResponse response = HttpUtil.post(getProperty(ITSConfig.BASE_URL, null), 
					outboundPayload(obdu, recipient, text, mask), "");
			
			System.out.println(response.body());
			
			Utils.createITSResponse(response, resp);
			
			if(resp.containsKey("SUCCESS") && (boolean) resp.get("SUCCESS")){
				String messageId = resp.get("messageid").toString();
				
				Map<String, String> extras = new HashMap<>();
				extras.put(SmsServiceConstants.EXTERNAL_SYSTEM_MESSAGE_ID.name(), externalSystemMessageId);
				extras.put(SmsServiceConstants.EXTERNAL_SYSTEM_ID.name(), from);
				if(contactId != null){
				extras.put(SmsServiceConstants.EXTERNAL_SYSTEM_CONTACT_ID.name(), contactId);
				}
				extras.put(SmsServiceConstants.ITS_MESSAGE_ID.name(), messageId);
				
				// SHOULD keep log of sent messages as well
				String id = s.getServiceIdentifier();
				if(StringUtils.isEmptyOrWhitespaceOnly(id)){
					id = s.getServiceName();
				}
				
				OutboundMessage om = Utils.createOutbound(new Date(), id, mask, recipient, text, new Date(), 1, 
						projectId, systemProcessingStartDate, OutboundStatus.WAITING, messageId, extras);
				
				tsc.getCustomQueryService().save(om);
				
				resp.put("referenceNumber", om.getReferenceNumber());

				String queryUrl = requestPath + "/query?"+SmsServiceConstants.ITS_MESSAGE_ID.name()+"="+messageId;
				ServiceLog sl = Utils.createServiceLog(2, true, projectId, queryUrl, "POST", s.getAuthenticationKey(), extras);
				
				Serializable slId = tsc.getCustomQueryService().save(sl);
				
				resp.put(SmsServiceConstants.SERVICE_LOG_ID.name(), slId.toString());
			}
			else {
				//TODO log sms in smstarseel and log a service that tries sending again.
				// try resend after 4 hours
				if(ITSConfig.isConfigured(ITSConfig.OUTBOUND_AUTO_RETRY)
						&& ITSContext.getProperty(ITSConfig.OUTBOUND_AUTO_RETRY, "false").equalsIgnoreCase("true"))
				{
					String postUrl = requestPath + "/send?"+URLDecoder.decode(request.getQueryString(), "UTF-8");//here to would be whole string again
					ServiceLog sl = Utils.createServiceLog(60*4, true, projectId, postUrl, "POST", s.getAuthenticationKey(), null);
					tsc.getCustomQueryService().save(sl);
				}
			}
			tsc.commitTransaction();
		}
		catch(RuntimeException e){
			e.printStackTrace();
			// log for resend only incase of exception or internet issue
			// try resend after 4 hours
			
			if(ITSConfig.isConfigured(ITSConfig.OUTBOUND_AUTO_RETRY)
					&& ITSContext.getProperty(ITSConfig.OUTBOUND_AUTO_RETRY, "false").equalsIgnoreCase("true"))
			{
				String postUrl = requestPath + "/send?"+URLDecoder.decode(request.getQueryString(), "UTF-8");//here to would be whole string again
				ServiceLog sl = Utils.createServiceLog(60*4, true, projectId, postUrl, "POST", s.getAuthenticationKey(), null);
				tsc.getCustomQueryService().save(sl);
				tsc.commitTransaction();				
			}
			
			Utils.createErrorResponse(e.getMessage(), resp);
		}
		catch (Exception e) {
			e.printStackTrace();
			Utils.createErrorResponse(e.getMessage(), resp);
		}
		finally{
			tsc.closeSession();
		}
		
		return resp;
	}
	
	private String outboundPayload(ITSConfig obdu, String recipient, String text, String mask) {
		String payload = "action="+getProperty(obdu, null);
		payload += "&username="+getProperty(ITSConfig.USERNAME, null);
		payload += "&recipient="+recipient;
		payload += "&messagedata="+text;
		payload += "&password="+getProperty(ITSConfig.PASSWORD, null);
		payload += "&originator="+mask;
		
		return payload;
	}
	
	@RequestMapping(value = "query")
	public @ResponseBody Map<String, Object> queryOutbound(HttpServletRequest request) {
		Map<String, Object> resp = new HashMap<String, Object>();

		String msgId = Utils.getStringFilter(SmsServiceConstants.ITS_MESSAGE_ID.name(), request);
		Integer serviceLogId = Utils.getIntegerFilter(SmsServiceConstants.SERVICE_LOG_ID.name(), request);
		
		if(msgId == null){
			return Utils.createMissingParamErrorResponse(SmsServiceConstants.ITS_MESSAGE_ID.name(), resp);
		}
		
		String payload = "username="+getProperty(ITSConfig.USERNAME, null);
		payload += "&msgid="+msgId;
		payload += "&messagedata=extraparamteret";
		payload += "&password="+getProperty(ITSConfig.PASSWORD, null);
		payload += "&originator=extraparam";
		
		HttpResponse response = HttpUtil.post(getProperty(ITSConfig.OUTBOUND_QUERY_URL, null)+"?"+payload, "", "");
	
		System.out.println(response.body());
		
		String status = "";
		try {
			status = new JSONObject(response.body()).getJSONArray("data").getJSONObject(0).getString("status");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		resp.put("status", status);
		
		if(serviceLogId != null){//TODO change approach; what if dev forget to provide service log id
			// TODO also handle if service log is not needed but outboud was created and needs to be updated
			if(status.toLowerCase().contains("delivered") || status.toLowerCase().contains("accepted")){
				markServiceLogResolved(serviceLogId, true);
				updateOutboundStatus(msgId, OutboundStatus.SENT, "", "", null);
			}
			else {
				markServiceLogResolved(serviceLogId, false);
				updateOutboundStatus(msgId, OutboundStatus.FAILED, "", "", null);
			}
		}
		
		return resp;
	}
	
	private void updateOutboundStatus(String messageId, OutboundStatus status, String errormessage, String failureCause, Integer tries){
		TarseelServices tsc = TarseelContext.getServices();
		try{
			OutboundMessage ob = tsc.getSmsService().findOutboundMessageByReferenceNumber(messageId, false);
			if(ob != null){
				if(!StringUtils.isEmptyOrWhitespaceOnly(failureCause)){
					ob.setErrormessage(ob.getErrorMessage()+";"+errormessage);
					ob.setFailureCause(ob.getFailureCause()+";"+failureCause);					
				}
				if(ob.getSentDate() == null){
					ob.setSentDate(new Date());
				}
				ob.setStatus(status);
				ob.setSystemProcessingEndDate(new Date());
				ob.setTries(tries);
				
				tsc.getCustomQueryService().update(ob);
				tsc.commitTransaction();
			}
		}
		catch (Exception e) {
			// TODO: what to do with exception
			e.printStackTrace();
		}
		finally {
			tsc.closeSession();
		}
	}
	
	private int markServiceLogResolved(Integer serviceLogId, boolean smsSent) {
		TarseelServices tsc = TarseelContext.getServices();
		try{
			// Assuming that serviceLogId would be correct.
			List<ServiceLog> sll = tsc.getCustomQueryService().getDataByHQL("FROM ServiceLog WHERE serviceLogId="+serviceLogId);
			
			ServiceLog sl = sll.get(0);
			sl.setDateEdited(new Date());
			sl.setEditedByUserId("daemon");
			sl.setEditedByUsername("Daemon ServiceLog Manager");
			sl.setStatus(ServiceLogStatus.RESOLVED);

			// TODO do we need to refer to Service object directly; can we add it to extras?
			List<Service> s = tsc.getCustomQueryService().getDataByHQL("FROM Service WHERE authenticationKey='"+sl.getAuthKey()+"'");
			if(s.size() > 0){//TODO could it be 0 ?
				String successUrl = s.get(0).getOutboundSuccessReportUrl();
				String failureUrl = s.get(0).getOutboundFailureReportUrl();
				if(smsSent && !StringUtils.isEmptyOrWhitespaceOnly(successUrl)){
					successUrl = successUrl.replace("{{id}}", sl.getExtras().get(SmsServiceConstants.EXTERNAL_SYSTEM_MESSAGE_ID.name()));
					HttpUtil.post(successUrl, "", "");//TODO
				}
				else if(!StringUtils.isEmptyOrWhitespaceOnly(failureUrl)) {
					failureUrl = failureUrl.replace("{{id}}", sl.getExtras().get(SmsServiceConstants.EXTERNAL_SYSTEM_MESSAGE_ID.name()));
					HttpUtil.post(failureUrl, "", "");
				}
			}
			
			tsc.getCustomQueryService().update(sl);
			tsc.commitTransaction();
			
			return sl.getRetries();
		}
		catch (Exception e) {
			e.printStackTrace();//TODO what to do with exception
		}
		finally{
			tsc.closeSession();
		}
		
		return -1;
	}
	
}
