package org.irdresearch.smstarseel;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.irdresearch.smstarseel.call.CallLogReader;
import org.irdresearch.smstarseel.comm.HttpSender;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.constant.TarseelGlobals.ErrorMessages;
import org.irdresearch.smstarseel.dialogue.ConfirmPopup;
import org.irdresearch.smstarseel.dialogue.ErrorAlert;
import org.irdresearch.smstarseel.form.SmsTarseelForm;
import org.irdresearch.smstarseel.global.RequestParam;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.DeviceRegisterParam;
import org.irdresearch.smstarseel.global.RequestParam.ProjectParams;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.sms.CleanupService;
import org.irdresearch.smstarseel.sms.SmsCollector;
import org.irdresearch.smstarseel.sms.SmsDispenser;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class RegisterDeviceActivity extends SmsTarseelForm
{
	private Spinner		projList;
	private EditText	simnum;
	private TextView 	name;
	boolean justloggedin;
	private TextView screenname;
	private TextView welcome;
	private TextView doitline;
	private TextView projectname;
	private TextView projectnote;
	
	//private TextView	message;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerdevice);
		
		projList = (Spinner)  findViewById(R.id.devregisterddp);
		simnum = (EditText) findViewById(R.id.simnumedtxt);
		
		// to get user name from login screen
		
		
		
		screenname = (TextView)  findViewById(R.id.registernote);
		welcome = (TextView)  findViewById(R.id.welcome);
		doitline = (TextView)  findViewById(R.id.doitnote);
		name = (TextView)  findViewById(R.id.welcomeuser);
		projectnote = (TextView) findViewById(R.id.projectis);
		
		
         
		Bundle bun = getIntent().getExtras();
		
		ArrayList<String> prjs = bun.getStringArrayList(ProjectParams.LIST_ID.KEY());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prjs);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		projList.setAdapter(adapter);
		
		justloggedin= bun.getBoolean("justloggedin");
		
		if(justloggedin)
		{
			
			welcome.setVisibility(TextView.VISIBLE);
			projectnote.setVisibility(TextView.INVISIBLE);
			String username = TarseelGlobals.LOGGEDIN_USERNAME(this);
			name.setText(username);
			screenname.setText("Register Device");
			doitline.setText("Please register your device and select project");				
		}
		else if(!justloggedin)
		{
			welcome.setVisibility(TextView.INVISIBLE);			
			screenname.setText("Switch Project");
			doitline.setText("Please select another project");
			projectnote.setVisibility(TextView.VISIBLE);
			String proname = TarseelGlobals.PROJECTS(this);
			
			projectname = (TextView)  findViewById(R.id.projectname);
			projectname.setText(proname);
	         
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		
		String actualsim = TarseelGlobals.getTelephonyManager(this).getLine1Number();
		if (actualsim != null && !actualsim.trim().equals("")) {
			simnum.setText(actualsim);
			simnum.setFocusable(false);
		}
		else {
			String simentered = TarseelGlobals.getPreference(this, TarseelGlobals.SIM_PREF_NAME, null);
			simnum.setText(simentered);
			simnum.setFocusable(true);
		}

		if(!justloggedin){
			((TextView)findViewById(R.id.simnumbertext)).setVisibility(Button.INVISIBLE);
			((EditText)findViewById(R.id.simnumedtxt)).setVisibility(Button.INVISIBLE);
		}
	}
	
	@Override
	public void onBackPressed()
	{
		final Activity ac = this;
		if(justloggedin)
		{
		new ConfirmPopup(this, "Do you want to exit application without registering project ?", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				TarseelGlobals.logout(ac);
			}
		});
		}
		else if(!justloggedin)
		{
			SMSTarseel.startHomeScreen(ac);
		}
	}
	
	private void doRegister(){
		
		try
		{
			
			TarseelGlobals.setSIM(this, simnum.getText().toString());
			
			JSONObject resp = HttpSender.sendLargeText(this, createRequest());
			
			 if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.ERROR.CODE())){
				ErrorAlert.showError(this, (String)resp.get(ResponseMessage.NAME));
			}
			
			else if(((String)resp.get(RequestParam.ResponseCode.NAME)).equals(RequestParam.ResponseCode.SUCCESS.CODE()))
			{
				TarseelGlobals.setPROJECTS(this, projList.getSelectedItem().toString());
				TarseelGlobals.addTo_CONSOLE_BUFFER(null, (String)resp.get(ProjectParams.DETAILS.KEY()));

				CleanupService.getInstance().startService(this);
			
				SMSTarseel.startHomeScreen(this);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			ErrorAlert.showError(this, "Unexpected error. Contact program vendor. Details are :"+e.getMessage());
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
			ErrorAlert.showError(this, ErrorMessages.CONNECTION_ERROR+". Details are :"+e.getMessage());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			ErrorAlert.showError(this, ErrorMessages.CONNECTION_ERROR+". Details are :"+e.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorAlert.showError(this, "Unexpected error. Details are :"+e.getMessage());
		}
	}
	
	public void registerClickHandler(View view){
	
		final Activity ac=this;
		boolean ispassed = false;
		if(simnum.getText().toString().equals("") || !simnum.getText().toString().matches("\\d{3,15}")){
			ErrorAlert.showError(this, "Specify a valid Sim number");
		}
		else{
			ispassed = true;
		}
		
		if(ispassed) {
			if( TarseelGlobals.PROJECTS(this) != null
				&& !TarseelGlobals.PROJECTS(this).trim().replace(",", "").equalsIgnoreCase(projList.getSelectedItem().toString()))
			{
				if(justloggedin){
				new ConfirmPopup(this, "Device is already registered for "+TarseelGlobals.PROJECTS(this)+" ? Do you still want to override previous data ?", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						doRegister();
					}
				});}
				else{
					new ConfirmPopup(this, "All the running services will be shut down, do you still want to switch project?", new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							
							SmsDispenser.getInstance().shutdownService(ac);								
							SmsCollector.getInstance().shutdownService(ac);							
							CallLogReader.getInstance().shutdownService(ac);
							doRegister();
						}
					});	
				}
			}
			else {
				doRegister();
			}
		}
		
	}
	
	@Override
	public SmsTarseelRequest createRequest()
	{
		SmsTarseelRequest req = null;
		try
		{
			req = new SmsTarseelRequest(this, App_Service.REGISTER_DEVICE_PROJECT);
			req.addParam(DeviceRegisterParam.PROJECT_NAME.KEY(), projList.getSelectedItem().toString());
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return req;
	}

	@Override
	public boolean validateFormData()
	{
		return false;
	}
}
