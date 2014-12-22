package org.irdresearch.smstarseel.data.dao;

import java.util.List;

import org.irdresearch.smstarseel.data.Setting;

public interface DAOSetting extends DAO{
	
	Setting findById(int id);
	
	List<Setting> matchByCriteria(String settingName);
	
	List<Setting> getAll();
	
	Setting getSetting(String settingName);
}
