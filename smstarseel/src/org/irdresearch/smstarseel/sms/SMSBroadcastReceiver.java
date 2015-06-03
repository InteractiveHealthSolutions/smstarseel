package org.irdresearch.smstarseel.sms;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.irdresearch.smstarseel.comm.HttpSender;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.db.DataAccess;
import org.irdresearch.smstarseel.global.DateUtils;
import org.irdresearch.smstarseel.global.RequestParam;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.InboundSmsParams;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	private Context context;
	private static final String LOG_TAG = "SmsCollector";
	private static final String SERVICE_UID = "org.irdresearch.smstarseel.sms.SmsCollector";

	private static final String SMS_ID = "_id";//  (long)
	private static final String SMS_THREAD_ID = "thread_id";//   (long)
	private static final String SMS_ADDRESS = "address";//   (String)
	//private static final String SMS_PERSON = "person";//   (String)
	private static final String SMS_DATE = "date";//     (long)
	//private static final String protocol
	private static final String SMS_READ = "read";
	private static final String SMS_STATUS = "status";
	private static final String SMS_TYPE = "type";
	//private static final String reply_path_present
	//private static final String subject    (String)
	private static final String SMS_BODY = "body";//    (String)
	//private static final String service_center
	//private static final String locked
	//private static final String error_code
	//private static final String seen
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "SMSBroadcastReceiver";
	private DataAccess dataAccess;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(SmsCollector.getInstance().isServiceStarted(context)) {
			
		
			Log.i(TAG, "Intent recieved: " + intent.getAction());
			this.context = context;
			try{
				if (intent.getAction().equals(SMS_RECEIVED)) {
					Bundle bundle = intent.getExtras();
					if (bundle != null) {
						Object[] pdus = (Object[]) bundle.get("pdus");
						final SmsMessage[] messages = new SmsMessage[pdus.length];
						// final ArrayList<Map<String, String>> readlist = readInbox(new String[]{SMS_ID,SMS_THREAD_ID,SMS_ADDRESS,SMS_DATE,SMS_BODY,SMS_TYPE,SMS_STATUS,SMS_READ}, fetchsize);
						TarseelGlobals.addTo_CONSOLE_BUFFER(null, "read "+messages.length+" smses");
						
						// JSONArray smsList = new JSONArray();
						
						for (int i = 0; i < pdus.length; i++) {
							messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						}
						for(SmsMessage sms:messages) {
							final JSONObject inbound = new JSONObject();
							inbound.put(InboundSmsParams.SYSTEM_PROCESS_START_DATE.KEY(), DateUtils.formatRequestDate(new Date()));
							
							String messageId = sms.getTimestampMillis()+sms.getOriginatingAddress();
							inbound.put(InboundSmsParams.SMSID.KEY(), messageId);
							// inbound.put(InboundSmsParams.THREADID.KEY(), item.get(SMS_THREAD_ID));
							inbound.put(InboundSmsParams.IS_SAVED.KEY(), false);
							
							final String recvDt = DateUtils.formatRequestDate(new Date(sms.getTimestampMillis()));
							inbound.put(InboundSmsParams.RECIEVED_DATE.KEY(), recvDt);
							//we need date in long to delete sms from inbox
							inbound.put(InboundSmsParams.RECIEVED_DATE_IN_LONG.KEY(), sms.getTimestampMillis()+"");
							inbound.put(InboundSmsParams.SENDER_NUM.KEY(), sms.getOriginatingAddress());
							inbound.put(InboundSmsParams.SIM.KEY(), TarseelGlobals.SIM(context));
							//inbound.put(InboundSmsParams.SYSTEM_RECIEVED_DATE.KEY(), recvDt);
							inbound.put(InboundSmsParams.TEXT.KEY(), sms.getMessageBody());
							// inbound.put(InboundSmsParams.TYPE.KEY(), item.get(SMS_TYPE));
							
							dataAccess = DataAccess.getInstance(context);
		        			try {
		        				dataAccess.createInboundNotUploaded(messageId, inbound);
		        			}
		        			catch (Exception e) {
								e.printStackTrace();
			        			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, e.getMessage());
								FileUtil.writeLog(LOG_TAG, "Exception:"+e.getMessage());
							    FileUtil.writeLog(e);
							}
							
							// smsList.put(inbound);
						}
						
						JSONArray toUpload = dataAccess.getAllUnUploadedInbound();
						// Keep uploading until all the messages in local database are not sent
						int wait = 10;
						while(toUpload.length() > 0) {
							try {
								JSONObject resp = send(toUpload);
								processResponse(resp);
							} catch(Exception e) {
								e.printStackTrace();
								TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, e.getMessage());
								FileUtil.writeLog(LOG_TAG, "Exception:"+e.getMessage());
							    FileUtil.writeLog(e);
							}
							
							if(wait <300) {
								wait+=10;
							}
							TimeUnit.SECONDS.sleep(wait);
							
							toUpload = dataAccess.getAllUnUploadedInbound();
						}
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, e.getMessage());
				FileUtil.writeLog(LOG_TAG, "Exception:"+e.getMessage());
			    FileUtil.writeLog(e);
			}
		}
	}

	private JSONObject send(JSONArray smsList) throws JSONException, IOException {
		
		final SmsTarseelRequest payload = new SmsTarseelRequest(context, App_Service.SUBMIT_RECIEVED_SMS);
		payload.addObjectList(InboundSmsParams.LIST_ID.KEY(), smsList);
		
		return HttpSender.sendLargeText(context, payload);
	}
	private void processResponse(JSONObject resp) throws JSONException {
		if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.ERROR.CODE())) {
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Unable to retrieve recieved sms submit confirmation, Error :"+(String)resp.get(ResponseMessage.NAME));
	        FileUtil.writeLog(LOG_TAG, "Unable to retrieve recieved sms submit confirmation, Error :"+(String)resp.get(ResponseMessage.NAME));
		} else {
			final JSONArray smslist = (JSONArray)resp.get(InboundSmsParams.LIST_ID.KEY());
			
			if(smslist.length() > 0) {
				for (int i = 0; i < smslist.length(); i++) {
					final JSONObject sms = smslist.getJSONObject(i);
					
					if(sms.getBoolean(InboundSmsParams.IS_SAVED.KEY())) {
	        			try {
	            			dataAccess.deleteUploadedInbound((String)sms.get(InboundSmsParams.SMSID.KEY()));
	        			} catch(Exception e) {
							e.printStackTrace();
		        			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, e.getMessage());
							FileUtil.writeLog(LOG_TAG, "Exception:"+e.getMessage());
						    FileUtil.writeLog(e);
						}
					}
				}
			}
		}
	}
}
