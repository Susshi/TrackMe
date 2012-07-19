package de.androidlab.trackme.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SettingsData {
	
	private static boolean isInitialized = false;
	//DTN
	// 15 minutes default retransmission time
	public static int default_retransmission_time = 1000 * 60 * 1;
	// default presence notification delay - 5 seconds
	public static int default_presence_notification_delay = 1000 * 5;
	// default presence ttl - 1000 seconds
	public static int default_presence_ttl = 1000;
	// default data ttl - 1000 seconds
	public static int default_data_ttl = 1000;
	
	//DB
	public static long default_location_update_min_time = 30; //seconds
	public static long default_location_update_min_distance = 10; //meters
	public static long default_max_refresh_time = 120; //seconds
	public static long default_route_lifetime = 72; //hours
	
    public static void init(SharedPreferences pref) {
        restoreFromPreferences(pref);
    }
    
    public static void storeInPreferences(SharedPreferences.Editor pref) {
        pref.putInt("default_rtt", default_retransmission_time);
        pref.putInt("default_presence_delay", default_presence_notification_delay);
        pref.putInt("default_presence_ttl", default_presence_ttl);
        pref.putInt("default_data_ttl", default_data_ttl);
        pref.putLong("default_location_update_min_time", default_location_update_min_time);
        pref.putLong("default_location_update_min_distance", default_location_update_min_distance);
        pref.putLong("default_max_refresh_time", default_max_refresh_time);
        pref.putLong("default_route_lifetime", default_route_lifetime);
        pref.commit();
        isInitialized = false;
    }
    
    public static void restoreFromPreferences(SharedPreferences pref) {
        if (isInitialized == false) {
            default_retransmission_time = pref.getInt("default_rtt", default_retransmission_time);
            default_presence_notification_delay = pref.getInt("default_presence_delay", default_presence_notification_delay);
            default_presence_ttl = pref.getInt("default_presence_ttl", default_presence_ttl);
            default_data_ttl = pref.getInt("default_data_ttl", default_data_ttl);
            default_location_update_min_time = pref.getLong("default_location_update_min_time", default_location_update_min_time);
            default_location_update_min_distance = pref.getLong("default_location_update_min_distance", default_location_update_min_distance);
            default_max_refresh_time = pref.getLong("default_max_refresh_time", default_max_refresh_time);
            default_route_lifetime = pref.getLong("default_location_update_min_time", default_route_lifetime);
            isInitialized = true;
        }
    }
	

}
