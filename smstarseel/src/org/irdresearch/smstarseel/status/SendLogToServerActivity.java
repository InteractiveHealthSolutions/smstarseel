package org.irdresearch.smstarseel.status;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.irdresearch.smstarseel.SmsTarseel;
import org.irdresearch.smstarseel.comm.HttpSender;
import org.irdresearch.smstarseel.comm.SmsTarseelRequest;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.constant.TarseelGlobals.ErrorMessages;
import org.irdresearch.smstarseel.dialogue.ConfirmPopup;
import org.irdresearch.smstarseel.dialogue.ErrorAlert;
import org.irdresearch.smstarseel.form.SmsTarseelList;
import org.irdresearch.smstarseel.global.RequestParam;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.LogFileParams;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.util.FileUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SendLogToServerActivity extends SmsTarseelList{

	private List<String> fileList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		File root = new File(TarseelGlobals.getLogFileDir());
		listDir(root);
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		final Activity ac = this;
		
		final String item = (String) l.getAdapter().getItem(position);
		new ConfirmPopup(this, "Are you sure you want to send log file to server ? A huge data transfer will take place.",
				new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						SmsTarseelRequest req = null ;
						try
						{
							File logfile = new File(item);

							req = new SmsTarseelRequest(ac, App_Service.SEND_LOG);
							req.addParam(LogFileParams.FILE_NAME.KEY(), logfile.getPath());
							req.addParam(LogFileParams.LAST_MODIFIED_DATE.KEY(), Long.toString(logfile.lastModified()));
							req.addParam(LogFileParams.SIZE.KEY(), Long.toString(logfile.length()));
						
							JSONObject resp = HttpSender.sendFile(ac, req);
							if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.ERROR.CODE())){
								ErrorAlert.showError(ac, (String)resp.get(ResponseMessage.NAME));
							}
							else if(resp.get(RequestParam.ResponseCode.NAME).equals(RequestParam.ResponseCode.SUCCESS.CODE()))
							{
								ErrorAlert.showError(ac, (String)resp.get(LogFileParams.DETAILS.KEY()));
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							ErrorAlert.showError(ac, "Unexpected error. Contact program vendor. Details are :"+e.getMessage());
						}
						catch (ClientProtocolException e)
						{
							e.printStackTrace();
							ErrorAlert.showError(ac, ErrorMessages.CONNECTION_ERROR+". Details are :"+e.getMessage());
						}
						catch (IOException e)
						{
							e.printStackTrace();
							ErrorAlert.showError(ac, ErrorMessages.CONNECTION_ERROR+". Details are :"+e.getMessage());
						}
			        	catch (Exception e)
						{
							e.printStackTrace();
						    FileUtil.writeLog("SendLogToServerActivity", "Getting log file threw exception"+e.getMessage());
							ErrorAlert.showError(ac, ErrorMessages.CONNECTION_ERROR+". Details are :"+e.getMessage());
						}
					}
				});
	}
	@Override
    public void onBackPressed()
    {
		SmsTarseel.startHomeScreen(this);
    }
	@Override
	protected SmsTarseelRequest createRequest()
	{
		return null;
	}

	@Override
	protected boolean validateFormData()
	{
		return false;
	}

	void listDir(File f){
		File[] files = f.listFiles();
		fileList.clear();
		for (File file : files) {
			fileList.add(file.getPath());
		}

		ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, fileList);
		setListAdapter(directoryList);
	}
}
