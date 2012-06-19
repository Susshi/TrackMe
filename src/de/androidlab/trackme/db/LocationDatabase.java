package de.androidlab.trackme.db;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

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
	
	/**
	 * Creates a database object and tries to open or create
	 * the necessary underlying database
	 * 
	 * @param context Most times this is the Base Context from the 'main' activity
	 */
	public LocationDatabase(Context context) {
		openHelper = new LocationDatabaseOpenHelper(context);
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
	public boolean insertLocation(String hash, double latitude, double longitude, long expirationInUnixSeconds) {
		boolean returnValue = false;
		
		if(hash != null && checkLatitude(latitude) && checkLongitude(longitude) && checkDate(expirationInUnixSeconds)) {
			ContentValues toInsert = new ContentValues();
			toInsert.put("hash", hash);
			toInsert.put("latitude", latitude);
			toInsert.put("longitude", longitude);
			toInsert.put("expirationDate", expirationInUnixSeconds);
			
			// TODO: CHECK IF CURRENT DATA IS EXPIRED IF YES SET RETURN VALUE TO TRUE AND !IGNORE INSERTION!
			// ELSE:
			
			long row = -1;
			try {
				row = openHelper.getWritableDatabase().insert("locations", null, toInsert);
			} 
			catch (SQLException e)
			{
				Log.e("DATABASE", "Error while inserting", e);
				return false;
			}
			Log.i("DATABASE", "Inserted to Row " + row + ": " + hash + " | " + latitude + " | " + longitude + " | " + new Date(expirationInUnixSeconds * 1000));
			returnValue = true;
		}
		else
			returnValue = false;
		
		//TODO: CHECK FOR EXPIRED LOCATION DATES IN DATABASE
		return returnValue;
	}
	
	/**
	 * Closes the current database.
	 * (Reopening is done if there are any database operations
	 * after this method was evoked)
	 */
	public void close()
	{
		openHelper.close();
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
}
