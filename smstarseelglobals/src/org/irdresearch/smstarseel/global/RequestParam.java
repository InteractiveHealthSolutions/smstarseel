package org.irdresearch.smstarseel.global;

import java.util.Date;

public class RequestParam {
	
	public enum	App_Service{
		LOGIN("lgn"),
		SEND_LOG("sndlgfl"),
		REGISTER_DEVICE_PROJECT("regdevprj"),
		QUERY_PROJECTLIST("qryprjlst"),
		FETCH_PENDING_SMS("fchpndsms"),
		SUBMIT_RECIEVED_SMS("subrcvdsms"),
		SUBMIT_CALL_LOG("subclllog"),
		SUBMIT_SMS_SEND_ATTEMPT_RESULT("subsmssndattmrslt"),
		;

		public static final String NAME = "serv";
		
		private String VALUE;
		
		private App_Service(String value) {
			this.VALUE = value;
		}
		public String VALUE()
		{
			return VALUE;
		}
		@Override
		public String toString()
		{
			return VALUE;
		}
	}
	public enum RequestMendatoryParam{
		IMEI("imei"),
		SIM("sim"),
		DATE("phdt"),
		PROJECT_REGISTERED("regprj"),
		;
		private String KEY;
		
		private RequestMendatoryParam(String param){
			this.KEY = param;
		}
		public String KEY()
		{
			return KEY;
		}
		@Override
		public String toString()
		{
			return KEY;
		}
	}
	public enum ResponseCode{
		SUCCESS("ok"),
		ERROR("error");
		
		public static final String NAME = "respcd";
		
		private String CODE;
		
		private ResponseCode(String code){
			this.CODE = code;
		}
		public String CODE()
		{
			return CODE;
		}
		
		@Override
		public String toString()
		{
			return CODE;
		}
		
	}
	public enum ResponseMessage{
		INAVLID_REQUEST("invalid request"),
		ERROR("error"),
		SUCCESS("success"),
		INVALID_PHONE_DATE_ERROR("error while parsing date and time on phone. Date format on server is "+SmsTarseelGlobal.DEFAULT_DATE_FORMAT+". current date on server is "+new Date()+". your system time should be in sync upto 1 hour"),
		INVALID_PHONE_DATE("date and time on phone was found to be invalid. current date on server is "+new Date()+". your system time should be in sync upto 1 hour"),
		INVALID_USER("user authentication error"),
		INVALID_PROJECT("project with given name was not found. contact program vendor"),
		INVALID_PROJECT_ERROR("error while finding project with given name. contact program vendor"),
		UNKNOWN_ERROR("unhandled error");

		public static final String NAME = "respmsg";
		
		private String MESSAGE;
		
		private ResponseMessage(String message){
			this.MESSAGE = message;
		}
		public String MESSAGE()
		{
			return MESSAGE;
		}
		@Override
		public String toString()
		{
			return MESSAGE;
		}
	}
	public enum LoginRequest{
		USERNAME("usrnm"),
		PASSWORD("pwd"),
		;
		private String KEY;
		
		private LoginRequest(String param){
			this.KEY = param;
		}
		public String KEY()
		{
			return KEY;
		}
		@Override
		public String toString()
		{
			return KEY;
		}
	}
	public enum LoginResponse{
		DETAILS("details"),;
		//PROJECT_LIST_PREFIX("project"),;
		
		private String KEY;
		
		private LoginResponse(String param){
			this.KEY = param;
		}
		public String KEY()
		{
			return KEY;
		}
		@Override
		public String toString()
		{
			return KEY;
		}
	}
	public enum OuboundSmsParams{
		DETAILS("details"),
		SMSID("smsid"),
		TEXT("text"),
		CELL("cell"),
		LIST_ID("outsmslist"),
		IS_SENT("issent"),		
		SIM("sim"),
		SENT_DATE("sentdate"),
		DESC("desc"),
		ERR_MSG("errmsg"),
		FAIL_CAUSE("failcau"),
		;
		
		private String KEY;
		
		private OuboundSmsParams(String param){
			this.KEY = param;
		}
		public String KEY()
		{
			return KEY;
		}
		@Override
		public String toString()
		{
			return KEY;
		}
	}
	
	public enum LogFileParams{
		DETAILS("details"),
		FILE_NAME("flnm"),
		FILE_DATA("fldt"),
		SIZE("flsiz"),
		LAST_MODIFIED_DATE("lstmoddt"),
		;
		
		private String KEY;
		
		private LogFileParams(String param){
			this.KEY = param;
		}
		public String KEY()
		{
			return KEY;
		}
		@Override
		public String toString()
		{
			return KEY;
		}
	}
	
	public enum InboundSmsParams{
		DETAILS("details"),
		SMSID("smsid"),
		THREADID("thrdid"),
		TEXT("text"),
		SENDER_NUM("sndrnum"),
		LIST_ID("recvdsmslist"),
		SIM("sim"),
		RECIEVED_DATE("recvdate"),
		RECIEVED_DATE_IN_LONG("recvdatelng"),
		SYSTEM_RECIEVED_DATE("sysrecvdate"),
		SYSTEM_PROCESS_START_DATE("sysprocstrtdate"),
		TYPE("typ"),
		IS_SAVED("issvd"),
		/*DESC("desc"),
		ERR_MSG("errmsg"),
		FAIL_CAUSE("failcau"),*/
		;
		
		private String KEY;
		
		private InboundSmsParams(String param){
			this.KEY = param;
		}
		public String KEY()
		{
			return KEY;
		}
		@Override
		public String toString()
		{
			return KEY;
		}
	}
	
	public enum CallLogParams{
		DETAILS("details"),
		CALLID("callid"),
		CALLER_NUM("cllrnum"),
		LIST_ID("cllloglist"),
		SIM("sim"),
		CALL_DATE("recvdate"),
		DURATION("dur"),
		SYSTEM_PROCESS_START_DATE("sysprocstrtdate"),
		TYPE("typ"),
		IS_SAVED("issvd"),
		/*DESC("desc"),
		ERR_MSG("errmsg"),
		FAIL_CAUSE("failcau"),*/
		;
		
		private String KEY;
		
		private CallLogParams(String param){
			this.KEY = param;
		}
		public String KEY()
		{
			return KEY;
		}
		@Override
		public String toString()
		{
			return KEY;
		}
	}
	public enum ProjectParams{
		DETAILS("details"),
		PID("pid"),
		NAME("name"),
		LIST_ID("prjlist"),
		;
		
		private String KEY;
		
		private ProjectParams(String param){
			this.KEY = param;
		}
		public String KEY()
		{
			return KEY;
		}
		@Override
		public String toString()
		{
			return KEY;
		}
	}
	public enum DeviceRegisterParam{
		PROJECT_NAME("prjnam");
		
		private String KEY;
		
		private DeviceRegisterParam(String param){
			this.KEY = param;
		}
		public String KEY()
		{
			return KEY;
		}
		@Override
		public String toString()
		{
			return KEY;
		}
	}
}
