package de.androidlab.trackme.data;

import java.util.ArrayList;
import java.util.List;

import de.androidlab.trackme.R;
import de.androidlab.trackme.map.ColorGenerator;
import de.androidlab.trackme.map.RouteListEntry;

public class MapData {
    
    public static ColorGenerator colorGenerator = new ColorGenerator(10);
    public static List<RouteListEntry> data = new ArrayList<RouteListEntry>();
    public static int lastActive;
    public static boolean traffic;
    public static boolean sattelite;
    public static boolean followActive;
    
    public static boolean defaultUpdate = true;
    public static boolean defaultFollow = true;
    public static boolean defaultAntialiasing = true;
    public static int defaultStrokeWidth = 3;
    public static boolean defaultSatellite = false;
    public static boolean defaultTraffic = false;
    public static int defaultSetting = R.id.mapview_radio_friends;
}
