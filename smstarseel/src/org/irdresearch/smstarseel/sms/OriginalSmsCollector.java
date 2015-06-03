package org.irdresearch.smstarseel.sms;

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
import org.irdresearch.smstarseel.global.RequestParam.InboundSmsParams;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.net.Uri;

public class OriginalSmsCollector extends TarseelService
{
	private static OriginalSmsCollector _instance;
	public static OriginalSmsCollector getInstance(){
		if(_instance == null){
			_instance = new OriginalSmsCollector();
		}
		return _instance;
	}
	
	/** WARNING - USE getInstance() method*/
	public OriginalSmsCollector() {
		super(LOG_TAG, SERVICE_UID, OriginalSmsCollector.class, SMS_COLLECTOR_INITIAL_DELAY_SEC_PREF_NAME, SMS_COLLECTOR_RUN_INTERVAL_SEC_PREF_NAME, 60, 90);
	}
	
	private static final String LOG_TAG = "SmsCollector";
	private static final String SERVICE_UID = "org.irdresearch.smstarseel.sms.SmsCollector";

	private static final String SMS_ID = "_id";//  (long)
	private static final String SMS_THREAD_ID = "thread_id";//   (long)
	private static final String SMS_ADDRESS = "address";//   (String)
	//private static final String SMS_PERSON = "person";//   (String)
	private static final String SMS_DATE = "date";//     (long)
	//private static final String protocol
	private static final String SMS_READ = "read";
	private static final String SMS_STATUS = "status";
	private static final String SMS_TYPE = "type";
	//private static final String reply_path_present
	//private static final String subject    (String)
	private static final String SMS_BODY = "body";//    (String)
	//private static final String service_center
	//private static final String locked
	//private static final String error_code
	//private static final String seen
	
	private static final String SMS_COLLECTOR_RUN_INTERVAL_SEC_PREF_NAME = "SMS_COLLECTOR_RUN_INTERVAL_SEC";
	private static final String SMS_COLLECTOR_INITIAL_DELAY_SEC_PREF_NAME = "SMS_COLLECTOR_INITIAL_DELAY_SEC";
	
	private static final String SMS_COLLECTOR_FETCHSIZE_PREF_NAME = "SMS_COLLECTOR_FETCHSIZE";

	protected void runTask() throws Exception
	{
			int fetchsize = 6;
			try{
				fetchsize = Integer.parseInt(TarseelGlobals.getPreference(this, SMS_COLLECTOR_FETCHSIZE_PREF_NAME,"6"));
			}
			catch (Exception e) {
				e.printStackTrace();
				TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Error finding inbox fetchsize:"+e.getMessage());
		        FileUtil.writeLog(LOG_TAG, "Error finding inbox fetchsize:"+e.getMessage());
		        FileUtil.writeLog(e);
			}
			
			while (true){
				final ArrayList<Map<String, String>> readlist = readInbox(new String[]{SMS_ID,SMS_THREAD_ID,SMS_ADDRESS,SMS_DATE,SMS_BODY,SMS_TYPE,SMS_STATUS,SMS_READ}, fetchsize);
				TarseelGlobals.addTo_CONSOLE_BUFFER(null, "read "+readlist.size()+" smses");

				final SmsTarseelRequest payload = new SmsTarseelRequest(this, App_Service.SUBMIT_RECIEVED_SMS);
				JSONArray smsList = new JSONArray();
				
				for (Map<String, String> item : readlist)
				{
					final JSONObject inbound = new JSONObject();
					inbound.put(InboundSmsParams.SYSTEM_PROCESS_START_DATE.KEY(), DateUtils.formatRequestDate(new Date()));

					inbound.put(InboundSmsParams.SMSID.KEY(), item.get(SMS_ID));
					inbound.put(InboundSmsParams.THREADID.KEY(), item.get(SMS_THREAD_ID));
					inbound.put(InboundSmsParams.IS_SAVED.KEY(), false);
					
					final String recvDt = DateUtils.formatRequestDate(new Date(Long.parseLong((String)item.get(SMS_DATE))));
					inbound.put(InboundSmsParams.RECIEVED_DATE.KEY(), recvDt);
					//we need date in long to delete sms from inbox
					inbound.put(InboundSmsParams.RECIEVED_DATE_IN_LONG.KEY(), (String)item.get(SMS_DATE));
					inbound.put(InboundSmsParams.SENDER_NUM.KEY(), item.get(SMS_ADDRESS));
					inbound.put(InboundSmsParams.SIM.KEY(), TarseelGlobals.SIM(this));
					//inbound.put(InboundSmsParams.SYSTEM_RECIEVED_DATE.KEY(), recvDt);
					inbound.put(InboundSmsParams.TEXT.KEY(), item.get(SMS_BODY));
					inbound.put(InboundSmsParams.TYPE.KEY(), item.get(SMS_TYPE));
					
					smsList.put(inbound);
				}
				
				payload.addObjectList(InboundSmsParams.LIST_ID.KEY(), smsList);
				
				final JSONObject resp = HttpSender.sendLargeText(this, payload);
				
				if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.ERROR.CODE()))
				{
					TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Unable to retrieve recieved sms submit confirmation, Error :"+(String)resp.get(ResponseMessage.NAME));
			        FileUtil.writeLog(LOG_TAG, "Unable to retrieve recieved sms submit confirmation, Error :"+(String)resp.get(ResponseMessage.NAME));
				}
				else{
					final JSONArray smslist = (JSONArray)resp.get(InboundSmsParams.LIST_ID.KEY());
					
					if(smslist.length() > 0){
						for (int i = 0; i < smslist.length(); i++)
						{
							final JSONObject sms = smslist.getJSONObject(i);
							
							if(sms.getBoolean(InboundSmsParams.IS_SAVED.KEY())){
								deleteSms((String)sms.get(InboundSmsParams.SMSID.KEY()), 
										(String)sms.get(InboundSmsParams.THREADID.KEY()),
										(String)sms.get(InboundSmsParams.RECIEVED_DATE_IN_LONG.KEY()));
							}
						}
					}
				}
				if(readlist.size() == 0){
					break;
				}
			}
		}
	
	private void deleteSms(String smsId, String smsThreadId,String recieveDateInlong){
		try{
			/*Cursor a = getContentResolver().query(Uri.parse("content://sms"), null, ""+SMS_ID+"=? and "+SMS_ADDRESS+"=? and "+SMS_THREAD_ID+"=? and "+SMS_DATE+"=?", new String[]{smsId,senderNum,smsThreadId,recieveDateInlong}, null);
			while (a.moveToNext())
			{String ss="";
				for (int i = 0; i < a.getColumnCount(); i++){
					ss+= "cname:"+a.getColumnName(i)+", vl:"+a.getString(i)+"\n";
				}
		        System.out.println(ss);
			}*/
			System.out.println("rowsdeleted:"+getContentResolver().delete(Uri.parse("content://sms"),  ""+SMS_ID+"=? and "+SMS_THREAD_ID+"=? and "+SMS_DATE+"=?", new String[]{smsId,smsThreadId,recieveDateInlong}));
		}
		catch (Exception e) {
			e.printStackTrace();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Deleting sms threw exception:"+e.getMessage());
	        FileUtil.writeLog(LOG_TAG, "Deleting sms threw exception:"+e.getMessage());
	        FileUtil.writeLog(e);
		}
	}
	private ArrayList<Map<String, String>> readInbox(String[] fieldsToRead, int fetchsize)
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Cursor cur = null;
		try{
			Uri uriSMSURI = Uri.parse("content://sms/inbox");
			cur = getContentResolver().query(uriSMSURI, fieldsToRead, null, null, null);
			int i = 0;
			while (i < fetchsize && cur.moveToNext())
			{
				Map<String, String> map = new HashMap<String,String>();
				for (String string : fieldsToRead)
				{
					map.put(string, cur.getString(cur.getColumnIndex(string)));
				}
				list.add(map);
				
				i++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Reading inbox threw exception:"+e.getMessage());
	        FileUtil.writeLog(LOG_TAG, "Reading inbox threw exception:"+e.getMessage());
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
