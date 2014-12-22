package org.irdresearch.smstarseel.service;

import java.io.Serializable;
import java.util.List;

import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.Permission;
import org.irdresearch.smstarseel.data.Role;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.data.User.UserStatus;

public interface UserService {

	public List<User> getAllUsers(int firstResult, int fetchSize) ;

	public User getUser(String userId);

	public List<Permission> getAllPermissions(boolean isreadonly);
	
	public List<Role> getAllRoles();
	
	public Role getRole(String rolename);
	
	public List<Role> getRolesByName(String name);
	
	public Serializable createUser(User user) throws UserServiceException, Exception;
	
	public Serializable addRole(Role role) throws UserServiceException;
	
	public void deleteUser(User user) ;
	
	public void deleteRole(Role role) ;
	
	public User updateUser(User user) throws Exception ;
	
	public void updateRole(Role role) ;
	
	public void changePassword(String username,String oldpwd, String pw1, String pw2) throws UserServiceException, Exception;

	List<User> findUserByCriteria(String email, String partOfFirstOrLastName,
			UserStatus userStatus, boolean putNotWithUserStatus,int firstResult, int fetchSize)
			throws DataException;

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	Permission getPermission(String permission,boolean isreadonly);
}
