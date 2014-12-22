package org.irdresearch.smstarseel.autosys;

import ird.xoutTB.emailer.emailServer.EmailServer.AttachmentType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.hibernate.Session;
import org.irdresearch.smstarseel.EmailEngine;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.queries.DataQuery;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;
import org.irdresearch.smstarseel.web.util.WebGlobals.TarseelSetting;

import com.mysql.jdbc.StringUtils;

public class DailySummaryNotifierJob implements Runnable {
    private final ScheduledExecutorService service;
    private final String currentRunTimeSetting;
    
    public DailySummaryNotifierJob(ScheduledExecutorService service, String currentRunTimeSetting){
        this.service = service;
        this.currentRunTimeSetting = currentRunTimeSetting;
        
    }

    private void appendReturnedDataToCsvStrem(ByteArrayOutputStream fw, List dataSet, String mainHeading) throws IOException{
    	fw.write(("\""+mainHeading+"\"").getBytes());
		fw.write('\n');
		fw.write('\n');
    	for (Object object : dataSet) {
			if(object instanceof Object[]){
				Object[] coldata = (Object[]) object;
				for (int i = 0 ; i < coldata.length ; i++) {
					fw.write(("\""+(coldata[i]==null?"":coldata[i].toString().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
					if(i < (coldata.length -1)){
						fw.write(',');
					}
				}
				fw.write('\n');
			}
			else{
				fw.write(("\""+(object==null?"":object.toString().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
				fw.write('\n');
			}
		}
    	fw.write('\n');
    	fw.write('\n');
    }
    
    public void run(){
    	TarseelServices tsc = TarseelContext.getServices();

        try{
        	System.out.println("......RUNNING DailySummaryNotifierJob......");
        	
    		List<Project> prjl = tsc.getDeviceService().getAllProjects(0, 10);
    		for (Project project : prjl) {
    			String subject = project.getName().toUpperCase()+": Smstarseel Daily Summary";
    			String message = "";
    			Session ss = TarseelContext.getNewSession();
    			try{
	    			ss.beginTransaction();
	    			
	     			ByteArrayOutputStream fw = new ByteArrayOutputStream();
	
	    			int setpid = ss.createSQLQuery("SET @projectId="+project.getProjectId()).executeUpdate();
	     			List dsob = ss.createSQLQuery(DataQuery.DAILY_SUMMARY_OUTBOUND_QUERY).list();
	    			appendReturnedDataToCsvStrem(fw, dsob, "OUTBOUNDS / OUTGOING SMSES");
	    			
	    			//int setpid = ss.createSQLQuery("SET @projectId="+project.getProjectId()).executeUpdate();
	     			List dsib = ss.createSQLQuery(DataQuery.DAILY_SUMMARY_INBOUND_QUERY).list();
	    			appendReturnedDataToCsvStrem(fw, dsib, "INBOUNDS / INCOMING SMSES");
	    			
	    			//int setpid = ss.createSQLQuery("SET @projectId="+project.getProjectId()).executeUpdate();
	     			List dscl = ss.createSQLQuery(DataQuery.DAILY_SUMMARY_CALLLOG_QUERY).list();
	    			appendReturnedDataToCsvStrem(fw, dscl, "CALL LOG");
	    			
	    			byte[] b=fw.toString().replace("\"null\"", "\"\"").getBytes();
	    			
	    			ByteArrayOutputStream zo = new ByteArrayOutputStream();
	    			zo.write(b);
	    			
	    			String[] recpl = TarseelContext.getSetting(TarseelSetting.DAILY_SUMMARY_NOTIFIER_RECIPIENTS.NAME(), null).replaceAll(" ", "").split(",");
	    			EmailEngine.getEmailer().postEmailWithAttachment(recpl, subject, message, "smstarseel@ird.org", zo.toByteArray(), project.getName().toUpperCase()+"-DailySummary"+DateUtils.convertToString(new Date())+".csv",AttachmentType.CSV);
	    			
	    			zo.close();
    			}
    			finally{
    				ss.close();
    			}
    		}
        }
        catch (Exception e) {
			e.printStackTrace();
			EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error sending daily summary", "SmsTarseel: Error sending daily summary: "+e.getMessage());
		}
        finally{
        	tsc.closeSession();
        	//Prevent this task from stalling due to RuntimeExceptions.

        	System.out.println("SCHEDULING NEXT");
        	
        	String[] timings = TarseelContext.getSetting(TarseelSetting.DAILY_SUMMARY_NOTIFIER_TIME.NAME(), "23:50:50").replaceAll(" ", "").split(",");
        	String lastRunTime = TarseelContext.getSetting(TarseelSetting.DAILY_SUMMARY_NOTIFIER_LAST_RUN.NAME(), null);

        	SimpleDateFormat dateSdf = new SimpleDateFormat("dd-MMM-yyyy");
        	SimpleDateFormat dateTimeSdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        	
        	String dateNow = dateSdf.format(new Date());

        	Date nextTimeToRun = null;
        	String nextTime = null;

        	for (String tmg : timings) {

        		// Check when job ran last time
        		Date lastRunDate = new Date();
        		if(!StringUtils.isEmptyOrWhitespaceOnly(lastRunTime)){
        			try {
						lastRunDate = dateTimeSdf.parse(dateNow+" "+lastRunTime);
					}
					catch (ParseException e) {
						e.printStackTrace();
					}
        		}
        		
        		// CurrentDate with this (for object) timing assigned
            	Date dateNowWithNextTime = null;
    			try {
    				dateNowWithNextTime = dateTimeSdf.parse(dateNow+" "+tmg);
    			}
    			catch (ParseException e) {
    				e.printStackTrace();
    			}
    			
    			// if job last run time has not passed current run time for today, schedule job for this time else move forward
    			if(dateNowWithNextTime.after(lastRunDate)){
            		nextTimeToRun = dateNowWithNextTime;
            		nextTime = tmg;
            		break;
            	}
			}
        	
        	// if nextTime is still null and all timings have passed, assign job first runtime tomorrow
        	if(nextTimeToRun == null){
        		String dateTomorow = dateSdf.format(DateUtils.addInterval(new Date(), 1, TIME_INTERVAL.DATE));
            	Date dateTomorrowWithNextTime = null;
    			try {
    				dateTomorrowWithNextTime = dateTimeSdf.parse(dateTomorow+" "+timings[0]);
    				nextTimeToRun = dateTomorrowWithNextTime;
    				nextTime = timings[0];
    			}
    			catch (ParseException e) {
    				e.printStackTrace();
    			}
        	}
        	
            System.out.println("LAST RUN AT "+lastRunTime+" AND NEXT TO RUN "+nextTimeToRun);

        	//calculate how many ms to next launch
        	final long currntMillis = System.currentTimeMillis();
        	final long nextMillis = nextTimeToRun.getTime(); // millis to act as a second operand for next time run calculation
        
        	//calculate how many ms left for next launch
        	final long remainingTime = nextMillis - currntMillis;
        	
            System.out.println(remainingTime);
        	
            service.schedule(new DailySummaryNotifierJob(service, nextTime),remainingTime,TimeUnit.MILLISECONDS);
            
            TarseelContext.updateSetting(TarseelSetting.DAILY_SUMMARY_NOTIFIER_LAST_RUN.NAME(), currentRunTimeSetting, new User());
            System.out.println("..... SCHEDULED AFTER "+nextMillis+"ms AND ENDED SUCCESSFULLY......");
        }
    }
}

