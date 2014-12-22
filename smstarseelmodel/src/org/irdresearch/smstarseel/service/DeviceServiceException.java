package org.irdresearch.smstarseel.service;

public class DeviceServiceException extends Exception{
	
	public static final String DEVICE_NOT_EXISTS="Given Device is not registered in DB";
	public static final String Device_ALREADY_Connected="Given Device is already connected in";
	public static final String Device_EXISTS="A Device already exists in database with the same Devicename";
	public static final String INVILD_Device_NAME="Devicename is invalid";
	public static final String DeviceNAME_EMPTY="Device name is not specified";
	public static final String AUTHENTICATION_EXCEPTION="Invalid Devicename or password. Is caps lock on?";
	public static final String ACCOUNT_DISABLED="Your account is disabled by the admin";
	public static final String SESSION_EXPIRED="Your current session has expired . please login again";
	public static final String DEVICES_DUPLICATION="Multiple Devices exists in database with the same imei and status";
	public static final String OTHER="Other";

	/**
	 * 
	 */
	private static final long serialVersionUID = 714368144228840189L;
	private String errorMessage;
	public String ERROR_CODE;

	public DeviceServiceException(String errorcode){
		this.errorMessage=errorcode;
		this.ERROR_CODE=errorcode;
	}
	public DeviceServiceException(String message,String errorcode){
		this.errorMessage=message;
		this.ERROR_CODE=errorcode;
	}
	
	public String getMessage(){
		return errorMessage+(super.getMessage()==null?"":("\n"+super.getMessage()));
	}
}
