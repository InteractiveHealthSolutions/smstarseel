package org.irdresearch.smstarseel;

import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.util.FileUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MmsReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.provider.Telephony.WAP_PUSH_DELIVER".equals(intent.getAction())) {
        	try{
            	TarseelGlobals.addTo_CONSOLE_BUFFER("MmsReceiver", "MMS RECEIVED");
        	}
        	catch (Exception e) {
				e.printStackTrace();
				FileUtil.writeLog("SmsReceiver", "Error :"+e.getMessage());
		        FileUtil.writeLog(e);
			}
        }
	}
}
