package org.irdresearch.smstarseel.data.dao;

import java.util.List;

import org.irdresearch.smstarseel.data.Permission;

public interface DAOPermission extends DAO{
	
	Permission findById(int id);
	
	List<Permission> getAll(boolean isreadonly);
	
	Permission findByPermissionName(String permissionName,boolean isreadonly);
}
