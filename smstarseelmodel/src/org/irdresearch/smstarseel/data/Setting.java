package org.irdresearch.smstarseel.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "setting")
public class Setting implements java.io.Serializable {

	private static final long serialVersionUID = 3049965164746037921L;

	@Id
	@GeneratedValue
	@Column(name = "settingId")
	private int		settingId;

	@Column(name = "name", unique = true)
	private String	name;

	@Column(name = "displayName", unique = true)
	private String	displayName;

	@Column(name = "value")
	private String	value;
	
	@Column(name = "validatorRegx")
	private String	validatorRegex;
	
	@Column(name = "isEditable")
	private Boolean isEditable;
	
	@Column(name = "isViewable")
	private Boolean isViewable;

	@Column(name = "description")
	private String	description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastUpdated")
	private Date	lastUpdated;

	@Column(name = "lastEditedByUserId")
	private String	lastEditedByUserId;

	@Column(name = "lastEditedByUserName")
	private String	lastEditedByUserName;

	public Setting()
	{
	}

	public Setting(int settingId, String name, String value) {
		this.settingId = settingId;
		this.name = name;
		this.value = value;
	}

	public Setting(int settingId, String name, String value,
			String description, Date lastUpdated, String lastEditedByUserId,
			String lastEditedByUserName) {
		this.settingId = settingId;
		this.name = name;
		this.value = value;
		this.description = description;
		this.lastUpdated = lastUpdated;
		this.lastEditedByUserId = lastEditedByUserId;
		this.lastEditedByUserName = lastEditedByUserName;
	}

	public int getSettingId() {
		return this.settingId;
	}

	public void setSettingId(int settingId) {
		this.settingId = settingId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName () {
		return displayName;
	}

	public void setDisplayName (String displayName) {
		this.displayName = displayName;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValidatorRegex() {
		return validatorRegex;
	}

	public void setValidatorRegex(String validatorRegex) {
		this.validatorRegex = validatorRegex;
	}

	public Boolean getIsEditable () {
		return isEditable;
	}

	public void setIsEditable (Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public Boolean getIsViewable () {
		return isViewable;
	}

	public void setIsViewable (Boolean isViewable) {
		this.isViewable = isViewable;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastEditedByUserId() {
		return this.lastEditedByUserId;
	}

	public void setLastEditedByUserId(String lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	public String getLastEditedByUserName() {
		return this.lastEditedByUserName;
	}

	public void setLastEditedByUserName(String lastEditedByUserName) {
		this.lastEditedByUserName = lastEditedByUserName;
	}
	public void setLastEditor(User editor){
		lastEditedByUserId=editor.getName();
		lastEditedByUserName=editor.getFullName();
		lastUpdated=new Date();
	}
}
