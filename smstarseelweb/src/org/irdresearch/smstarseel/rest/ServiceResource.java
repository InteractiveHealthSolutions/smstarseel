package org.irdresearch.smstarseel.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.irdresearch.smstarseel.TarseelWebGlobals.SmsServiceConstants;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.Service;
import org.irdresearch.smstarseel.data.Service.ServiceStatus;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.rest.util.Utils;
import org.irdresearch.smstarseel.service.UserServiceException;
import org.irdresearch.smstarseel.web.util.ResponseUtil;
import org.irdresearch.smstarseel.web.util.UserSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/service")
public class ServiceResource {

	@RequestMapping(value = "register")//TODO
	public @ResponseBody Map<String, Object> registerService(@RequestParam("username") String user, 
			@RequestParam("password") String password, @RequestParam("project") String project) {
		Map<String, Object> resp = new HashMap<>();
		TarseelServices tsc = TarseelContext.getServices();
		try {
			User u = TarseelContext.getAuthenticatedUser(user, password);
			
			//TODO prj = sc.getDeviceService().findProject(project).get(0);

			
			String authKey = UUID.randomUUID().toString();
			
			Service s = new Service();
			s.setAddedByUsername(u.getName());
			s.setAuthenticationKey(authKey);
			s.setDateAdded(new Date());
		//	s.setProject(project);
		//TODO	s.setServiceName(serviceName);
			s.setStatus(ServiceStatus.ACTIVE);
		} catch (Exception e) {
			Utils.createErrorResponse(e.getMessage(), resp);
		}
		return resp; 
	}
	
	public Service authenticatedService(HttpServletRequest request){
		return UserSessionUtils.getService(Utils.getStringFilter(SmsServiceConstants.API_KEY.name(), request));
	}
}
