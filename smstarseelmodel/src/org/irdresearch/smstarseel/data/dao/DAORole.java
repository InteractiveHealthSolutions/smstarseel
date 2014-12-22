package org.irdresearch.smstarseel.data.dao;

import java.util.List;

import org.irdresearch.smstarseel.data.Role;

public interface DAORole extends DAO{

	Role findById(int id);

	Role findByName(String roleName);

	List<Role> getAll();

	List<Role> findByCriteria(String roleName);

}
