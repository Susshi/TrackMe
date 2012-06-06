package de.androidlab.trackme.data;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;

import de.androidlab.trackme.R;
import de.androidlab.trackme.map.ColorGenerator;
import de.androidlab.trackme.map.RouteListEntry;

public class MapData {
    
    public static boolean isInitialized = false;
    public static ColorGenerator colorGenerator = new ColorGenerator(10);
    public static List<RouteListEntry> data = new ArrayList<RouteListEntry>();
    public static int lastActive;
    public static boolean traffic;
    public static boolean satellite;
    public static boolean followActive;
    
    public static boolean useDefaults = true;
    
    public static boolean defaultUpdate = true;
    public static boolean defaultFollow = true;
    public static boolean defaultAntialiasing = true;
    public static int defaultStrokeWidth = 3;
    public static boolean defaultSatellite = false;
    public static boolean defaultTraffic = false;
    public static int defaultSetting = R.id.mapview_radio_friends;
    
    public static void storeInPreferences(SharedPreferences.Editor pref) {
        pref.putBoolean("default_update", defaultUpdate);
        pref.putBoolean("default_follow", defaultFollow);
        pref.putBoolean("default_antialiasing", defaultAntialiasing);
        pref.putBoolean("default_satellite", defaultSatellite);
        pref.putBoolean("default_traffic", defaultTraffic);
        pref.putInt("default_stroke_width", defaultStrokeWidth);
        pref.putInt("default_setting", defaultSetting);
        pref.commit();
        isInitialized = false;
    }
    
    public static void restoreFromPreferences(SharedPreferences pref) {
        if (isInitialized == false) {
            defaultUpdate = pref.getBoolean("default_update", defaultUpdate);
            defaultFollow = pref.getBoolean("default_follow", defaultFollow);
            followActive = defaultFollow;
            defaultAntialiasing = pref.getBoolean("default_antialiasing", defaultAntialiasing);
            defaultSatellite = pref.getBoolean("default_satellite", defaultSatellite);
            satellite = defaultSatellite;
            defaultTraffic = pref.getBoolean("default_traffic", defaultTraffic);
            traffic = defaultTraffic;
            defaultStrokeWidth = pref.getInt("default_stroke_width", defaultStrokeWidth);
            defaultSetting = pref.getInt("default_setting", defaultSetting);
            lastActive = defaultSetting;
            isInitialized = true;
        }
    }
}
