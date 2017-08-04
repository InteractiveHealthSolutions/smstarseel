package org.irdresearch.smstarseel;

public class TarseelWebGlobals {

	public static int	MAX_OUTBOUND_FETCH_PER_GO		= 6;
	public static int	MAX_OUTBOUND_SPAM_DUPLICATE_BOUNDARY		= 2;
	public static int	MAX_OUTBOUND_SPAM_SMS_ALLOWED_BOUNDARY		= 6;

	public enum SmsServiceConstants{
		API_KEY,
		TELENOR_MESSAGE_ID,
		ITS_MESSAGE_ID,
		EXTERNAL_SYSTEM_MESSAGE_ID,
		EXTERNAL_SYSTEM_ID,
		EXTERNAL_SYSTEM_CONTACT_ID,
		SERVICE_LOG_ID,
		SERVICE_LOG_LAST_RESPONSE
	}
}
