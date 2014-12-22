package org.irdresearch.smstarseel.service;

import java.util.Date;
import java.util.List;

import org.irdresearch.smstarseel.context.TarseelGlobals;
import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.InboundMessage;
import org.irdresearch.smstarseel.data.InboundMessage.InboundStatus;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundType;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;
import org.irdresearch.smstarseel.data.dao.DAOInboundMessage;
import org.irdresearch.smstarseel.data.dao.DAOOutBoundMessage;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;

public class SMSServiceImpl implements SMSService{

	private DAOInboundMessage	daoinbound;
	private DAOOutBoundMessage	daooutbound;
	//private Number				LAST_QUERY_TOTAL_ROW__COUNT;
	
	public SMSServiceImpl(DAOInboundMessage daoinbound, DAOOutBoundMessage daooutbound) {
		this.daoinbound = daoinbound;
		this.daooutbound = daooutbound;
	}

	/*private void setLASTS_ROWS_RETURNED_COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}*/
	
	public Number LAST_QUERY_TOTAL_ROW__COUNT(Class clazz) {
		if(clazz == InboundMessage.class){
			return daoinbound.LAST_QUERY_TOTAL_ROW__COUNT();
		}
		
		if(clazz == OutboundMessage.class){
			return daooutbound.LAST_QUERY_TOTAL_ROW__COUNT();
		}
		
		return null;
	}
	
	@Override
	public InboundMessage findInboundById(long id)
	{
		return daoinbound.findById(id);
	}
	
	public List<InboundMessage> findInbound(Date recieveDatesmaller, Date recieveDategreater,
			InboundStatus smsStatus, String recipient, String originator, String imei, Integer projectId,
			boolean putNotWithSmsStatus) throws DataException
	{
		if(recieveDatesmaller != null) recieveDatesmaller = DateUtils.truncateDatetoDate(recieveDatesmaller);
		if(recieveDategreater != null) recieveDategreater = DateUtils.roundoffDatetoDate(recieveDategreater);
		return daoinbound.findByCriteria(recieveDatesmaller
				, recieveDategreater, smsStatus, recipient, originator, imei, projectId, putNotWithSmsStatus);
	}

	public List<InboundMessage> findInbound(Date recieveDatesmaller, Date recieveDategreater,
			InboundStatus smsStatus, String recipient, String originator, String imei, String projectName,
			boolean putNotWithSmsStatus, int firstResult, int fetchsize) throws DataException
	{
		if(recieveDatesmaller != null) recieveDatesmaller = DateUtils.truncateDatetoDate(recieveDatesmaller);
		if(recieveDategreater != null) recieveDategreater = DateUtils.roundoffDatetoDate(recieveDategreater);
		return daoinbound.findByCriteria(recieveDatesmaller
				, recieveDategreater, smsStatus, recipient, originator, imei, projectName, putNotWithSmsStatus, firstResult, fetchsize);
	}

	public List<OutboundMessage> findOutbound(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater, OutboundStatus smsStatus, String recipient,
			String originator, String imei, Integer projectId, boolean putNotWithSmsStatus, boolean orderByPriority)
			throws DataException
	{
		if(duedatesmaller != null) duedatesmaller = DateUtils.truncateDatetoDate(duedatesmaller);
		if(duedategreater != null) duedategreater = DateUtils.roundoffDatetoDate(duedategreater);
		if(sentdatesmaller != null) sentdatesmaller = DateUtils.truncateDatetoDate(sentdatesmaller);
		if(sentdategreater != null) sentdategreater = DateUtils.roundoffDatetoDate(sentdategreater);
		return daooutbound.findByCriteria(duedatesmaller,
				duedategreater, 
				sentdatesmaller,
				sentdategreater,
				smsStatus, recipient, originator, imei, projectId, putNotWithSmsStatus, orderByPriority);
	}
	
	public List<OutboundMessage> findPendingOutboundTillNow(String projectName, boolean orderByPriority, int fetchsize)
			throws DataException
	{
		return daooutbound.findByCriteria(DateUtils.subtractInterval(new Date(), 2, TIME_INTERVAL.YEAR),
				new Date(), null, null, OutboundStatus.PENDING, null, null, null, projectName
				, false, orderByPriority,0,fetchsize);
	}
	@Override
	public OutboundMessage findOutboundById(long id)
	{
		return daooutbound.findById(id);
	}
	
	public List<OutboundMessage> findOutbound(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater, OutboundStatus smsStatus, String recipient,
			String originator, String imei, String projectName, boolean putNotWithSmsStatus
			,boolean orderByPriority,int firstResult, int fetchsize) throws DataException
	{
		if(duedatesmaller != null) duedatesmaller = DateUtils.truncateDatetoDate(duedatesmaller);
		if(duedategreater != null) duedategreater = DateUtils.roundoffDatetoDate(duedategreater);
		if(sentdatesmaller != null) sentdatesmaller = DateUtils.truncateDatetoDate(sentdatesmaller);
		if(sentdategreater != null) sentdategreater = DateUtils.roundoffDatetoDate(sentdategreater);
		return daooutbound.findByCriteria(duedatesmaller,
				duedategreater, 
				sentdatesmaller,
				sentdategreater,
				smsStatus, recipient, originator, imei, projectName, putNotWithSmsStatus,
				orderByPriority, firstResult, fetchsize);
	}
	
	public List<InboundMessage> getAllInboundRecord(int firstResult, int fetchsize)
	{
		return daoinbound.getAll(firstResult, fetchsize);
	}

	public List<OutboundMessage> getAllOutboundRecord(int firstResult, int fetchsize)
	{
		return daooutbound.getAll(firstResult, fetchsize);
	}

	public void saveInbound(InboundMessage Inbound)
	{
		daoinbound.save(Inbound);
	}


	@Override
	public int markInboundAsRead(String referenceNumber)
	{
		return daoinbound.markInboundAsRead(referenceNumber);
	}

	@Override
	public int markInboundAsRead(long inboundId)
	{
		return daoinbound.markInboundAsRead(inboundId);
	}
	
	/*public void saveOutbound(OutboundMessage Outbound)
	{
		daooutbound.save(Outbound);
	}*/

	public String createNewOutboundSms(String recipient, String text, Date duedate, Priority priority, int validityPeriod, PeriodType periodType, Integer projectId, String additionalDescription) {
		OutboundMessage o = new OutboundMessage();
		o.setCreatedDate(new Date());
		o.setDescription(additionalDescription);
		o.setDueDate(duedate);
		o.setValidityPeriod(validityPeriod);
		o.setPeriodType(periodType);
		o.setPriority(priority);
		o.setProjectId(projectId);
		o.setRecipient(recipient);
		o.setStatus(OutboundStatus.PENDING);
		o.setText(text);
		o.setType(OutboundType.SMS);
		
		o.setReferenceNumber(TarseelGlobals.getUniqueSmsId(projectId));
		daooutbound.save(o);
		
		return o.getReferenceNumber();
	}
	
	public void updateInbound(InboundMessage Inbound)
	{
		daoinbound.update(Inbound);
	}

	public void updateOutbound(OutboundMessage Outbound)
	{
		daooutbound.update(Outbound);
	}

	@Override
	public InboundMessage findInboundMessageByReferenceNumber(String referenceNumber, boolean readonly)
	{
		return daoinbound.findByReferenceNumber(referenceNumber, readonly);
	}

	@Override
	public OutboundMessage findOutboundMessageByReferenceNumber(String referenceNumber,	boolean readonly)
	{
		return daooutbound.findByReferenceNumber(referenceNumber, readonly);
	}
}
