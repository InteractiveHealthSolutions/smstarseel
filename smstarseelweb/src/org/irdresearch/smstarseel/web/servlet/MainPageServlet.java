package org.irdresearch.smstarseel.web.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;
import org.irdresearch.smstarseel.data.Role;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.data.User.UserStatus;
import org.irdresearch.smstarseel.service.utils.SecurityUtils;
import org.irdresearch.smstarseel.web.util.WebGlobals.ServiceType;

import com.mysql.jdbc.StringUtils;

public class MainPageServlet extends HttpServlet {
	private static final long serialVersionUID = 2298563548442465187L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException 
	{
		String srv = req.getParameter("srvtyp");
		String errorMsg = "";
		String successMessage = "";
		if(StringUtils.isEmptyOrWhitespaceOnly(srv)){
			errorMsg += "Service Type was not found while parsing request;";
		}
		else if(srv.equalsIgnoreCase(ServiceType.CHANGE_OWN_PWD.toString())){
			String username = req.getParameter("ownusername");
			String oldpwd = req.getParameter("ownoldPwd");
			String newpwd = req.getParameter("ownnewPwd");
			String repwd = req.getParameter("ownreNewPwd");

			if(username == null){
				errorMsg += "\nError finding username from request;";
			}
			else if(oldpwd == null){
				errorMsg += "\nError finding old password from request;";
			}
			else if(newpwd == null){
				errorMsg += "\nError finding new password from request;";
			}
			else if(repwd == null){
				errorMsg += "\nError finding new password confirmation from request;";
			}
			else if(!newpwd.equals(repwd)){
				errorMsg += "\nNew password and its confirmation does not match;";
			} 
			else {
				TarseelServices tsc = TarseelContext.getServices();
				User user = tsc.getUserService().getUser(username);
				try {
					if (user == null) {
						errorMsg += "\nUser with given username does not exists;";
					} 
					else {
						if (!SecurityUtils.decrypt(user.getPassword(), user.getName()).equals(oldpwd)) {
							errorMsg += "\nOld password given doesnot match with existing one;";
						}
						else {
							user.setClearTextPassword(newpwd);
							user.setLastEditor(user);
							
							tsc.getUserService().updateUser(user);
							tsc.commitTransaction();
							
							successMessage = "DONE!!";
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					errorMsg += "\nError :" + e.getMessage();
				}
				finally{
					tsc.closeSession();
				}
			}
		}
		else if(srv.equalsIgnoreCase(ServiceType.CHANGE_OTHER_USER_PWD.toString())){
			String actionPwd = req.getParameter("actionPwd");
			String username = req.getParameter("username");
			String newpwd = req.getParameter("newPwd");
			String repwd = req.getParameter("reNewPwd");

			if(actionPwd == null){
				errorMsg += "\nError finding administrator password from request;";
			}
			else if(username == null){
				errorMsg += "\nError finding username from request;";
			}
			else if(newpwd == null){
				errorMsg += "\nError finding new password from request;";
			}
			else if(repwd == null){
				errorMsg += "\nError finding new password confirmation from request;";
			}
			else if(!newpwd.equals(repwd)){
				errorMsg += "\nNew password and its confirmation does not match;";
			} 
			else {
				TarseelServices tsc = TarseelContext.getServices();
				try {
					User admin = tsc.getUserService().getUser("administrator");
					User user = tsc.getUserService().getUser(username);

					if(admin == null){
						errorMsg += "\nUser administrator does not exists;";
					}
					else if (!SecurityUtils.decrypt(admin.getPassword(), admin.getName()).equals(actionPwd)) {
						errorMsg += "\nPassword for administrator doesnot match with existing one;";
					}
					else if (user == null) {
						errorMsg += "\nUser with given username does not exists;";
					} 
					else {
							user.setClearTextPassword(newpwd);
							user.setLastEditor(admin);
							
							tsc.getUserService().updateUser(user);
							tsc.commitTransaction();
							
							successMessage = "DONE!!";
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					errorMsg += "\nError :" + e.getMessage();
				}
				finally{
					tsc.closeSession();
				}
			}
		}
		else if(srv.equalsIgnoreCase(ServiceType.ADD_USER.toString())){
			String actionPwd = req.getParameter("newActionPwd");
			String username = req.getParameter("newusername");
			String newpwd = req.getParameter("pwd");
			String repwd = req.getParameter("rePwd");
			String firstName = req.getParameter("firstName");
			String lastName = req.getParameter("lastName");
			String userRoleName = req.getParameter("userRoleName");

			if(actionPwd == null){
				errorMsg += "\nError finding administrator password from request;";
			}
			else if(username == null){
				errorMsg += "\nError finding username/login Id from request;";
			}
			else if(newpwd == null){
				errorMsg += "\nError finding user password from request;";
			}
			else if(repwd == null){
				errorMsg += "\nError finding user password confirmation from request;";
			}
			else if(!newpwd.equals(repwd)){
				errorMsg += "\nUser password and its confirmation does not match;";
			} 
			else if(firstName == null){
				errorMsg += "\nError finding user first name from request;";
			}
			else if(lastName == null){
				errorMsg += "\nError finding user last name from request;";
			}
			else if(userRoleName == null){
				errorMsg += "\nError finding user role name from request;";
			}
			else {
				TarseelServices tsc = TarseelContext.getServices();
				try {
					User admin = tsc.getUserService().getUser("administrator");

					if(admin == null){
						errorMsg += "\nUser administrator does not exists;";
					}
					else if (!SecurityUtils.decrypt(admin.getPassword(), admin.getName()).equals(actionPwd)) {
						errorMsg += "\nPassword for administrator doesnot match with existing one;";
					}
					else {
						User user = new User(0, username);
						user.setFirstName(firstName);
						user.setLastName(lastName);
						user .setClearTextPassword(newpwd);
						user.setStatus(UserStatus.ACTIVE);
						user.setCreator(admin);
						user.setRoles(new HashSet<Role>(tsc.getUserService().getRolesByName(userRoleName)));
						tsc.getUserService().createUser(user);
						tsc.commitTransaction();

						successMessage = "DONE!!";
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					errorMsg += "\nError :" + e.getMessage();
				}
				finally{
					tsc.closeSession();
				}
			}
		}
		else if(srv.equalsIgnoreCase(ServiceType.SEND_TEST_SMS.toString())){
			String actionPwd = req.getParameter("smsActionPwd");
			String cellNumber = req.getParameter("cellNumber");
			String smsText = req.getParameter("smsText");
			String smsProject = req.getParameter("smsProject");
			
			if(actionPwd == null){
				errorMsg += "\nError finding administrator password from request;";
			}
			else if(cellNumber == null){
				errorMsg += "\nError finding cell number from request;";
			}
			else if(smsText == null){
				errorMsg += "\nError finding sms text from request;";
			}
			else if(smsProject == null){
				errorMsg += "\nError finding sms project from request;";
			}
			else {
				TarseelServices tsc = TarseelContext.getServices();
				try {
					User admin = tsc.getUserService().getUser("administrator");

					if(admin == null){
						errorMsg += "\nUser administrator does not exists;";
					}
					else if (!SecurityUtils.decrypt(admin.getPassword(), admin.getName()).equals(actionPwd)) {
						errorMsg += "\nPassword for administrator doesnot match with existing one;";
					}
					else {//TODO what if no project ???
						tsc.getSmsService().createNewOutboundSms(cellNumber, smsText, new Date(), Priority.HIGH, 1, PeriodType.DAY, tsc.getDeviceService().findProject(smsProject).get(0).getProjectId(), null);
						tsc.commitTransaction();
						
						successMessage = "DONE!!";
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					errorMsg += "\nError :" + e.getMessage();
				}
				finally{
					tsc.closeSession();
				}
			}
		}
		
		req.setAttribute("errorMessage", errorMsg);
		req.setAttribute("successMessage", successMessage);
		RequestDispatcher rd = req.getRequestDispatcher("/smstarseel.jsp");
		rd.forward(req,resp);
	}
}
