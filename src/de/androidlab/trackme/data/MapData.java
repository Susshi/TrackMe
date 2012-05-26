package de.androidlab.trackme.data;

import java.util.ArrayList;
import java.util.List;

import de.androidlab.trackme.map.ColorGenerator;
import de.androidlab.trackme.map.RouteListEntry;

public class MapData {
    public static int lastActive;
    public static ColorGenerator colorGenerator = new ColorGenerator(10);
    public static List<RouteListEntry> data = new ArrayList<RouteListEntry>();
}
