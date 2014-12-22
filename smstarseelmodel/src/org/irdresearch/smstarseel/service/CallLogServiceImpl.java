package org.irdresearch.smstarseel.service;

import java.util.Date;
import java.util.List;

import org.irdresearch.smstarseel.data.CallLog;
import org.irdresearch.smstarseel.data.CallLog.CallStatus;
import org.irdresearch.smstarseel.data.CallLog.CallType;
import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.dao.DAOCallLog;
import org.irdresearch.smstarseel.service.utils.DateUtils;

public class CallLogServiceImpl implements CallLogService{

	DAOCallLog			daocall;
/*	private Number	LAST_QUERY_TOTAL_ROW__COUNT;
*/	
	public CallLogServiceImpl(DAOCallLog daocall)
	{
		this.daocall = daocall;
	}
/*	private void setLASTS_ROWS_RETURNED_COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}*/
	
	public Number LAST_QUERY_TOTAL_ROW__COUNT(Class clazz) {
		if(clazz == CallLog.class){
			return daocall.LAST_QUERY_TOTAL_ROW__COUNT();
		}
		
		return null;
	}
	
	@Override
	public void saveCallLog(CallLog call)
	{
		daocall.save(call);
	}

	@Override
	public void updateCallLog(CallLog call)
	{
		daocall.update(call);
	}

	@Override
	public List<CallLog> getAllCalls(int firstResult, int fetchsize, boolean readonly)
	{
		return daocall.getAll(firstResult, fetchsize, readonly);
	}

	@Override
	public List<CallLog> findCall(Date callDatesmaller, Date callDategreater, CallType callType,
			String recipient, String caller, boolean putNotWithCallType, CallStatus	callStatus, String imei,
			Integer projectId) throws DataException
	{
		if(callDatesmaller != null) callDatesmaller = DateUtils.truncateDatetoDate(callDatesmaller);
		if(callDategreater != null) callDategreater = DateUtils.roundoffDatetoDate(callDategreater);
		return daocall.findByCriteria(
				callDatesmaller,
				callDategreater
				, callType, recipient, caller, putNotWithCallType, callStatus, imei, projectId);
	}

	@Override
	public List<CallLog> findCall(Date callDatesmaller, Date callDategreater, CallType callType,
			String recipient, String caller, boolean putNotWithCallType, CallStatus	callStatus, String imei,
			String projectName, int firstResult, int fetchsize) throws DataException
	{
		if(callDatesmaller != null) callDatesmaller = DateUtils.truncateDatetoDate(callDatesmaller);
		if(callDategreater != null) callDategreater = DateUtils.roundoffDatetoDate(callDategreater);
		return daocall.findByCriteria(
				callDatesmaller,
				callDategreater
				, callType, recipient, caller, putNotWithCallType, callStatus, imei, projectName,firstResult,fetchsize);
	}
	@Override
	public CallLog findByReferenceNumber(String referenceNumber, boolean readonly)
	{
		return daocall.findByReferenceNumber(referenceNumber, readonly);
	}
	@Override
	public int markCallAsRead(String referenceNumber)
	{
		return daocall.markCallAsRead(referenceNumber);
	}
	@Override
	public int markCallAsRead(long calllogId)
	{
		return daocall.markCallAsRead(calllogId);
	}

}
