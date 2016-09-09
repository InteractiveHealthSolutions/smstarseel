package org.irdresearch.smstarseel.autosys;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceAlreadyExistsException;

public class AutomatedTasks {
	 private static ScheduledExecutorService scheduler;
	 
	 public static void instantiate() throws InstanceAlreadyExistsException{
		 if(scheduler == null){
			 scheduler = Executors.newScheduledThreadPool(2);
			 scheduler.schedule(new DailySummaryNotifierJob(scheduler, null), 1, TimeUnit.MINUTES);
			 scheduler.scheduleWithFixedDelay(new ServiceLogExecuterJob(), 1, 1, TimeUnit.MINUTES);
		 }
		 else {
			 throw new InstanceAlreadyExistsException("An instance of AutomatedTasks already exists");
		 }
	 }
}
