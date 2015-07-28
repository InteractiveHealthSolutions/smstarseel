package org.irdresearch.smstarseel;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.irdresearch.smstarseel.comm.HttpSender;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.constant.TarseelGlobals.ErrorMessages;
import org.irdresearch.smstarseel.dialogue.ErrorAlert;
import org.irdresearch.smstarseel.form.SmsTarseelForm;
import org.irdresearch.smstarseel.global.RequestParam;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.ProjectParams;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.status.ConsoleActivity;
import org.irdresearch.smstarseel.status.SendLogToServerActivity;
import org.irdresearch.smstarseel.status.StatusActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SMSTarseel extends SmsTarseelForm {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
    	TarseelGlobals.initGlobals(getApplication());
        startLoginForm(this);
    }

    public void closeApplication(){
    	//TODO stop services
    	//TODO shutdown services
    	//TODO send finalization msg to server
    	//TODO close application...
    	finish();
    	
    }

    public static void startLoginForm(Activity activity){
//        //TARSEEL_GLOBALS().IS_SCREEN_LOCKED = true;
        Intent loginIntent = new Intent(activity.getBaseContext() , LoginActivity.class);
        activity.startActivity(loginIntent);
    }
    
    public static void startHomeScreen(Activity activity){
    	Intent homestarter = new Intent(activity.getBaseContext() , HomeActivity.class);
    	activity.startActivity( homestarter);
    }
    
/*    public static void showClock(Activity activity){
    	Intent clockstarter = new Intent(activity.getBaseContext() , ClockDialogue.class);
    	activity.startActivity( clockstarter);
    }
    
    public static void showLog(Activity activity){
    	Intent logstarter = new Intent(activity.getBaseContext() , LogActivity.class);
    	activity.startActivity( logstarter);
    }*/
    
	public static void sendLogToServer(Activity activity) {
		Intent logliststarter = new Intent(activity.getBaseContext() , SendLogToServerActivity.class);
    	activity.startActivity( logliststarter);
	}
	
    public static void showSettingForm(Activity activity){
    	Intent setstarter = new Intent(activity.getBaseContext() , SettingActivity.class);
    	activity.startActivity( setstarter);
    }
    
    public static void showConsole(Activity activity){
    	Intent consolestarter = new Intent(activity.getBaseContext() , ConsoleActivity.class);
    	activity.startActivity( consolestarter);
    }
    
    public static void showStatus(Activity activity){
    	Intent statusstarter = new Intent(activity.getBaseContext() , StatusActivity.class);
    	activity.startActivity( statusstarter);
    }
    
    public static void startRegisterDeviceProjectForm(Activity activity, boolean justloggedin){
    	JSONObject resp;
    	
		try
		{
			resp = HttpSender.sendLargeText(activity, createProjectListRequest(activity));
		
			if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.ERROR.CODE())){
				ErrorAlert.showError(activity, (String)resp.get(ResponseMessage.NAME));
			}
			else if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.SUCCESS.CODE()))
			{
		    	Intent registerdevicestarter = new Intent(activity , RegisterDeviceActivity.class);
				Bundle data = new Bundle();
				
				ArrayList<String> prjs = new ArrayList<String>();
				
				JSONArray arr = (JSONArray)resp.get(ProjectParams.LIST_ID.KEY());
				for (int i = 0; i < arr.length(); i++)
				{
					JSONObject obj = arr.getJSONObject(i); 
					prjs.add((String) obj.get(ProjectParams.NAME.KEY()));
				}
				
				data.putStringArrayList(ProjectParams.LIST_ID.KEY(), prjs);
				data.putBoolean("justloggedin", justloggedin);
				registerdevicestarter.putExtras(data);
				activity.startActivity(registerdevicestarter);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			ErrorAlert.showError(activity, "Unexpected error. Contact program vendor. Details are :"+e.getMessage());
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
			ErrorAlert.showError(activity, ErrorMessages.CONNECTION_ERROR+". Details are :"+e.getMessage());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			ErrorAlert.showError(activity, ErrorMessages.CONNECTION_ERROR+". Details are :"+e.getMessage());
		}
    }
    
    private static SmsTarseelRequest createProjectListRequest(Activity activity){
    	SmsTarseelRequest req = null ;
		try
		{
			req = new SmsTarseelRequest(activity, App_Service.QUERY_PROJECTLIST);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return req;
    }
	@Override
	protected SmsTarseelRequest createRequest()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean validateFormData()
	{
		// TODO Auto-generated method stub
		return false;
	}

}