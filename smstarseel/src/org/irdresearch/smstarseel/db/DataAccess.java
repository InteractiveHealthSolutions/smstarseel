package org.irdresearch.smstarseel.db;

import java.util.HashMap;
import java.util.Map;

import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.db.TarseelSQLiteHelper.Column_inbound_messages;
import org.irdresearch.smstarseel.db.TarseelSQLiteHelper.Column_unsubmitted_outbound;
import org.irdresearch.smstarseel.db.TarseelSQLiteHelper.SmsTarseelTables;
import org.irdresearch.smstarseel.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataAccess {
	private static final String LOG_TAG = "DataAccess";
	private DataAccess() {

	}

	private DataAccess(Context context) {
		this.context = context;
	}

	private Context context;
	private static DataAccess dataAccess;
	
	public static DataAccess getInstance(Context context) {
		
		if(dataAccess == null) {
			dataAccess = new DataAccess(context);
		}
		
		return dataAccess;
	}
	
	public long createUnsubmittedOutbound(String json) {
		SQLiteDatabase database = TarseelSQLiteHelper.getInstance(context).getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Column_unsubmitted_outbound.json.name(), json);
		return database.insert(SmsTarseelTables.unsubmitted_outbound.name(), null, values);
	}

	public void deleteUnsubmittedOutbound(long id) {
		SQLiteDatabase database = TarseelSQLiteHelper.getInstance(context).getWritableDatabase();
		database.delete(SmsTarseelTables.unsubmitted_outbound.name(), Column_unsubmitted_outbound._id + " = " + id, null);
	}

	public long createInboundNotUploaded(String messageId, JSONObject json) {
		SQLiteDatabase database = TarseelSQLiteHelper.getInstance(context).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Column_inbound_messages.json.name(), json.toString());
		values.put(Column_inbound_messages._id.name(), messageId);
		return database.insert(SmsTarseelTables.unuploaded_inbounds.name(), null, values);
	}
	
	public void deleteUploadedInbound(String id) {
		SQLiteDatabase database = TarseelSQLiteHelper.getInstance(context).getWritableDatabase();
		database.delete(SmsTarseelTables.unuploaded_inbounds.name(), Column_inbound_messages._id + " = '" + id+"'", null);
	}
	
	public Map<Long, String> getAllUnsubmittedOutbound() {
		SQLiteDatabase database = TarseelSQLiteHelper.getInstance(context).getWritableDatabase();
		Cursor cursor = database.query(SmsTarseelTables.unsubmitted_outbound.name(), null, null, null, null, null, null);
		try{
			Map<Long, String> map = new HashMap<Long, String>();
			while (cursor.moveToNext()) {
				map.put(cursor.getLong(cursor.getColumnIndex(Column_unsubmitted_outbound._id.name())), cursor.getString(cursor.getColumnIndex(Column_unsubmitted_outbound.json.name())));
			}
	
			return map;
		}
		finally{
			if(cursor != null) 
				if (!cursor.isClosed())
					cursor.close();
		}
	}
	
	public JSONArray getAllUnUploadedInbound() {
		JSONArray jsonArray = new JSONArray();
		SQLiteDatabase database = TarseelSQLiteHelper.getInstance(context).getWritableDatabase();
		Cursor cursor = database.query(SmsTarseelTables.unuploaded_inbounds.name(), null, null, null, null, null, null);
		try{
			while (cursor.moveToNext()) {
				try {
					JSONObject obj = new JSONObject( cursor.getString(cursor.getColumnIndex(Column_inbound_messages.json.name())));
					jsonArray.put(obj);
				} catch (JSONException e) {
					e.printStackTrace();
					TarseelGlobals.addTo_CONSOLE_BUFFER(LOG_TAG, e.getMessage());
					FileUtil.writeLog(LOG_TAG, "Exception:"+e.getMessage());
				    FileUtil.writeLog(e);
				}
			}
		} finally{
			if(cursor != null) 
				if (!cursor.isClosed())
					cursor.close();
		}
		
		return jsonArray;
	}
}
