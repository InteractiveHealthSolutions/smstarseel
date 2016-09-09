package org.irdresearch.smstarseel.rest.telenor;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.irdresearch.smstarseel.rest.telenor.TelenorContext.Config;
import org.irdresearch.smstarseel.rest.util.HttpResponse;
import org.irdresearch.smstarseel.rest.util.HttpUtil;
import org.irdresearch.smstarseel.rest.util.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/rest/telenor/authenticate")
public class Authenticate {

	private TelenorAuthenticator authenticator;

	public TelenorAuthenticator getAuthenticator() { return authenticator; }
	
	public class TelenorAuthenticator{
		private Boolean authenticated;

		private String authenticationKey;
		private Timer keepalive;
		private String msisdn;
		private String password;
		
		public TelenorAuthenticator(boolean authenticated, String authenticationKey, String msisdn, String password) {
			this.authenticated = authenticated;
			this.authenticationKey = authenticationKey.toString();
			this.msisdn = msisdn;
			this.password = password;
		}
		
		public Boolean getAuthenticated() { return authenticated; }
		public String getAuthenticationKey() { return authenticationKey; }
		
		public Map<String, Object> relogin() {
			Map<String, Object> resp = new HashMap<>();
			login(msisdn, password, resp);
			return resp;
		}
		
		//TODO re-evaluate it again
		public void keepAlive() {
			keepalive = new Timer(true);
			keepalive.schedule(new TimerTask() {
				@Override
				public void run() {
					Map<String, Object> resp = new HashMap<>();
					
					String pingerUrl = TelenorContext.getProperty(Config.PING_URL, null);
					HttpResponse response = HttpUtil.post(pingerUrl  , "session_id="+authenticationKey, "");
					System.out.println(response.body());
					Utils.createResponse(response, resp);
					
					if(resp.containsKey("SUCCESS") && (boolean) resp.get("SUCCESS")){
						// TODO do nothing for now may be logging would be great
					}
					else {
						resp = relogin();
						if(resp.containsKey("SUCCESS") && (boolean) resp.get("SUCCESS")){
							authenticated = true;
							authenticationKey = resp.get("data").toString();
						}
						else {
							authenticated = false;
							authenticationKey = null;			
						}
					}
				}
			}, 1000*60*12, 1000*60*12);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> authenticate(@RequestParam("msisdn") String msisdn, @RequestParam("password") String password, 
			@RequestParam(value = "keepAlive", required = false) Boolean keepalive) {
		Map<String, Object> resp = new HashMap<>();
		if(StringUtils.isEmptyOrWhitespaceOnly(msisdn)){
			Utils.createErrorResponse("No msisdn specified", resp);
			return resp;
		}
		else if(StringUtils.isEmptyOrWhitespaceOnly(password)){
			Utils.createErrorResponse("No password specified", resp);
			return resp;
		}

		String authenticatorUrl = TelenorContext.getProperty(Config.AUTH_URL, null);
		if(StringUtils.isEmptyOrWhitespaceOnly(authenticatorUrl )){
			Utils.createErrorResponse("No setting specified as telenor.url.authenticator in smstarseel", resp);
			return resp;
		}
		
		String pingerUrl = TelenorContext.getProperty(Config.PING_URL, null);
		if(keepalive != null && keepalive && StringUtils.isEmptyOrWhitespaceOnly(pingerUrl )){
			Utils.createErrorResponse("No setting specified as telenor.url.ping in smstarseel", resp);
			return resp;
		}
		
		login(msisdn, password, resp);
		
		if(keepalive != null && keepalive){
			authenticator = new TelenorAuthenticator(true, resp.get("data").toString(), msisdn, password);
		}
		
		return resp;
	}
	
	public boolean autoAuthenticate(boolean keepalive) {
		if(authenticator != null && authenticator.getAuthenticated() != null && authenticator.getAuthenticated()){
			return true;
		}
		
		String username = TelenorContext.getProperty(Config.MSISDN, null);
		String password = TelenorContext.getProperty(Config.PASSWORD, null);
		if(StringUtils.isEmptyOrWhitespaceOnly(username ) || StringUtils.isEmptyOrWhitespaceOnly(password )){
			throw new IllegalStateException("No authentication crdentials defined. "
					+ "Make sure that smstarseel.properties have valid values defined in properties "
					+ "telenor.username and telenor.password");
		}
		
		return authenticate(username, password, keepalive).get("SUCCESS").toString().toLowerCase().contains("true");
	}
	
	public String getAuthenticatedSessionId() {
		String username = TelenorContext.getProperty(Config.MSISDN, null);
		String password = TelenorContext.getProperty(Config.PASSWORD, null);
		if(StringUtils.isEmptyOrWhitespaceOnly(username) || StringUtils.isEmptyOrWhitespaceOnly(password)){
			throw new IllegalStateException("No authentication credentials defined. "
					+ "Make sure that smstarseel.properties have valid values defined in properties "
					+ "telenor.username and telenor.password");
		}
		
		Map<String, Object> resp = authenticate(username, password, false);
		if(resp.containsKey("SUCCESS") && (boolean) resp.get("SUCCESS")){
			return resp.get("data").toString();
		}
		
		return null;
	}
	
	private void login(String msisdn, String password, Map<String, Object> resp) {
		HttpResponse response = HttpUtil.post(Config.fullUrl(Config.AUTH_URL) , "msisdn="+msisdn+"&password="+password, "");
		//System.out.println(response.body());
		Utils.createResponse(response, resp);
	}
}
