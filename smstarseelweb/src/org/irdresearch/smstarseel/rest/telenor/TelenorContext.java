package org.irdresearch.smstarseel.rest.telenor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.jmatrix.eproperties.EProperties;

import org.apache.log4j.Logger;
import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.rest.util.HttpUtil;

import com.mysql.jdbc.StringUtils;

public class TelenorContext {
	public enum Config {
		BASE_URL("telenor.api.base-url"),
		AUTH_URL("telenor.api.url.auth"),
		PING_URL("telenor.api.url.ping"),
		OUTBOUND_DISPATCH_URL("telenor.api.url.outbound.dispatch"),
		OUTBOUND_QUERY_URL("telenor.api.url.outbound.query"),
		OUTBOUND_KEEP_LOG("telenor.outbound.keep-log"),
		OUTBOUND_AUTO_RETRY("telenor.outbound.auto-retry"),
		SUBSCRIBER_CREATE_LIST_URL("telenor.api.url.subscriber.create-list"),
		SUBSCRIBER_LIST_ADD_CONTACT_URL("telenor.api.url.subscriber-list.add-contacts"),
		CAMPAIGN_CREATE_URL("telenor.api.url.campaign.create"),
		CAMPAIGN_QUERY_URL("telenor.api.url.campaign.query"),
		CALL_DISPATCH_URL("telenor.api.url.call.dispatch"),
		CALL_QUERY_URL("telenor.api.url.call.query"),
		MSISDN("telenor.msisdn"),
		PASSWORD("telenor.password"),
		;
		
		private String property;
		
		private Config(String propertyName) {
			this.property = propertyName;
		}

		public String property() { return property; }
		
		public static String fullUrl(Config serviceUrl){
			String base = HttpUtil.removeEndingSlash(getProperty(BASE_URL, null));
			String service = HttpUtil.removeEndingSlash(HttpUtil.removeTrailingSlash(getProperty(serviceUrl, null)));
			return base + "/" + service;
		}
		
		public static boolean isConfigured(Config property) {
			if(StringUtils.isEmptyOrWhitespaceOnly(getProperty(property.property, null))){
				return false;
			}
			return true;
		}

	}
	private static Logger lg = Logger.getLogger(TelenorContext.class);
	
	private static Properties properties;
	
	public static Properties getProperties() { return properties; }
	static void setProperties(Properties properties) { TelenorContext.properties = properties; }

	static {
		try {
			lg.info(">>>>LOADING TELENOR PROPERTIES...");
			InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream("telenor.properties");
			if(f == null){
				lg.warn("Telenor Properties not configured.. System not using Telenor. If needed configure all required properties in telenor.properties file");
			}
			else{
				// Java Properties donot seem to support substitutions hence EProperties are used to accomplish the task
				EProperties root = new EProperties();
				root.load(f);
		
				// Java Properties to send to context and other APIs for configuration
				Properties prop = new Properties();
				prop.putAll(SmsTarseelUtil.convertEntrySetToMap(root.entrySet()));
		
				properties = prop;
				
				if(StringUtils.isEmptyOrWhitespaceOnly(getProperty(Config.BASE_URL, null))){
					throw new RuntimeException("No property configured as "+Config.BASE_URL.property);
				}
				if(StringUtils.isEmptyOrWhitespaceOnly(getProperty(Config.PING_URL, null))){
					throw new RuntimeException("No property configured as "+Config.PING_URL.property);
				}
				if(StringUtils.isEmptyOrWhitespaceOnly(getProperty(Config.MSISDN, null))){
					throw new RuntimeException("No property configured as "+Config.MSISDN.property);
				}
				if(StringUtils.isEmptyOrWhitespaceOnly(getProperty(Config.PASSWORD, null))){
					throw new RuntimeException("No property configured as "+Config.PASSWORD.property);
				}
				
				lg.info("......TELENOR PROPERTIES LOADED SUCCESSFULLY......");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
	
	public static String getProperty(String name, String defaultVal) {
		return properties.getProperty(name, defaultVal);
	}
	
	public static String getProperty(Config name, String defaultVal) {
		return properties.getProperty(name.property(), defaultVal);
	}
	
	public static String createReferenceNumber(Long messageId) {
		return "Telenor:"+messageId.toString();
	}
	
	public static String createReferenceNumber(Object messageId) {
		return createReferenceNumber(new Double(messageId.toString()).longValue());
	}
	
	public static String getMessageId(String referenceNumber) {
		return referenceNumber.substring(referenceNumber.lastIndexOf(":")+1);
	}

}
