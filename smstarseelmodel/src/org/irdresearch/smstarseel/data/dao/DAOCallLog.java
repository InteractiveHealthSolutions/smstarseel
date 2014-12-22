package org.irdresearch.smstarseel.data.dao;

import java.util.Date;
import java.util.List;

import org.irdresearch.smstarseel.data.CallLog;
import org.irdresearch.smstarseel.data.CallLog.CallStatus;
import org.irdresearch.smstarseel.data.CallLog.CallType;
import org.irdresearch.smstarseel.data.DataException;

public interface DAOCallLog extends DAO{

	CallLog findById(long id);

	CallLog findByReferenceNumber(String referenceNumber, boolean readonly);

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	List<CallLog> findByCriteria(Date callDatesmaller, Date callDategreater
			, CallType callType , String recipient,  String caller,
			boolean putNotWithCallType, CallStatus	callStatus, String imei,  Integer projectId)
			throws DataException;
	
	List<CallLog> findByCriteria(Date callDatesmaller, Date callDategreater
			,CallType callType , String recipient,  String caller,
			boolean putNotWithCallType, CallStatus	callStatus, String imei,  String projectName, int firstResult, int fetchsize)
			throws DataException;

	List<CallLog> getAll(int firstResult, int fetchsize, boolean readonly);
	
	int markCallAsRead(String referenceNumber);
	
	int markCallAsRead(long calllogId);
}
