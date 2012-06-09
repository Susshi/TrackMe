package de.androidlab.trackme.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/** Listener for the current Location of this mobile phone 
 * @author Mario Wozenilek
 *
 */
public class TrackMeLocationListener implements LocationListener {

	/* 
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location arg0) {
		System.out.println("Lat: " + Double.toString(arg0.getLatitude()) + "Long: " + Double.toString(arg0.getLongitude()));
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

}
