package org.irdresearch.smstarseel.rest.its;

import static org.irdresearch.smstarseel.rest.its.ITSContext.getProperty;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.irdresearch.smstarseel.EmailEngine;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.InboundMessage;
import org.irdresearch.smstarseel.data.InboundMessage.InboundStatus;
import org.irdresearch.smstarseel.data.InboundMessage.InboundType;
import org.irdresearch.smstarseel.data.Service;
import org.irdresearch.smstarseel.data.Setting;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.rest.its.ITSContext.ITSConfig;
import org.irdresearch.smstarseel.rest.util.HttpResponse;
import org.irdresearch.smstarseel.rest.util.HttpUtil;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.StringUtils;

public class ITSInboundServiceJob extends TimerTask{
	private static final String ITS_INBOUND_SETTING = "ITS_INBOUND_SETTING";
	private static DateTime lastErrorEmail = DateTime.now().minusYears(2);

	@Override
	public void run() {
		try {
			String setting = TarseelContext.getSetting(ITS_INBOUND_SETTING, null);

			Long startId = null;
			if(StringUtils.isEmptyOrWhitespaceOnly(setting))
			{
				TarseelServices tsc = TarseelContext.getServices();
				
				Setting set = new Setting();
				set.setDescription("DONOT MODIFY; Used by ITS auto inbound handler");
				set.setDisplayName(ITS_INBOUND_SETTING);
				set.setIsEditable(false);
				set.setIsViewable(true);
				set.setName(ITS_INBOUND_SETTING);
				set.setValue("0");
				
				tsc.getCustomQueryService().save(set);
				tsc.commitTransaction();
				tsc.closeSession();
				
				TarseelContext.refreshAndGetSettings();
				
				startId = 0L;
			}
			else {
				startId = Long.parseLong(setting);
			}
			
			List<Service> serviceL = new ArrayList<>();
			TarseelServices tsc = TarseelContext.getServices();
			try{
				serviceL = tsc.getCustomQueryService().getDataByHQL("FROM Service");
			}
			finally {
				tsc.closeSession();
			}
			
			String ibdqu = ITSContext.getProperty(ITSConfig.INBOUND_QUERY_URL, null);
			
			if(StringUtils.isEmptyOrWhitespaceOnly(ibdqu)){
				throw new RuntimeException("No setting configured for '"+ITSConfig.INBOUND_QUERY_URL.name()+"'");
			}
			
			String payload = "username="+getProperty(ITSConfig.USERNAME, null);
			payload += "&recipient=no recipinet configured";
			payload += "&messagedata=no message data specified in request";
			payload += "&password="+getProperty(ITSConfig.PASSWORD, null);
			payload += "&startid="+startId;
						
			HttpResponse response = HttpUtil.post(ibdqu, payload, "");
			
			System.out.println(response.body());

			JSONArray smses = new JSONObject(response.body()).optJSONArray("data");
			if(smses != null){
				for (int i = 0; i < smses.length(); i++) 
				{
					JSONObject sms = smses.getJSONObject(i);
					String smsId = sms.getString("id");

					tsc = TarseelContext.getServices();
					try{
						String receiver = sms.getString("receiver");

						List<Service> listners = getServiceForCode(receiver, serviceL);

						if(listners.size() == 0){
							saveInbound(smsId, receiver, null, sms, tsc);
						}
						else{
							for (Service service : listners) {
								Integer projectId = service.getProject()==null?null:service.getProject().getProjectId();

								InboundMessage ib = saveInbound(smsId, receiver, projectId, sms, tsc);
								try{
									String url = service.getInboundReportUrl();
									if(!StringUtils.isEmptyOrWhitespaceOnly(url)){
										url = url.replace("{{recipient}}", ib.getRecipient());
										url = url.replace("{{originator}}", ib.getOriginator());
										url = url.replace("{{text}}", ib.getText());
										url = url.replace("{{datetime}}", ITSContext.DATETIME_FORMAT.format(ib.getRecieveDate()));
										url = url.replace("{{reference}}", ib.getReferenceNumber());

										HttpResponse resp = HttpUtil.post(service.getInboundReportUrl(), "", null);

										if(resp.statusCode() >= 200 && resp.statusCode() < 300){
											ib.setStatus(InboundStatus.READ);
											tsc.getSmsService().updateInbound(ib);
										}
									}
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

						tsc.commitTransaction();
					}
					catch (Exception e) {
						e.printStackTrace();
						EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error Handling SMS "+smsId+" in "+this.getClass().getName()+" "+e.getMessage(), "SmsTarseel: Error handling Inbound SMS: "+smsId+"-"+e.getMessage());
					}
					finally{
						tsc.closeSession();
					}

					TarseelContext.updateSetting(ITS_INBOUND_SETTING, smsId, new User(99, "Daemon"));
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			if(lastErrorEmail.plusHours(1).isBefore(DateTime.now())){
				lastErrorEmail = DateTime.now();
				EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error Handling SMSes"+this.getClass().getName()+" "+e.getMessage(), "SmsTarseel: Error handling Inbound SMSes: "+e.getMessage());
			}
		}
	}
	
	private InboundMessage saveInbound(String smsId, String receiver, Integer projectId, JSONObject sms, TarseelServices tsc) throws ParseException, JSONException {
		InboundMessage ib = new InboundMessage();
		ib.setImei(receiver);
		ib.setOriginator(sms.getString("sender"));
		ib.setProjectId(projectId);
		ib.setReferenceNumber(projectId+"-ITS-"+smsId);
		
		Date recvDate = ITSContext.DATETIME_FORMAT.parse(sms.getString("receivedtime"));
		Date sentDate = ITSContext.DATETIME_FORMAT.parse(sms.getString("senttime"));
		
		ib.setRecieveDate(recvDate);
		ib.setRecipient(receiver);
		ib.setStatus(InboundStatus.UNREAD);//should be unread until the application (whome it is meant for) doesnot mark it read
		ib.setSystemProcessingStartDate(sentDate);
		ib.setText(sms.getString("msgdata"));
		ib.setType(InboundType.SMS);
		ib.setSystemProcessingEndDate(new Date());
		
		tsc.getSmsService().saveInbound(ib);
		
		return ib;
	}
	
	private List<Service> getServiceForCode(String code, List<Service> serviceL){
		List<Service> results = new ArrayList<>();
		for (Service s : serviceL) {
			if((s.getInboundReportUrl() != null && s.getInboundReportUrl().trim().equalsIgnoreCase(code))
					|| (s.getServiceIdentifier() != null && s.getServiceIdentifier().trim().equalsIgnoreCase(code))
					|| (s.getServiceName() != null && s.getServiceName().trim().equalsIgnoreCase(code))){
				results.add(s);
			}
		}
		return results;
	}

}
