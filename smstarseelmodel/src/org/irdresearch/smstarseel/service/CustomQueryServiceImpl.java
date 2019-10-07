package org.irdresearch.smstarseel.service;

import java.io.Serializable;
import java.util.List;

import org.irdresearch.smstarseel.data.dao.DAO;
import org.irdresearch.smstarseel.data.dao.DAODirectQuery;

public class CustomQueryServiceImpl implements CustomQueryService{

	private DAODirectQuery daoqur;
	
	public CustomQueryServiceImpl(DAODirectQuery daoqur) {
		this.daoqur = daoqur;
	}

	@Override
	public List getDataByHQL(String hql) {
		return daoqur.getDataByHQL(hql);
	}

	@Override
	public List getDataBySQL(String sql) {
		return daoqur.getDataBySQL(sql);
	}
	
	@Override
	public List getDataByHQLMapResult(String hql) {
		return daoqur.getDataByHQLMapResult(hql);
	}

	@Override
	public List getDataBySQLMapResult(String sql) {
		return daoqur.getDataBySQLMapResult(sql);
	}

	@Override
	public Serializable save(Object objectinstance) {
		return daoqur.save(objectinstance);
	}

	@Override
	public void delete(Object objectinstance) {
		throw new UnsupportedOperationException("you can not delete any data");
	}

	@Override
	public Object merge(Object objectinstance) {
		return daoqur.merge(objectinstance);
	}

	@Override
	public void update(Object objectinstance) {
		daoqur.update(objectinstance);
	}
}
