package org.irdresearch.smstarseel.service;

public class UserServiceException extends Exception{

	public static final String USER_NOT_EXISTS="Given username doesnot exists";
	public static final String USER_ALREADY_LOGGED_IN="Given username is already logged in";
	public static final String PASSWORDS_NOT_MATCH="Both passwords donot matches";
	public static final String WRONG_PASSWORD="Wrong password.";
	public static final String ROLE_EXISTS="A role already exists in database with the same name";
	public static final String PERMISSION_EXISTS="A permission already exists in database with the same name";
	public static final String USER_EXISTS="A user already exists in database with the same username";
	public static final String INVILD_USER_NAME="Username is invalid";
	public static final String USERNAME_EMPTY="User name is not specified";
	public static final String USER_FIRST_NAME_MISSING="User first name is not specified";
	public static final String USER_PASSWORD_MISSING="User password is not provided";
	public static final String INVALID_PASSWORD_CHARACTERS="Password contains invalid characters. Only allowed charcters are a-zA-Z0-9@#%*()_[]{}|;.";
	public static final String USER_MUST_HAVE_ROLE="User must have atleast one role";
	public static final String AUTHENTICATION_EXCEPTION="Invalid username or password. Is caps lock on?";
	public static final String ACCOUNT_DISABLED="Your account is disabled by the admin";
	public static final String ADMIN_ACCOUNT_NEEDED="Your account should be admin to perform requested operation";
	public static final String SESSION_EXPIRED="Your current session has expired . please login again";
	public static final String PERMISSION_UNGRANTED="Your account should have permissions to perform device operations";
	public static final String OTHER="Other";

	/**
	 * 
	 */
	private static final long serialVersionUID = 714368144228840189L;
	private String errorMessage;
	public String ERROR_CODE;

	public UserServiceException(String errorcode){
		this.errorMessage=errorcode;
		this.ERROR_CODE=errorcode;
	}
	public UserServiceException(String message,String errorcode){
		this.errorMessage=message;
		this.ERROR_CODE=errorcode;
	}
	
	public String getMessage(){
		return errorMessage+(super.getMessage()==null?"":("\n"+super.getMessage()));
	}
}
