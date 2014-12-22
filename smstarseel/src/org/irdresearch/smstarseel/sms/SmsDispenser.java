package org.irdresearch.smstarseel.sms;

import org.irdresearch.smstarseel.TarseelService;
import org.irdresearch.smstarseel.comm.HttpSender;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.db.TarseelSQLiteHelper;
import org.irdresearch.smstarseel.global.RequestParam;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.OuboundSmsParams;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.IntentFilter;

public class SmsDispenser extends TarseelService
{
	private static SmsDispenser _instance;
	public static SmsDispenser getInstance(){
		if(_instance == null){
			_instance = new SmsDispenser();
		}
		return _instance;
	}
	
	/** WARNING - USE getInstance() method*/
	public SmsDispenser() {
		super(LOG_TAG, SERVICE_UID, SmsDispenser.class, SMS_DISPENSER_INITIAL_DELAY_SEC_PREF_NAME, SMS_DISP_FETCH_INTERVAL_SEC_PREF_NAME, 60, 60);
	}

	private static final String LOG_TAG = "SmsDispenser";
	private static final String SERVICE_UID = "org.irdresearch.smstarseel.sms.SmsDispenser";

	public static final String SENDER_CELL_PARAM = "cell";
	public static final String SENDER_TEXT_PARAM = "text";
	
	public static final String SENDER_IS_SENT_PARAM = "issent";
	public static final String SENDER_ERROR_MESSAGE_PARAM = "errormsg";
	public static final String SENDER_FAILURE_CAUSE_PARAM = "failurecause";
	public static final String SENDER_SENT_DATE_PARAM = "sentdate";
	public static final String SENDER_NO_ERROR_MESSAGE_STRING = "NO_ERROR";

	private static final String SMS_DISP_FETCH_INTERVAL_SEC_PREF_NAME = "SMS_DISPENSER_FETCH_INTERVAL_SEC";
	private static final String SMS_DISPENSER_INITIAL_DELAY_SEC_PREF_NAME = "SMS_DISPENSER_INITIAL_DELAY_SEC";
		
	protected void runTask() throws Exception{
		final SmsTarseelRequest payload = new SmsTarseelRequest(this, App_Service.FETCH_PENDING_SMS);
		final JSONObject resp = HttpSender.sendLargeText(this, payload);
		if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.ERROR.CODE()))
		{
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Unable to fetch sms, Error :"+(String)resp.get(ResponseMessage.NAME));
	        FileUtil.writeLog(LOG_TAG, "Unable to fetch sms, Error :"+(String)resp.get(ResponseMessage.NAME));
		}
		else if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.SUCCESS.CODE()))
		{
			final JSONArray smslist = (JSONArray)resp.get(OuboundSmsParams.LIST_ID.KEY());
			TarseelGlobals.addTo_CONSOLE_BUFFER(null, "Fetched "+smslist.length()+"Smses");

			if(smslist.length() > 0){
				for (int i = 0; i < smslist.length(); i++)
				{
					while(!SmsSender.isSmsSenderServiceFree()){
						//TarseelGlobals.addTo_CONSOLE_BUFFER("Send Sms was found service busy");

						/*try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}*/
					}

					final JSONObject sms = smslist.getJSONObject(i);
					final String cell = sms.getString(OuboundSmsParams.CELL.KEY()).trim();
					sms.put(OuboundSmsParams.SIM.KEY(), TarseelGlobals.SIM(this));
					sms.put(OuboundSmsParams.IS_SENT.KEY(), false);

					if(cell.matches("\\+?[0-9]+")){
						SmsSender.lockSmsSender(sms);

						IntentFilter filter = new IntentFilter(TarseelGlobals.SMS_SENDER_CALLBACK_IDENTIFIER);
						filter.addCategory(Intent.CATEGORY_DEFAULT);

						SmsSenderNotification notification = new SmsSenderNotification();
						registerReceiver(notification, filter);

						try{
							Intent in = new Intent(getBaseContext(), SmsSender.class);
							in.putExtra(SmsDispenser.SENDER_CELL_PARAM, cell);
							in.putExtra(SmsDispenser.SENDER_TEXT_PARAM, sms.getString(OuboundSmsParams.TEXT.KEY()));

							startService(in);
							
			        		while(!SmsSender.isSmsSenderServiceFree()){
							//stuck here
			        		}
						}
						finally{
			        		unregisterReceiver(notification);
						}
					}
					else
					{
						sms.put(OuboundSmsParams.FAIL_CAUSE.KEY(), "INVALID CELL NUMBER");
						sms.put(OuboundSmsParams.ERR_MSG.KEY(), "Sending attempt failed:invalid cell Num "+cell);
					}
				}
				
        		TarseelGlobals.addTo_CONSOLE_BUFFER(null, "Submitting sent result..");

				SmsTarseelRequest payload2 = new SmsTarseelRequest(this, App_Service.SUBMIT_SMS_SEND_ATTEMPT_RESULT);
				//remove text param to avoid resending of huge text through application in response again
				for (int i = 0; i < smslist.length(); i++) {
					smslist.getJSONObject(i).put(OuboundSmsParams.TEXT.KEY(), null);
				}
				payload2.addObjectList(OuboundSmsParams.LIST_ID.KEY(), smslist);
				
				int submitAttempts = 1;
				boolean isSubmitted = false;
				while(submitAttempts <= 3 && !isSubmitted){
					try{
						JSONObject resp2 = HttpSender.sendLargeText(this, payload2);
						if(resp2.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.SUCCESS.CODE()))
						{
							isSubmitted = true;
						}
					}catch (Exception e) {
						  FileUtil.writeLog(LOG_TAG, "Exception:"+e.getMessage());
					      FileUtil.writeLog(e);
					}
					
					submitAttempts++;
				}
				
        		if(isSubmitted) {
        			TarseelGlobals.addTo_CONSOLE_BUFFER(null, "Submitted..");
        		}
        		else {
        			TarseelSQLiteHelper sql = null;
        			try{
            			sql = new TarseelSQLiteHelper(this);
            			sql.open();
            			long id = sql.createUnsubmittedOutbound(payload2.toString());
            			sql.close();
        			}
        			catch (Exception e) {
						e.printStackTrace();
	        			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, e.getMessage());
						FileUtil.writeLog(LOG_TAG, "Exception:"+e.getMessage());
					    FileUtil.writeLog(e);
					}
        			TarseelGlobals.addTo_CONSOLE_BUFFER(null, "Not submitted("+submitAttempts+" attempts)");
        		}
			}
		}
	}
}
