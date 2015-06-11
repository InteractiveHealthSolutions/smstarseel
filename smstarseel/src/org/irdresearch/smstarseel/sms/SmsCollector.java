package org.irdresearch.smstarseel.sms;

import org.irdresearch.smstarseel.TarseelService;

public class SmsCollector extends TarseelService
{
	private static SmsCollector _instance;
	public static SmsCollector getInstance(){
		if(_instance == null){
			_instance = new SmsCollector();
		}
		return _instance;
	}
	
	/** WARNING - USE getInstance() method*/
	public SmsCollector() {
		super(LOG_TAG, SERVICE_UID, SmsCollector.class, SMS_COLLECTOR_INITIAL_DELAY_SEC_PREF_NAME, SMS_COLLECTOR_RUN_INTERVAL_SEC_PREF_NAME, 60, 90);
	}
	
	private static final String LOG_TAG = "SmsCollector";
	private static final String SERVICE_UID = "org.irdresearch.smstarseel.sms.SmsCollector";
	
	private static final String SMS_COLLECTOR_RUN_INTERVAL_SEC_PREF_NAME = "SMS_COLLECTOR_RUN_INTERVAL_SEC";
	private static final String SMS_COLLECTOR_INITIAL_DELAY_SEC_PREF_NAME = "SMS_COLLECTOR_INITIAL_DELAY_SEC";
	
	private static final String SMS_COLLECTOR_FETCHSIZE_PREF_NAME = "SMS_COLLECTOR_FETCHSIZE";
	protected void runTask() throws Exception
	{

        
	}
}
