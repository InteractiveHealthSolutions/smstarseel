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
@Table(name = "servicelog")
public class ServiceLog {

	public enum ServiceLogStatus {
		PENDING , DISCARDED , RESOLVED , ERROR
	}

	@Id
	@GeneratedValue
	@Column(name = "serviceLogId")
	private int				serviceLogId;

	@Column(name = "serviceUrl")
	private String			serviceUrl;
	
	@Column(name = "serviceMethod")
	private String			serviceMethod;

	@Column(name = "nativeService")
	private Boolean			nativeService;
	
	@Column(name = "authKey")
	private String	authKey;//TODO is it right to save
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateDue")
	private Date			dateDue;
	
	@Column(name = "retries")
	private int			retries;
	
	@Column(name = "description")
	private String			description;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "projectId")
	@Basic(fetch = FetchType.EAGER)
	private Project			project;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ServiceLogStatus	status;
	
	@Column(name = "failureCause", length = 1000)
	private String	failureCause;

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
    @Column(name="attributeValue", length = 1000)
    @CollectionTable(name="servicelog_extras", joinColumns=@JoinColumn(name="servicelog_extras_id"))
    Map<String, String> extras = new HashMap<String, String>(); // maps from attribute name to value

	public ServiceLog() {
	}

	public int getServiceLogId() {
		return serviceLogId;
	}

	public void setServiceLogId(int serviceLogId) {
		this.serviceLogId = serviceLogId;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getServiceMethod() {
		return serviceMethod;
	}

	public void setServiceMethod(String serviceMethod) {
		this.serviceMethod = serviceMethod;
	}

	public Boolean getNativeService() {
		return nativeService;
	}

	public void setNativeService(Boolean nativeService) {
		this.nativeService = nativeService;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public Date getDateDue() {
		return dateDue;
	}

	public void setDateDue(Date dateDue) {
		this.dateDue = dateDue;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
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

	public ServiceLogStatus getStatus() {
		return status;
	}

	public void setStatus(ServiceLogStatus status) {
		this.status = status;
	}

	public String getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
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
		if(extras == null){
			extras = new HashMap<String, String>();
		}
		return extras;
	}

	public void setExtras(Map<String, String> extras) {
		this.extras = extras;
	}
	
	public void addExtra(String name, String val) {
		extras.put(name, val);
	}
}
