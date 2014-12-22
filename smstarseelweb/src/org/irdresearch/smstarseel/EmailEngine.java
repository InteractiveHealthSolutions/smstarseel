package org.irdresearch.smstarseel;

import ird.xoutTB.emailer.emailServer.EmailServer;
import ird.xoutTB.emailer.exception.EmailException;

import java.util.Arrays;
import java.util.Properties;

import javax.mail.MessagingException;

import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.service.utils.DataValidation;
import org.irdresearch.smstarseel.service.utils.ExceptionUtil;
import org.irdresearch.smstarseel.service.utils.LoggerUtil;
import org.irdresearch.smstarseel.service.utils.REG_EX;
/**
 *  donot forget to instantiate EmailEngine by calling instantiateEmailEngine(Properties props)
 *  properties are mendatory required to get smtp host for email server
 *  
 *  mail.transport.protocol=smtp 	(for example)
 *	mail.host=smtp.gmail.com 		(for example)
 *	mail.user.username=immunization.reminder@gmail.com (for example)
 *	mail.user.password=xxxxxxxx 	(for example)
 *	mail.smtp.auth=true
 *	mail.smtp.port=465				(for example)
 *
 * call this method just only once in your application as it will make singleton instance 
 * of EmailEngine and calling it again and again will have no effect
 * 
 * @author maimoonak
 *
 */
public class EmailEngine {
	private static EmailEngine _instance=new EmailEngine();
	private static EmailServer emailer;
	private final static String emailFrom = "ADMIN_ALERTS@xyzcompany.com";
	private static String adminEmailAddress;

	private EmailEngine() {
	}
	
	public static synchronized void instantiateEmailEngine(Properties props, String adminEmail) throws EmailException{
		if(emailer==null){
			emailer=new EmailServer(props);
		}
		
		if(!DataValidation.validate(REG_EX.EMAIL, adminEmail)) {
			throw new EmailException("Invalid admin-email address");
		}
		
		adminEmailAddress = adminEmail;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static EmailEngine getInstance() {
		return _instance;
	}
	
	public static EmailServer getEmailer(){
		return emailer;
	}
	
	public boolean sendSimpleMail(String[] recipients, String subject, String message){
		try {
			return getEmailer().postSimpleMail(recipients, subject, message, emailFrom);
		} catch (MessagingException e) {
			logUnsentEmails(recipients, subject, message, e);
		}
		return false;
	}
	
	public boolean sendHtmlMail(String[] recipients, String subject, String message){
		try {
			return getEmailer().postHtmlMail(recipients, subject, message, emailFrom);
		} catch (MessagingException e) {
			logUnsentEmails(recipients, subject, message,e);
		}
		return false;
	}
		
	private void logUnsentEmails(String[] recipients,String subject, String message, MessagingException e){
		LoggerUtil.logIt(ExceptionUtil.getStackTrace(e));
		LoggerUtil.logIt(
				"*********************************************************************\n"+
				"SUBJECT 	:\t"+subject+"\n"+
				"RECIPIENTS :\t"+Arrays.asList(recipients).toString()+"\n"+
				"CONTENT	:\t"+message+"\n"+
				"*********************************************************************\n");
	}
	
	public synchronized void emailErrorReportToAdmin(String subject,String message){
		String[] recipients=new String[]{adminEmailAddress};
		try {
			LoggerUtil.logIt("Sending error report to admin :"+message);
			getEmailer().postSimpleMail(recipients, subject, message, emailFrom);
		} catch (MessagingException e) {
			logUnsentEmails(recipients, subject, message,e);
		}
	}
	
	public void emailErrorReportToAdminAsASeparateThread(String subject,String message){
		final String msg=message;
		final String sub=subject;
		Runnable emailr = new Runnable() {
			@Override
			public void run() {
				emailErrorReportToAdmin(sub, msg);
			}
		};
		new Thread(emailr).start();  
	}
}
