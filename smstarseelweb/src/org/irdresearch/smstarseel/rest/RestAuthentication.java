package org.irdresearch.smstarseel.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.TarseelWebGlobals.SmsServiceConstants;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.Service;
import org.irdresearch.smstarseel.data.Service.ServiceStatus;
import org.irdresearch.smstarseel.rest.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;

import sun.misc.BASE64Decoder;

import com.mysql.jdbc.StringUtils;

public class RestAuthentication extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String reqp = request.getHeader("Authorization");
		String apikey = request.getHeader(SmsServiceConstants.API_KEY.name());
		apikey = StringUtils.isEmptyOrWhitespaceOnly(apikey)?request.getParameter(SmsServiceConstants.API_KEY.name()):apikey;
		
		if(StringUtils.isEmptyOrWhitespaceOnly(reqp) && StringUtils.isEmptyOrWhitespaceOnly(apikey)){
			Utils.createErrorResponse("Neither any Basic Authoriztion params nor any API Authorization Key specified", response);
			return;
		}

		if(!StringUtils.isEmptyOrWhitespaceOnly(reqp)){
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
				Utils.createErrorResponse("Authentication error: "+e.getMessage(), response);
			}
		}
		else if(!StringUtils.isEmptyOrWhitespaceOnly(apikey)){
			TarseelServices tsc = TarseelContext.getServices();
			try {
				List s = tsc.getCustomQueryService().getDataByHQL("FROM Service WHERE authenticationKey='"+apikey.trim()+"'");
				if(s.size() == 0){
					Utils.createErrorResponse("Authentication error: No registered service with API Key "+apikey, response);
					return;
				}
				else if(!((Service)s.get(0)).getStatus().equals(ServiceStatus.ACTIVE)){
					Utils.createErrorResponse(("Authentication error: Service with API Key "+apikey + " is "+((Service)s.get(0)).getStatus()), response);
					return;
				}
				
				filterChain.doFilter(request, response);
			}
			catch(Exception e){
				e.printStackTrace();
				Utils.createErrorResponse(e.getMessage(), response);
			}
			finally{
				tsc.closeSession();
			}
		}
	}
}
