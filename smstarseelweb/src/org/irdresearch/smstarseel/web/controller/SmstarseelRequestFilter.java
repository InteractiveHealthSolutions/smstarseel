package org.irdresearch.smstarseel.web.controller;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.web.util.LoggedInUser;
import org.irdresearch.smstarseel.web.util.UserSessionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class SmstarseelRequestFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		//System.out.println("Start filtering");
		LoggedInUser lgUser = null;

		if((lgUser = UserSessionUtils.getActiveUser(request)) == null) {
			request.setAttribute("errorMessage", "Login to access data");
			getServletContext().getRequestDispatcher("/login.htm").forward(request, response);
		}
		else if(!lgUser.getUser().hasAdministrativePrivileges()){
			request.setAttribute("errorMessage", "Only users with administrative privileges are allowed to access data");
			getServletContext().getRequestDispatcher("/login.htm").forward(request, response);
		}
		else {
			filterChain.doFilter(request, response);
		}
		//System.out.println("Done Filtering");
	}
}
