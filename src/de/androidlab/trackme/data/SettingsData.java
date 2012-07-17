package de.androidlab.trackme.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsData {
	
	private static boolean isInitialized = false;
	//DTN
	// 15 minutes default retransmission time
	private static int default_retransmission_time = 1000 * 60 * 1;
	// default presence notification delay - 5 seconds
	private static int default_presence_notification_delay = 1000 * 5;
	// default presence ttl - 5 seconds
	private static int default_presence_ttl = 1000;
	// default data ttl - 120 seconds
	private static int default_data_ttl = 1000;
	
    public static void init(SharedPreferences pref) {
        restoreFromPreferences(pref);
    }
    
    public static void storeInPreferences(SharedPreferences.Editor pref) {
        pref.putInt("default_rtt", default_retransmission_time);
        pref.putInt("default_presence_delay", default_presence_notification_delay);
        pref.putInt("default_presence_ttl", default_presence_ttl);
        pref.putInt("default_data_ttl", default_data_ttl);
        pref.commit();
        isInitialized = false;
    }
    
    public static void restoreFromPreferences(SharedPreferences pref) {
        if (isInitialized == false) {
            default_retransmission_time = pref.getInt("default_rtt", default_retransmission_time);
            default_presence_notification_delay = pref.getInt("default_presence_delay", default_presence_notification_delay);
            default_presence_ttl = pref.getInt("default_presence_ttl", default_presence_ttl);
            default_data_ttl = pref.getInt("default_data_ttl", default_data_ttl);
            isInitialized = true;
        }
    }
	

}
