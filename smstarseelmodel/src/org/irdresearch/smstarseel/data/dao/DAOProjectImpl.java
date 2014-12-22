package org.irdresearch.smstarseel.data.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.irdresearch.smstarseel.data.Project;

public class DAOProjectImpl extends DAOHibernateImpl implements DAOProject{
	private Session	session;
	private Number			LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOProjectImpl(Session session)
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
	
	public Project findById(int id)
	{
		Project obj = (Project) session.get(Project.class, id);
		setLAST_QUERY_TOTAL_ROW__COUNT(obj == null ? 0 : 1);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public List<Project> getAll(int firstResult, int fetchsize)
	{
		Criteria cri = session.createCriteria(Project.class);
		
		setLAST_QUERY_TOTAL_ROW__COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Project> findByCriteria(String projectName)
	{
		Criteria cri = session.createCriteria(Project.class);

		if (projectName != null)
		{
			cri.add(Restrictions.like("name", projectName, MatchMode.EXACT));
		}
		
		List l = cri.list();
		setLAST_QUERY_TOTAL_ROW__COUNT(l.size());
		
		return l;
	}
}
