package org.irdresearch.smstarseel.form;

import org.irdresearch.smstarseel.comm.SmsTarseelRequest;

import android.app.Activity;
import android.app.ListActivity;

public abstract class SmsTarseelList extends ListActivity implements ISmsTarseelDisplayable{
	private boolean		isCurrentlyVisible	= false;

	protected abstract SmsTarseelRequest createRequest();

	protected abstract boolean validateFormData();

	@Override
	public Activity getActivity()
	{
		return this;
	}
	@Override
	public boolean isCurrentlyVisible()
	{
		return isCurrentlyVisible;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		isCurrentlyVisible = hasFocus;

		super.onWindowFocusChanged(hasFocus);
		/*if(hasFocus){
	       TextView txtCurrentTime= (TextView)findViewById(R.id.datetimelbl);
	       Clock.getInstance().displayClock(this, txtCurrentTime);
		}*/
	}
}
