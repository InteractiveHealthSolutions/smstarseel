package org.irdresearch.smstarseel.sms;

import java.util.Map;

import org.irdresearch.smstarseel.TarseelService;
import org.irdresearch.smstarseel.comm.HttpSender;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.db.TarseelSQLiteHelper;
import org.irdresearch.smstarseel.global.RequestParam;
import org.irdresearch.smstarseel.util.FileUtil;
import org.json.JSONObject;

public class CleanupService extends TarseelService
{
	private static CleanupService _instance;
	public static CleanupService getInstance(){
		if(_instance == null){
			_instance = new CleanupService();
		}
		return _instance;
	}
	
	/** WARNING - USE getInstance() method*/
	public CleanupService() {
		super(LOG_TAG, SERVICE_UID, CleanupService.class, "CLEANUP_SRV_INITIAL_DELAY_SEC_PREF_NAME", CLEANUP_SRV_REPEAT_INTERVAL_SEC_PREF_NAME, 60, 1800);
	}

	private static final String LOG_TAG = "CleanupService";
	private static final String SERVICE_UID = "org.irdresearch.smstarseel.sms.CleanupService";

	private static final String CLEANUP_SRV_REPEAT_INTERVAL_SEC_PREF_NAME = "CLEANUP_SRV_REPEAT_INTERVAL_SEC";

	protected void runTask() throws Exception{
		TarseelSQLiteHelper sql = new TarseelSQLiteHelper(this);
		sql.open();
		Map<Long, String> oldres = sql.getAllUnsubmittedOutbound();
		TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "fetched "+oldres.size()+" unsubmitted outbounds");
		for (Long id : oldres.keySet()) {
			SmsTarseelRequest payload2 = SmsTarseelRequest.fromString(oldres.get(id));
			try{
				JSONObject resp2 = HttpSender.sendLargeText(this, payload2);
				if(resp2.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.SUCCESS.CODE()))
				{
					sql.deleteUnsubmittedOutbound(id);
				}
			}catch (Exception e) {
				TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Exception:"+e.getMessage());
				FileUtil.writeLog(LOG_TAG, "Exception:"+e.getMessage());
			    FileUtil.writeLog(e);
			}
		}
		TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, "Submitted old sent result..");
		sql.close();
	}
}
