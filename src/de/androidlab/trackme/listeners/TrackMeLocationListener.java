package de.androidlab.trackme.listeners;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
		this.appContext = appContext; 
		LocationManager locMan = (LocationManager) this.appContext.getSystemService(Context.LOCATION_SERVICE);
		useGPS = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	/* 
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location newLocation) {
		if(useGPS & newLocation.getProvider().equals(LocationManager.NETWORK_PROVIDER)) // USE GPS only if possible
			return;
		
		Log.i("DATABASE","New Position: Lat: " + Double.toString(newLocation.getLatitude()) + "Long: " + Double.toString(newLocation.getLongitude()));
		String phoneNumber, hash;
		TelephonyManager tMgr =(TelephonyManager)appContext.getSystemService(Context.TELEPHONY_SERVICE);
		phoneNumber = tMgr.getLine1Number();

		phoneNumber = Converter.normalizeNumber(phoneNumber);
		hash = Converter.calculateHash(phoneNumber);
		  
		long timestamp = System.currentTimeMillis();
		long expiration = timestamp + 259200000; // 72 h
		
		db.insertLocation(hash, newLocation.getLatitude(), newLocation.getLongitude(), expiration, timestamp);
	}

	/* 
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
		if(provider.equals(LocationManager.GPS_PROVIDER))
			useGPS = false;
		System.out.println("Provider Disabled: " + provider);
	}

	/* 
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
		if(provider.equals(LocationManager.GPS_PROVIDER))
			useGPS = true;
		System.out.println("Provider Enabled: " + provider);

	}

	/* 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		System.out.println("Status changed: " + arg0);

	}
	
	LocationDatabase db;
	Context appContext;
	
	boolean useGPS;  
	
	public final static long gpsMinTime = 30 * 1000;  // in millisec
	public final static long gpsMinDistance = 10;     // in meter
	public final static long networkMinTime = 30 * 1000; // in millisec
	public final static long networkMinDistance = 10;    // in meter
}
