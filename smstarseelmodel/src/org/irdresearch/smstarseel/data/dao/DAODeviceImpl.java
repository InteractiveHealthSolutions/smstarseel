package org.irdresearch.smstarseel.data.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.Device;
import org.irdresearch.smstarseel.data.Device.DeviceStatus;

public class DAODeviceImpl extends DAOHibernateImpl implements DAODevice{
	private Session	session;
	private Number			LAST_QUERY_TOTAL_ROW__COUNT;

	public DAODeviceImpl(Session session)
	{
		super(session);
		this.session = session;
	}

	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT)
	{
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}

	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT()
	{
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	
	public Device findById(int id)
	{
		Device obj = (Device) session.get(Device.class, id);
		setLAST_QUERY_TOTAL_ROW__COUNT(obj == null ? 0 : 1);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public List<Device> getAll(int firstResult, int fetchsize)
	{
		Criteria cri = session.createCriteria(Device.class);
		
		setLAST_QUERY_TOTAL_ROW__COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return cri.addOrder(Order.desc("dateAdded")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}

	@SuppressWarnings("unchecked")
	public List<Device> findByCriteria(String imei, DeviceStatus status , boolean notStatus, String projectName , String sim, int firstResult, int fetchsize) throws DataException
	{
		Criteria cri = session.createCriteria(Device.class);

		if (imei != null)
		{
			cri.add(Restrictions.like("imei", imei, MatchMode.EXACT));
		}
		if (sim != null)
		{
			cri.add(Restrictions.like("sim", sim, MatchMode.EXACT));
		}
		if (projectName != null)
		{
			cri.createAlias("project", "p").add(Restrictions.like("p.name", projectName, MatchMode.EXACT));
		}
		if (status != null)
		{
			try
			{
				if(notStatus){
					cri.add(Restrictions.not(Restrictions.eq("status", status)));
				}
				else{
					cri.add(Restrictions.eq("status", status));
				}
			}
			catch (Exception e)
			{
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		
		setLAST_QUERY_TOTAL_ROW__COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return cri.addOrder(Order.desc("dateAdded")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
}
