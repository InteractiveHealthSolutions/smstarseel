package org.irdresearch.smstarseel.data.dao;

// Generated May 2, 2011 12:48:03 PM by Hibernate Tools 3.2.0.b9

import java.util.List;

import org.hibernate.Session;
import org.irdresearch.smstarseel.data.Permission;

/**
 * Home object for domain model class Permission.
 * 
 * @see org.ird.immunizationreminder.datamodel.entities.Permission
 * @author Hibernate Tools
 */
public class DAOPermissionImpl extends DAOHibernateImpl implements DAOPermission {

	private Session	session;

	public DAOPermissionImpl(Session session)
	{
		super(session);
		this.session = session;
	}

	public Permission findById(int id)
	{
		return (Permission) session.get(Permission.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Permission> getAll(boolean isreadonly)
	{
		return session.createQuery("from Permission order by name")
				.setReadOnly(isreadonly).list();
	}

	@SuppressWarnings("unchecked")
	public Permission findByPermissionName(String permissionName, boolean isreadonly)
	{
		List<Permission> plist = session.createQuery("from Permission where name=?")
				.setString(0, permissionName)
				.setReadOnly(isreadonly).list();
		if (plist.size() > 0)
		{
			return plist.get(0);
		}
		return null;
	}
}
