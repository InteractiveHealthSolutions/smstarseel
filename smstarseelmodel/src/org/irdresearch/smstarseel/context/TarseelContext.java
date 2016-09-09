package org.irdresearch.smstarseel.context;

import java.util.HashMap;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.irdresearch.smstarseel.data.Setting;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.data.dao.HibernateUtil;
import org.irdresearch.smstarseel.service.UserServiceException;
import org.irdresearch.smstarseel.service.utils.LoggerUtil;
import org.irdresearch.smstarseel.service.utils.SecurityUtils;

public class TarseelContext {

	public static HashMap<String, ActiveDevice> 	ACTIVE_DEVICES   = new HashMap<String, ActiveDevice>();

	private static TarseelContext _instance;
	private HashMap<String,Setting> currentSettings;
	private static Properties properties;
	
	public static String property(String name, String defaultVal) {
		if(properties == null){
			return defaultVal;
		}
		return properties.getProperty(name, defaultVal);
	}

	private TarseelContext(Properties props) {
		System.out.println("\nLoading Settings....");
		currentSettings = getAllSettings();
		System.out.println("\nSettings loaded successfully....");
		TarseelContext.properties = props;
	}
	
	private static SessionFactory sessionFactory;

	/**
	 * To access and setup DAOs and Services to Database this method MUST be called ONCE and ONLY ONCE in your application.
	 * @param properties: Object with required (all or partial) hiberate configuration properties. If NULL config file MUST have all required properties
	 * @param configFileName: cfg.xml file that should be used for configuring hibernate. If NULL, hibernate would search for default hibernate.cfg.xml file
	 * @throws InstanceAlreadyExistsException
	 */
	public static void instantiate(Properties properties, String configFileName) throws InstanceAlreadyExistsException{
		if(_instance != null){
			throw new InstanceAlreadyExistsException("An instance of Context already exists in system. Make sure to maintain correct flow.");
		}
		
		// session factory must have been instantiated before we could use any method involving data
		sessionFactory = HibernateUtil.getSessionFactory(properties, configFileName);

		_instance = new TarseelContext(properties);
	}
	
	private static HashMap<String, Setting> getAllSettings() {
		HashMap<String,Setting> curSettings=new HashMap<String, Setting>();
		TarseelServices sc = getServices();
		try{
			for (Setting setting : sc.getSettingService().getSettings()) {
				curSettings.put(setting.getName().trim(), setting);
			}
		}
		finally{
			sc.closeSession();
		}
		
		return curSettings;
	}
	
	public static HashMap<String, Setting> refreshAndGetSettings() {
		return _instance.currentSettings = getAllSettings();
	}
	
	public static String getSetting(String name,String defaultvalue){
		if(_instance.currentSettings.get(name)!=null){
			return _instance.currentSettings.get(name).getValue();
		}
		return defaultvalue;
	}
	
	/** Send only validated values. this function donot validate setting values.
	 *
	 * @param name the name
	 * @param newValue the new value
	 * @param user the user
	 * @return true, if successful
	 */
	public static void updateSetting(String name,String newValue, User editor) {
		Setting setting = _instance.currentSettings.get(name.trim());
		setting.setLastEditor(editor);
		setting.setValue(newValue.trim());
		
		TarseelServices sc = getServices();

		try{
			LoggerUtil.logIt("\nUpdating Setting :"+name);
			sc.getSettingService().updateSetting(setting);
			sc.commitTransaction();
			LoggerUtil.logIt("\nSetting :"+name+" updated to :"+newValue);
		}
		finally{
			sc.closeSession();
		}
	}
	
	public static User getAuthenticatedUser(String username,String password) throws UserServiceException, Exception{
		TarseelServices sc = getServices();

		User user=sc.getUserService().getUser(username);
		sc.closeSession();

		if(user == null){
			throw new UserServiceException(UserServiceException.AUTHENTICATION_EXCEPTION);
		}

		if(password.compareTo(SecurityUtils.decrypt(user.getPassword(),user.getName()))!= 0 ){
			throw new UserServiceException(UserServiceException.AUTHENTICATION_EXCEPTION);
		}
		if(user.getStatus() == User.UserStatus.DISABLED){
			throw new UserServiceException(UserServiceException.ACCOUNT_DISABLED);
		}
		return user;
	}
	
	/*public static Statistics getStatistics(){
		Statistics stats = sessionFactory.getStatistics();
		stats.setStatisticsEnabled(true);
		return stats;
	}*/
	
	/** Before calling this method make sure that TarseelContext has been instantiated ONCE and ONLY ONCE by calling {@linkplain TarseelContext#instantiate} method
	 */
	public static Session getNewSession() {
		return sessionFactory.openSession();
	}
	
	/** Before calling this method make sure that TarseelContext has been instantiated ONCE and ONLY ONCE by calling {@linkplain TarseelContext#instantiate} method
	 *  
	 * NOTE: For assurance of prevention from synchronization and consistency be sure to get new ServiceContext Object
	 * for each bulk or batch of transactions. i.e Using single object for whole application may produce undesired results
	 *
	 * @return the services
	 */
	public static TarseelServices getServices(){
		return new TarseelServices(sessionFactory);
	}
}
