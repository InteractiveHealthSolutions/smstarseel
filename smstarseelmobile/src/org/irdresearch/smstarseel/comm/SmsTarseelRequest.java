package org.irdresearch.smstarseel.comm;

import java.util.Date;

import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.global.DateUtils;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.RequestMendatoryParam;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class SmsTarseelRequest 
{
	private final JSONObject reqParams ;
	
	private SmsTarseelRequest(JSONObject jsonRequest){
		reqParams = jsonRequest;
	}
	public SmsTarseelRequest(Context context, App_Service requestedService) throws JSONException {
		reqParams = new JSONObject();
		
		addParam(App_Service.NAME, requestedService.VALUE());
		addParam(RequestMendatoryParam.IMEI.KEY(), TarseelGlobals.IMEI(context));
		addParam(RequestMendatoryParam.SIM.KEY(), TarseelGlobals.SIM(context));
		addParam(RequestMendatoryParam.DATE.KEY(), DateUtils.formatRequestDate(new Date()));
		addParam(RequestMendatoryParam.PROJECT_REGISTERED.KEY(), TarseelGlobals.PROJECTS(context));
	}
	
	public void addParam(String name , String value) throws JSONException{
		reqParams.put(name, value);
	}
	public void addObjectList(String arrayName, JSONArray jsonArray) throws JSONException{
		reqParams.put(arrayName, jsonArray);
	}
	public JSONObject getRequestParams(){
		return reqParams;
	}
	@Override
	public String toString() {
		return reqParams.toString();
	}
	public static SmsTarseelRequest fromString(String json) throws JSONException{
		return new SmsTarseelRequest(new JSONObject(json));
	}
}
