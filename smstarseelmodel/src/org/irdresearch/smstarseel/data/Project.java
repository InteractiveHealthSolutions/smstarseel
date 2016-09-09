package org.irdresearch.smstarseel.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "project")
public class Project {

	@Id
	@GeneratedValue
	@Column(name = "projectId")
	private Integer		projectId;

	@Column(name = "name", unique = true)
	private String	name;

	@Column(name = "description")
	private String	description;

	public Project() { }
	
	public Project(int projectId) {
		this.projectId = projectId;
	}
	
	public void setProjectId(Integer projectId)
	{
		this.projectId = projectId;
	}

	public Integer getProjectId()
	{
		return projectId;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}

}
