package org.irdresearch.smstarseel.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "role")
public class Role implements Serializable{

	private static final long serialVersionUID = 1731143919220579440L;

	@Id
	@GeneratedValue
	@Column(name = "roleId")
	private int				roleId	= 0;

	@Column(name = "name", unique = true)
	private String			name;

	@Column(name = "description")
	private String			description;

	@ManyToMany(targetEntity = Permission.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "roleId"), inverseJoinColumns = @JoinColumn(name = "permissionId"))
	@Fetch(FetchMode.JOIN)
	private Set<Permission>	permissions;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "roles", targetEntity = User.class)
	private List<User>		users;

	@Column(name = "createdByUserId")
	private String			createdByUserId;

	@Column(name = "createdByUserName")
	private String			createdByUserName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	private Date			createdDate;

	@Column(name = "lastEditedByUserId")
	private String			lastEditedByUserId;

	@Column(name = "lastEditedByUserName")
	private String			lastEditedByUserName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastUpdated")
	private Date			lastUpdated;

	public Role() {
		permissions = new HashSet<Permission>();
	}

	public Role(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		try {
			this.description = description.trim();
		} catch (Exception e) {
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public boolean isNew() {
		return roleId == 0;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public void addPermission(Permission permission) {
		if(permission!=null && !this.permissions.contains(permission)){
		this.permissions.add(permission);
		}
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public void addPermissions(List<Permission> permissions) {
		for (Permission x : permissions) {
			if ( x != null && !this.permissions.contains(x))
				this.permissions.add(x);
		}
	}

	public void removePermission(Permission permission) {
		if(permission!=null){
			for (Permission x : permissions) {
				if (x.getName().equals(permission.getName())) {
					permissions.remove(x);
					break;
				}
			}
		}
	}

	public boolean hasPermission(String permissionName) {
			
			if (permissions != null) {
				for (Permission p : permissions) {
					if (p.getName().equals(permissionName))
						return true;
				}
			}
			return false;
		}
	/**
	 * Checks if the given <code>Role</code> is the default administrator <code>Role</code> that ships with the system.
	 * @param role <code>Role</code> to check.
	 * @return <code>True only and only if role.getName().equals("Role_Administrator")
	 */
	public boolean isDefaultAdminRole() {
		return this.getName().equals("Role_Administrator");
	}
	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}
	public String getCreatedByUserId() {
		return createdByUserId;
	}
	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
	}
	public String getCreatedByUserName() {
		return createdByUserName;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreator(User creator){
		createdByUserId=creator.getName();
		createdByUserName=creator.getFullName();
		createdDate=new Date();
	}
	public void setLastEditedByUserId(String lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}
	public String getLastEditedByUserId() {
		return lastEditedByUserId;
	}
	public void setLastEditedByUserName(String lastEditedByUserName) {
		this.lastEditedByUserName = lastEditedByUserName;
	}
	public String getLastEditedByUserName() {
		return lastEditedByUserName;
	}
	public void setLastEditor(User editor){
		lastEditedByUserId=editor.getName();
		lastEditedByUserName=editor.getFullName();
		lastUpdated=new Date();
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
}
