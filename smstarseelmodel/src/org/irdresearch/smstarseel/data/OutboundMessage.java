package org.irdresearch.smstarseel.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "outboundmessage")
public class OutboundMessage {

	public enum OutboundStatus
	{
		PENDING, SENT, FAILED, MISSED, 
		UNKNOWN, // smstarseel picked for sending and now status is waiting to be updated
		WAITING //waiting for external service to update status
	}

	public enum OutboundType
	{
		SMS, MMS, PDU, UNKNOWN
	}

	public enum PeriodType{
		HOUR, DAY, WEEK
	}
	
	public enum Priority{
		HIGHEST,
		HIGH,
		MEDIUM,
		LOW,
		LOWEST
	}

	@Id
	@GeneratedValue
	@Column(name = "outboundId")
	private long			outboundId;

	@Column(name = "text" , nullable = false)
	private String			text;

	@Column(name = "recipient" , nullable = false)
	private String			recipient;

	@Column(name = "originator")
	private String			originator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dueDate" , nullable = false)
	private Date			dueDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sentDate")
	private Date			sentDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "systemProcessingStartDate")
	private Date			systemProcessingStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "systemProcessingEndDate")
	private Date			systemProcessingEndDate;
	
	@Column(name = "tries")
	private Integer tries;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@IndexColumn(name="ObStatusIndex")
	private OutboundStatus	status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private OutboundType	type;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "priority", nullable = false)
	private Priority	priority;
	
	@Column(name = "referenceNumber", nullable = false, unique = true)
	private String	referenceNumber;
	
	@Column(name = "validityPeriod", nullable = false)
	private int				validityPeriod;

	@Enumerated(EnumType.STRING)
	@Column(name = "periodType", nullable = false)
	private PeriodType	periodType;

	@Column(name = "imei")
	private String			imei;

	@Column(name = "description")
	private String			description;

	@Column(name = "errorMessage", length = 1000)
	private String			errorMessage;

	@Column(name = "failureCause", length = 1000)
	private String			failureCause;

	private Integer projectId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "projectId", updatable =false, insertable=false)
	private Project			project;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate", nullable = false)
	private Date			createdDate;

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="attributeKey")
    @Column(name="attributeValue", length = 1000)
    @CollectionTable(name="outbound_extras", joinColumns=@JoinColumn(name="outbound_extras_id"))
    Map<String, String> extras = new HashMap<String, String>(); // maps from attribute name to value

	/**
	 * @param recipient
	 *            the recipient to set
	 */
	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
	}

	/**
	 * @return the recipient
	 */
	public String getRecipient()
	{
		return this.recipient;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return this.text;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(Date dueDate)
	{
		this.dueDate = dueDate;
	}

	/**
	 * @return the dueDate
	 */
	public Date getDueDate()
	{
		return this.dueDate;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @param sentdate
	 *            the sentdate to set
	 */
	public void setSentdate(Date sentdate)
	{
		this.sentDate = sentdate;
	}

	/**
	 * @return the sentdate
	 */
	public Date getSentdate()
	{
		return this.sentDate;
	}

	/**
	 * @param errormessage
	 *            the errormessage to set
	 */
	public void setErrormessage(String errormessage)
	{
		this.errorMessage = errormessage;
	}

	/**
	 * @return the errormessage
	 */
	public String getErrormessage()
	{
		return this.errorMessage;
	}

	/**
	 * @param failureCause
	 *            the failureCause to set
	 */
	public void setFailureCause(String failureCause)
	{
		this.failureCause = failureCause;
	}

	/**
	 * @return the failureCause
	 */
	public String getFailureCause()
	{
		return this.failureCause;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate()
	{
		return this.createdDate;
	}

	public void setOriginator(String originator)
	{
		this.originator = originator;
	}

	public String getOriginator()
	{
		return originator;
	}

	public void setImei(String imei)
	{
		this.imei = imei;
	}

	public String getImei()
	{
		return imei;
	}

	public void setSystemProcessingStartDate(Date systemProcessingStartDate)
	{
		this.systemProcessingStartDate = systemProcessingStartDate;
	}

	public Date getSystemProcessingStartDate()
	{
		return systemProcessingStartDate;
	}

	public void setSystemProcessingEndDate(Date systemProcessingEndDate)
	{
		this.systemProcessingEndDate = systemProcessingEndDate;
	}

	public Date getSystemProcessingEndDate()
	{
		return systemProcessingEndDate;
	}

	public Integer getTries() {
		return tries;
	}

	public void setTries(Integer tries) {
		this.tries = tries;
	}

	public void setOutboundId(long outboundId)
	{
		this.outboundId = outboundId;
	}

	public long getOutboundId()
	{
		return outboundId;
	}

	public void setStatus(OutboundStatus status)
	{
		this.status = status;
	}

	public OutboundStatus getStatus()
	{
		return status;
	}

	public void setType(OutboundType type)
	{
		this.type = type;
	}

	public OutboundType getType()
	{
		return type;
	}

	public void setValidityPeriod(int validityPeriod)
	{
		this.validityPeriod = validityPeriod;
	}

	public int getValidityPeriod()
	{
		return validityPeriod;
	}

	public void setPeriodType(PeriodType periodType)
	{
		this.periodType = periodType;
	}

	public PeriodType getPeriodType()
	{
		return periodType;
	}

	public Integer getProjectId()
	{
		return projectId;
	}

	public void setProjectId(Integer projectId)
	{
		this.projectId = projectId;
	}

	void setProject(Project project)
	{
		this.project = project;
	}

	public Project getProject()
	{
		return project;
	}

	public Priority getPriority()
	{
		return priority;
	}

	public void setPriority(Priority priority)
	{
		this.priority = priority;
	}

	public String getReferenceNumber()
	{
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber)
	{
		this.referenceNumber = referenceNumber;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Map<String, String> getExtras() {
		return extras;
	}

	public void setExtras(Map<String, String> extras) {
		this.extras = extras;
	}
}
