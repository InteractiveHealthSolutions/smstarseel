package org.irdresearch.smstarseel.data.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.irdresearch.smstarseel.data.CallLog;
import org.irdresearch.smstarseel.data.CallLog.CallStatus;
import org.irdresearch.smstarseel.data.CallLog.CallType;
import org.irdresearch.smstarseel.data.DataException;

public class DAOCallLogImpl extends DAOHibernateImpl implements DAOCallLog{
	private Session	session;
	private Number			LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOCallLogImpl(Session session)
	{
		super(session);
		this.session = session;
	}

	public CallLog findById(long id)
	{
		CallLog usms = (CallLog) session.get(CallLog.class, id);
		setLAST_QUERY_TOTAL_ROW__COUNT(usms == null ? 0 : 1);
		return usms;
	}
	
	@SuppressWarnings("unchecked")
	public CallLog findByReferenceNumber(String referenceNumber, boolean readonly)
	{
		List<CallLog> list = session.createQuery("from CallLog where referenceNumber='"+referenceNumber+"' ")
				.setReadOnly(readonly).list();
		setLAST_QUERY_TOTAL_ROW__COUNT(list.size());
		return list.size()>0?list.get(0):null;
	}

	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT)
	{
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}

	public Number LAST_QUERY_TOTAL_ROW__COUNT()
	{
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}

	@SuppressWarnings("unchecked")
	public List<CallLog> getAll(int firstResult, int fetchsize, boolean readonly)
	{
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return session.createQuery("from CallLog order by callDate desc")
				.setFirstResult(firstResult).setMaxResults(fetchsize).setReadOnly(readonly).list();
	}

	public Number countAllRows()
	{
		return (Number) session.createQuery(
				"select count(*) from CallLog").uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<CallLog> findByCriteria(Date callDatesmaller, Date callDategreater
			, CallType callType , String recipient,  String caller,
			boolean putNotWithCallType, CallStatus	callStatus, String imei,  Integer projectId)
			throws DataException
	{
		setLAST_QUERY_TOTAL_ROW__COUNT(null);
		Criteria cri = session.createCriteria(CallLog.class);

		if (callDatesmaller != null && callDategreater != null)
		{
			cri.add(Restrictions.between("callDate", callDatesmaller, callDategreater));
		}
		if (recipient != null)
		{
			cri.add(Restrictions.like("recipient", recipient, MatchMode.END));
		}
		if (caller != null)
		{
			cri.add(Restrictions.like("callerNumber", caller, MatchMode.END));
		}
		if (imei != null)
		{
			cri.add(Restrictions.like("imei", imei, MatchMode.EXACT));
		}
		if (projectId != null)
		{
			cri.add(Restrictions.eq("projectId", projectId));
		}
		if (callType != null)
		{
			try
			{
				if (putNotWithCallType)
				{
					cri.add(Restrictions.not(Restrictions.eq("callType", callType)));
				}
				else
				{
					cri.add(Restrictions.eq("callType", callType));
				}
			}
			catch (Exception e)
			{
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if (callStatus != null)
		{
			cri.add(Restrictions.eq("callStatus", callStatus));
		}
		return cri.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<CallLog> findByCriteria(Date callDatesmaller, Date callDategreater
			,CallType callType , String recipient,  String caller,
			boolean putNotWithCallType, CallStatus	callStatus, String imei,  String projectName, int firstResult, int fetchsize)
			throws DataException
	{
		Criteria cri = session.createCriteria(CallLog.class);

		if (callDatesmaller != null && callDategreater != null)
		{
			cri.add(Restrictions.between("callDate", callDatesmaller, callDategreater));
		}
		if (recipient != null)
		{
			cri.add(Restrictions.like("recipient", recipient, MatchMode.END));
		}
		if (caller != null)
		{
			cri.add(Restrictions.like("callerNumber", caller, MatchMode.END));
		}
		if (imei != null)
		{
			cri.add(Restrictions.like("imei", imei, MatchMode.EXACT));
		}
		if (projectName != null)
		{
			cri.createAlias("project", "p").add(Restrictions.like("p.name", projectName, MatchMode.EXACT));
		}
		if (callStatus != null)
		{
			cri.add(Restrictions.eq("callStatus", callStatus));
		}
		if (callType != null)
		{
			try
			{
				if (putNotWithCallType)
				{
					cri.add(Restrictions.not(Restrictions.eq("callType", callType)));
				}
				else
				{
					cri.add(Restrictions.eq("callType", callType));
				}
			}
			catch (Exception e)
			{
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		
		setLAST_QUERY_TOTAL_ROW__COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}

	@Override
	public int markCallAsRead(String referenceNumber)
	{
		Query q = session.createQuery("update CallLog set callStatus = '"+CallStatus.READ+"' where referenceNumber = '"+referenceNumber+"' ");
		return q.executeUpdate();
	}

	@Override
	public int markCallAsRead(long calllogId)
	{
		Query q = session.createQuery("update CallLog set callStatus = '"+CallStatus.READ+"' where callLogId = "+calllogId+" ");
		return q.executeUpdate();
	}
}
