package de.androidlab.trackme.listeners;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import de.androidlab.trackme.data.SettingsData;
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
		currentBestLocation = null;
		LocationManager locMan = (LocationManager) this.appContext.getSystemService(Context.LOCATION_SERVICE);
		useGPS = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	/* 
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location newLocation) {
		//if(useGPS && newLocation.getProvider() != null && newLocation.getProvider().equals(LocationManager.NETWORK_PROVIDER)) // USE GPS only if possible
			//return;
		if(!isBetterLocation(newLocation, currentBestLocation))
			return;
		else
			currentBestLocation = newLocation;
		
		Log.i("DATABASE","New Position: Lat: " + Double.toString(newLocation.getLatitude()) + "Long: " + Double.toString(newLocation.getLongitude()));
		String phoneNumber, hash;
		TelephonyManager tMgr =(TelephonyManager)appContext.getSystemService(Context.TELEPHONY_SERVICE);
		phoneNumber = tMgr.getLine1Number();
			
		phoneNumber = Converter.normalizeNumber(phoneNumber);
		hash = Converter.calculateHash(phoneNumber);
		  
		long timestamp = System.currentTimeMillis();
		long expiration = timestamp + (SettingsData.default_route_lifetime * 3600000); 
		
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

	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	    	Log.i("LocationDBingame", "No old location -> newLocation is better");
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > SettingsData.default_max_refresh_time * 1000;
	    boolean isSignificantlyOlder = timeDelta < -SettingsData.default_max_refresh_time * 1000;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	    	Log.i("LocationDBingame", "Old location is too old -> newLocation is better");
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	    	Log.i("LocationDBingame", "NewLocation is more then two minutes older then oldLocation -> Old location is better");
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	    	Log.i("LocationDBingame", "New Location is more accurate -> newLocation is better");
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	    	Log.i("LocationDBingame", "New Location is equally accurate, but newer -> newLocation is better");
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	    	Log.i("LocationDBingame", "New Location is newer, from same Provider and not significantly less accurate -> newLocation is better");
	    	return true;
	    }
	    Log.i("LocationDBingame", "No improvement found -> Old location is better");
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	
	LocationDatabase db;
	Context appContext;
	
	boolean useGPS;  
	
	Location currentBestLocation;
}
