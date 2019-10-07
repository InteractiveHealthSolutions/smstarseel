package org.irdresearch.smstarseel.data.dao;

import java.util.List;

public interface DAODirectQuery extends DAO{

	List getDataByHQL(String hql);

	List getDataBySQL(String sql);
	
	List getDataByHQLMapResult(String hql);

	List getDataBySQLMapResult(String sql);
}
