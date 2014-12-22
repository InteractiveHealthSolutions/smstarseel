package org.irdresearch.smstarseel.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.Device;
import org.irdresearch.smstarseel.data.Device.DeviceStatus;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.data.Role;
import org.irdresearch.smstarseel.data.Setting;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.data.User.UserStatus;
import org.irdresearch.smstarseel.service.UserServiceException;
import org.irdresearch.smstarseel.web.util.ResponseUtil;
import org.irdresearch.smstarseel.web.util.UserSessionUtils;
import org.irdresearch.smstarseel.web.util.WebGlobals.CommunicationQueryParams;
import org.irdresearch.smstarseel.web.util.WebGlobals.QueryParams;
import org.irdresearch.smstarseel.web.util.WebGlobals.SettingQueryParams;
import org.irdresearch.smstarseel.web.util.WebGlobals.UserQueryParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value = "/admin/")
public class AdminController extends DataDisplayController{

	private static String NAVIGATION_TYPE = "admin";

	@Override
	String getData (Map modal) {
		return null;
	}
	
	@RequestMapping(value="/changePassword.htm")
	public String showChangePassword(HttpServletRequest request){
		return "changePassword"; 
	}
	

	@RequestMapping(value="/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		UserSessionUtils.logout(request, response);
		return "redirect:/login.htm";
	}
	
	@RequestMapping(value="/addNewUser.htm")
	public String addNewUser(Map modal){
		TarseelServices tsc = TarseelContext.getServices();
		try{
			List<Role> roll = tsc.getUserService().getAllRoles();
			modal.put("roles", roll);
		}
		catch (Exception e) {
			e.printStackTrace();
			modal.put("message", "Error:"+e.getMessage());
		}
		finally{
			tsc.closeSession();
		}
		return "newUser";
	}
	
	@RequestMapping(value="/viewUsers.htm")
	public String viewUsers(HttpServletRequest request){
		return "viewUsers";
	}
	
	@RequestMapping(value="/viewSettings.htm")
	public String viewSettings(HttpServletRequest request){
		return "viewSettings";
	}
	
	@RequestMapping(value="/change_password.dm")
	public @ResponseBody Map<String, Object> changePassword(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		Map queryParams = request.getParameterMap();
		TarseelServices tsc = TarseelContext.getServices();
		try {
			String username = UserSessionUtils.getActiveUser(request).getUser().getName();
			if(username == null){
				map.put("message", "Session expired. Login again.");
				return map;
			}
	
			String oldpwd = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.OLD_PASSWORD, queryParams, false);
			String pw1 = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.NEW_PASSWORD, queryParams, false);
			String pw2 = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.RENEW_PASSWORD, queryParams, false);
			
			tsc.getUserService().changePassword(username, oldpwd, pw1, pw2);
			tsc.commitTransaction();
	
			map.put("message", "Password changed successfully");
		}
		catch (UserServiceException e) {
			e.printStackTrace();
			map.put("message", "Error:"+e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("message", "Error:"+e.getMessage());
		}
		finally{
			tsc.closeSession();
		}
		
		return map;
	}
	
	@RequestMapping(value="/reset_password.dm")
	public @ResponseBody Map<String, Object> resetPassword(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		Map queryParams = request.getParameterMap();
		TarseelServices tsc = TarseelContext.getServices();
		try {
			User userlgd = UserSessionUtils.getActiveUser(request).getUser();
			if(userlgd == null){
				map.put("message", "Session expired. Login again.");
				return map;
			}
			else if(!userlgd.hasAdministrativePrivileges()){
				map.put("message", "User not allowed for operations.");
				return map;
			}
						
			String username = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.USERNAME, queryParams, false);
			String pw1 = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.NEW_PASSWORD, queryParams, false);
			String pw2 = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.RENEW_PASSWORD, queryParams, false);
			
			if(!pw1.equals(pw2)){
				map.put("message", "Passwords donot match");
				return map;
			}
			
			User u = tsc.getUserService().getUser(username);
			u.setClearTextPassword(pw1);

			tsc.getUserService().updateUser(u);
			tsc.commitTransaction();
	
			map.put("message", "Password changed successfully");
		}
		catch (UserServiceException e) {
			e.printStackTrace();
			map.put("message", "Error:"+e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("message", "Error:"+e.getMessage());
		}
		finally{
			tsc.closeSession();
		}
		
		return map;
	}
	
	@RequestMapping(value="/edit_setting.dm")
	public @ResponseBody Map<String, Object> editSetting(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		Map queryParams = request.getParameterMap();
		TarseelServices tsc = TarseelContext.getServices();
		try {
			User userlgd = UserSessionUtils.getActiveUser(request).getUser();
			if(userlgd == null){
				map.put("message", "Session expired. Login again.");
				return map;
			}
			else if(!userlgd.hasAdministrativePrivileges()){
				map.put("message", "User not allowed for operations.");
				return map;
			}
			
			String settingName = SmsTarseelUtil.getSingleParamFromRequestMap(SettingQueryParams.NAME, queryParams, false);
			String newValue = SmsTarseelUtil.getSingleParamFromRequestMap(SettingQueryParams.NEW_VALUE, queryParams, false);
			
			Setting set = TarseelContext.refreshAndGetSettings().get(settingName);
			if(set == null){
				map.put("message", "Setting not found.");
				return map;
			}
			else if(newValue.matches(set.getValidatorRegex())){
				map.put("message", "Value specified doesnot conform to pattern allowed for setting.");
				return map;
			}
			
			TarseelContext.updateSetting(settingName, newValue, userlgd);
	
			map.put("message", "Setting updated successfully. Refresh to update view");
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("message", "Error:"+e.getMessage());
		}
		finally{
			tsc.closeSession();
		}
		
		return map;
	}
	
	@RequestMapping(value="/add_user.dm")
	public @ResponseBody Map<String, Object> addUser(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		Map queryParams = request.getParameterMap();
		TarseelServices tsc = TarseelContext.getServices();
		try {
			User userlogd = UserSessionUtils.getActiveUser(request).getUser();
			if(userlogd == null){
				map.put("message", "Session expired. Login again.");
				return map;
			}
	
			String loginid = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.USERNAME, queryParams, false);
			String pwd = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.NEW_PASSWORD, queryParams, false);
			String pwdre = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.RENEW_PASSWORD, queryParams, false);
			String firstname = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.FIRSTNAME, queryParams, false);
			String lastname = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.LASTNAME, queryParams, false);
			String email = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.EMAIL, queryParams, false);
			String role = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.ROLE, queryParams, false);
			
			String errorMsg = "";
			if(loginid == null){
				errorMsg  += "\nError finding username/login Id from request;";
			}
			else if(loginid.trim().equalsIgnoreCase("admin") || loginid.trim().equalsIgnoreCase("administrator")){
				errorMsg += "\nUsername/login ID should not be admin or administrator;";
			}
			else if(pwd == null){
				errorMsg += "\nError finding user password from request;";
			}
			else if(pwdre == null){
				errorMsg += "\nError finding user password confirmation from request;";
			}
			else if(!pwd.equals(pwdre)){
				errorMsg += "\nUser password and its confirmation does not match;";
			} 
			else if(firstname == null){
				errorMsg += "\nError finding user first name from request;";
			}
			else if(email == null){
				errorMsg += "\nError finding user email from request;";
			}
			else if(role == null){
				errorMsg += "\nError finding user role name from request;";
			}
			else {
				try {
					User user = new User(0, loginid);
					user.setFirstName(firstname);
					user.setLastName(lastname);
					user .setClearTextPassword(pwd);
					user.setStatus(UserStatus.ACTIVE);
					user.setEmail(email);
					user.setCreator(userlogd);
					user.setRoles(new HashSet<Role>(tsc.getUserService().getRolesByName(role)));
					tsc.getUserService().createUser(user);
					tsc.commitTransaction();
				}
				catch (Exception e) {
					e.printStackTrace();
					errorMsg += "\nError :" + e.getMessage();
				}
				finally{
					tsc.closeSession();
				}
			}
	
			map.put("message", errorMsg==""?"User added successfully":errorMsg);
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("message", "Error:"+e.getMessage());
		}
		finally{
			tsc.closeSession();
		}
		
		return map;
	}
	
	@RequestMapping(value="/admin.htm")
	public String showAdminPage(Map modal){
		TarseelServices tsc = TarseelContext.getServices();
		
		try{// limit if projects exceed 50 to maintain consistent flow, in real these would never be more than 10
			List<Project> prjl = tsc.getDeviceService().getAllProjects(0, 50);
			modal.put("projects", prjl);
		}
		finally{
			tsc.closeSession();
		}

		return "admin";
	}
	
	@RequestMapping(value="/traverse_devices.do")
	public @ResponseBody Map<String, Object> traverseDevices(HttpServletRequest request){
		Map queryParams = request.getParameterMap();
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<Device> items = new ArrayList<Device>();
		
		TarseelServices tsc = TarseelContext.getServices();
		try {
			String imei = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.IMEI, queryParams, false);
			String sim = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.REFERENCE_NUMBER, queryParams, false);
			String st = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.STATUS, queryParams, false);
			DeviceStatus dvst = !StringUtils.isEmptyOrWhitespaceOnly(st)? DeviceStatus.valueOf(st):null;
			
			//String adddateF = SmsTarseelUtil.getSingleParamFromRequestMap(DeviceQueryParams.ADDDATE_FROM, queryParams, false);
			//String adddateT = SmsTarseelUtil.getSingleParamFromRequestMap(DeviceQueryParams.ADDDATE_TO, queryParams, false);
			//Date adddateFrom = StringUtils.isEmptyOrWhitespaceOnly(adddateF)?null:WebGlobals.GLOBAL_SDF_DATE.parse(adddateF);
			//Date adddateTo = StringUtils.isEmptyOrWhitespaceOnly(adddateT)?null:WebGlobals.GLOBAL_SDF_DATE.parse(adddateT);
			
			Integer pageSize = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(QueryParams.PAGE_SIZE, queryParams, false));
			Integer pageNumber = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(QueryParams.PAGE_NUMBER, queryParams, false));

			items = tsc.getDeviceService().findDevice(imei, dvst, false, null, sim, (pageNumber-1)*pageSize, pageSize);
			
			map.put("rows", ResponseUtil.prepareDataResponse((ArrayList<Device>) items, null));
		    map.put("total", tsc.getDeviceService().LAST_QUERY_TOTAL_ROW__COUNT(Device.class).intValue());
		}
		catch (DataException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e1) {
			e1.printStackTrace();
		}
		catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		finally{
			tsc.closeSession();
		}
	    
		return map;
	}
	
	@RequestMapping(value="/traverse_settings.do")
	public @ResponseBody Map<String, Object> traverseSettings(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			Collection<Setting> items = TarseelContext.refreshAndGetSettings().values();
			
			map.put("rows", items);
		    map.put("total", items.size());
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	    
		return map;
	}
	
	@RequestMapping(value="/traverse_users.do")
	public @ResponseBody Map<String, Object> traverseUsers(HttpServletRequest request){
		Map queryParams = request.getParameterMap();
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<User> items = new ArrayList<User>();
		
		TarseelServices tsc = TarseelContext.getServices();
		try {
			String email = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.EMAIL, queryParams, false);
			String namePart = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.PART_OF_NAME, queryParams, false);
			String st = SmsTarseelUtil.getSingleParamFromRequestMap(UserQueryParams.STATUS, queryParams, false);
			UserStatus usst = !StringUtils.isEmptyOrWhitespaceOnly(st)? UserStatus.valueOf(st):null;
			
			Integer pageSize = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(QueryParams.PAGE_SIZE, queryParams, false));
			Integer pageNumber = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(QueryParams.PAGE_NUMBER, queryParams, false));

			items = tsc.getUserService().findUserByCriteria(email, namePart, usst, false, (pageNumber-1)*pageSize, pageSize);
			
			map.put("rows", ResponseUtil.prepareDataResponse((ArrayList<User>) items, new String[]{"roles"}));
		    //map.put("total", tsc.getUserService().LAST_QUERY_TOTAL_ROW__COUNT(User.class).intValue());
		}
		catch (DataException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e1) {
			e1.printStackTrace();
		}
		catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		finally{
			tsc.closeSession();
		}
	    
		return map;
	}
	
	@Override
	@ModelAttribute
	void setNavigationType (HttpServletRequest request) {
		/*System.out.println("----------------------------------------------------------");
		Enumeration rhs = request.getHeaderNames();
		while (rhs.hasMoreElements()) {
			Object object = (Object) rhs.nextElement();
			System.out.println(object+": "+request.getHeader((String) object));
		}
		System.out.println("getQueryString():"+request.getQueryString());*/
		
		request.setAttribute("navigationType", NAVIGATION_TYPE);
		
		//System.out.println(NAVIGATION_TYPE);
	}
}
