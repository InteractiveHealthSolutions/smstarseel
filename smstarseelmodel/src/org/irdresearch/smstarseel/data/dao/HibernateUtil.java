package org.irdresearch.smstarseel.data.dao;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.mysql.jdbc.StringUtils;

public class HibernateUtil {
	
	private static SessionFactory sessionFactory;

 	/**
 	 * Gets the sessionFactory with given Properties. 
 	 * @param properties Properties that would be used to configure hibernate. Null if should be default i.e. read from cfg.xml file
 	 * @param configFileName File where hibernate mapping are defined. Null if should be default i.e. hibernate.cfg.xml
 	 * @return
 	 */
 	public synchronized static SessionFactory getSessionFactory (Properties properties, String configFileName) {
 		if (sessionFactory == null) {
			Configuration conf = new Configuration();

			conf.addAnnotatedClass(org.irdresearch.smstarseel.data.CallLog.class);
			conf.addAnnotatedClass(org.irdresearch.smstarseel.data.Device.class);
			conf.addAnnotatedClass(org.irdresearch.smstarseel.data.InboundMessage.class);
			conf.addAnnotatedClass(org.irdresearch.smstarseel.data.Setting.class);
			conf.addAnnotatedClass(org.irdresearch.smstarseel.data.OutboundMessage.class);
			conf.addAnnotatedClass(org.irdresearch.smstarseel.data.Permission.class);
			conf.addAnnotatedClass(org.irdresearch.smstarseel.data.Project.class);
			conf.addAnnotatedClass(org.irdresearch.smstarseel.data.Role.class);
			conf.addAnnotatedClass(org.irdresearch.smstarseel.data.User.class);
			
			if(properties != null){
				conf.setProperties(properties);
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(configFileName)){
				conf.configure(configFileName);
			}
			else {
				conf.configure();
			}
			
			sessionFactory = conf.buildSessionFactory();
		}
		return sessionFactory;
	}
}
