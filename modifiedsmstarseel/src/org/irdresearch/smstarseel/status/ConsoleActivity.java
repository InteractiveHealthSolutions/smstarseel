package org.irdresearch.smstarseel.status;

import org.irdresearch.smstarseel.R;
import org.irdresearch.smstarseel.SMSTarseel;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.form.SmsTarseelForm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

public class ConsoleActivity extends SmsTarseelForm{
	private TextView projectname;
	TextView consoletv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tarseelconsole);
		populateTConsoleView();
		
		String proname = TarseelGlobals.PROJECTS(this);
		
		projectname = (TextView)  findViewById(R.id.projectname);
		projectname.setText(proname);
         
	}
	@Override
    public void onBackPressed()
    {
		SMSTarseel.startHomeScreen(this);
    }
	private void populateTConsoleView()
	{
		final Activity ac = this;
		consoletv = (TextView) findViewById(R.id.consoletv);
		consoletv.setText(TarseelGlobals.get_CONSOLE_BUFFER());
		final ScrollView sc = ((ScrollView) findViewById(R.id.Con_SCROLLER_ID));
		sc.post(new Runnable()
		{
			@Override
			public void run()
			{
				//sc.fullScroll(ScrollView.FOCUS_DOWN);we donot need to focus at bottom 
				sc.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						populateTConsoleView();
					}
				}, (TarseelGlobals.CONSOLE_REFRESH_INTERVAL * 1000));
			}
		});
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		populateTConsoleView();
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
