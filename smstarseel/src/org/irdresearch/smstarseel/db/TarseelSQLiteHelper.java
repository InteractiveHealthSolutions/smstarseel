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
	
	private static TarseelSQLiteHelper instance;
	
	public static TarseelSQLiteHelper getInstance(Context context) {
		if(instance == null) {
			instance = new TarseelSQLiteHelper(context);
		}
		
		return instance;
	}

	public enum Column_unsubmitted_outbound {
		_id,
		json
	}
	
	public enum Column_inbound_messages {
		_id,
		json
	}
	
	public enum SmsTarseelTables{
		unsubmitted_outbound,
		unuploaded_inbounds
	}

	private static final String DATABASE_NAME = "smstarseel.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String TABLE_OUTBOUND_UNSUBMITTED = "create table "
			+ SmsTarseelTables.unsubmitted_outbound + "(" + Column_unsubmitted_outbound._id	+ " integer primary key autoincrement, " + Column_unsubmitted_outbound.json	+ " text not null);";
	
	private static final String TABLE_RECEIVED_MESSAGES = "create table "
			+ SmsTarseelTables.unuploaded_inbounds + "(" + Column_inbound_messages._id	+ " text primary key, " + Column_inbound_messages.json	+ " text not null);";

	private TarseelSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_OUTBOUND_UNSUBMITTED);
		database.execSQL(TABLE_RECEIVED_MESSAGES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TarseelSQLiteHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + SmsTarseelTables.unsubmitted_outbound);
		db.execSQL("DROP TABLE IF EXISTS " + SmsTarseelTables.unuploaded_inbounds);
		onCreate(db);
	}

}
