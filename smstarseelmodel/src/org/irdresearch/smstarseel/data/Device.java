package org.irdresearch.smstarseel.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "device")
public class Device {

	public enum DeviceStatus {
		ACTIVE , INACTIVE , CRASHED , DISCARDED , WAITING
	}

	@Id
	@GeneratedValue
	@Column(name = "deviceId")
	private int				deviceId;

	@Column(name = "deviceName")
	private String			deviceName;

	@Column(name = "imei")
	private String			imei;

	@Column(name = "description")
	private String			description;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "projectId")
	@Basic(fetch = FetchType.EAGER)
	private Project			project;

	@Column(name = "sim")
	private String			sim;
	
	private String			commport;
	
	private String			commportCommand;

	@Column(name = "pin")
	private String			pin;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private DeviceStatus	status;

	@Column(name = "error")
	private String			error;

	@Column(name = "addedByUserId")
	private String			addedByUserId;

	@Column(name = "addedByUsername")
	private String			addedByUsername;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateAdded")
	private Date			dateAdded;

	@Column(name = "editedByUserId")
	private String			editedByUserId;

	@Column(name = "editedByUsername")
	private String			editedByUsername;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateEdited")
	private Date			dateEdited;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateLastOutboundPing")
	private Date			dateLastOutboundPing;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateLastInboundPing")
	private Date			dateLastInboundPing;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateLastCalllogPing")
	private Date			dateLastCalllogPing;

	public int getDeviceId()
	{
		return deviceId;
	}
	
	public void setDeviceId(int deviceId)
	{
		this.deviceId = deviceId;
	}
	
	public String getDeviceName()
	{
		return deviceName;
	}
	
	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}
	
	public String getImei()
	{
		return imei;
	}
	
	public void setImei(String imei)
	{
		this.imei = imei;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public void setProject(Project project)
	{
		this.project = project;
	}
	
	public String getPin()
	{
		return pin;
	}
	
	public void setPin(String pin)
	{
		this.pin = pin;
	}
	
	public String getError()
	{
		return error;
	}
	
	public void setError(String error)
	{
		this.error = error;
	}
	
	public String getAddedByUserId()
	{
		return addedByUserId;
	}
	
	public void setAddedByUserId(String addedByUserId)
	{
		this.addedByUserId = addedByUserId;
	}
	
	public String getAddedByUsername()
	{
		return addedByUsername;
	}
	
	public void setAddedByUsername(String addedByUsername)
	{
		this.addedByUsername = addedByUsername;
	}

	public void setDateAdded(Date dateAdded)
	{
		this.dateAdded = dateAdded;
	}

	public Date getDateAdded()
	{
		return dateAdded;
	}

	public void setEditedByUserId(String editedByUserId)
	{
		this.editedByUserId = editedByUserId;
	}

	public String getEditedByUserId()
	{
		return editedByUserId;
	}

	public void setEditedByUsername(String editedByUsername)
	{
		this.editedByUsername = editedByUsername;
	}

	public String getEditedByUsername()
	{
		return editedByUsername;
	}

	public void setDateEdited(Date dateEdited)
	{
		this.dateEdited = dateEdited;
	}

	public Date getDateEdited()
	{
		return dateEdited;
	}

	public Date getDateLastOutboundPing () {
		return dateLastOutboundPing;
	}

	public void setDateLastOutboundPing (Date dateLastOutboundPing) {
		this.dateLastOutboundPing = dateLastOutboundPing;
	}

	public Date getDateLastInboundPing () {
		return dateLastInboundPing;
	}

	public void setDateLastInboundPing (Date dateLastInboundPing) {
		this.dateLastInboundPing = dateLastInboundPing;
	}

	public Date getDateLastCalllogPing () {
		return dateLastCalllogPing;
	}

	public void setDateLastCalllogPing (Date dateLastCalllogPing) {
		this.dateLastCalllogPing = dateLastCalllogPing;
	}

	public void setSim(String sim)
	{
		this.sim = sim;
	}

	public String getSim()
	{
		return sim;
	}

	public String getCommport() {
		return commport;
	}

	public void setCommport(String commport) {
		this.commport = commport;
	}

	public String getCommportCommand() {
		return commportCommand;
	}

	public void setCommportCommand(String commportCommand) {
		this.commportCommand = commportCommand;
	}

	public void setStatus(DeviceStatus status)
	{
		this.status = status;
	}

	public DeviceStatus getStatus()
	{
		return status;
	}
}
