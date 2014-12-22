package org.irdresearch.smstarseel.service;

import java.util.Date;
import java.util.List;

import org.irdresearch.smstarseel.data.CallLog;
import org.irdresearch.smstarseel.data.CallLog.CallStatus;
import org.irdresearch.smstarseel.data.CallLog.CallType;
import org.irdresearch.smstarseel.data.DataException;

public interface CallLogService {

	Number LAST_QUERY_TOTAL_ROW__COUNT(Class clazz);
	
	void saveCallLog(CallLog call);

	void updateCallLog(CallLog call);

	int markCallAsRead(String referenceNumber);
	
	int markCallAsRead(long calllogId);
	
	List<CallLog> getAllCalls(int firstResult, int fetchsize, boolean readonly);
	
	CallLog findByReferenceNumber(String referenceNumber, boolean readonly);

	List<CallLog> findCall(Date callDatesmaller, Date callDategreater
			, CallType callType , String recipient,  String caller,
			boolean putNotWithCallType, CallStatus	callStatus, String imei,  Integer projectId)
			throws DataException;
	
	List<CallLog> findCall(Date callDatesmaller, Date callDategreater
			,CallType callType , String recipient,  String caller,
			boolean putNotWithCallType, CallStatus	callStatus, String imei,  String projectName, int firstResult, int fetchsize)
			throws DataException;
}
