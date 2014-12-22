package org.irdresearch.smstarseel.data.dao;

import java.io.Serializable;

import org.hibernate.Session;

public class DAOHibernateImpl implements DAO {

	private Session	session;

	public DAOHibernateImpl(Session session)
	{
		this.session = session;
	}

	public Serializable save(Object objectinstance)
	{
		return session.save(objectinstance);
	}

	public void delete(Object objectinstance)
	{
		session.delete(objectinstance);
	}

	public Object merge(Object objectinstance)
	{
		return session.merge(objectinstance);
	}

	public void update(Object objectinstance)
	{
		session.update(objectinstance);
	}

}
