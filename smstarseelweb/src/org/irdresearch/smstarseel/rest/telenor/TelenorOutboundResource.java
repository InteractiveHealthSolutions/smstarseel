package org.irdresearch.smstarseel.rest.telenor;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
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
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundType;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.data.Service;
import org.irdresearch.smstarseel.data.ServiceLog;
import org.irdresearch.smstarseel.data.ServiceLog.ServiceLogStatus;
import org.irdresearch.smstarseel.rest.ServiceResource;
import org.irdresearch.smstarseel.rest.telenor.TelenorContext.Config;
import org.irdresearch.smstarseel.rest.util.HttpResponse;
import org.irdresearch.smstarseel.rest.util.HttpUtil;
import org.irdresearch.smstarseel.rest.util.Utils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonSyntaxException;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/telenor/outbound")
public class TelenorOutboundResource {
	private Authenticate auth;
	private ServiceResource serviceResource;
	
	@Autowired
	public TelenorOutboundResource(Authenticate auth, ServiceResource serviceResource) {
		this.auth = auth;
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
		Boolean unicode = Utils.getBooleanFilter("unicode", request);
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
	
			String session_id = auth.getAuthenticatedSessionId();
			if(session_id == null){
				Utils.createErrorResponse("Unhandled error. Telenor authentication not successful.", resp);
				return resp;
			}
					
			Config obdu = Config.OUTBOUND_DISPATCH_URL;
			if(!Config.isConfigured(obdu)){
				return Utils.createMissingSettingErrorResponse(obdu.property(), resp);
			}
			
			HttpResponse response = HttpUtil.post(Config.fullUrl(obdu) , 
					outboundPayload(session_id, recipient, text, mask, unicode), "");
			System.out.println(response.body());
			Utils.createTelenorResponse(response, resp);
			
			if((resp.containsKey("SUCCESS") && (boolean) resp.get("SUCCESS"))){
				Long messageId = new Double(resp.get("data").toString()).longValue();
				
				Map<String, String> extras = new HashMap<>();
				extras.put(SmsServiceConstants.EXTERNAL_SYSTEM_MESSAGE_ID.name(), externalSystemMessageId);
				extras.put(SmsServiceConstants.EXTERNAL_SYSTEM_ID.name(), from);
				if(contactId != null){
				extras.put(SmsServiceConstants.EXTERNAL_SYSTEM_CONTACT_ID.name(), contactId);
				}
				extras.put(SmsServiceConstants.TELENOR_MESSAGE_ID.name(), messageId.toString());
				
				// SHOULD keep log of sent messages as well
				String id = s.getServiceIdentifier();
				if(StringUtils.isEmptyOrWhitespaceOnly(id)){
					id = s.getServiceName();
				}
				String referenceNumber = TelenorContext.createReferenceNumber(messageId);
				OutboundMessage om = Utils.createOutbound(new Date(), id, mask, recipient, text, new Date(), 1,	projectId, 
						systemProcessingStartDate, OutboundStatus.WAITING, referenceNumber , extras);
				
				Integer outboundId = null;
				OutboundMessage obmsg = null;
				List<BigInteger> outboundIdL = tsc.getCustomQueryService().getDataBySQL("select outbound_extras_id from outbound_extras where attributeKey = 'EXTERNAL_SYSTEM_MESSAGE_ID' "
						+ "and attributeValue = "+ externalSystemMessageId);
				if(outboundIdL != null && outboundIdL.size() >0){
					outboundId = outboundIdL.get(0).intValue();
				}
				if(outboundId != null){
					obmsg = tsc.getSmsService().findOutboundById(outboundId);
				}
				if(obmsg == null){
					tsc.getCustomQueryService().save(om);
					resp.put("referenceNumber", om.getReferenceNumber());
				} else {
					obmsg.setImei(id);
					obmsg.setOriginator(from);
					obmsg.setRecipient(to);
					obmsg.setText(text);
//					obmsg.setTries(obmsg.getTries() +1);
					obmsg.setSentdate(new Date());
					obmsg.setStatus(OutboundStatus.WAITING);
					obmsg.setPeriodType(PeriodType.DAY);
					obmsg.setValidityPeriod(1);
					obmsg.setPriority(Priority.HIGH);
					obmsg.setProjectId(projectId);
					obmsg.setReferenceNumber(referenceNumber);
					obmsg.setSystemProcessingStartDate(systemProcessingStartDate);
					obmsg.setType(OutboundType.SMS);
					obmsg.setExtras(extras);
					obmsg.setReferenceNumber(referenceNumber);
					
					tsc.getCustomQueryService().update(obmsg);
					resp.put("referenceNumber", obmsg.getReferenceNumber());
				}
				String queryUrl = requestPath + "/query?"+SmsServiceConstants.TELENOR_MESSAGE_ID.name()+"="+messageId;
				ServiceLog sl = Utils.createServiceLog(2, true, projectId, queryUrl, "POST", s.getAuthenticationKey(), extras);
				tsc.getCustomQueryService().save(sl);
			}
			else {
				//TODO log sms in smstarseel and log a service that tries sending again.
				// try resend after 4 hours
				if(Config.isConfigured(Config.OUTBOUND_AUTO_RETRY)
						&& TelenorContext.getProperty(Config.OUTBOUND_AUTO_RETRY, "false").equalsIgnoreCase("true"))
				{
					String postUrl = requestPath + "/send?"+URLDecoder.decode(request.getQueryString(), "UTF-8");//here to would be whole string again
					ServiceLog sl = Utils.createServiceLog(60*4, true, projectId, postUrl, "POST", s.getAuthenticationKey(), null);
					tsc.getCustomQueryService().save(sl);
				} else {
					Long messageId; 
					BigInteger errorNo = (BigInteger)tsc.getCustomQueryService().getDataBySQL(
							"SELECT max(CONVERT(replace(referenceNumber, 'Telenor:Error',''),UNSIGNED INTEGER)) rn FROM outboundmessage WHERE referenceNumber LIKE 'Telenor:Error%';").get(0);
					if(errorNo != null){
						messageId = errorNo.longValue() + 1;
					} else {
						messageId = 1L;
					}
					Map<String, String> extras = new HashMap<>();
					extras.put(SmsServiceConstants.EXTERNAL_SYSTEM_MESSAGE_ID.name(), externalSystemMessageId);
					extras.put(SmsServiceConstants.EXTERNAL_SYSTEM_ID.name(), from);
					if(contactId != null){
					extras.put(SmsServiceConstants.EXTERNAL_SYSTEM_CONTACT_ID.name(), contactId);
					extras.put(SmsServiceConstants.TELENOR_MESSAGE_ID.name(), "Error" + messageId.toString());
					}
					
					String id = s.getServiceIdentifier();
					if(StringUtils.isEmptyOrWhitespaceOnly(id)){
						id = s.getServiceName();
					}
					
					Integer outboundId = null;
					OutboundMessage obmsg = null;
					List<BigInteger> outboundIdL = tsc.getCustomQueryService().getDataBySQL("select outbound_extras_id from outbound_extras where attributeKey = 'EXTERNAL_SYSTEM_MESSAGE_ID' "
							+ "and attributeValue = "+ externalSystemMessageId);
					if(outboundIdL != null && outboundIdL.size() >0){
						outboundId = outboundIdL.get(0).intValue();
					}
					if(outboundId != null){
						obmsg = tsc.getSmsService().findOutboundById(outboundId);
					}
					if(obmsg == null){
						String referenceNumber =  "Telenor:Error"+messageId.toString();
						OutboundMessage om = Utils.createOutbound(new Date(), id, mask, recipient, text, new Date(), 1,	projectId, 
								systemProcessingStartDate, OutboundStatus.FAILED, referenceNumber , extras);
						tsc.getCustomQueryService().save(om);
					}
				}
			}
			tsc.commitTransaction();
		}
		catch(RuntimeException e){
			e.printStackTrace();
			// log for resend only incase of exception or internet issue
			// try resend after 4 hours
			
			if(Config.isConfigured(Config.OUTBOUND_AUTO_RETRY)
					&& TelenorContext.getProperty(Config.OUTBOUND_AUTO_RETRY, "false").equalsIgnoreCase("true"))
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
//	
	private String outboundPayload(String session_id, String recipient, String text, String mask, Boolean unicode) {
		String payload = "session_id="+session_id;
		payload += "&to="+recipient;
		payload += "&text="+text;
		payload += "&mask="+mask;
		if(unicode != null){
			payload += "&unicode="+unicode;
		}
		
		return payload;
	}
	
	@RequestMapping(value = "query")
	public @ResponseBody Map<String, Object> queryOutbound(HttpServletRequest request) {
		Map<String, Object> resp = new HashMap<String, Object>();

		String msgId = Utils.getStringFilter(SmsServiceConstants.TELENOR_MESSAGE_ID.name(), request);
		Integer serviceLogId = Utils.getIntegerFilter(SmsServiceConstants.SERVICE_LOG_ID.name(), request);
		
		if(msgId == null){
			return Utils.createMissingParamErrorResponse(SmsServiceConstants.TELENOR_MESSAGE_ID.name(), resp);
		}
		
		String session_id = auth.getAuthenticatedSessionId();
		if(session_id == null){
			Utils.createErrorResponse("Unhandled error. Authentication not successfull.", resp);
			
			if(serviceLogId != null){
				updateServiceLog(serviceLogId, ServiceLogStatus.PENDING, false, resp.toString());
			}
			
			return resp;
		}
		
		Config obqm = Config.OUTBOUND_QUERY_URL;
		if(!Config.isConfigured(obqm)){
			return Utils.createMissingSettingErrorResponse(obqm.property(), resp);
		}
		
		HttpResponse response = HttpUtil.post(Config.fullUrl(obqm)+"?session_id="+session_id+"&msg_id="+msgId, "", "");
	//	System.out.println(response.body());
		Utils.createTelenorResponse(response, resp);
		
		if(serviceLogId != null){//TODO change approach; what if dev forget to provide service log id
			// TODO also handle if service log is not needed but outboud was created and needs to be updated
			if(resp.containsKey("SUCCESS") && (boolean) resp.get("SUCCESS")){
				int tries = updateServiceLog(serviceLogId, ServiceLogStatus.RESOLVED, true, null);
				updateOutboundStatus(msgId, OutboundStatus.SENT, "", "", tries);
			}
			else {
				int tries = updateServiceLog(serviceLogId, ServiceLogStatus.ERROR, false, (String) resp.get("ERROR_MESSAGE"));
				updateOutboundStatus(msgId, OutboundStatus.WAITING, (String) resp.get("ERROR_MESSAGE"), (String) resp.get("ERROR_MESSAGE"), tries);
			}
		}
		
		return resp;
	}
	
	private void updateOutboundStatus(Object messageId, OutboundStatus status, String errormessage, String failureCause, Integer tries){
		TarseelServices tsc = TarseelContext.getServices();
		try{
			OutboundMessage ob = tsc.getSmsService().findOutboundMessageByReferenceNumber(TelenorContext.createReferenceNumber(messageId), false);
			if(ob != null){
				if(!StringUtils.isEmptyOrWhitespaceOnly(failureCause) || !StringUtils.isEmptyOrWhitespaceOnly(failureCause)){
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
	
	private int updateServiceLog(Integer serviceLogId, ServiceLogStatus serviceLogStatus, boolean giveupRetry, String failureCause) {
		TarseelServices tsc = TarseelContext.getServices();
		try{
			// Assuming that serviceLogId would be correct.
			//TODO update ob status
			List<ServiceLog> sll = tsc.getCustomQueryService().getDataByHQL("FROM ServiceLog WHERE serviceLogId="+serviceLogId);
			ServiceLog sl = sll.get(0);
			sl.setDateEdited(new Date());
			sl.setEditedByUserId("daemon");
			sl.setEditedByUsername("Daemon ServiceLog Manager");
			sl.setStatus(serviceLogStatus);

			if(!serviceLogStatus.equals(ServiceLogStatus.RESOLVED)){
				if(giveupRetry == false){
					sl.setDateDue(DateTime.now().plusHours(6).toDate());
				}
				else if(giveupRetry){
					sl.setStatus(ServiceLogStatus.DISCARDED);
				}
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(failureCause)){
				sl.setFailureCause(sl.getFailureCause()+";"+failureCause);				
			}
			
			if(sl.getRetries() > 4){ // 6 hours gap with 4 tries would suffice to handle internet issues
				sl.setFailureCause(sl.getFailureCause()+";MAX TRIES EXCEEDED;");
				sl.setStatus(ServiceLogStatus.DISCARDED);
			}
			
			sl.setRetries(sl.getRetries()+1);
			
			// TODO do we need to refer to Service object directly; can we add it to extras?
			List<Service> s = tsc.getCustomQueryService().getDataByHQL("FROM Service WHERE authenticationKey='"+sl.getAuthKey()+"'");
			if(s.size() > 0){//TODO could it be 0 ?
				String successUrl = s.get(0).getOutboundSuccessReportUrl();
				String failureUrl = s.get(0).getOutboundFailureReportUrl();
				if(serviceLogStatus.equals(ServiceLogStatus.RESOLVED) && !StringUtils.isEmptyOrWhitespaceOnly(successUrl)){
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
