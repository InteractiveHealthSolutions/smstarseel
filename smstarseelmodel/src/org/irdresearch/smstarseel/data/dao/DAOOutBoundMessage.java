package org.irdresearch.smstarseel.data.dao;

import java.util.Date;
import java.util.List;

import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;

public interface DAOOutBoundMessage extends DAO{

	OutboundMessage findById(long id);
	
	OutboundMessage findByReferenceNumber(String referenceNumber, boolean readonly);
	
	Number LAST_QUERY_TOTAL_ROW__COUNT();

	List<OutboundMessage> findByCriteria(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater, OutboundStatus smsStatus
			, String recipient,  String originator
			, String imei,  Integer projectId,
			boolean putNotWithSmsStatus, boolean orderByPriority)
			throws DataException;
	
	List<OutboundMessage> findByCriteria(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater, OutboundStatus smsStatus
			, String recipient,  String originator
			, String imei,  String projectName,
			boolean putNotWithSmsStatus, boolean orderByPriority, int firstResult, int fetchsize)
			throws DataException;

	List<OutboundMessage> getAll(int firstResult, int fetchsize);
}
