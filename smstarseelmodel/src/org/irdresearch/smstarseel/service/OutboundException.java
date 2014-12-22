package org.irdresearch.smstarseel.service;

public class OutboundException extends Exception{

	public static final String OUTBOUND_FIELD_NOT_EMPTY="Outbound message at the time of creation should have following fields null [error message, failure cause, originator, sentdate, system]";
	public static final String OTHER="Other";

	/**
	 * 
	 */
	private static final long serialVersionUID = 714368144228840189L;
	private String errorMessage;
	public String ERROR_CODE;

	public OutboundException(String errorcode){
		this.errorMessage=errorcode;
		this.ERROR_CODE=errorcode;
	}
	public OutboundException(String message,String errorcode){
		this.errorMessage=message;
		this.ERROR_CODE=errorcode;
	}
	
	public String getMessage(){
		return errorMessage+(super.getMessage()==null?"":("\n"+super.getMessage()));
	}
}
