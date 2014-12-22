/*
 *  Licensed to the OpenXdata Foundation (OXDF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The OXDF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at <p>
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  </p>
 *
 *  Unless required by applicable law or agreed to in writing, 
 *  software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 *  Copyright 2010 http://www.openxdata.org.
 */
package org.irdresearch.smstarseel.data;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission implements Serializable{

	private static final long serialVersionUID = -673786832403456831L;

	@Id
	@GeneratedValue
	@Column(name = "permissionId")
	private int			permissionId;

	@Column(name = "name", unique = true)
	private String		name;

	@Column(name = "description")
	private String		description;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "permissions", targetEntity = Role.class)
	private Set<Role>	roles;

	public Permission(){
		
	}

	public Permission(String name){
		this.name = name;
	}
	
	public int getPermissionId(){
		return permissionId;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}

	public void setPermissionId(int permissionId){
		this.permissionId = permissionId;
	}
	
	public void setName(String name){
		this.name = name.toUpperCase();
	}
	
	public void setDescription(String description){
		this.description = description;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	public Set<Role> getRoles()
	{
		return roles;
	}

}
