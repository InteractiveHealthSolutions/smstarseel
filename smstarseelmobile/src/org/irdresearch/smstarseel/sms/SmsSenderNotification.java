package org.irdresearch.smstarseel.sms;

import org.irdresearch.smstarseel.global.RequestParam.OuboundSmsParams;
import org.irdresearch.smstarseel.util.FileUtil;
import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SmsSenderNotification extends BroadcastReceiver{

	private static final String LOG_TAG = "SmsSenderNotification";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			SmsSender.getSenderStatus().getCurrentSms().put(OuboundSmsParams.IS_SENT.KEY(), intent.getBooleanExtra(SmsDispenser.SENDER_IS_SENT_PARAM, false));
			SmsSender.getSenderStatus().getCurrentSms().put(OuboundSmsParams.ERR_MSG.KEY(), intent.getStringExtra(SmsDispenser.SENDER_ERROR_MESSAGE_PARAM));
			SmsSender.getSenderStatus().getCurrentSms().put(OuboundSmsParams.FAIL_CAUSE.KEY(), intent.getStringExtra(SmsDispenser.SENDER_FAILURE_CAUSE_PARAM));
			SmsSender.getSenderStatus().getCurrentSms().put(OuboundSmsParams.SENT_DATE.KEY(), intent.getStringExtra(SmsDispenser.SENDER_SENT_DATE_PARAM));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			FileUtil.writeLog(LOG_TAG, "exception:"+e.getMessage());
		}

		SmsSender.getSenderStatus().setHasSenderBcNotified(true);
	}
}
