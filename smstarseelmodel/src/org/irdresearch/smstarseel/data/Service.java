package org.irdresearch.smstarseel.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "service")
public class Service {

	public enum ServiceStatus {
		ACTIVE , INACTIVE , DISCARDED , WAITING
	}

	@Id
	@GeneratedValue
	@Column(name = "serviceId")
	private int				serviceId;

	@Column(name = "serviceName")
	private String			serviceName;
	
	@Column(name = "serviceIdentifier")
	private String			serviceIdentifier;
	
	@Column(name = "outboundSuccessReportUrl")
	private String			outboundSuccessReportUrl;

	@Column(name = "outboundFailureReportUrl")
	private String			outboundFailureReportUrl;
	
	@Column(name = "deliveryReportUrl")
	private String			deliveryReportUrl;
	
	@Column(name = "inboundReportUrl")
	private String			inboundReportUrl;

	@Column(name = "authenticationKey")
	private String			authenticationKey;

	@Column(name = "description")
	private String			description;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "projectId")
	@Basic(fetch = FetchType.EAGER)
	private Project			project;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ServiceStatus	status;

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

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="attributeKey")
    @Column(name="attributeValue")
    @CollectionTable(name="service_extras", joinColumns=@JoinColumn(name="service_extras_id"))
    Map<String, String> extras = new HashMap<String, String>(); // maps from attribute name to value

	public Service() {
	}
	
	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceIdentifier() {
		return serviceIdentifier;
	}

	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}

	public String getOutboundSuccessReportUrl() {
		return outboundSuccessReportUrl;
	}

	public void setOutboundSuccessReportUrl(String outboundSuccessReportUrl) {
		this.outboundSuccessReportUrl = outboundSuccessReportUrl;
	}

	public String getOutboundFailureReportUrl() {
		return outboundFailureReportUrl;
	}

	public void setOutboundFailureReportUrl(String outboundFailureReportUrl) {
		this.outboundFailureReportUrl = outboundFailureReportUrl;
	}

	public String getDeliveryReportUrl() {
		return deliveryReportUrl;
	}

	public void setDeliveryReportUrl(String deliveryReportUrl) {
		this.deliveryReportUrl = deliveryReportUrl;
	}

	public String getInboundReportUrl() {
		return inboundReportUrl;
	}

	public void setInboundReportUrl(String inboundReportUrl) {
		this.inboundReportUrl = inboundReportUrl;
	}

	public String getAuthenticationKey() {
		return authenticationKey;
	}

	public void setAuthenticationKey(String authenticationKey) {
		this.authenticationKey = authenticationKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ServiceStatus getStatus() {
		return status;
	}

	public void setStatus(ServiceStatus status) {
		this.status = status;
	}

	public String getAddedByUserId() {
		return addedByUserId;
	}

	public void setAddedByUserId(String addedByUserId) {
		this.addedByUserId = addedByUserId;
	}

	public String getAddedByUsername() {
		return addedByUsername;
	}

	public void setAddedByUsername(String addedByUsername) {
		this.addedByUsername = addedByUsername;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getEditedByUserId() {
		return editedByUserId;
	}

	public void setEditedByUserId(String editedByUserId) {
		this.editedByUserId = editedByUserId;
	}

	public String getEditedByUsername() {
		return editedByUsername;
	}

	public void setEditedByUsername(String editedByUsername) {
		this.editedByUsername = editedByUsername;
	}

	public Date getDateEdited() {
		return dateEdited;
	}

	public void setDateEdited(Date dateEdited) {
		this.dateEdited = dateEdited;
	}

	public Map<String, String> getExtras() {
		return extras;
	}

	public void setExtras(Map<String, String> extras) {
		this.extras = extras;
	}

	
}
