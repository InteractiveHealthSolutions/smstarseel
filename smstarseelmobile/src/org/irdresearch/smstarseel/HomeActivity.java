package org.irdresearch.smstarseel;

import org.irdresearch.smstarseel.call.CallLogReader;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.dialogue.ConfirmPopup;
import org.irdresearch.smstarseel.dialogue.ErrorAlert;
import org.irdresearch.smstarseel.form.SmsTarseelForm;
import org.irdresearch.smstarseel.sms.SmsCollector;
import org.irdresearch.smstarseel.sms.SmsDispenser;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class HomeActivity extends SmsTarseelForm implements android.view.View.OnClickListener{

	public enum ServiceStatus {
		started,
		stopped
	}
	
	private ImageView imgConsole;
	private Button settingsViewBtn;
	private Button statusViewBtn;
	private Button logoutBtn;
	private Button lockScreenBtn;
	private Button logfileSendServerBtn;
	
	//private Button smsDispenserActionBtn;
	//private Button smsCollectorActionBtn;
	//private Button callLogReaderActionBtn;
	
	//private TextView smsDispenserStatustv;
//private TextView smsCollectorStatustv;
	//private TextView callLogReaderStatustv;
	
	private Switch	smsDispenserToggle;
	private Switch	smsCollectorToggle;
	private Switch	CallLogToggle;
	private TextView console;
	private TextView projectname;
	//private Drawable dStart;
	//private Drawable dStop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		smsDispenserToggle = (Switch) findViewById(R.id.smsdispensertoggle);
		smsDispenserToggle.setOnClickListener(this);
		
		smsCollectorToggle = (Switch) findViewById(R.id.smscollectortoggle);
		smsCollectorToggle.setOnClickListener(this);
		
		CallLogToggle = (Switch) findViewById(R.id.calllogtoggle);
		CallLogToggle.setOnClickListener(this);
	
		imgConsole = (ImageView) findViewById(R.id.imgConsole);
		imgConsole.setOnClickListener(this);
		
		console = (TextView) findViewById(R.id.consoletext);
		console.setOnClickListener(this);
		
		// to get project name from login screen
				String proname = TarseelGlobals.PROJECTS(this);
				
				projectname = (TextView)  findViewById(R.id.projectname);
				projectname.setText(proname);
		         
		
	/*	settingsViewBtn = (Button) findViewById(R.id.settingsViewbtn);
		settingsViewBtn.setOnClickListener(this);
		
		statusViewBtn = (Button) findViewById(R.id.statusViewbtn);
		statusViewBtn.setOnClickListener(this);

		logoutBtn = (Button) findViewById(R.id.logoutbtn);
		logoutBtn.setOnClickListener(this);
		
		lockScreenBtn = (Button) findViewById(R.id.lockScreenbtn);
		lockScreenBtn.setOnClickListener(this);
		
		logfileSendServerBtn = (Button) findViewById(R.id.logfileSendServerbtn);
		logfileSendServerBtn.setOnClickListener(this);
	*/
		
		//dStart = getResources().getDrawable(R.drawable.start);
		//dStop = getResources().getDrawable(R.drawable.stop);
		
		//smsDispenserActionBtn = (Button) findViewById(R.id.smsDispenserActionbtn);
		//smsDispenserActionBtn.setOnClickListener(this);
		
		//smsCollectorActionBtn = (Button) findViewById(R.id.smsCollectorActionbtn);
		//smsCollectorActionBtn.setOnClickListener(this);
		
		//callLogReaderActionBtn = (Button) findViewById(R.id.callLogReaderActionbtn);
		//callLogReaderActionBtn.setOnClickListener(this);
		
		//smsDispenserStatustv = (TextView) findViewById(R.id.smsDispenserStatustv);
		//smsCollectorStatustv = (TextView) findViewById(R.id.smsCollectorStatustv);
		//callLogReaderStatustv = (TextView) findViewById(R.id.callLogReaderStatustv);

		renderServiceView(SmsDispenser.class);
		renderServiceView(SmsCollector.class);
		renderServiceView(CallLogReader.class);
	}
	 public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflator=new MenuInflater(this);
		inflator.inflate(R.layout.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	 
	
	
	private void renderServiceView(Class service){
		final Activity ac = this;
		// if service is running i.e. already started, then show button to STOP and show status STATRED
		// if service is STOPPED i.e. not running, then show button to START and show status STOPPED
		if(service.equals(SmsDispenser.class)){
			if(SmsDispenser.getInstance().isServiceStarted(ac)){
			    smsDispenserToggle.setChecked(true);
			}
			else {
				smsDispenserToggle.setChecked(false);
			}
		}
		else if(service.equals(SmsCollector.class)){
			if(SmsCollector.getInstance().isServiceStarted(ac)){
				smsCollectorToggle.setChecked(true);
			}
			else {
				smsCollectorToggle.setChecked(false);			}
		}
		else if(service.equals(CallLogReader.class)){
			if(CallLogReader.getInstance().isServiceStarted(ac)){
				CallLogToggle.setChecked(true);
			}
			else {
				CallLogToggle.setChecked(false);
			}
		}
	}
	
	@Override
	public void onBackPressed()
	{
		final Activity ac = this;
		new ConfirmPopup(this, "Do you want to exit and lock screen?", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				TarseelGlobals.writePreference(ac, TarseelGlobals.LAST_UNLOCK_TIME_PREF_NAME, null);
				SMSTarseel.startLoginForm(ac);
			}
		});
	}

	@Override
	protected SmsTarseelRequest createRequest()
	{
		return null;
	}

	@Override
	protected boolean validateFormData()
	{
		return false;
	}
	
	
public boolean onOptionsItemSelected(MenuItem item) {


	final Activity ac = this;
		
		switch (item.getItemId()) {
		case R.id.settings:
			SMSTarseel.showSettingForm(ac);
			break;
		case R.id.viewstatus:
			SMSTarseel.showStatus(ac);
			break;
		case R.id.sendlogtoserver:
			SMSTarseel.sendLogToServer(ac);
			break;
		case R.id.switchproject:
			SMSTarseel.startRegisterDeviceProjectForm(ac,false);
			break;
		case R.id.lockscreen:
			TarseelGlobals.ENABLE_UNLOCK = true;
			SMSTarseel.startLoginForm(ac);
			break;
		case R.id.logout:
			
			new ConfirmPopup(ac, "Are you sure you want to shutdown all Services if running and logout ?",
					new OnClickListener()
					{
						@Override
						public void onClick (DialogInterface dialog, int which)
						{
							TarseelGlobals.logout(ac);
						}
					});
			break;
		
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick (View v) {
		final Activity ac = this;
		
		/*if(v.equals(smsDispenserActionBtn)
				||v.equals(smsCollectorActionBtn)
				||v.equals(callLogReaderActionBtn))*/
			
			if(v.equals(smsDispenserToggle)
					||v.equals(smsCollectorToggle)
					||v.equals(CallLogToggle))
		{
			if(TarseelGlobals.PROJECTS(ac) == null || TarseelGlobals.PROJECTS(ac).trim().replace(",", "").equals("")){
				ErrorAlert.showError(ac, "Device is not registered for any project. Inconsistent system state. Contact program vendor");
				return;
			}
		}
		
		if(v.equals(imgConsole)|| v.equals(console)){
			SMSTarseel.showConsole(ac);
		}
		/*else if(v.equals(settingsViewBtn)){
			SmsTarseel.showSettingForm(ac);
		}
		else if(v.equals(statusViewBtn)){
			SmsTarseel.showStatus(ac);
		}
		else if(v.equals(logoutBtn)){
			new ConfirmPopup(ac, "Are you sure you want to shutdown all Services and logout ?",
				new OnClickListener()
				{
					@Override
					public void onClick (DialogInterface dialog, int which)
					{
						TarseelGlobals.logout(ac);
					}
				});
		}
		else if(v.equals(lockScreenBtn)){
			TarseelGlobals.ENABLE_UNLOCK = true;
			SmsTarseel.startLoginForm(ac);
		}
		else if(v.equals(logfileSendServerBtn)){
			SmsTarseel.sendLogToServer(ac);
		}
		*/
		else if(v.equals(smsDispenserToggle)){
			renderServiceView(SmsDispenser.class);
			// if user is seeing start icon on screen and service is NOT Started then start service,
			// if user is seeing stop icon on screen and service is Started then stop service,
			// Otherwise it shows that application is behaving inconsistently and should be modified 
			// to display correct image on screen that is being interacted by user, hence display error message
			if(!SmsDispenser.getInstance().isServiceStarted(ac))
			{
				new ConfirmPopup(ac, "Are you sure you want to start SmsDispenser Service ?",
						new OnClickListener()
						{
							@Override
							public void onClick (DialogInterface dialog, int which)
							{
								SmsDispenser.getInstance().startService(ac);
								//Re-render view to reflect new system state
								renderServiceView(SmsDispenser.class);
							}
						});
				
				
			}
			else if(SmsDispenser.getInstance().isServiceStarted(ac))
			{
				new ConfirmPopup(ac, "Are you sure to shutdown SmsDispener service ?",
						new OnClickListener()
						{
							@Override
							public void onClick (DialogInterface dialog, int which)
							{
								SmsDispenser.getInstance().shutdownService(ac);
								//Re-render view to reflect new system state
								renderServiceView(SmsDispenser.class);
							}
						});
			}
			else {
				ErrorAlert.showError(ac, "Service`s internal status doesnot conform to requested operation. Inconsistent system state. Contact program vendor.");
			}
			
			
			
		}
		else if(v.equals(smsCollectorToggle)){
			renderServiceView(SmsCollector.class);
			// if user is seeing start icon on screen and service is NOT Started then start service,
			// if user is seeing stop icon on screen and service is Started then stop service,
			// Otherwise it shows that application is behaving inconsistently and should be modified 
			// to display correct image on screen that is being interacted by user, hence display error message
			if(!SmsCollector.getInstance().isServiceStarted(ac))
			{
				new ConfirmPopup(ac, "Are you sure you want to start SmsCollector Service ?",
						new OnClickListener()
						{
							@Override
							public void onClick (DialogInterface dialog, int which)
							{
								SmsCollector.getInstance().startService(ac);
								//Re-render view to reflect new system state
								renderServiceView(SmsCollector.class);
							}
						});
			}
			else if(SmsCollector.getInstance().isServiceStarted(ac))
			{
				new ConfirmPopup(ac, "Are you sure to shutdown SmsCollector service ?",
						new OnClickListener()
						{
							@Override
							public void onClick (DialogInterface dialog, int which)
							{
								SmsCollector.getInstance().shutdownService(ac);
								//Re-render view to reflect new system state
								renderServiceView(SmsCollector.class);
							}
						});
			}
			else {
				ErrorAlert.showError(ac, "Service`s internal status doesnot conform to requested operation. Inconsistent system state. Contact program vendor.");
			}
		}
		else if(v.equals(CallLogToggle)){
			renderServiceView(CallLogReader.class);
			// if user is seeing start icon on screen and service is NOT Started then start service,
			// if user is seeing stop icon on screen and service is Started then stop service,
			// Otherwise it shows that application is behaving inconsistently and should be modified 
			// to display correct image on screen that is being interacted by user, hence display error message
			if(!CallLogReader.getInstance().isServiceStarted(ac))
			{
				new ConfirmPopup(ac, "Are you sure you want to start CallLogReader Service ?",
						new OnClickListener()
						{
							@Override
							public void onClick (DialogInterface dialog, int which)
							{
								CallLogReader.getInstance().startService(ac);
								//Re-render view to reflect new system state
								renderServiceView(CallLogReader.class);
							}
						});
			}
			else if(CallLogReader.getInstance().isServiceStarted(ac))
			{
				new ConfirmPopup(ac, "Are you sure to shutdown CallLogReader service ?",
						new OnClickListener()
						{
							@Override
							public void onClick (DialogInterface dialog, int which)
							{
								CallLogReader.getInstance().shutdownService(ac);
								//Re-render view to reflect new system state
								renderServiceView(CallLogReader.class);
							}
						});
			}
			else {
				ErrorAlert.showError(ac, "Service`s internal status doesnot conform to requested operation. Inconsistent system state. Contact program vendor.");
			}
		}
		
	}
}
