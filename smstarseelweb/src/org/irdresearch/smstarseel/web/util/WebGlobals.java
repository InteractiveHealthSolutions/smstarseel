package org.irdresearch.smstarseel.web.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.mysql.jdbc.StringUtils;

public class WebGlobals {

	public enum TarseelSetting{
		OUTBOUND_FETCH_PER_GO("outbound.fetch-per-go", 6),
		SPAM_OUTBOUND_MAX_DUPLICATE_PER_RECP("outbound.spam.max-duplicate-allowed", 3),
		SPAM_OUTBOUND_MAX_SMS_PER_RECP("outbound.spam.max-sms-allowed", 10),
		OUTBOUND_MAX_RETRIES("outbound.max-retries", 4),
		OUTBOUND_LOST_RETRY_INTERVAL_MIN("outbound.lost-retry.interval-min", 60),
		OUTBOUND_FAILED_RETRY_INTERVAL_MIN("outbound.failed-retry.interval-min", 180),
		ADMIN_EMAIL_ADDRESS("admin.email-address",null),
		CELL_NUMBER_VALIDATOR_REGEX("cell-number.validator-regex", null),
		DAILY_SUMMARY_NOTIFIER_RECIPIENTS("notifier.daily-summary.recipients", null),
		DAILY_SUMMARY_NOTIFIER_TIME("notifier.daily-summary.email-time", "15:00:00"),
		DAILY_SUMMARY_NOTIFIER_LAST_RUN("notifier.daily-summary.last-run", null),
		SERVICE_CRASH_ALERT_RECIPIENTS("service-crash-alert.email-address", null);

		private String NAME;
		private Object DEFAULT;
		
		public String NAME () {
			return NAME;
		}

		public Object DEFAULT () {
			return DEFAULT;
		}

		TarseelSetting(String settingNameInDB, Object defaultValue){
			NAME = settingNameInDB;
			DEFAULT = defaultValue;
		}
	}
	
	public static final String VERSION_CSS_JS = "1.0.0";
	public static final String GLOBAL_DATE_FORMAT = "dd-MMM-yyyy";
	public static final String GLOBAL_DATETIME_FORMAT = "dd-MMM-yyyy HH:mm:ss";
	public static final SimpleDateFormat GLOBAL_SDF_DATE = new SimpleDateFormat(GLOBAL_DATE_FORMAT);
	public static final SimpleDateFormat GLOBAL_SDF_DATETME = new SimpleDateFormat(GLOBAL_DATETIME_FORMAT);

	public enum QueryParams {
		PAGE_NUMBER,
		PAGE_SIZE
	}
	
	public enum SettingQueryParams {
		NAME,
		NEW_VALUE,
	}
	
	public enum UserQueryParams {
		USERNAME,
		OLD_PASSWORD,
		NEW_PASSWORD,
		RENEW_PASSWORD,
		EMAIL,
		STATUS,
		PART_OF_NAME,
		FIRSTNAME,
		LASTNAME,
		ROLE,
		PASSWORD_AUTH,
	}
	public enum CommunicationQueryParams {
		REFERENCE_NUMBER,
		REFERRED_NUMBER,
		ROW_ID,
		IMEI,
		SIM,
		STATUS,
		PROJECT
	}
	
	public enum OutboundQueryParams {
		DUEDATE_FROM,
		DUEDATE_TO,
		SENTDATE_FROM,
		SENTDATE_TO,
		
		DUEDATE,
		TEXT,
		ADD_NOTE,
		VALIDITY,
		VALIDITY_TYPE,
	}
	
	public enum DeviceQueryParams {
		ADDDATE_FROM,
		ADDDATE_TO,
	}
	
	public enum InboundQueryParams {
		RECEIVEDATE_FROM,
		RECEIVEDATE_TO,
	}
	
	public enum CallQueryParams {
		CALLDATE_FROM,
		CALLDATE_TO,
	}
	
	
	public enum ServiceType{
		CHANGE_OWN_PWD,
		CHANGE_OTHER_USER_PWD,
		ADD_USER,
		SEND_TEST_SMS;
		
		public static String convertToString(String boundaryStart, String boundaryEnd, String itemSeparater){
			StringBuffer res = new StringBuffer(StringUtils.isEmptyOrWhitespaceOnly(boundaryStart)?"":boundaryStart.trim());
			for (ServiceType s : values()) {
				res.append(s + ",");
			}
			
			if(res.charAt(res.length()-1) == ','){
				res.deleteCharAt(res.length()-1);
			}
			
			return res.toString();
		}
	}

	/** MUST be in sync with outbound cleanup PROCEDURE error messages*/
	public enum OutBoundDeliveryError {
		LOST_RETRY("LOST_RETRY", "LOST_RETRY"),
		FAILED_RETRY("FAILED_RETRY", "FAILED_RETRY"),
		VALIDITY_PERIOD_PASSED("VALIDITY_PERIOD_PASSED", "VALIDITY_PERIOD_PASSED"),
		REVERTED("REVERTED", "REVERTED"),
		SPAM("SPAM", "SPAM_DETECTED");
		
		private String ERROR_MESSAGE;
		private String FAILURE_CAUSE;
		
		private OutBoundDeliveryError(String failureCause, String errorMessage) {
			this.FAILURE_CAUSE = failureCause;
			this.ERROR_MESSAGE = errorMessage;
		}

		public String ERROR_MESSAGE() {
			return ERROR_MESSAGE;
		}

		public String FAILURE_CAUSE() {
			return FAILURE_CAUSE;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(ServiceType.values()));
	}
}
