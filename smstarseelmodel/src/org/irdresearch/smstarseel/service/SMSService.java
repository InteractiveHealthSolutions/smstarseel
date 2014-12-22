package org.irdresearch.smstarseel.service;

import java.util.Date;
import java.util.List;

import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.InboundMessage;
import org.irdresearch.smstarseel.data.InboundMessage.InboundStatus;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;

public interface SMSService {

	Number LAST_QUERY_TOTAL_ROW__COUNT(Class clazz);
	
	List<InboundMessage> getAllInboundRecord(int firstResult, int fetchsize);

	InboundMessage findInboundById(long id);

	InboundMessage findInboundMessageByReferenceNumber(String referenceNumber, boolean readonly);

	List<InboundMessage> findInbound(Date recieveDatesmaller, Date recieveDategreater
			, InboundStatus smsStatus , String recipient,  String originator
			, String imei,  Integer projectId,
			boolean putNotWithSmsStatus)
			throws DataException;
	
	List<InboundMessage> findInbound(Date recieveDatesmaller, Date recieveDategreater
			,InboundStatus smsStatus , String recipient,  String originator
			, String imei,  String projectName,
			boolean putNotWithSmsStatus, int firstResult, int fetchsize)
			throws DataException;

	int markInboundAsRead(String referenceNumber);
	
	int markInboundAsRead(long inboundId);
	
	void updateInbound(InboundMessage Inbound);

	void saveInbound(InboundMessage Inbound);

	List<OutboundMessage> getAllOutboundRecord(int firstResult, int fetchsize);

	OutboundMessage findOutboundById(long id);

	OutboundMessage findOutboundMessageByReferenceNumber(String referenceNumber, boolean readonly);

	List<OutboundMessage> findOutbound(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater, OutboundStatus smsStatus
			, String recipient,  String originator
			, String imei, Integer projectId,
			boolean putNotWithSmsStatus, boolean orderByPriority)
			throws DataException;
	
	List<OutboundMessage> findOutbound(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater, OutboundStatus smsStatus
			, String recipient,  String originator
			, String imei,  String projectName,
			boolean putNotWithSmsStatus, boolean orderByPriority, int firstResult, int fetchsize)
			throws DataException;

	List<OutboundMessage> findPendingOutboundTillNow(String projectName, boolean orderByPriority, int fetchsize) throws DataException;
	
	void updateOutbound(OutboundMessage Outbound);

	String createNewOutboundSms(String recipient, String text, Date duedate, Priority priority, int validityPeriod, PeriodType periodType, Integer projectId, String additionalDescription);
	/*void saveOutbound(OutboundMessage Outbound);*/
}
