package org.irdresearch.smstarseel.sms;

import org.json.JSONObject;

public class SenderStatusHandler{
	private JSONObject currentSms;
	private boolean hasManagerBcNotified;
	private boolean hasSenderBcNotified;
	
	public SenderStatusHandler(JSONObject currentSms)
	{
		this.currentSms = currentSms;
		hasManagerBcNotified = false;
		hasSenderBcNotified = false;
	}
	public SenderStatusHandler(String sms)
	{
		this.currentSms = new JSONObject();
		hasManagerBcNotified = false;
		hasSenderBcNotified = false;
	}
	public boolean getHasManagerBcNotified()
	{
		return hasManagerBcNotified;
	}
	public void setHasManagerBcNotified(boolean hasManagerBcNotified)
	{
		this.hasManagerBcNotified = hasManagerBcNotified;
	}
	public boolean getHasSenderBcNotified()
	{
		return hasSenderBcNotified;
	}
	public void setHasSenderBcNotified(boolean hasSenderBcNotified)
	{
		this.hasSenderBcNotified = hasSenderBcNotified;
	}

	public JSONObject getCurrentSms()
	{
		return currentSms;
	}

	public void setCurrentSms(JSONObject currentSms)
	{
		this.currentSms = currentSms;
	}
}