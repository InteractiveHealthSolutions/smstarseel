package org.irdresearch.smstarseel.service;

import java.util.List;

import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.Device;
import org.irdresearch.smstarseel.data.Device.DeviceStatus;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.data.dao.DAODevice;
import org.irdresearch.smstarseel.data.dao.DAOProject;

public class DeviceServiceImpl implements DeviceService{

	DAODevice daodevice;
	DAOProject daoproj;
	
	public DeviceServiceImpl(DAODevice daodevice, DAOProject daoproj)
	{
		this.daodevice = daodevice;
		this.daoproj = daoproj;
	}

	public Number LAST_QUERY_TOTAL_ROW__COUNT(Class clazz) {
		if(clazz == Device.class){
			return daodevice.LAST_QUERY_TOTAL_ROW__COUNT();
		}
		
		if(clazz == Project.class){
			return daoproj.LAST_QUERY_TOTAL_ROW__COUNT();
		}
		
		return null;
	}
	
	public List<Device> findDevice(String imei, DeviceStatus status , boolean notStatus, String projectName, String sim, int firstResult, int fetchsize)
			throws DataException
	{
		return daodevice.findByCriteria(imei, status, notStatus ,projectName, sim, firstResult, fetchsize);
	}

	public Device findDeviceById(int id)
	{
		return daodevice.findById(id);
	}

	public List<Project> findProject(String projectname)
	{
		return daoproj.findByCriteria(projectname);
	}

	public Project findProjectById(int id)
	{
		return daoproj.findById(id);
	}

	public List<Device> getAllDevices(int firstResult, int fetchsize)
	{
		return daodevice.getAll(firstResult, fetchsize);
	}

	public List<Project> getAllProjects(int firstResult, int fetchsize)
	{
		return daoproj.getAll(firstResult, fetchsize);
	}

	public void saveDevice(Device device)
	{
		daodevice.save(device);
	}

	public void saveProject(Project project)
	{
		daoproj.save(project);
	}

	public void updateDevice(Device device)
	{
		daodevice.update(device);
	}

	public void updateProject(Project project)
	{
		daoproj.update(project);
	}
}
