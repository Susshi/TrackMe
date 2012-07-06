package de.androidlab.trackme.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class LocationDatabaseOpenHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "trackMeLocationDatabase";
	
	public LocationDatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	
	// TODO: CHECK CREATE STATEMENT (ESPECIALLY THE PRIMARY KEY)
	// Evoked when database to be opened does not exist
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE IF NOT EXISTS locations(" +
			"hash TEXT NOT NULL, latitude REAL NOT NULL, longitude REAL NOT NULL, " +
			"expirationDate INTEGER NOT NULL, timestamp INTEGER PRIMARY KEY NOT NULL)");
		}
		catch(SQLException e) {
			Log.e("DATABASE", e.getLocalizedMessage());
		}
	}

	// Evoked when changes in database scheme is necessary
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: (Maybe) implement
	}

}
