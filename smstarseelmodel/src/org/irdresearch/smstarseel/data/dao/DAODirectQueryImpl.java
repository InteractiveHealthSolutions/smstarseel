package org.irdresearch.smstarseel.data.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.transform.Transformers;

public class DAODirectQueryImpl extends DAOHibernateImpl implements DAODirectQuery{

	private Session session;

	public DAODirectQueryImpl(Session session) {
		super(session);
		this.session=session;
	}

	@Override
	public List getDataByHQL(String hql) {
		return session.createQuery(hql).list();
	}

	@Override
	public List getDataBySQL(String sql) {
		return session.createSQLQuery(sql).list();
	}

	@Override
	public List getDataByHQLMapResult(String hql) {
		return session.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public List getDataBySQLMapResult(String sql) {
		return session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
}
