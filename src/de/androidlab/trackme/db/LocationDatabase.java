package de.androidlab.trackme.db;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.util.Pair;

import com.google.android.maps.GeoPoint;

import de.androidlab.trackme.interfaces.DatabaseListener;

/**
 * Database for storing location information of users
 * The database stores locations as long as their expiration date
 * is not exceeded. You can insert new location data with 
 * {@linkPlain #insertLocation(String, double, double, long)}.
 * 
 * @author Mario Wozenilek
 *
 */
public class LocationDatabase {
	
	public LocationDatabase() {
	}
	
	/**
	 * Creates a database object and tries to open or create
	 * the necessary underlying database
	 * 
	 * @param context Most times this is the Base Context from the 'main' activity
	 */
	public void init(Context context)
	{
		openHelper = new LocationDatabaseOpenHelper(context);
	
	}
	
	/**
	 * Inserts a bunch of new locations to the database. See 
	 * {@linkPlain de.androidlab.trackme#insertLocation(String hash, double latitude, double longitude, long expirationInUnixSeconds, long timestamp) insertLocation}
	 * for more information about inserting. This method extracts the information
	 * from a vector of Strings (Every String has to contain one entry) and inserts
	 * the extracted entry if the entry is complete 
	 * @returns True if all entries were correct, false if at least one entry was ignored (all (other)correct entries have been inserted however)
	 */
	public boolean insertLocations(Vector<String> entriesAsStrings)
	{
		boolean hasSucceeded = true;
		boolean continueWhile = false;
		
		Vector<String> entries = new Vector<String>(entriesAsStrings);
		
		while(entries.size() > 0)
		{
			String entry = entries.firstElement();
			entries.remove(0);
			Log.d("DATABASE", "BEFORE: " + entry);
			String[] columns = entry.trim().split(" ");
			Log.d("DATABASE", "AFTER: " + Arrays.toString(columns));
			for(int i = 0; i < 5; i++)
			{
				try{
					if(columns[i] == null)
					{
						hasSucceeded = false;
						continueWhile = true;
						break;
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					hasSucceeded = false;
					continueWhile = true;
					break;
				}
			}
			if(continueWhile) 
			{
				continueWhile = false;
				continue;
			}
			
			double lat;
			double lon;
			long expiration;
			long timestamp;
			
			try{
				lat = Double.parseDouble(columns[1]);
				lon = Double.parseDouble(columns[2]);
				expiration = Long.parseLong(columns[3]);
				timestamp = Long.parseLong(columns[4]);
			}catch(NumberFormatException e)
			{
				hasSucceeded = false;
				Log.e("DATABASE", "Could not extract number from String", e);
				continue;
			}
			
			if(hasSucceeded)
				hasSucceeded = insertLocation(columns[0], lat, lon, expiration, timestamp);
			else
				insertLocation(columns[0], lat, lon, expiration, timestamp);
		}
		
		return hasSucceeded;
	}
	
	/**
	 * This inserts a new row into the location database. Please
	 * notice the limits of the parameters (see below). If any of
	 * them exceeds the limit the row is not inserted and the method
	 * returns false. (However if the expiration Date lies in the past
	 * the method returns true but won't insert anything)
	 * 
	 * After the insertion attempt the database is checked for expired location data
	 * 
	 * @param hash 			The hash of the users mobile phone number
	 * @param latitude 		The latitude of this user's position (must be in between -90.0 and 90.0) 
	 * @param longitude 	The longitude of this user's positions (must be in between -180.0 and 180.0)
	 * @param expirationInUnixSeconds The expiration date of this user's position data (must be greater than
	 * 0. Notice that this parameter is in SECONDS! If the corresponding Unix-Date lies in the past,
	 * the method returns true but won't insert the current data into the database)
	 * @return True, if insertion succeeded (which is when the data was inserted OR was not inserted but the
	 * expiration date lies in the past), false else. 
	 */
	public boolean insertLocation(String hash, double latitude, double longitude, long expirationInUnixSeconds, long timestamp) {
		boolean returnValue = false;
		
		if(hash != null && checkLatitude(latitude) && checkLongitude(longitude) && checkDate(expirationInUnixSeconds)) {
			ContentValues toInsert = new ContentValues();
			toInsert.put("hash", hash);
			toInsert.put("latitude", latitude);
			toInsert.put("longitude", longitude);
			toInsert.put("expirationDate", expirationInUnixSeconds);
			toInsert.put("timestamp", timestamp);
			
			if(expirationInUnixSeconds > System.currentTimeMillis())
			{
				long row = -1;
				try {
					Log.i("DATABASE","Try to insert: " + toInsert.getAsString("hash") + " | " + toInsert.getAsString("latitude") + " | " + toInsert.getAsString("longitude") + " | " + 
							toInsert.getAsString("expirationDate") + " | " + toInsert.getAsString("timestamp"));
					row = openHelper.getWritableDatabase().insertOrThrow("locations", null, toInsert);
				} 
				catch (SQLException e)
				{
					Log.e("DATABASE", "Error while inserting", e);
					return false;
				}
				Log.i("DATABASE", "Inserted to Row " + row + ": " + hash + " | " + latitude + " | " + longitude + " | " + new Date(expirationInUnixSeconds * 1000) + " | " + timestamp);
				returnValue = true;
			}
			else
			{
				returnValue = true;
			}
			
		}
		else
			returnValue = false;
		
		
		deleteOldEntries();
		
		if(returnValue)
		{
			informListeners(false);
		}
		return returnValue;
	}
	
	/**
	 * Closes the current database.
	 * (Reopening is done if there are any database operations
	 * after this method was evoked)
	 */
	public void close()
	{
		if(openHelper != null)
			openHelper.close();
	}
	
	/**
	 * Registers a new listener. The listener must implement the method
	 * {@linkPlain de.androidlab.trackme#onDatabaseChange(Vector<Pair<String, GeoPoint[]>> newData) onDatabaseChange}
	 * which is evoked on every change (insertion / deletion / altering) of the database. 
	 * The full database is given in parameter 'data' 
	 * 
	 * @param listener The new listener that wants to be updated on any change in the database
	 * @returns True, if listener was not registered before, but is now registered, false otherwise (which means
	 * the listener was registered before and/or was not registered now)
	 */
	public boolean registerDatabaseListener(DatabaseListener listener) {
		if(!listeners.contains(listener))
		{
			listeners.add(listener);
			listener.onDatabaseChange(informListeners(true));
			return true;
		}
		else
		{
			listener.onDatabaseChange(informListeners(true));
			return false;
		}
	}
	
	/**
	 * Unregisters listener. No more updated data from the database
	 * will be transmitted to the DatabaseListener
	 * 
	 * @param listener The listener that wants to be deleted
	 * @returns True, if listener was found and deleted, false otherwise
	 */
	public boolean unregisterDatabaseListener(DatabaseListener listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Checks if there are entries in the database that have exceeded their
	 * lifetime. If so, they are deleted immediately.
	 * @return The number of deleted entries
	 */
	private int deleteOldEntries()
	{
		Long millisec = System.currentTimeMillis();
		int rowsDeleted = openHelper.getWritableDatabase().delete("locations", "expirationDate < " + millisec, null);
		if(rowsDeleted > 0) {
			informListeners(false);
		}
		Log.i("DATABASE","Deleted " + rowsDeleted + " rows because their expiration date was exceeded.");
		return rowsDeleted;
	}
	
	/** 
	 * Returns all current database entries as list (vector)
	 * of Strings (one String per entry)
	 */
	public Vector<String> getDatabaseAsStrings()
	{
		deleteOldEntries();
		
		Cursor rows = openHelper.getReadableDatabase().query(
				"locations", 
				new String[]{"hash","latitude","longitude","expirationDate","timestamp"},
				null,    
				null,    
				null,	 
				null,    
				"hash asc, timestamp desc");
		
		if(rows.getCount() <= 0)
			return new Vector<String>();
		
		Vector<String> dbAsStringVector = new Vector<String>();
		
		while(rows.moveToNext())
		{
			String raw = "";
			for(int k = 0; k < rows.getColumnCount(); k++)
				raw = raw + " " + rows.getString(k); 
			dbAsStringVector.add(raw);
		}
		
		return dbAsStringVector;
	}
	
	/**
	 * Informs the Listeners about the data in the database
	 * The data is given to the listeners as they require the information.
	 * @param simulate Sets whether the Listers should really be informed (simulate = false), or if
	 * just the return vectors are created but not sent to the listeners (simulate = true). In second case 
	 * one can use the returning vector for further use without bothering the listeners.
	 * @return All entries of the database
	 */
	private Vector<Pair<String, GeoPoint[]>> informListeners(boolean simulate)
	{
		if(listeners.size() <= 0)
			return new Vector<Pair<String, GeoPoint[]>>();
		
		Vector<String> rawData = new Vector<String>();
		Vector<Pair<String, GeoPoint[]>> abstractedData = new Vector<Pair<String, GeoPoint[]>>();
		
		Cursor rows = openHelper.getReadableDatabase().query(
				"locations", 
				new String[]{"hash","latitude","longitude","expirationDate","timestamp"},
				null,    
				null,    
				null,	 
				null,    
				"hash asc, timestamp asc");
		
		if(rows.getCount() <= 0)
			return new Vector<Pair<String, GeoPoint[]>>();
		
		String currentHash = "";
		Vector<GeoPoint> currentPoints = new Vector<GeoPoint>();
		
		while(rows.moveToNext())
		{
			String raw = "";
			for(int k = 0; k < rows.getColumnCount(); k++)
				raw = raw + " " + rows.getString(k); 
			rawData.add(raw);
			Log.i("DATABASE","Informed Listeners about entry no " + 
					new Integer(rows.getPosition()).toString() + " : " + raw);
			
			if(!currentHash.equals(rows.getString(0)))
			{
				if(!currentHash.equals("")) {
					GeoPoint[] points = new GeoPoint[currentPoints.size()];
					currentPoints.toArray(points);
					abstractedData.add(new Pair<String, GeoPoint[]>(currentHash, points));
					Log.d("DATABASE","Added to Return vector: Hash:" + currentHash + " Points: "+ Arrays.toString(points));
					currentPoints.clear();
				}
				currentHash = rows.getString(0);
			}
			
			int lat, lon;
			lat = (int)(rows.getFloat(1) * 1E6);
			lon = (int)(rows.getFloat(2) * 1E6);
			GeoPoint point = new GeoPoint(lat,lon);
			currentPoints.add(point);	
		}
		
		GeoPoint[] points = new GeoPoint[currentPoints.size()];
		currentPoints.toArray(points);
		abstractedData.add(new Pair<String, GeoPoint[]>(currentHash, points));
		Log.d("DATABASE","Added to Return vector: Hash:" + currentHash + " Points: "+ Arrays.toString(points));
		currentPoints.clear();
		
		
		if(!simulate) {
			for(int i = 0; i < listeners.size(); i++)
			{
				DatabaseListener listener = listeners.elementAt(i);
				listener.onDatabaseChange(abstractedData);
				listener.onDatabaseChangeRaw(rawData);
			}
		}
		
		return abstractedData;
	}
	
	/**
	 * Checks if latitude value is in correct limits
	 * @param latitude Must be in between -90.0 and 90.0
	 * @return True if latitude value is in the limits, false else.
	 */
	private boolean checkLatitude(double latitude) {
		if(latitude >= -90.0 && latitude <= 90.0)
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if longitude value is in correct limits
	 * @param longitude Must be in between -180.0 and 180.0
	 * @return True if longitude value is in the limits, false else.
	 */
	private boolean checkLongitude(double longitude) {
		if(longitude >= -180.0 && longitude <= 180.0)
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if unixTimeInSeconds is greater than 0
	 * @param unixTimeInSeconds must be greaten than 0
	 * @return true if unixTimeInSeconds is greater than 0, false else.
	 */
	private boolean checkDate(long unixTimeInSeconds)
	{
		if(unixTimeInSeconds >= 0)
			return true;
		else
			return false;
	}
	
	/**
	 * The openHelper for the database (cares about opening/creating)
	 * the database as well as closing it.
	 */
	private LocationDatabaseOpenHelper openHelper;
	
	/**
	 * All registered DatabaseListeners are stored here
	 */
	Vector<DatabaseListener> listeners = new Vector<DatabaseListener>();
}
