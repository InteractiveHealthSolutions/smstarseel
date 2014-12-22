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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends SmsTarseelForm implements android.view.View.OnClickListener{

	public enum ServiceStatus {
		started,
		stopped
	}
	
	private Button consoleViewBtn;
	private Button settingsViewBtn;
	private Button statusViewBtn;
	private Button logoutBtn;
	private Button lockScreenBtn;
	private Button logfileSendServerBtn;
	
	private Button smsDispenserActionBtn;
	private Button smsCollectorActionBtn;
	private Button callLogReaderActionBtn;
	
	private TextView smsDispenserStatustv;
	private TextView smsCollectorStatustv;
	private TextView callLogReaderStatustv;
	
	private Drawable dStart;
	private Drawable dStop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		consoleViewBtn = (Button) findViewById(R.id.consoleViewbtn);
		consoleViewBtn.setOnClickListener(this);
		
		settingsViewBtn = (Button) findViewById(R.id.settingsViewbtn);
		settingsViewBtn.setOnClickListener(this);
		
		statusViewBtn = (Button) findViewById(R.id.statusViewbtn);
		statusViewBtn.setOnClickListener(this);

		logoutBtn = (Button) findViewById(R.id.logoutbtn);
		logoutBtn.setOnClickListener(this);
		
		lockScreenBtn = (Button) findViewById(R.id.lockScreenbtn);
		lockScreenBtn.setOnClickListener(this);
		
		logfileSendServerBtn = (Button) findViewById(R.id.logfileSendServerbtn);
		logfileSendServerBtn.setOnClickListener(this);
		
		dStart = getResources().getDrawable(R.drawable.start);
		dStop = getResources().getDrawable(R.drawable.stop);
		
		smsDispenserActionBtn = (Button) findViewById(R.id.smsDispenserActionbtn);
		smsDispenserActionBtn.setOnClickListener(this);
		
		smsCollectorActionBtn = (Button) findViewById(R.id.smsCollectorActionbtn);
		smsCollectorActionBtn.setOnClickListener(this);
		
		callLogReaderActionBtn = (Button) findViewById(R.id.callLogReaderActionbtn);
		callLogReaderActionBtn.setOnClickListener(this);
		
		smsDispenserStatustv = (TextView) findViewById(R.id.smsDispenserStatustv);
		smsCollectorStatustv = (TextView) findViewById(R.id.smsCollectorStatustv);
		callLogReaderStatustv = (TextView) findViewById(R.id.callLogReaderStatustv);

		renderServiceView(SmsDispenser.class);
		renderServiceView(SmsCollector.class);
		renderServiceView(CallLogReader.class);
	}

	private void renderServiceView(Class service){
		final Activity ac = this;
		// if service is running i.e. already started, then show button to STOP and show status STATRED
		// if service is STOPPED i.e. not running, then show button to START and show status STOPPED
		if(service.equals(SmsDispenser.class)){
			if(SmsDispenser.getInstance().isServiceStarted(ac)){
				smsDispenserActionBtn.setBackgroundDrawable(dStop);
				smsDispenserStatustv.setText(ServiceStatus.started.name());
			}
			else {
				smsDispenserActionBtn.setBackgroundDrawable(dStart);
				smsDispenserStatustv.setText(ServiceStatus.stopped.name());
			}
		}
		else if(service.equals(SmsCollector.class)){
			if(SmsCollector.getInstance().isServiceStarted(ac)){
				smsCollectorActionBtn.setBackgroundDrawable(dStop);
				smsCollectorStatustv.setText(ServiceStatus.started.name());
			}
			else {
				smsCollectorActionBtn.setBackgroundDrawable(dStart);
				smsCollectorStatustv.setText(ServiceStatus.stopped.name());
			}
		}
		else if(service.equals(CallLogReader.class)){
			if(CallLogReader.getInstance().isServiceStarted(ac)){
				callLogReaderActionBtn.setBackgroundDrawable(dStop);
				callLogReaderStatustv.setText(ServiceStatus.started.name());
			}
			else {
				callLogReaderActionBtn.setBackgroundDrawable(dStart);
				callLogReaderStatustv.setText(ServiceStatus.stopped.name());
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
				SmsTarseel.startLoginForm(ac);
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

	@Override
	public void onClick (View v) {
		final Activity ac = this;
		
		if(v.equals(smsDispenserActionBtn)
				||v.equals(smsCollectorActionBtn)
				||v.equals(callLogReaderActionBtn))
		{
			if(TarseelGlobals.PROJECTS(ac) == null || TarseelGlobals.PROJECTS(ac).trim().replace(",", "").equals("")){
				ErrorAlert.showError(ac, "Device is not registered for any project. Inconsistent system state. Contact program vendor");
				return;
			}
		}
		
		if(v.equals(consoleViewBtn)){
			SmsTarseel.showConsole(ac);
		}
		else if(v.equals(settingsViewBtn)){
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
		else if(v.equals(smsDispenserActionBtn)){
			// if user is seeing start icon on screen and service is NOT Started then start service,
			// if user is seeing stop icon on screen and service is Started then stop service,
			// Otherwise it shows that application is behaving inconsistently and should be modified 
			// to display correct image on screen that is being interacted by user, hence display error message
			if(v.getBackground().getCurrent().equals(dStart)
					&& !SmsDispenser.getInstance().isServiceStarted(ac))
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
			else if(v.getBackground().getCurrent().equals(dStop)
					&& SmsDispenser.getInstance().isServiceStarted(ac))
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
		else if(v.equals(smsCollectorActionBtn)){
			// if user is seeing start icon on screen and service is NOT Started then start service,
			// if user is seeing stop icon on screen and service is Started then stop service,
			// Otherwise it shows that application is behaving inconsistently and should be modified 
			// to display correct image on screen that is being interacted by user, hence display error message
			if(v.getBackground().getCurrent().equals(dStart)
					&& !SmsCollector.getInstance().isServiceStarted(ac))
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
			else if(v.getBackground().getCurrent().equals(dStop)
					&& SmsCollector.getInstance().isServiceStarted(ac))
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
		else if(v.equals(callLogReaderActionBtn)){
			// if user is seeing start icon on screen and service is NOT Started then start service,
			// if user is seeing stop icon on screen and service is Started then stop service,
			// Otherwise it shows that application is behaving inconsistently and should be modified 
			// to display correct image on screen that is being interacted by user, hence display error message
			if(v.getBackground().getCurrent().equals(dStart)
					&& !CallLogReader.getInstance().isServiceStarted(ac))
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
			else if(v.getBackground().getCurrent().equals(dStop)
					&& CallLogReader.getInstance().isServiceStarted(ac))
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
