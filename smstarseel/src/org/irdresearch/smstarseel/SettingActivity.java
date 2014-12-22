package org.irdresearch.smstarseel;

import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.form.SmsTarseelPreference;
import org.irdresearch.smstarseel.status.StatusActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SettingActivity extends SmsTarseelPreference{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		 super.onCreate(savedInstanceState);       
		 addPreferencesFromResource(R.xml.preferences); 
	}
	@Override
	public void onBackPressed()
	{
	    SmsTarseel.startHomeScreen(this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, 0, 0, "Show current settings");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case 0:
			startActivity(new Intent(this, StatusActivity.class));
			return true;
		}
		return false;
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
