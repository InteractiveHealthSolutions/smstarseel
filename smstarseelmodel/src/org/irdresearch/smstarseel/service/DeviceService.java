package org.irdresearch.smstarseel.service;

import java.util.List;

import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.Device;
import org.irdresearch.smstarseel.data.Device.DeviceStatus;
import org.irdresearch.smstarseel.data.Project;

public interface DeviceService {

	Number LAST_QUERY_TOTAL_ROW__COUNT(Class clazz);

	Device findDeviceById(int id);
	
	List<Device> findDevice(String imei, DeviceStatus status, boolean notStatus , String projectName , String sim, int firstResult, int fetchsize) throws DataException;

	List<Device> getAllDevices(int firstResult, int fetchsize);
	
	void saveDevice(Device device);
	
	void updateDevice(Device device);
	
	Project findProjectById(int id);
	
	List<Project> findProject(String projectname);

	List<Project> getAllProjects(int firstResult, int fetchsize);
	
	void saveProject(Project project);
	
	void updateProject(Project project);
}
