package org.irdresearch.smstarseel.service;

import java.util.List;

import org.irdresearch.smstarseel.data.dao.DAO;

public interface CustomQueryService extends DAO{
	
	List getDataByHQL(String hql);
	
	List getDataBySQL(String sql);
	
	List getDataByHQLMapResult(String hql);

	List getDataBySQLMapResult(String sql);
	
}
