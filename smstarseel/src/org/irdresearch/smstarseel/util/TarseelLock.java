package org.irdresearch.smstarseel.util;

import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.constant.TarseelGlobals.COMM_MODE;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class TarseelLock 
{
	private static final String LOG_TAG = "TarseelLock";
	private WifiLock wifiLock;
    private WakeLock wakeLock;
 
    public void lock() {
        try {
            wakeLock.acquire();
            if(wifiLock != null){
           		wifiLock.acquire();
            }
        } 
        catch(Exception e) {
        	e.printStackTrace();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "locking error:"+e.getMessage());
	        FileUtil.writeLog(LOG_TAG, e.getMessage());
	        FileUtil.writeLog(e);
        }
    }
 
    public void unlock() {
		try {
			if (wakeLock.isHeld())
				wakeLock.release();
			if (wifiLock != null && wifiLock.isHeld())
				wifiLock.release();
		} 
		catch (Exception e) {
			e.printStackTrace();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, e.getMessage());
			FileUtil.writeLog(LOG_TAG, "unlocking error:"+e.getMessage());
			FileUtil.writeLog(e);
		}
    }
 
    public TarseelLock(Context context, String callerID)
    {
        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE))
                        .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, callerID);
        
    	String commType = TarseelGlobals.getPreference(context, TarseelGlobals.COMM_TYPE_PREF_NAME, null);
		if(commType.equalsIgnoreCase(COMM_MODE.WIFI.name()))
		{
        wifiLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                        .createWifiLock(WifiManager.WIFI_MODE_FULL , callerID);
		}
    }
}
