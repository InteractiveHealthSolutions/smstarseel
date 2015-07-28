package org.irdresearch.smstarseel;

import org.irdresearch.smstarseel.call.CallLogReader;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.sms.SmsCollector;
import org.irdresearch.smstarseel.sms.SmsDispenser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TarseelAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            // reset all needed preferences
        	try{
        		TarseelGlobals.setLOGGEDIN_PASSWORD(context, null);
        		TarseelGlobals.setLOGGEDIN_USERNAME(context, null);
        		TarseelGlobals.writePreference(context, SmsDispenser.getInstance().getServiceBusyStatusPropertyName(), null);
        		TarseelGlobals.writePreference(context, SmsDispenser.getInstance().getServiceStartedStatusPropertyName(), null);
        		TarseelGlobals.writePreference(context, SmsCollector.getInstance().getServiceBusyStatusPropertyName(), null);
        		TarseelGlobals.writePreference(context, SmsCollector.getInstance().getServiceStartedStatusPropertyName(), null);
        		TarseelGlobals.writePreference(context, CallLogReader.getInstance().getServiceBusyStatusPropertyName(), null);
        		TarseelGlobals.writePreference(context, CallLogReader.getInstance().getServiceStartedStatusPropertyName(), null);
        	}
        	catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
}
