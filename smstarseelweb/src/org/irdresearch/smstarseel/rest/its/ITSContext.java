package org.irdresearch.smstarseel.rest.its;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Timer;

import net.jmatrix.eproperties.EProperties;

import org.apache.log4j.Logger;
import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.rest.util.HttpUtil;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;

@Service
public class ITSContext {
	public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static Timer timer = instantiateTimer();
	
	public enum ITSConfig {
		BASE_URL("its.api.base-url"),
		OUTBOUND_DISPATCH_URL("its.api.url.outbound.dispatch"),
		OUTBOUND_QUERY_URL("its.api.url.outbound.query"),
		INBOUND_QUERY_URL("its.api.url.inbound.query"),
		OUTBOUND_KEEP_LOG("its.outbound.keep-log"),
		OUTBOUND_AUTO_RETRY("its.outbound.auto-retry"),
		USERNAME("its.username"),
		PASSWORD("its.password"),
		;
		
		private String property;
		
		private ITSConfig(String propertyName) {
			this.property = propertyName;
		}

		public String property() { return property; }
		
		public static String fullUrl(ITSConfig service){
			String base = HttpUtil.removeEndingSlash(getProperty(BASE_URL, null));
			String s = HttpUtil.removeEndingSlash(HttpUtil.removeTrailingSlash(getProperty(service, null)));
			return base + "?action=" + s;
		}
		
		public static boolean isConfigured(ITSConfig property) {
			if(StringUtils.isEmptyOrWhitespaceOnly(getProperty(property.property, null))){
				return false;
			}
			return true;
		}

	}
	private static Logger lg = Logger.getLogger(ITSContext.class);
	
	private static Properties properties;
	
	public static Properties getProperties() { return properties; }
	
	private static Timer instantiateTimer() {
		if(timer != null){
			timer.cancel();
		}
		Timer t = new Timer();
		t.schedule(new ITSInboundServiceJob(), 1000*120, 1000*5);
		return t;
	}
	
	static void setProperties(Properties properties) { ITSContext.properties = properties; }

	static {
		try {
			lg.info(">>>>LOADING ITS PROPERTIES...");
			InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream("its.properties");
			if(f == null){
				lg.warn("ITS Properties not configured.. System not using ITS. If needed configure all required properties in its.properties file");
			}
			else{
				// Java Properties donot seem to support substitutions hence EProperties are used to accomplish the task
				EProperties root = new EProperties();
				root.load(f);
		
				// Java Properties to send to context and other APIs for configuration
				Properties prop = new Properties();
				prop.putAll(SmsTarseelUtil.convertEntrySetToMap(root.entrySet()));
		
				properties = prop;
				
				if(StringUtils.isEmptyOrWhitespaceOnly(getProperty(ITSConfig.BASE_URL, null))){
					throw new RuntimeException("No property configured as "+ITSConfig.BASE_URL.property);
				}
				if(StringUtils.isEmptyOrWhitespaceOnly(getProperty(ITSConfig.USERNAME, null))){
					throw new RuntimeException("No property configured as "+ITSConfig.USERNAME.property);
				}
				if(StringUtils.isEmptyOrWhitespaceOnly(getProperty(ITSConfig.PASSWORD, null))){
					throw new RuntimeException("No property configured as "+ITSConfig.PASSWORD.property);
				}
				
				lg.info("......ITS PROPERTIES LOADED SUCCESSFULLY......");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
	
	public static String getProperty(String name, String defaultVal) {
		return properties.getProperty(name, defaultVal);
	}
	
	public static String getProperty(ITSConfig name, String defaultVal) {
		return properties.getProperty(name.property(), defaultVal);
	}
	
	public static String createReferenceNumber(Long messageId) {
		return "ITS:"+messageId.toString();
	}
	
	public static String createReferenceNumber(Object messageId) {
		return createReferenceNumber(new Double(messageId.toString()).longValue());
	}
	
	public static String getMessageId(String referenceNumber) {
		return referenceNumber.substring(referenceNumber.lastIndexOf(":")+1);
	}

}
