package org.irdresearch.smstarseel.data.dao;

import java.util.List;

import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.Device;
import org.irdresearch.smstarseel.data.Device.DeviceStatus;

public interface DAODevice extends DAO{

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	Device findById(int id);
	
	List<Device> findByCriteria(String imei, DeviceStatus status , boolean notStatus, String projectName , String sim,  int firstResult, int fetchsize) throws DataException;

	List<Device> getAll(int firstResult, int fetchsize);
}
