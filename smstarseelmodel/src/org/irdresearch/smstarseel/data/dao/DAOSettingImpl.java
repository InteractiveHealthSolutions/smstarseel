package org.irdresearch.smstarseel.data.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.irdresearch.smstarseel.data.Setting;

public class DAOSettingImpl extends DAOHibernateImpl implements DAOSetting{

	private Session session ;

	public DAOSettingImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	@SuppressWarnings("unchecked")
	public List<Setting> getAll() {
		return session.createQuery("from Setting").list();
	}

	public Setting findById(int id) {
			return (Setting) session.get(Setting.class,id);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Setting getSetting(String settingName) {
		List l= session.createCriteria(Setting.class)
								.add(Restrictions.like("name", settingName,MatchMode.EXACT)).list();
		if(l.size()>0){
			return (Setting) l.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Setting> matchByCriteria(String settingName) {
		Criteria cri= session.createCriteria(Setting.class)
								.add(Restrictions.like("name", settingName,MatchMode.ANYWHERE));
		return cri.list();
	}
}
