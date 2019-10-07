package org.irdresearch.smstarseel.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.context.TarseelContext;
import org.springframework.web.filter.OncePerRequestFilter;

import sun.misc.BASE64Decoder;

import com.mysql.jdbc.StringUtils;

public class RestAuthentication extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String reqp = request.getHeader("Authorization");
		if(StringUtils.isEmptyOrWhitespaceOnly(reqp)){
			response.getWriter().write("No Basic Authoriztion params specified");
			return;
		}

		BASE64Decoder d =new BASE64Decoder();
		String dcoded = new String(d.decodeBuffer(reqp.replace("Basic ", "")));
		String usr = dcoded.substring(0, dcoded.indexOf(":"));
		String pwd = dcoded.substring(dcoded.indexOf(":")+1);
		try {
			if(TarseelContext.getAuthenticatedUser(usr, pwd) != null){
				filterChain.doFilter(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("Authentication error: "+e.getMessage());
		}
	}
}
