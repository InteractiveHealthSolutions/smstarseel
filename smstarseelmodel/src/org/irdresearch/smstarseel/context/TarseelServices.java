package org.irdresearch.smstarseel.context;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.irdresearch.smstarseel.data.dao.DAOCallLog;
import org.irdresearch.smstarseel.data.dao.DAOCallLogImpl;
import org.irdresearch.smstarseel.data.dao.DAODevice;
import org.irdresearch.smstarseel.data.dao.DAODeviceImpl;
import org.irdresearch.smstarseel.data.dao.DAODirectQuery;
import org.irdresearch.smstarseel.data.dao.DAODirectQueryImpl;
import org.irdresearch.smstarseel.data.dao.DAOInboundMessage;
import org.irdresearch.smstarseel.data.dao.DAOInboundMessageImpl;
import org.irdresearch.smstarseel.data.dao.DAOOutBoundMessage;
import org.irdresearch.smstarseel.data.dao.DAOOutboundMessageImpl;
import org.irdresearch.smstarseel.data.dao.DAOPermissionImpl;
import org.irdresearch.smstarseel.data.dao.DAOProject;
import org.irdresearch.smstarseel.data.dao.DAOProjectImpl;
import org.irdresearch.smstarseel.data.dao.DAORoleImpl;
import org.irdresearch.smstarseel.data.dao.DAOSettingImpl;
import org.irdresearch.smstarseel.data.dao.DAOUserImpl;
import org.irdresearch.smstarseel.service.CallLogService;
import org.irdresearch.smstarseel.service.CallLogServiceImpl;
import org.irdresearch.smstarseel.service.CustomQueryService;
import org.irdresearch.smstarseel.service.CustomQueryServiceImpl;
import org.irdresearch.smstarseel.service.DeviceService;
import org.irdresearch.smstarseel.service.DeviceServiceImpl;
import org.irdresearch.smstarseel.service.SMSService;
import org.irdresearch.smstarseel.service.SMSServiceImpl;
import org.irdresearch.smstarseel.service.SettingService;
import org.irdresearch.smstarseel.service.SettingServiceImpl;
import org.irdresearch.smstarseel.service.UserService;
import org.irdresearch.smstarseel.service.UserServiceImpl;

public class TarseelServices {

	private Session session;
	private Transaction  transaction;	
	private UserService usrs;
	private DeviceService devs;
	private SettingService setts;
	private SMSService smss;
	private CallLogService calllogs;
	private CustomQueryService customQueryService;
	
	TarseelServices(SessionFactory sessionFactoryObj) 
	{
		session = sessionFactoryObj.openSession();
		transaction = session.beginTransaction();
		
		DAOUserImpl udao=new DAOUserImpl(session);
		DAORoleImpl rdao=new DAORoleImpl(session);
		DAOPermissionImpl pdao= new DAOPermissionImpl(session);
		this.usrs=new UserServiceImpl(udao, rdao,pdao);
		
		DAOSettingImpl irdao=new DAOSettingImpl(session);
		this.setts=new SettingServiceImpl(irdao);
		
		DAOInboundMessage inbmdao= new DAOInboundMessageImpl(session);
		DAOOutBoundMessage obmdao = new DAOOutboundMessageImpl(session);
		this.smss=new SMSServiceImpl(inbmdao , obmdao);
		
		DAODevice devdao = new DAODeviceImpl(session);
		DAOProject projdao = new DAOProjectImpl(session);
		this.devs = new DeviceServiceImpl(devdao, projdao);
		
		DAOCallLog calldao = new DAOCallLogImpl(session);
		this.calllogs = new CallLogServiceImpl(calldao);
		
		DAODirectQuery dqs = new DAODirectQueryImpl(session);
		this.customQueryService = new CustomQueryServiceImpl(dqs);
	}

	public void beginTransaction(){
		if(transaction == null){
			transaction = session.beginTransaction();
		}
	}
	
	public void closeSession(){
		try{session.close();
		}catch (Exception e) {}
	}
	
	public void commitTransaction(){
		transaction.commit();
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		closeSession();
	}

	public void rollbackTransaction() {
		if(transaction != null){
			transaction.rollback();
		}
	}
	
	public void  flushSession() {
		session.flush();
	}
	
	public UserService getUserService()
	{
		return usrs;
	}
	public DeviceService getDeviceService()
	{
		return devs;
	}
	public SettingService getSettingService()
	{
		return setts;
	}
	public SMSService getSmsService()
	{
		return smss;
	}
	public CallLogService getCallService()
	{
		return calllogs;
	}

	public CustomQueryService getCustomQueryService() {
		return customQueryService;
	}

}
