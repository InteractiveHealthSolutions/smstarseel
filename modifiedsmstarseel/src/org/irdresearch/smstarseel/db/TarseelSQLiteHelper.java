package org.irdresearch.smstarseel.db;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TarseelSQLiteHelper extends SQLiteOpenHelper {

	public enum Column_unsubmitted_outbound {
		_id,
		json
	}
	public enum SmsTarseelTables{
		unsubmitted_outbound
	}

	private static final String DATABASE_NAME = "smstarseel.db";
	private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase database;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ SmsTarseelTables.unsubmitted_outbound + "(" + Column_unsubmitted_outbound._id	+ " integer primary key autoincrement, " + Column_unsubmitted_outbound.json	+ " text not null);";

	public TarseelSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TarseelSQLiteHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + SmsTarseelTables.unsubmitted_outbound);
		onCreate(db);
	}

	public void open() throws SQLException {
		database = super.getWritableDatabase();
	}

	public void close() {
		super.close();
	}

	public long createUnsubmittedOutbound(String json) {
		ContentValues values = new ContentValues();
		values.put(Column_unsubmitted_outbound.json.name(), json);
		return database.insert(SmsTarseelTables.unsubmitted_outbound.name(), null, values);
	}

	public void deleteUnsubmittedOutbound(long id) {
		database.delete(SmsTarseelTables.unsubmitted_outbound.name(), Column_unsubmitted_outbound._id + " = " + id, null);
	}

	public Map<Long, String> getAllUnsubmittedOutbound() {
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
}
