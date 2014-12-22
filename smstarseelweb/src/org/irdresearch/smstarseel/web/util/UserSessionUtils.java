package org.irdresearch.smstarseel.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.SystemPermissions;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.service.UserServiceException;

public class UserSessionUtils {
	public static final String USERNAME_KEY = "username";
	private static Map<String, LoggedInUser> currentlyLoggedInUsers = new HashMap<String, LoggedInUser>();
	
	public static Map<String, LoggedInUser> getCurrentlyLoggedInUsers() {
		return currentlyLoggedInUsers;
	}
	
	private static void keepUserAlive(String username, HttpServletRequest req){
		getLoggedInUser(username).refreshDateTime();
		
		req.setAttribute( "user" , getLoggedInUser( username ) );
		req.getSession().setAttribute( USERNAME_KEY , username );
		req.getSession().setAttribute( "fullname" , getLoggedInUser(username).getUser().getFullName());
	}

	private static LoggedInUser getLoggedInUser(String username){
		return currentlyLoggedInUsers.get(username);
	}
	
	public static void login(String username, User user, HttpServletRequest req , HttpServletResponse resp ){
		currentlyLoggedInUsers.put(username, new LoggedInUser(user));
		
		resp.addCookie(createCookie(USERNAME_KEY, username, 60*60*8));
		
		keepUserAlive(username, req);
	}
	
	public static void logout(String username, HttpServletRequest req , HttpServletResponse resp){
		currentlyLoggedInUsers.remove(username);
		
		removeCookie(username, req, resp);
		
		clearSession(req);
	}
	
	public static void logout(HttpServletRequest req , HttpServletResponse resp){
		logout(getActiveUser(req ).getUser().getName(), req, resp);
	}
	
	private static void clearSession( HttpServletRequest req ) {
		try {
			req.getSession().removeAttribute( USERNAME_KEY );
			req.getSession().removeAttribute( "user" );
		}
		catch ( Exception e ) {
		}
		
		req.getSession().invalidate();
	}

	public static LoggedInUser getActiveUser( HttpServletRequest req ) {
		LoggedInUser user = null;
		try {
			String username = (String) getUsername( req );
			user = getLoggedInUser( username );
			if(user != null) keepUserAlive(username, req);
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		return user;
	}

	public static boolean hasActiveUserPermission(SystemPermissions permission, HttpServletRequest request) throws UserServiceException {
		boolean perm = false;
		try {
			perm = getActiveUser( request ).hasPermission( permission.name() );
		}
		catch ( NullPointerException e ) {// throw null pointer exception only
											// if user is null means not active
			throw new UserServiceException( UserServiceException.SESSION_EXPIRED );
		}
		return perm;
	}

	
	public static boolean isUserSessionActive( HttpServletRequest req ) {
		try {
			String username = (String) getUsername( req );
			
			if(username != null && currentlyLoggedInUsers.get(username) != null){
				keepUserAlive( username, req );
				return true;
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
			clearSession( req );
			req.getSession().setAttribute( "logmessage" , UserServiceException.SESSION_EXPIRED );
		}
		return false;
	}

	private static Object getUsername( HttpServletRequest req ) {
		Object uname = req.getSession().getAttribute( USERNAME_KEY );
		if (uname == null) {
			for ( Cookie c : req.getCookies() ) {
				if (c.getName().compareTo( USERNAME_KEY ) == 0) {
					uname = c.getValue();
					break;
				}
			}
		}
		return uname;
	}

	private static Cookie createCookie (String name, String value, int age) {
		Cookie cok = new Cookie( name , value );
		cok.setMaxAge( age );
		return cok;
	}

	private static void removeCookie( String name, HttpServletRequest req, HttpServletResponse resp ) {
		for ( Cookie c : req.getCookies() ) {
			if (c.getName().compareTo( name ) == 0) {
				c.setMaxAge( -1 );
				resp.addCookie( c );
				break;
			}
		}
	}

	private static Cookie getCookie( String name , HttpServletRequest req ) {
		for ( Cookie c : req.getCookies() ) {
			if (c.getName().compareTo( name ) == 0) {
				return c;
			}
		}
		return null;
	}
}
