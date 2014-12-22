package org.irdresearch.smstarseel.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.service.UserServiceException;
import org.irdresearch.smstarseel.web.util.ExceptionHandlerUtil;
import org.irdresearch.smstarseel.web.util.UserSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value = {"/login.htm"})
public class LoginController extends DataManipulateController{

	@Override
	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public ModelAndView showForm (Map modal) {
		return new ModelAndView("login");
	}
	
	@Override
	@RequestMapping(value = "/login.htm", method = RequestMethod.POST)
	public String submitForm (HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		try {
			User us = TarseelContext.getAuthenticatedUser(username, password);
			if(!us.hasAdministrativePrivileges()){
				request.setAttribute("errorMessage", "User not allowed for requested operation");
				return "login";
			}
			UserSessionUtils.login(username, us, request, response);
		}
		catch (UserServiceException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
			return "login";
		}
		catch (Exception e) {
			e.printStackTrace();
			return ExceptionHandlerUtil.showException(request, e);
		}
		
		return "redirect:/home/home.htm";
	}
}
