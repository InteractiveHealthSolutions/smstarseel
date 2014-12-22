package org.irdresearch.smstarseel.data.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;

public class DAOOutboundMessageImpl extends DAOHibernateImpl implements DAOOutBoundMessage{
	private Session	session;
	private Number			LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOOutboundMessageImpl(Session session)
	{
		super(session);
		this.session = session;
	}

	public OutboundMessage findById(long id)
	{
		OutboundMessage usms = (OutboundMessage) session.get(OutboundMessage.class, id);
		setLAST_QUERY_TOTAL_ROW__COUNT(usms == null ? 0 : 1);
		return usms;
	}

	@SuppressWarnings("unchecked")
	public OutboundMessage findByReferenceNumber(String referenceNumber, boolean readonly)
	{
		List<OutboundMessage> list = session.createQuery("from OutboundMessage where referenceNumber='"+referenceNumber+"' ")
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
	public List<OutboundMessage> getAll(int firstResult, int fetchsize)
	{
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return session.createQuery("from OutboundMessage order by dueDate desc")
				.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}

	public Number countAllRows()
	{
		return (Number) session.createQuery(
				"select count(*) from OutboundMessage").uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<OutboundMessage> findByCriteria(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater
			, OutboundStatus smsStatus, String recipient,  String originator
			, String imei,  Integer projectId,
			boolean putNotWithSmsStatus, boolean orderByPriority) throws DataException
	{
		setLAST_QUERY_TOTAL_ROW__COUNT(null);
		Criteria cri = session.createCriteria(OutboundMessage.class);

		if (duedatesmaller != null && duedategreater != null)
		{
			cri.add(Restrictions.between("dueDate", duedatesmaller, duedategreater));
		}
		if (sentdatesmaller != null && sentdategreater != null)
		{
			cri.add(Restrictions.between("sentDate", sentdatesmaller, sentdategreater));
		}
		if (recipient != null)
		{
			cri.add(Restrictions.like("recipient", recipient, MatchMode.END));
		}
		if (originator != null)
		{
			cri.add(Restrictions.like("originator", originator, MatchMode.END));
		}
		if (imei != null)
		{
			cri.add(Restrictions.like("imei", imei, MatchMode.EXACT));
		}
		if (projectId != null)
		{
			cri.add(Restrictions.eq("projectId", projectId));
		}
		if (smsStatus != null)
		{
			try
			{
				if (putNotWithSmsStatus)
				{
					cri.add(Restrictions.not(Restrictions.eq("status", smsStatus)));
				}
				else
				{
					cri.add(Restrictions.eq("status", smsStatus));
				}
			}
			catch (Exception e)
			{
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if(orderByPriority){
			cri.addOrder(Order.asc("priority"));
		}
		return cri.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<OutboundMessage> findByCriteria(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater
			, OutboundStatus smsStatus,  String recipient,  String originator
			, String imei,  String projectName,
			boolean putNotWithSmsStatus, boolean orderByPriority, int firstResult, int fetchsize) throws DataException
	{
		Criteria cri = session.createCriteria(OutboundMessage.class);

		if (duedatesmaller != null && duedategreater != null)
		{
			cri.add(Restrictions.between("dueDate", duedatesmaller, duedategreater));
		}
		if (sentdatesmaller != null && sentdategreater != null)
		{
			cri.add(Restrictions.between("sentDate", sentdatesmaller, sentdategreater));
		}
		if (recipient != null)
		{
			cri.add(Restrictions.like("recipient", recipient, MatchMode.END));
		}
		if (originator != null)
		{
			cri.add(Restrictions.like("originator", originator, MatchMode.END));
		}
		if (imei != null)
		{
			cri.add(Restrictions.like("imei", imei, MatchMode.EXACT));
		}
		if (projectName != null)
		{
			cri.createAlias("project", "p").add(Restrictions.like("p.name", projectName, MatchMode.EXACT));
		}
		if (smsStatus != null)
		{
			try
			{
				if (putNotWithSmsStatus)
				{
					cri.add(Restrictions.not(Restrictions.eq("status", smsStatus)));
				}
				else
				{
					cri.add(Restrictions.eq("status", smsStatus));
				}
			}
			catch (Exception e)
			{
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if(orderByPriority){
			cri.addOrder(Order.asc("priority"));
		}
		
		setLAST_QUERY_TOTAL_ROW__COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
}
