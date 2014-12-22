package org.irdresearch.smstarseel.global;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.irdresearch.smstarseel.global.RequestParam.ResponseCode;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmsTarseelResponse {
	JSONObject reqOrResp = new JSONObject();

	public SmsTarseelResponse(ResponseCode responseCode, ResponseMessage responseMessage, String customMessage) throws JSONException {

		reqOrResp.put(ResponseCode.NAME, responseCode.CODE());
		reqOrResp.put(ResponseMessage.NAME, responseMessage.MESSAGE() + ((customMessage == null || customMessage == "" )? "" : "\ndetails are :" + customMessage));
	}
	public void addElement(String name , String value) throws JSONException{
		reqOrResp.put(name, value);
	}
	public void addObjectList(String arrayName, JSONArray jsonArray) throws JSONException{
		reqOrResp.put(arrayName, jsonArray);
	}
	public String jsonToString() throws JSONException{
        return reqOrResp.toString();
    }
	
	@Override
	public String toString() {
        return reqOrResp.toString();
	}
	
	public static JSONObject convertToJson(String jsonString) throws JSONException{
		return new JSONObject(jsonString);
	}
	
	public static JSONObject convertToJson(InputStream jsonStream) throws IOException, JSONException {
		
		BufferedReader r = new BufferedReader(new InputStreamReader(jsonStream));
		StringBuilder stringJson = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			stringJson.append(line);
		}
		return new JSONObject(stringJson.toString());
	}
}
