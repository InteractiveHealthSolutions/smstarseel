package org.irdresearch.smstarseel.call;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.irdresearch.smstarseel.TarseelService;
import org.irdresearch.smstarseel.comm.HttpSender;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.global.DateUtils;
import org.irdresearch.smstarseel.global.RequestParam;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.CallLogParams;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.provider.CallLog.Calls;

public class CallLogReader extends TarseelService
{
	private static CallLogReader _instance;
	public static CallLogReader getInstance(){
		if(_instance == null){
			_instance = new CallLogReader();
		}
		return _instance;
	}
	
	/** WARNING - USE getInstance() method*/
	public CallLogReader() {
		super(LOG_TAG, SERVICE_UID, CallLogReader.class, CALLLOG_READER_INITIAL_DELAY_SEC_PREF_NAME, CALLLOG_READER_RUN_INTERVAL_SEC_PREF_NAME, 60, 150);
	}

	private static final String LOG_TAG = "CallLogReader";
	private static final String SERVICE_UID = "org.irdresearch.smstarseel.call.CallLogReader";

	private static final String CALLLOG_READER_RUN_INTERVAL_SEC_PREF_NAME = "CALLLOG_READER_RUN_INTERVAL_SEC";
	private static final String CALLLOG_READER_INITIAL_DELAY_SEC_PREF_NAME = "CALLLOG_READER_INITIAL_DELAY_SEC";
	
	private static final String CALLLOG_READER_FETCHSIZE_PREF_NAME = "CALLLOG_READER_FETCHSIZE";

	protected void runTask() throws Exception
	{
		final String myPackageName = getPackageName();
        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
            // App is not default.
        	TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "NOT default app. Cannot proceed.");
	        FileUtil.writeLog(LOG_TAG, "NOT default app. Cannot proceed.");
		}
        
		int fetchsize = 6;
		try{
			fetchsize = Integer.parseInt(TarseelGlobals.getPreference(this, CALLLOG_READER_FETCHSIZE_PREF_NAME,"6"));
		}
		catch (Exception e) {
			e.printStackTrace();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Error finding calllog fetchsize:"+e.getMessage());
	        FileUtil.writeLog(LOG_TAG, "Error finding calllog fetchsize:"+e.getMessage());
	        FileUtil.writeLog(e);
		}
		
		while (true){
			final ArrayList<Map<String, String>> list = readCallLog(getContentResolver(), fetchsize);
			TarseelGlobals.addTo_CONSOLE_BUFFER(null, "read "+list.size()+" calls");

			final SmsTarseelRequest payload = new SmsTarseelRequest(this, App_Service.SUBMIT_CALL_LOG);
			JSONArray callList = new JSONArray();
			
			for (Map<String, String> item : list)
			{
				final JSONObject calllog = new JSONObject();
				calllog.put(CallLogParams.SYSTEM_PROCESS_START_DATE.KEY(), DateUtils.formatRequestDate(new Date()));

				calllog.put(CallLogParams.CALLID.KEY(), item.get(Calls._ID));
				calllog.put(CallLogParams.DURATION.KEY(), item.get(Calls.DURATION));
				calllog.put(CallLogParams.IS_SAVED.KEY(), false);
				
				final String date = DateUtils.formatRequestDate(new Date(Long.parseLong((String)item.get(Calls.DATE))));
				calllog.put(CallLogParams.CALL_DATE.KEY(), date);
				calllog.put(CallLogParams.CALLER_NUM.KEY(), item.get(Calls.NUMBER));
				calllog.put(CallLogParams.SIM.KEY(), TarseelGlobals.SIM(this));
				int type = Integer.parseInt(item.get(Calls.TYPE));
				String callType="";
				
				switch (type) {
				case Calls.INCOMING_TYPE:
					callType = "INCOMING";
						break;

				case Calls.OUTGOING_TYPE:
					callType = "OUTGOING";
						break;
						
				case Calls.MISSED_TYPE:
					callType = "MISSED";
						break;
				}
				calllog.put(CallLogParams.TYPE.KEY(), callType);
				
				callList.put(calllog);
			}
		
			payload.addObjectList(CallLogParams.LIST_ID.KEY(), callList);
			
			final JSONObject resp = HttpSender.sendLargeText(this, payload);
			
			if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.ERROR.CODE()))
			{
				TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Unable to retrieve call log submit confirmation, Error :"+(String)resp.get(ResponseMessage.NAME));
		        FileUtil.writeLog(LOG_TAG, "Unable to retrieve recieved calllog submit confirmation, Error :"+(String)resp.get(ResponseMessage.NAME));
			}
			else{
				final JSONArray savedcallList = (JSONArray)resp.get(CallLogParams.LIST_ID.KEY());
				
				if(savedcallList.length() > 0){
					for (int i = 0; i < savedcallList.length(); i++)
					{
						final JSONObject call = savedcallList.getJSONObject(i);
						
						if(call.getBoolean(CallLogParams.IS_SAVED.KEY())){
							deleteCall((String)call.get(CallLogParams.CALLID.KEY()));
						}
					}
				}
			}
			
			if(list.size() == 0){
				break;
			}
		}
	}
	
	
	private void deleteCall(String callId){
		try{
			/*Cursor a = getContentResolver().query(Uri.parse("content://call_log/calls"), null, ""+Calls._ID+"=?", new String[]{callId}, null);
			while (a.moveToNext())
			{
				String ss="";
				for (int i = 0; i < a.getColumnCount(); i++)
				{
					ss+= "cname:"+a.getColumnName(i)+", vl:"+a.getString(i)+"\n";
				}
		       // System.out.println(ss);
			}*/
			
			System.out.println("calllog rowsdeleted:"+getContentResolver().delete(Uri.parse("content://call_log/calls"),  ""+Calls._ID+"=?", new String[]{callId}));
		}
		catch (Exception e) {
			e.printStackTrace();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "calllog delete error:"+e.getMessage());
			FileUtil.writeLog(LOG_TAG, "Deleting calllog threw exception: "+e.getMessage());
			FileUtil.writeLog(e);
		}
	}
	
	private ArrayList<Map<String, String>> readCallLog(ContentResolver cr, int fetchsize){
		ArrayList<Map<String, String>> list = new ArrayList<Map<String,String>>();

		Cursor cur = null;
		try{
			//reading all data in descending order according to DATE
		    //String strOrder = Calls.DATE + " DESC";
		    Uri callUri = Uri.parse("content://call_log/calls");
		    cur = cr.query(callUri, null, null, null, null);
		    // loop through cursor
		    int i = 0;
			while (i < fetchsize && cur.moveToNext())
			{
				Map<String, String> map = new HashMap<String, String>();
	
				map.put(Calls.NUMBER, cur.getString(cur.getColumnIndex(Calls.NUMBER)));
				map.put(Calls.DATE, cur.getString(cur.getColumnIndex(Calls.DATE)));
				map.put(Calls.TYPE, cur.getString(cur.getColumnIndex(Calls.TYPE)));
				map.put(Calls.NEW, cur.getString(cur.getColumnIndex(Calls.NEW)));
				map.put(Calls.DURATION, cur.getString(cur.getColumnIndex(Calls.DURATION)));
				map.put(Calls._ID, cur.getString(cur.getColumnIndex(Calls._ID)));
	
				list.add(map);
				
				i++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "calllog reading error:"+e.getMessage());
			FileUtil.writeLog(LOG_TAG, "Reading calllog threw exception: "+e.getMessage());
			FileUtil.writeLog(e);
		}
		finally{
			if(cur != null) 
				if (!cur.isClosed())
					cur.close();
		}
	    return list;
	}
}
