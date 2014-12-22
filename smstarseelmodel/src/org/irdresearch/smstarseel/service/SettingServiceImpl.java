package org.irdresearch.smstarseel.service;

import java.util.List;

import org.irdresearch.smstarseel.data.Setting;
import org.irdresearch.smstarseel.data.dao.DAOSetting;

public class SettingServiceImpl implements SettingService{
	
	private DAOSetting dao;
	
	public SettingServiceImpl(DAOSetting dao) {
		this.dao=dao;
	}

	public Setting getSetting(String name) {
		return dao.getSetting(name);
	}

	public List<Setting> getSettings() {
		return dao.getAll();
	}

	public List<Setting> matchSetting(String settingName){
		return dao.matchByCriteria(settingName);
	}

	public void updateSetting(Setting setting) {
		dao.update(setting);
	}
}
