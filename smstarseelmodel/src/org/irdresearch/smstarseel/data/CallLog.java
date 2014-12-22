package org.irdresearch.smstarseel.data;

import java.util.Date;

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

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name="calllog")
public class CallLog {
	public enum CallStatus
	{
		READ, UNREAD, UNKNOWN
	}
	
	public enum CallType{
		MISSED,
		INCOMING,
		OUTGOING
	}

	@Id
	@GeneratedValue
	@Column(name = "callLogId")
	private long		callLogId;
	
	@Column(name = "callerNumber")
	private String		callerNumber;
	
	@Column(name = "recipient")
	private String		recipient;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "callDate")
	private Date		callDate;
	
	@Column(name = "durationInSec")
	private Integer durationInSec;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "callType")
	private CallType	callType;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "callStatus")
	@IndexColumn(name="CllgStatusIndex")
	private CallStatus	callStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "systemProcessingStartDate")
	private Date			systemProcessingStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "systemProcessingEndDate")
	private Date			systemProcessingEndDate;
	
	@Column(name = "imei")
	private String			imei;

	@Column(name = "referenceNumber", nullable = false, unique = true)
	private String	referenceNumber;
	
	private Integer projectId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "projectId", updatable =false, insertable=false)
	private Project			project;

	public String getRecipient()
	{
		return recipient;
	}

	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
	}

	public Date getCallDate()
	{
		return callDate;
	}

	public void setCallDate(Date callDate)
	{
		this.callDate = callDate;
	}

	public CallType getCallType()
	{
		return callType;
	}

	public void setCallType(CallType callType)
	{
		this.callType = callType;
	}

	public CallStatus getCallStatus()
	{
		return callStatus;
	}

	public void setCallStatus(CallStatus callStatus)
	{
		this.callStatus = callStatus;
	}

	public Date getSystemProcessingStartDate()
	{
		return systemProcessingStartDate;
	}

	public void setSystemProcessingStartDate(Date systemProcessingStartDate)
	{
		this.systemProcessingStartDate = systemProcessingStartDate;
	}

	public Date getSystemProcessingEndDate()
	{
		return systemProcessingEndDate;
	}

	public void setSystemProcessingEndDate(Date systemProcessingEndDate)
	{
		this.systemProcessingEndDate = systemProcessingEndDate;
	}

	public String getImei()
	{
		return imei;
	}

	public void setImei(String imei)
	{
		this.imei = imei;
	}

	public Project getProject()
	{
		return project;
	}

	void setProject(Project project)
	{
		this.project = project;
	}

	public String getReferenceNumber()
	{
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber)
	{
		this.referenceNumber = referenceNumber;
	}

	public Integer getProjectId()
	{
		return projectId;
	}

	public void setProjectId(Integer projectId)
	{
		this.projectId = projectId;
	}

	public Integer getDurationInSec()
	{
		return durationInSec;
	}

	public void setDurationInSec(Integer durationInSec)
	{
		this.durationInSec = durationInSec;
	}

	public long getCallLogId()
	{
		return callLogId;
	}

	public void setCallLogId(long callLogId)
	{
		this.callLogId = callLogId;
	}

	public String getCallerNumber()
	{
		return callerNumber;
	}

	public void setCallerNumber(String callerNumber)
	{
		this.callerNumber = callerNumber;
	}
}
