package org.irdresearch.smstarseel.status;

import org.irdresearch.smstarseel.R;
import org.irdresearch.smstarseel.SmsTarseel;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.form.SmsTarseelForm;

import android.os.Bundle;
import android.widget.TextView;

public class StatusActivity extends SmsTarseelForm{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tarseelstatus);
		TextView tv = (TextView) findViewById(R.id.statustv);
		tv.setText(TarseelGlobals.getGlobalAttribs(this));
		tv.setTextSize(tv.getTextSize()-2);
	}
	@Override
    public void onBackPressed()
    {
		SmsTarseel.startHomeScreen(this);
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
