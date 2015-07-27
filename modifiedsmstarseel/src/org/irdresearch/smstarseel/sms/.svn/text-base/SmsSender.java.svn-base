package org.irdresearch.smstarseel.sms;

import java.util.ArrayList;

import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.util.FileUtil;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

public class SmsSender extends IntentService
{
	private static final String LOG_TAG = "SmsSender";

	private static boolean isSmsSenderServiceFree = true;
	private static SenderStatusHandler senderStatus = null;
	private Intent sendIntent;
	private IntentFilter sendIntentFilter;
	private SmsManagerNotification smsnotification;
	
	public SmsSender()
	{
		super("SMS SENDER SERVICE");
		sendIntent = new Intent(TarseelGlobals.SMS_MANAGER_CALLBACK_IDENTIFIER);
		sendIntentFilter = new IntentFilter(TarseelGlobals.SMS_MANAGER_CALLBACK_IDENTIFIER);
 
		///or if it have single / class level obj then dont forget to call reset for this reciever 
        smsnotification = new SmsManagerNotification(this, TarseelGlobals.SMS_MANAGER_CALLBACK_IDENTIFIER);
	}

	public static void lockSmsSender(JSONObject currentSms){
		if(senderStatus != null || !isSmsSenderServiceFree()){
			FileUtil.writeLog(LOG_TAG, "SenderStatusHandler should be null and smsSenderFree boolean should be true while getting lock.");
			throw new IllegalStateException("SenderStatusHandler should be null and smsSenderFree boolean should be true while getting lock.");
		}
		setSmsSenderServiceFree(false);
		setSenderStatus(new SenderStatusHandler(currentSms));
	}
	
	public static void lockSmsSender(String sms){
		if(senderStatus != null || !isSmsSenderServiceFree()){
			FileUtil.writeLog(LOG_TAG, "SenderStatusHandler should be null and smsSenderFree boolean should be true while getting lock.");
			throw new IllegalStateException("SenderStatusHandler should be null and smsSenderFree boolean should be true while getting lock.");
		}
		setSmsSenderServiceFree(false);
		setSenderStatus(new SenderStatusHandler(sms));
	}
	
	public static void unlockSmsSender(){
		if(senderStatus == null || isSmsSenderServiceFree()){
			FileUtil.writeLog(LOG_TAG, "SenderStatusHandler should be null and smsSenderFree boolean should be false while releasing lock.");
			throw new IllegalStateException("SenderStatusHandler should be null and smsSenderFree boolean should be false while releasing lock.");
		}
		setSenderStatus(null);
		setSmsSenderServiceFree(true);
	}
	public static SenderStatusHandler getSenderStatus()
	{
		return senderStatus;
	}

	private static void setSenderStatus(SenderStatusHandler senderStatus)
	{
		SmsSender.senderStatus = senderStatus;
	}

	public static boolean isSmsSenderServiceFree()
	{
		return isSmsSenderServiceFree;
	}

	private static void setSmsSenderServiceFree(boolean isSmsSenderServiceFree)
	{
		SmsSender.isSmsSenderServiceFree = isSmsSenderServiceFree;
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		if(senderStatus == null){
			FileUtil.writeLog(LOG_TAG, "You must set senderStatus object to currentSms being sent.");
			throw new IllegalStateException("You must set senderStatus object to currentSms being sent.");
			//TODO
		}
		
        final String cell = intent.getStringExtra(SmsDispenser.SENDER_CELL_PARAM);
        final String text = intent.getStringExtra(SmsDispenser.SENDER_TEXT_PARAM);
        
		smsnotification.reset();
		//---when the SMS has been sent---
        registerReceiver(smsnotification , sendIntentFilter);
        try{
		/*
		 String DELIVERED = "SMS_DELIVERED"; PendingIntent deliveredPI =
		 PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
		 
		 //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
		*/
	       
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sendIntent, PendingIntent.FLAG_ONE_SHOT);

		sendMultipartSms(cell, text, sendIntent);
		
		while(!getSenderStatus().getHasManagerBcNotified() || !getSenderStatus().getHasSenderBcNotified()){
			//TODO 
		}
		
		
		}catch (Exception e) {
			e.printStackTrace();
		}
        finally{
        	unregisterReceiver(smsnotification);
        }
		unlockSmsSender();
	}

	private void sendMultipartSms(String cell, String text, Intent sendIntent)
	{
		SmsManager sms = SmsManager.getDefault();
		ArrayList<String> parts = sms.divideMessage(text);
		int numParts = parts.size();
		ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
		for (int i = 0; i < numParts; i++)
		{
			sentIntents.add(PendingIntent.getBroadcast(getBaseContext(), 0, sendIntent, 0));
		}
		sms.sendMultipartTextMessage(cell, null, parts, sentIntents, null);
		try
		{
			smsnotification.waitForCalls(numParts, 1000*120);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	/*private void sendSimpleSms(String cell, String text){
		 SmsManager sms = SmsManager.getDefault();
	     sms.sendTextMessage(cell, null, text, sentPI, null); 
	}*/
}
