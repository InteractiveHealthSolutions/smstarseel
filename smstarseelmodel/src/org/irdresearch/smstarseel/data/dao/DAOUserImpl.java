package org.irdresearch.smstarseel.data.dao;

// Generated May 2, 2011 12:48:03 PM by Hibernate Tools 3.2.0.b9

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.data.User.UserStatus;

/**
 * Home object for domain model class Users.
 * @see org.ird.immunizationreminder.datamodel.entities.Users
 * @author Hibernate Tools
 */
public class DAOUserImpl extends DAOHibernateImpl implements DAOUser{
	private Session session ;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOUserImpl(Session session) {
		super(session);
		this.session=session;
	}

	public User findById(int id) {
		User u= (User)session.get(User.class,id);
		setLAST_QUERY_TOTAL_ROW__COUNT(u==null?0:1);
		return u;
	}
	
	@SuppressWarnings("unchecked")
	public User findByUsername(String username) {
		List<User> ulist=session.createQuery("from User where name=? ").setString(0, username).list();
		setLAST_QUERY_TOTAL_ROW__COUNT(ulist.size());
		if(ulist.size()>0){
			return ulist.get(0);
		}
		return null;
	}
	
	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}

	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getAll(int firstResult, int fetchsize) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return session.createQuery("from User order by name").setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}

	public Number countAllRows() {
		return (Number) session.createQuery("select count(*) from User").uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findByCriteria(String email,String partOfFirstOrLastName,UserStatus userStatus,boolean putNotWithUserStatus, int firstResult, int fetchsize) throws DataException {
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaRows(email, partOfFirstOrLastName, userStatus, putNotWithUserStatus));
		Criteria cri= session.createCriteria(User.class);
		if(partOfFirstOrLastName!=null){
			cri.add(Restrictions.or(Restrictions.like("firstName", partOfFirstOrLastName,MatchMode.START)
									,Restrictions.like("lastName", partOfFirstOrLastName,MatchMode.START)));
		}
		if(email!=null){
			cri.add(Restrictions.like("email", email,MatchMode.EXACT));
		}
		if(userStatus!=null){
			try{
				if(putNotWithUserStatus){
					cri.add(Restrictions.not(Restrictions.eq("status", userStatus)));
				}else{
					cri.add(Restrictions.eq("status", userStatus));
				}
			}catch (Exception e) {
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		return cri.addOrder(Order.asc("name")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}

	public Number countCriteriaRows(String email,String partOfFirstOrLastName,UserStatus userStatus,boolean putNotWithUserStatus) throws DataException {
		Criteria cri= session.createCriteria(User.class);
		if(partOfFirstOrLastName!=null){
			cri.add(Restrictions.or(Restrictions.like("firstName", partOfFirstOrLastName,MatchMode.START)
									,Restrictions.like("lastName", partOfFirstOrLastName,MatchMode.START)));
		}
		if(email!=null){
			cri.add(Restrictions.like("email", email,MatchMode.EXACT));
		}
		if(userStatus!=null){
			try{
				if(putNotWithUserStatus){
					cri.add(Restrictions.not(Restrictions.eq("status", userStatus)));
				}else{
					cri.add(Restrictions.eq("status", userStatus));
				}
			}catch (Exception e) {
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}

}
