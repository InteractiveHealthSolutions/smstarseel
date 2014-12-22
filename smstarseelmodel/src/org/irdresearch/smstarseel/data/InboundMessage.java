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
@Table(name = "inboundmessage")
public class InboundMessage {

	public enum InboundStatus
	{
		READ, UNREAD, UNKNOWN
	}

	public enum InboundType
	{
		SMS, MMS, PDU, UNKNOWN
	}

	@Id
	@GeneratedValue
	@Column(name = "inboundId")
	private long			inboundId;

	@Column(name = "text")
	private String			text;

	@Column(name = "recipient")
	private String			recipient;

	@Column(name = "originator")
	private String			originator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "recieveDate")
	private Date			recieveDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "systemRecieveDate")
	private Date			systemRecieveDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "systemProcessingStartDate")
	private Date			systemProcessingStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "systemProcessingEndDate")
	private Date			systemProcessingEndDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	@IndexColumn(name="IbStatusIndex")
	private InboundStatus	status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private InboundType		type;

	@Column(name = "imei")
	private String			imei;

	@Column(name = "referenceNumber", nullable = false, unique = true)
	private String	referenceNumber;
	
	private Integer projectId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "projectId", updatable =false, insertable=false)
	private Project			project;

	public long getInboundId()
	{
		return inboundId;
	}

	public void setInboundId(long inboundId)
	{
		this.inboundId = inboundId;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getRecipient()
	{
		return recipient;
	}

	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
	}

	public String getOriginator()
	{
		return originator;
	}

	public void setOriginator(String originator)
	{
		this.originator = originator;
	}

	public Date getRecieveDate()
	{
		return recieveDate;
	}

	public void setRecieveDate(Date recieveDate)
	{
		this.recieveDate = recieveDate;
	}

	public Date getSystemRecieveDate()
	{
		return systemRecieveDate;
	}

	public void setSystemRecieveDate(Date systemRecieveDate)
	{
		this.systemRecieveDate = systemRecieveDate;
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

	public InboundStatus getStatus()
	{
		return status;
	}

	public void setStatus(InboundStatus status)
	{
		this.status = status;
	}

	public InboundType getType()
	{
		return type;
	}

	public void setType(InboundType type)
	{
		this.type = type;
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
}
