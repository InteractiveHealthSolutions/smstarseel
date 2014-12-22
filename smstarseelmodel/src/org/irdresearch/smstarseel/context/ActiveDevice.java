package org.irdresearch.smstarseel.context;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ActiveDevice {

	private String	deviceImei;
	private Date	lastSeen;
	private Date	lastSmsSenderPing;
	private Date	lastSmsRecieverPing;
	private Date	lastCallLoggerPing;
	private boolean	isConnected;
	private Timer	timer;
	
	public ActiveDevice(String deviceImei)
	{
		this.deviceImei = deviceImei;
		setLastSeen(new Date());
		setConnected(true);
		timer = new Timer(true);
		timer.schedule(new ConnectionHandler(), 5 * 60 * 1000, 2 * 60 * 1000);
	}
	
	public void cleanupDevice(){
		deviceImei = null;
		setLastSeen(null);
		isConnected = false;
		timer.cancel();
		try
		{
			finalize();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
	public String getDevice()
	{
		return deviceImei;
	}

	public void setConnected(boolean isConnected)
	{
		this.isConnected = isConnected;
	}

	public boolean isConnected()
	{
		return isConnected;
	}
	public Date getLastSeen()
	{
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen)
	{
		this.lastSeen = lastSeen;
	}
	public Date getLastSmsSenderPing()
	{
		return lastSmsSenderPing;
	}

	public void setLastSmsSenderPing(Date lastSmsSenderPing)
	{
		this.lastSmsSenderPing = lastSmsSenderPing;
	}
	public Date getLastCallLoggerPing()
	{
		return lastCallLoggerPing;
	}

	public void setLastCallLoggerPing(Date lastCallLoggerPing)
	{
		this.lastCallLoggerPing = lastCallLoggerPing;
	}
	public Date getLastSmsRecieverPing()
	{
		return lastSmsRecieverPing;
	}

	public void setLastSmsRecieverPing(Date lastSmsRecieverPing)
	{
		this.lastSmsRecieverPing = lastSmsRecieverPing;
	}
	class ConnectionHandler extends TimerTask{

		@Override
		public void run()
		{
			
		}
	}
}
