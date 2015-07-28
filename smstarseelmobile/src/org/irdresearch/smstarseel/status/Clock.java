package org.irdresearch.smstarseel.status;

import java.util.Date;

import org.irdresearch.smstarseel.form.ISmsTarseelDisplayable;
import org.irdresearch.smstarseel.global.DateUtils;

import android.widget.TextView;

public class Clock {

	private static Clock clockinst;
	private static ISmsTarseelDisplayable	displayable;
	private static TextView	view;

	private Clock()
	{

	}

	public static Clock getInstance()
	{
		if(clockinst == null){
			clockinst = new Clock();
		}
		return clockinst;
	}
	public void displayClock(ISmsTarseelDisplayable activity, TextView	v)
	{
		Clock.displayable = activity;
		view = v;
		
		Thread myThread = null;

		Runnable runnable = new CountDownRunner();
		myThread = new Thread(runnable);
		myThread.start();
	}

	public void doWork()
	{
		if(displayable != null && view != null){
			displayable.getActivity().runOnUiThread(new Runnable()
			{
				public void run()
				{
					//System.out.println(displayable.getActivity()+"date thread running:doWork()");
					try
					{
						view.setText(DateUtils.formatRequestDate(new Date()));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
		}else{
			System.out.println(displayable.getActivity().getLocalClassName()+"activity is null or view is null:doWork()");
		}
	}

	class CountDownRunner implements Runnable {

		@Override
		public void run()
		{
			while(displayable != null && view != null && displayable.isCurrentlyVisible())
			{
				//System.out.println(displayable.getActivity().getLocalClassName()+"CountDownRunner : running");

				doWork();
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
