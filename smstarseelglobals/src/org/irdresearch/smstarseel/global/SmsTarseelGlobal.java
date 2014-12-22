package org.irdresearch.smstarseel.global;


public class SmsTarseelGlobal {

	public enum SmsSendStatus{
		SUCCESS,
		FAILURE
	}

	public static final int			APP_URL_LENGTH					= 54;
	public static final String		JSON_REQUEST_DATA_PARAM_NAME	= "jsonparam";
	public static final String		XML_RESPONSE_ROOT_TAG			= "smstarseel";
	//public static final String		SERVER_URL						= "http://10.0.2.2:8080/smstarseelweb/smstarseel";
	public static final String		VERSION							= "";
	public static final int			USERNAME_MIN_LENGTH				= 3;
	public static final int			PASSWORD_MIN_LENGTH				= 3;
	public static final String		DEFAULT_DATE_FORMAT				= "dd-MM-yyyy HH:mm:ss";
	//public static final SimpleDateFormat ddMMMyyHHmmss_DF	= new SimpleDateFormat("ddMMMyy HH:mm:ss");

	public static final String		LIST_SEPARATER					= ";";
	public static final String		XML_LIST_NAME					= "maplist";
	//public static final int			MAX_OUTBOUND_FETCH_PER_GO		= 12;
	public static final String		XML_LIST_ITEM_TAG_NAME			= "listitem";
	public static final String		MOBILE_NUM_REGX					= "(\\+92|92|03)?[0-9]{9}";
	public static final String		PTCL_WIRELESS_NUM_REGX			= "(\\+92|92|0)?213[0-9]{7}";
	public static final String		PTCL_LANDLINE_NUM_REGX			= "(\\+92|92|0)?213[0-9]{7}";
	/*public static final String[]	ALLOWABLE_CONTACT_NUM_REGX		= new String[] {
			"(\\+92|92|0)?3[0-9]{9}", "(\\+92|92|0)?213[0-9]{7}"	};*/
	public static final String SOCKET_MESSAGE_END_FLAG = "//=_TrslSktEnd_=//";
	public static final String SOCKET_MESSAGE_START_FLAG = "//=_TrslSktStart_=//";

}
