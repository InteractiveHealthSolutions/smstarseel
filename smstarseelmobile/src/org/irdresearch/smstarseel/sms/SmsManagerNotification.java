package org.irdresearch.smstarseel.sms;

import java.util.Date;

import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.global.DateUtils;

import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsManager;

public class SmsManagerNotification extends BroadcastReceiver{

	private IntentService	senderIntentService;
	private String			action;
	private int				mCalls;
	private int				mExpectedCalls;
	private Object			mLock;
	private Intent			recievedIntent;
     
	public SmsManagerNotification(IntentService senderIntentService, String action)
	{
		this.senderIntentService = senderIntentService;
		this.action = action;
		 reset();
         mLock = new Object();
	}
	
	void reset() {
        mExpectedCalls = Integer.MAX_VALUE;
        mCalls = 0;
    }
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(action)) {
            synchronized (mLock) {
                mCalls += 1;
                if (mCalls >= mExpectedCalls) {
                    mLock.notify();
                }
            }
        }
		
		this.recievedIntent = intent;
	}
	 public void waitForCalls(int expectedCalls, long timeout) throws InterruptedException {
         synchronized(mLock) {
        	 boolean isTimedOut = false;
        	 
             mExpectedCalls = expectedCalls;
             long startTime = SystemClock.elapsedRealtime();

             while (mCalls < mExpectedCalls) {
                 long waitTime = timeout - (SystemClock.elapsedRealtime() - startTime);
                 if (waitTime > 0) {
                     mLock.wait(waitTime);
                 } else {
                	 isTimedOut = false;  // timed out
                 }
             }
             
         	String sentdate = DateUtils.formatRequestDate(new Date());
            boolean issent = false;
            String errormsg = SmsDispenser.SENDER_NO_ERROR_MESSAGE_STRING;
            String failurecause = null;
            
            switch (getResultCode())
            {
                case Activity.RESULT_OK:
                	issent = true;
                    break;
                    
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                	errormsg = "RESULT_ERROR_GENERIC_FAILURE";
                	failurecause = "GENERIC_FAILURE";
                    break;
                    
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                	errormsg = "RESULT_ERROR_NO_SERVICE";
                	failurecause = "NO_SERVICE";
                    break;
                    
                case SmsManager.RESULT_ERROR_NULL_PDU:
                	errormsg = "RESULT_ERROR_NULL_PDU";
                	failurecause = "NULL_PDU";
                    break;
                    
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                	errormsg = "RESULT_ERROR_RADIO_OFF";
                	failurecause = "RADIO_OFF";
                    break;
            }
            
            String extraMessages = "";
            try{
            	Bundle ex = recievedIntent.getExtras();
            for (String key : ex.keySet())
			{
            	extraMessages = extraMessages + ex.get(key)+",:,";
			}
            
            errormsg = errormsg + " : Trace: " + extraMessages;

            }catch (Exception e) {
				e.printStackTrace();
			}
            
            if(isTimedOut){
                errormsg = errormsg + ": Sender Notifier Timed Out";
            }
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(TarseelGlobals.SMS_SENDER_CALLBACK_IDENTIFIER);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra(SmsDispenser.SENDER_IS_SENT_PARAM, issent);
			broadcastIntent.putExtra(SmsDispenser.SENDER_ERROR_MESSAGE_PARAM, errormsg);
			broadcastIntent.putExtra(SmsDispenser.SENDER_FAILURE_CAUSE_PARAM, failurecause);
			broadcastIntent.putExtra(SmsDispenser.SENDER_SENT_DATE_PARAM, sentdate);

    		senderIntentService.sendBroadcast(broadcastIntent);
    		SmsSender.getSenderStatus().setHasManagerBcNotified(true);
         }
     }
}
