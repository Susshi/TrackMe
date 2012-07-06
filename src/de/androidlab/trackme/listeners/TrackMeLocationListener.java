package de.androidlab.trackme.listeners;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import de.androidlab.trackme.db.LocationDatabase;
import de.androidlab.trackme.utils.Converter;

/** Listener for the current Location of this mobile phone 
 * @author Mario Wozenilek
 *
 */
public class TrackMeLocationListener implements LocationListener {

	public TrackMeLocationListener(LocationDatabase db, Context appContext) {
		this.db = db;
		lastLocation = null;
		this.appContext = appContext;
	}
	
	/* 
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location newLocation) {
		Log.i("DATABASE","New Position: Lat: " + Double.toString(newLocation.getLatitude()) + "Long: " + Double.toString(newLocation.getLongitude()));
		if(lastLocation == null || newLocation.distanceTo(lastLocation) > 10)
		{
			String phoneNumber, hash;
			TelephonyManager tMgr =(TelephonyManager)appContext.getSystemService(Context.TELEPHONY_SERVICE);
			phoneNumber = tMgr.getLine1Number();

			phoneNumber = Converter.normalizeNumber(phoneNumber);
			hash = Converter.calculateHash(phoneNumber);
			  
			long timestamp = System.currentTimeMillis();
			long expiration = timestamp + 259200000; // 72 h
			
			db.insertLocation(hash, newLocation.getLatitude(), newLocation.getLongitude(), expiration, timestamp);
			lastLocation = newLocation;
		}
		else
			return;
	}

	/* 
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String arg0) {
		System.out.println("Provider Disabled: " + arg0);

	}

	/* 
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String arg0) {
		System.out.println("Provider Enabled: " + arg0);

	}

	/* 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		System.out.println("Status changed: " + arg0);

	}
	
	LocationDatabase db;
	Location lastLocation;
	Context appContext;
}
