package org.irdresearch.smstarseel;

import java.util.HashMap;
import java.util.Map;

import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.sms.SmsCollector;
import org.irdresearch.smstarseel.util.FileUtil;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.provider.Telephony.SMS_DELIVER".equals(intent.getAction())) {
        	try{
            	TarseelGlobals.addTo_CONSOLE_BUFFER("SmsReceiver", "SMS RECEIVED");
            	
            	Map<String, String> msg = retrieveMessages(intent);
            	//TODO use msg date instead of new date
            	for (String key : msg.keySet()) {
            		ContentValues values = new ContentValues(); 
            		values.put(SmsCollector.SMS_ADDRESS, key); 
            		values.put(SmsCollector.SMS_RECEIVE_DATE, System.currentTimeMillis()+""); 
            		values.put(SmsCollector.SMS_READ, "1"); 
            		values.put(SmsCollector.SMS_TYPE, "1"); 
            		values.put(SmsCollector.SMS_BODY,msg.get(key)); 
            		//values.put(SmsCollector.SMS_STATUS,status); 
            		Uri uri = Uri.parse("content://sms/"); 
            		context.getContentResolver().insert(uri,values);
				}
        	}
        	catch (Exception e) {
				e.printStackTrace();
				FileUtil.writeLog("SmsReceiver", "Error :"+e.getMessage());
		        FileUtil.writeLog(e);
			}
        }
	}
	
	 private static Map<String, String> retrieveMessages(Intent intent) {
	        Map<String, String> msg = null; 
	        SmsMessage[] msgs;
	        Bundle bundle = intent.getExtras();
	        
	        if (bundle != null && bundle.containsKey("pdus")) {
	            Object[] pdus = (Object[]) bundle.get("pdus");

	            if (pdus != null) {
	                int nbrOfpdus = pdus.length;
	                msg = new HashMap<String, String>(nbrOfpdus);
	                msgs = new SmsMessage[nbrOfpdus];
	                
	                // There can be multiple SMS from multiple senders, there can be a maximum of nbrOfpdus different senders
	                // However, send long SMS of same sender in one message
	                for (int i = 0; i < nbrOfpdus; i++) {
	                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
	                    
	                    String originatinAddress = msgs[i].getOriginatingAddress();
	                    
	                    // Check if index with number exists                    
	                    if (!msg.containsKey(originatinAddress)) { 
	                        // Index with number doesn't exist                                               
	                        // Save string into associative array with sender number as index
	                        msg.put(msgs[i].getOriginatingAddress(), msgs[i].getMessageBody()); 
	                        
	                    } else {    
	                        // Number has been there, add content but consider that
	                        // msg.get(originatinAddress) already contains sms:sndrNbr:previousparts of SMS, 
	                        // so just add the part of the current PDU
	                        String previousparts = msg.get(originatinAddress);
	                        String msgString = previousparts + msgs[i].getMessageBody();
	                        msg.put(originatinAddress, msgString);
	                    }
	                }
	            }
	        }
	        
	        return msg;
	    }

}
