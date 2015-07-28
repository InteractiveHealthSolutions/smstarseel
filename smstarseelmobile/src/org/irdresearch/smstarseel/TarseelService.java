package org.irdresearch.smstarseel;

import java.text.ParseException;
import java.util.Date;

import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.util.FileUtil;
import org.irdresearch.smstarseel.util.TarseelLock;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;

public abstract class TarseelService extends IntentService {

	private String LOG_TAG;
	private String SERVICE_UID;
	private Class SERVICE_CLASS;
	private String INITIAL_DELAY_SEC_PREF_NAME;
	private int DEFAULT_DELAY;
	private int DEFAULT_REPEAT_INTERVAL;
	private String REPEAT_INTERVAL_SEC_PREF_NAME;
	
	@SuppressWarnings("rawtypes")
	public TarseelService (String logTag, String serviceUid, Class serviceClass, String startingDelaySecPrefName, String repeatIntervalSecPrefName, int defaultDelay, int defaultRepeatInerval) {
		super(serviceUid);
		LOG_TAG = logTag;
		SERVICE_UID = serviceUid;
		SERVICE_CLASS = serviceClass;
		INITIAL_DELAY_SEC_PREF_NAME = startingDelaySecPrefName;
		REPEAT_INTERVAL_SEC_PREF_NAME = repeatIntervalSecPrefName;
		DEFAULT_DELAY = defaultDelay;
		DEFAULT_REPEAT_INTERVAL = defaultRepeatInerval;
	}

	public String getServiceStartedStatusPropertyName(){
		return SERVICE_CLASS.getSimpleName()+"_isStarted";
	}
	
	public String getServiceLastRunPropertyName(){
		return SERVICE_CLASS.getSimpleName()+"_lastRun";
	}
	
	public String getServiceBusyStatusPropertyName(){
		return SERVICE_CLASS.getSimpleName()+"_isBusy";
	}
	
	public boolean isServiceStarted(Context context){
		String isStarted = TarseelGlobals.getPreference(context, getServiceStartedStatusPropertyName(), null);
		return !TarseelGlobals.isEmptyOrWhiteSpaceOnly(isStarted)&&Boolean.parseBoolean(isStarted);
	}
	
	private boolean setServiceStarted(boolean started, Context context){
		return TarseelGlobals.writePreference(context, getServiceStartedStatusPropertyName(), Boolean.toString(started));
	}
	
	private boolean isServiceBusy(Context context){
		String isBusy = TarseelGlobals.getPreference(context, getServiceBusyStatusPropertyName(), null);
		return !TarseelGlobals.isEmptyOrWhiteSpaceOnly(isBusy)&&Boolean.parseBoolean(isBusy);
	}
	
	private boolean setServiceBusy(boolean started, Context context){
		return TarseelGlobals.writePreference(context, getServiceBusyStatusPropertyName(), Boolean.toString(started));
	}
	
	private int getInitialDelaySecond(Context context){
		int delayInterval = DEFAULT_DELAY;
		try{
			String delaystr= TarseelGlobals.getPreference(context, INITIAL_DELAY_SEC_PREF_NAME, String.valueOf(DEFAULT_DELAY));
			delayInterval = Integer.parseInt(delaystr);
		}
		catch (Exception e) {
			e.printStackTrace();
			String msg = "Error finding delay:"+e.getMessage();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, msg);
	        FileUtil.writeLog(LOG_TAG, msg);
	        FileUtil.writeLog(e);
		}
		
		return delayInterval;
	}
	
	public int getRepeatIntervalSecond(Context context){
		int repeatInterval = DEFAULT_REPEAT_INTERVAL;
		try{
			String reptstr= TarseelGlobals.getPreference(context, REPEAT_INTERVAL_SEC_PREF_NAME, String.valueOf(DEFAULT_REPEAT_INTERVAL));
			repeatInterval = Integer.parseInt(reptstr);
		}
		catch (Exception e) {
			e.printStackTrace();
			String msg = "Error finding repeat interval:"+e.getMessage();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, msg);
	        FileUtil.writeLog(LOG_TAG, msg);
	        FileUtil.writeLog(e);
		}
		return repeatInterval;
	}
	
	/** should never be called without user input*/
	public void startService(Context context){
		try{
			if(isServiceStarted(context) || isServiceBusy(context)){
				String msg = "Service already started or marked busy.. Forcing shutdown and restart";
				TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, msg);
		        FileUtil.writeLog(LOG_TAG, msg);
			}
			//Force remove any timer if accidentally scheduled
			stopTimer(context, SERVICE_CLASS);
			
			int delayInterval = 1000 * getInitialDelaySecond(context);

			setServiceStarted(true, context);

			scheduleNextRun(delayInterval, context, SERVICE_CLASS);
			
			String m = "Started with delay "+delayInterval;
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, m);
	        FileUtil.writeLog(LOG_TAG, m);
		}
		catch (Exception e) {
			e.printStackTrace();
			
			String msg = "Error starting service:"+e.getMessage();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, msg);
	        FileUtil.writeLog(LOG_TAG, msg);
	        FileUtil.writeLog(e);
		}
	}
	
	/** Forces shutdown of service. Make sure that task have been completed before calling method */
	public void shutdownService(Context context)
	{
		try{
			if(!isServiceStarted(context)){
				TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Already shutdown..");
		        FileUtil.writeLog(LOG_TAG, "Already shutdown..");
			}
			
			if(isServiceBusy(context)){
				TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Service busy..");
		        FileUtil.writeLog(LOG_TAG, "Service busy..");
			}
			
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "forcing shutdown");
	        FileUtil.writeLog(LOG_TAG, "forcing shutdown");
	        
			setServiceBusy(false, context);
			
			stopTimer(context, SERVICE_CLASS);
			
			setServiceStarted(false, context);
			
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "shutdown done");
	        FileUtil.writeLog(LOG_TAG, "shutdown done");
		}
		catch (Exception e) {
			e.printStackTrace();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Shutting down threw exception:"+e.getMessage());
	        FileUtil.writeLog(LOG_TAG, "Shutting down threw exception:"+e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void scheduleNextRun(int delaymillis, Context context, Class clazz)
	{
		try{
		    Intent intent = new Intent(context, clazz);
		    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
		    // The update frequency should often be user configurable.  currentservice is not.
		    long currentTimeMillis = System.currentTimeMillis();
		    
			int repeatInterval = 1000 * getRepeatIntervalSecond(context);

		    long nextUpdateTimeMillis = currentTimeMillis + repeatInterval + delaymillis;
		    Time nextUpdateTime = new Time();
		    nextUpdateTime.set(nextUpdateTimeMillis);
	/*
		    if (nextUpdateTime.hour < 8 || nextUpdateTime.hour >= 18)
		    {
		      nextUpdateTime.hour = 8;
		      nextUpdateTime.minute = 0;
		      nextUpdateTime.second = 0;
		      nextUpdateTimeMillis = nextUpdateTime.toMillis(false) + DateUtils.DAY_IN_MILLIS;
		    }*/
		    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		    alarmManager.set(AlarmManager.RTC_WAKEUP, nextUpdateTimeMillis, pendingIntent);
		}catch (Exception e) {
			e.printStackTrace();
			String msg = "scheduling next run threw error:"+e.getMessage();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, msg);
	        FileUtil.writeLog(LOG_TAG, msg);
	        FileUtil.writeLog(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	//Class should be service not inherited one else all services may share same intent causing invalid behaviour
	private void stopTimer(Context context, Class clazz)
	{
	    Intent intent = new Intent(context, clazz);
	    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    alarmManager.cancel(pendingIntent);
	    
	    String msg = "force-stop scheduler";
	    TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, msg);
        FileUtil.writeLog(LOG_TAG, msg);
	}
	
	@Override
	public void onLowMemory() {
		TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "running low memory.");
        FileUtil.writeLog(LOG_TAG, "running low memory.");
	}
	
	private void alertNetworkConnectivity(){
		String datelupd = TarseelGlobals.getPreference(this, TarseelGlobals.LAST_NTWK_UPDATE_SMS_DATE_PREF_NAME, "11-12-2011 00:00:00");
		boolean datediff = false;
		try {
			datediff = TarseelGlobals.DEFAULT_DT_FRMT_SDF.parse(datelupd).before(new Date(new Date().getTime()-1000*60*60L));
		} catch (ParseException e) {
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "getting datediff of last network update sms threw exception:"+e.getMessage());
		}
		if(datelupd == null 
				|| datelupd.trim().equals("")
				|| datediff){
			if(!TarseelGlobals.isNetworkConnectionAvailable(this)){
				TarseelGlobals.sendSmsToAdmin("Wifi or network service Not Available.", this);
				FileUtil.writeLog(LOG_TAG, "Wifi or network service Not Available. datediff:"+datediff);

				TarseelGlobals.writePreference(this, TarseelGlobals.LAST_NTWK_UPDATE_SMS_DATE_PREF_NAME, TarseelGlobals.DEFAULT_DT_FRMT_SDF.format(new Date()));
			}
		}
	}
	
	// if service found not started means anything wrong in logic as it could never be set with out user input, so exit and do force shutdown
	// if service found busy means app did crashed in running/performing actual task logic hence should be marked as not busy by app here.
	// hence change service busy to false as only one intent is processed at a time hence no chance of conflict or multithread
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		TarseelLock tl = null;

		try{
			tl = new TarseelLock(this, LOG_TAG);
			tl.lock();
			
			if(!isServiceStarted(this)){
				String msg = "Service found unstarted.. cannot continue. start using static void startService(Context context)";
				TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, msg);
		        FileUtil.writeLog(LOG_TAG, msg);
		        
		        shutdownService(this);
		        
		        return;
			}
			else {
				try{
					TarseelGlobals.initGlobals(this.getApplication());
				}
				catch (Exception e) {
					e.printStackTrace();
					TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Initing globals threw exception:"+e.getMessage());
			        FileUtil.writeLog(LOG_TAG, "Initing globals threw exception");
			        FileUtil.writeLog(e);
				}
				
				if(isServiceBusy(this)){
					TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Service found busy.. marking as not busy");
			        FileUtil.writeLog(LOG_TAG, "Service found busy.. marking as not busy");
			        
			        setServiceBusy(false, this);
				}
				
				alertNetworkConnectivity();
				
				TarseelGlobals.writePreference(this, getServiceLastRunPropertyName(), TarseelGlobals.DEFAULT_DT_FRMT_SDF.format(new Date()));

				setServiceBusy(true, this);
				
				runTask();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			String msg = "Error running service:"+e.getMessage();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, msg);
	        FileUtil.writeLog(LOG_TAG, msg);
	        FileUtil.writeLog(e);
	        
			scheduleNextRun(1000 * 60, this, SERVICE_CLASS);
		}
		finally{
			if(isServiceBusy(this)){
				setServiceBusy(false, this);
				scheduleNextRun(0, this, SERVICE_CLASS);
			}
			else{
				TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "SERVICE found NOT BUSY at ending task");
		        FileUtil.writeLog(LOG_TAG, "SERVICE found NOT BUSY at ending task");
		    }
			
			if(tl != null) tl.unlock();
		}
		
		System.gc();
	}

	protected abstract void runTask () throws Exception;
}
