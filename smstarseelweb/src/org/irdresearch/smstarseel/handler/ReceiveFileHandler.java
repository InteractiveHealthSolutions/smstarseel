package org.irdresearch.smstarseel.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.global.RequestParam.LoginResponse;
import org.irdresearch.smstarseel.global.RequestParam.ResponseCode;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.global.SmsTarseelResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiveFileHandler {

	public ReceiveFileHandler() {

	}
	
	public static void receiveLogFileData(JSONObject jReq, HttpServletResponse resp, FileItem file) throws IOException, JSONException {
		try
		{
			SmsTarseelUtil.PHONELOGLOGGER.error("--------------------******************************--------------------");
			SmsTarseelUtil.PHONELOGLOGGER.error(jReq.toString());
			byte[] b = new byte[(int) file.getSize()];
			file.getInputStream().read(b);
			SmsTarseelUtil.PHONELOGLOGGER.error(new String(b));
			SmsTarseelUtil.PHONELOGLOGGER.error("--------------------***************END OF LOG FILE***************--------------------");
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR, e2.getMessage()).jsonToString());
			return;
		}
		
		SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
		json.addElement(LoginResponse.DETAILS.KEY(), "Log saved successfully on server");
		
		SmsTarseelUtil.sendResponse(resp, json.jsonToString());
	}
}
