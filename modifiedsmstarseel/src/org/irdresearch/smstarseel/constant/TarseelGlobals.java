package org.irdresearch.smstarseel.constant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Level;
import org.irdresearch.smstarseel.R;
import org.irdresearch.smstarseel.SMSTarseel;
import org.irdresearch.smstarseel.call.CallLogReader;
import org.irdresearch.smstarseel.global.SmsTarseelGlobal;
import org.irdresearch.smstarseel.sms.CleanupService;
import org.irdresearch.smstarseel.sms.SmsCollector;
import org.irdresearch.smstarseel.sms.SmsDispenser;
import org.irdresearch.smstarseel.sms.SmsSender;
import org.irdresearch.smstarseel.sms.SmsSenderNotification;
import org.irdresearch.smstarseel.util.FileUtil;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class TarseelGlobals extends Application{
	
	public static void initGlobals(Application context){
		if(_instance == null){
			_instance = new TarseelGlobals(context);
		}
	}
	
	public static boolean isEmptyOrWhiteSpaceOnly(String str){
		return (str==null||str.trim().equals(""));
	}
	public static boolean ENABLE_UNLOCK = false;
	private static TarseelGlobals _instance;

	// public static Boolean IS_GLOBAL_INIT = false;

	public static final String SMS_SENDER_CALLBACK_IDENTIFIER = "smstarseel.callback.smssender";
	public static final String SMS_MANAGER_CALLBACK_IDENTIFIER = "smstarseel.callback.smsmanager";

	private static StringBuffer CONSOLE_BUFFER = new StringBuffer();

	public static final int CONSOLE_REFRESH_INTERVAL = 1;
	public static SimpleDateFormat DEFAULT_DT_FRMT_SDF = new SimpleDateFormat(SmsTarseelGlobal.DEFAULT_DATE_FORMAT);

	public static final String IMEI_PREF_NAME = "IMEI";
	public static final String SIM_PREF_NAME = "sim_num";
	public static final String MAX_CONSOLE_BUFFER_LN_PREF_NAME = "MAX_CONSOLE_BUFFER_LN";
	public static final String PROJECTS_PREF_NAME = "PROJECTS";
	public static final String MAX_LOG_BACKUP_NUM_PREF_NAME = "MAX_LOG_BACKUP_NUM";
	public static final String MAX_LOG_FILE_SIZE_PREF_NAME = "MAX_LOG_FILE_SIZE";
	public static final String SERVER_URL_PREF_NAME = "server_url";
	public static final String COMM_PORT_NUM_PREF_NAME = "comm_port_num";
	public static final String USERNAME_PREF_NAME = "username";
	public static final String PASSWORD_PREF_NAME = "password";
	public static final String COMM_TYPE_PREF_NAME = "comm_type";
	public static final String ADMIN_CELL_NUM_PREF_NAME = "admin_cell_number";
	public static final String LAST_NTWK_UPDATE_SMS_DATE_PREF_NAME = "last_network_update_sms_date";
	public static final String LOGIN_TIMEOUT_PREF_NAME = "LOGIN_TIMEOUT_SEC";
	public static final String LAST_UNLOCK_TIME_PREF_NAME = "LAST_UNLOCK_TIME";

	private static int MAX_CONSOLE_BUFFER_LN = 5000;
	
	public static void logout(Activity activity){
		if(SmsDispenser.getInstance().isServiceStarted(activity)){
			SmsDispenser.getInstance().shutdownService(activity);
		}
		if(SmsCollector.getInstance().isServiceStarted(activity)){
			SmsCollector.getInstance().shutdownService(activity);
		}
		if(CallLogReader.getInstance().isServiceStarted(activity)){
			CallLogReader.getInstance().shutdownService(activity);
		}
		if(CleanupService.getInstance().isServiceStarted(activity)){
			CleanupService.getInstance().shutdownService(activity);
		}
		
		setLOGGEDIN_USERNAME(activity, null);
		setLOGGEDIN_PASSWORD(activity, null);
		//setPROJECTS(activity, null);
		ENABLE_UNLOCK = false;
		
		SMSTarseel.startLoginForm(activity);
	}
	private TarseelGlobals(Application context)
	{
		String imei = getTelephonyManager(context).getDeviceId();
		if(imei!=null && !imei.trim().equals("")){
			writePreference(context, IMEI_PREF_NAME, imei);
		}
        
        String simnum=getTelephonyManager(context).getLine1Number();
        if(simnum!=null && !simnum.trim().equals("")){
        	writePreference(context, SIM_PREF_NAME, simnum);
        }
        
        resetWiFiSleepPolicy(context);

        MAX_CONSOLE_BUFFER_LN = Integer.parseInt(getPreference(context, MAX_CONSOLE_BUFFER_LN_PREF_NAME, "5000"));
        
        File file = new File(getLogFileDir());
		if (!file.exists())
		{
			file.mkdirs();
		}
		file = new File(getLogFileName());
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
        final LogConfigurator logConfigurator = new LogConfigurator();
        
        try{
	        logConfigurator.setFileName(getLogFileName());
	        logConfigurator.setMaxBackupSize(Integer.parseInt(getPreference(context, MAX_LOG_BACKUP_NUM_PREF_NAME, "10")));
	        logConfigurator.setMaxFileSize(Long.parseLong(getPreference(context, MAX_LOG_FILE_SIZE_PREF_NAME, "2000000")));
	        logConfigurator.setFilePattern("%m%n");
	        logConfigurator.setRootLevel(Level.DEBUG);
	        // Set log level of a specific logger
	        logConfigurator.setLevel("org.apache", Level.ERROR);
	        logConfigurator.configure();
	        addTo_CONSOLE_BUFFER("", "Log configured@"+getLogFileName());
        }
        catch (Exception e) {
			e.printStackTrace();
			addTo_CONSOLE_BUFFER("", "Error configuring log:"+e.getMessage());
		}
	}
	
	public static TelephonyManager getTelephonyManager(Context context){
        return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE));
	}
	
	public static String	IMEI(Context context){
		String IMEI = getPreference(context , IMEI_PREF_NAME, "");
		
		if(IMEI == null || IMEI.trim().equals("")){
			IMEI = getTelephonyManager(context).getDeviceId();
			writePreference(context, IMEI_PREF_NAME, IMEI);
		}
		return IMEI;
	}
	
	public static String	PROJECTS(Context context){
		return getPreference(context, PROJECTS_PREF_NAME, "");
	}
	
	public static void	setPROJECTS(Context context, String PROJECTS){
		writePreference(context, PROJECTS_PREF_NAME, PROJECTS);
	}
	
	public static String	SERVER_URL(Context context){
		return getPreference(context, SERVER_URL_PREF_NAME, "");
	}
	public static void setSERVER_URL(Context context, String SERVER_URL){
		writePreference(context, SERVER_URL_PREF_NAME, SERVER_URL);
	}
	public static String	SIM(Context context){
		String sim = getPreference(context, SIM_PREF_NAME, "");
		if(sim == null || sim.trim().equals("")){
			sim = getTelephonyManager(context).getLine1Number();
			writePreference(context, SIM_PREF_NAME, sim);
		}

		return sim;
	}
	public static void	setSIM(Context context, String sim){
		writePreference(context, SIM_PREF_NAME, sim);
	}
	public static String LOGGEDIN_PASSWORD(Context context) {
		return getPreference(context, PASSWORD_PREF_NAME, "");
	}
	public static void setLOGGEDIN_PASSWORD(Context context, String lOGGEDIN_PASSWORD) {
		writePreference(context, PASSWORD_PREF_NAME, lOGGEDIN_PASSWORD);
	}
	public static String LOGGEDIN_USERNAME(Context context) {
		return getPreference(context, USERNAME_PREF_NAME, "");
	}
	public static void setLOGGEDIN_USERNAME(Context context, String lOGGEDIN_USERNAME) {
		writePreference(context, USERNAME_PREF_NAME, lOGGEDIN_USERNAME);
	}
	/*public static void	set_SERVER_URL(Context context, String serverurl){
		writePreference(context, SERVER_URL_PREF_NAME, serverurl);
	}*/
	// added as an instance method to an Activity
	public static boolean isNetworkConnectionAvailable(Context context) {  
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getActiveNetworkInfo();     
	    if (info == null) return false;
	    return info.isConnectedOrConnecting();
	}
	
	public static boolean writePreference(Context context, String name, String value){
		PreferenceManager.setDefaultValues(context, R.xml.preferences, true);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		Editor ed = pref.edit();
		ed.putString(name, value);
		return ed.commit();
	}
	public static void sendSmsToAdmin(String sms, Context context){
		try{
			String admincellnumber = getPreference(context, ADMIN_CELL_NUM_PREF_NAME, null);
			
			if(isEmptyOrWhiteSpaceOnly(admincellnumber)){
				return;
			}
			
			while(!SmsSender.isSmsSenderServiceFree()){
				try
				{
					addTo_CONSOLE_BUFFER("sendSmsToAdmin","Send Sms service busy");
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			SmsSender.lockSmsSender(sms);
	
			IntentFilter filter = new IntentFilter(SMS_SENDER_CALLBACK_IDENTIFIER);
			filter.addCategory(Intent.CATEGORY_DEFAULT);
	
			SmsSenderNotification notification = new SmsSenderNotification();
			context.registerReceiver(notification, filter);
	
			try{
				Intent in = new Intent(context, SmsSender.class);
				in.putExtra(SmsDispenser.SENDER_CELL_PARAM, admincellnumber);
				in.putExtra(SmsDispenser.SENDER_TEXT_PARAM, sms);
		
				context.startService(in);
				
				while(!SmsSender.isSmsSenderServiceFree()){
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
			}
			finally{
				context.unregisterReceiver(notification);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			FileUtil.writeLog("sendSmsToAdmin","Sending Sms to ADMIN threw exception.");
			FileUtil.writeLog(e);
		}
	}
	public static  void resetWiFiSleepPolicy(Context context){
		Settings.System.putInt(context.getContentResolver(),
		        Settings.System.WIFI_SLEEP_POLICY, 
		        Settings.System.WIFI_SLEEP_POLICY_NEVER);
	}
	
	public static String getPreference(Context context, String key, String defaultVal){
		PreferenceManager.setDefaultValues(context, R.xml.preferences, true);
		return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultVal);
	}
	
	/** Logs to console view and returns true if successful 
	 * @return */
	public static boolean addTo_CONSOLE_BUFFER(String tag, String string){
		try{
			if(CONSOLE_BUFFER.length() > MAX_CONSOLE_BUFFER_LN){
				CONSOLE_BUFFER = new StringBuffer(CONSOLE_BUFFER.substring(CONSOLE_BUFFER.length() - MAX_CONSOLE_BUFFER_LN));
			}
			CONSOLE_BUFFER.append("\n" + (tag==null?"":tag+": ") + new SimpleDateFormat("dd-HH:mm").format(new Date()) + "-" + string);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static StringBuffer get_CONSOLE_BUFFER(){
		return CONSOLE_BUFFER;
	}
	public static Map<String, ?> getGlobalAttribs(Context context){
		StringBuilder gb = new StringBuilder();
		gb.append("\n<Max buffer(console)>: " + MAX_CONSOLE_BUFFER_LN);
		
		Map<String, ?> map = getPreferences(context);
		Map<String, ?> treeMap = new TreeMap(map);
		
		for (String key : treeMap.keySet())
		{
			gb.append("\n<"+key + ">:" + map.get(key));
		}
		
		//return gb;
		return treeMap;
	}
	public static Map<String, ?> getPreferences(Context context) {
		PreferenceManager.setDefaultValues(context, R.xml.preferences, true);
		return PreferenceManager.getDefaultSharedPreferences(context).getAll();
	}

	/*private String getlogDirPath(){
		return  Environment.getExternalStorageDirectory() + File.separator	+ "smstarseel";
	}*/
	private static String getLogFileName(){
		return getLogFileDir()+ File.separator + "tarseel.log";
	}
	public static String getLogFileDir(){
		return Environment.getExternalStorageDirectory() + File.separator	+ "smstarseel";
	}
	/*private File getLogFile(){
		final LogConfigurator logConfigurator = new LogConfigurator();
	                
	        logConfigurator.setFileName(getLogFileName());
	        logConfigurator.setMaxBackupSize(10);
	        logConfigurator.setMaxFileSize(MAX_FILE_SIZE);
	        logConfigurator.setRootLevel(Level.DEBUG);
	        // Set log level of a specific logger
	        logConfigurator.setLevel("org.apache", Level.ERROR);
	        logConfigurator.configure();
		File file = new File(getlogDirPath());
		if (!file.exists())
		{
			file.mkdirs();
		}
		file = new File(file.getPath() + File.separator + getLogFileName());
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return file;
	}*/
	
	public static File LOG_FILE(){
		return new File(getLogFileName());
	}
	
	public enum ErrorMessages{
		NO_ERROR("no error"),
		LOGGEDIN_USER_CREDENTIALS_NOT_MATCHES("Given username or password donot matches with logged in user"),
		EMPTY_USER_CREDENTIALS("Empty or whitespace-only username or password is unacceptable"),
		PASSWORD_MIN_LENGTH_ERROR("Password should have minimum length of " + SmsTarseelGlobal.PASSWORD_MIN_LENGTH),
		USERNAME_MIN_LENGTH_ERROR("Username should have minimum length of " + SmsTarseelGlobal.USERNAME_MIN_LENGTH),
		CONNECTION_ERROR("Check if server is running on given url or if network is accessible."),
		APPLICATION_CONNECTION_ERROR("Make sure that Smstarseel application is hosted and running on specified server."),
		INVALID_SERVERURL("No valid server url is specified"),
		INVALID_COMMPORT("No valid port number is specified"),
		COMM_TYPE_MISSING("A comm type must be specified"),
		INVALID_USERNAME_CHARACTERS("Phone only allows alpha, digits and _ ; . - ~ @ and spaces in user names"),
		INVALID_PASSWORD_CHARACTERS("Phone only allows alpha, digits and _ ; . - ~ @ and spaces in password"),;

		private String MESSAGE;
		
		private ErrorMessages(String message){
			this.MESSAGE = message;
		}
		public String MESSAGE()
		{
			return MESSAGE;
		}
		private ErrorMessages()
		{
			// TODO Auto-generated constructor stub
		}@Override
		public String toString()
		{
			return MESSAGE;
		}
	}
	
	public enum COMM_MODE{
		//USB,
		WIFI,
		//BLUETOOTH
	}
}
