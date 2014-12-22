package org.irdresearch.smstarseel.service;

import java.util.List;

import org.irdresearch.smstarseel.data.Setting;

public interface SettingService {
	Setting getSetting(String name);

	void updateSetting(Setting setting);

	List<Setting> getSettings();

	List<Setting> matchSetting(String settingName);
}
