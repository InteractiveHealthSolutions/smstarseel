package org.irdresearch.smstarseel;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.Device;
import org.irdresearch.smstarseel.data.Device.DeviceStatus;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;
import org.irdresearch.smstarseel.web.util.WebGlobals.TarseelSetting;

import com.mysql.jdbc.StringUtils;

public class ServiceCrashAlert extends TimerTask{

	@Override
	public void run () {
		TarseelServices tsc = TarseelContext.getServices();
		try{
			List<Device> prjRegDevl = tsc.getDeviceService().findDevice(null, DeviceStatus.ACTIVE, false, null, null, 0, 10);
			
			String message = "";
			for (Device device : prjRegDevl) {
				if(device.getDateLastCalllogPing() != null && DateUtils.differenceBetweenIntervals(new Date(), device.getDateLastCalllogPing(), TIME_INTERVAL.HOUR) > 2){
					message += "\nProject: " + device.getProject().getName();
					message += "\nService: CALL_LOG";
					message += "\nImei: "+device.getImei();
					message += "\nSim: "+device.getSim();
					message += "\nDate Added: "+device.getDateAdded();
					message += "\nLast Ping: " + device.getDateLastCalllogPing();
				}
				
				if(device.getDateLastInboundPing() != null && DateUtils.differenceBetweenIntervals(new Date(), device.getDateLastInboundPing(), TIME_INTERVAL.HOUR) > 2){
					message += "\nProject: " + device.getProject().getName();
					message += "\nService: INBOUND";
					message += "\nImei: "+device.getImei();
					message += "\nSim: "+device.getSim();
					message += "\nDate Added: "+device.getDateAdded();
					message += "\nLast Ping: " + device.getDateLastInboundPing();
				}
				
				if(device.getDateLastOutboundPing() != null && DateUtils.differenceBetweenIntervals(new Date(), device.getDateLastOutboundPing(), TIME_INTERVAL.HOUR) > 2){
					message += "\nProject: " + device.getProject().getName();
					message += "\nService: OUTBOUND";
					message += "\nImei: "+device.getImei();
					message += "\nSim: "+device.getSim();
					message += "\nDate Added: "+device.getDateAdded();
					message += "\nLast Ping: " + device.getDateLastOutboundPing();
				}
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(message)){
				String[] recpl = TarseelContext.getSetting(TarseelSetting.ADMIN_EMAIL_ADDRESS.NAME(), null).replaceAll(" ", "").split(",");
				EmailEngine.getEmailer().postSimpleMail(recpl, "Service Crashed", "Services crashed: \n"+message , "smstarseel@ird.org");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			tsc.closeSession();
		}
	}

}
