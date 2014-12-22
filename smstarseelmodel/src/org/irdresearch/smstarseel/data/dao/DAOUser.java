package org.irdresearch.smstarseel.data.dao;

import java.util.List;

import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.data.User.UserStatus;

public interface DAOUser extends DAO{

	User findById(int id);

	User findByUsername(String username);

	Number countCriteriaRows(String email, String partOfFirstOrLastName,
			UserStatus userStatus, boolean putNotWithUserStatus)
			throws DataException;

	Number countAllRows();

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	List<User> findByCriteria(String email, String partOfFirstOrLastName,
			UserStatus userStatus, boolean putNotWithUserStatus, int firstResult,
			int fetchsize) throws DataException;

	List<User> getAll(int firstResult, int fetchsize);

}
