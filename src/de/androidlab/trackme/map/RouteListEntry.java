package de.androidlab.trackme.map;

import com.mapquest.android.maps.GeoPoint;

public class RouteListEntry {
    public int image;
    public String name;
    public boolean checked;
    public int color;
    public GeoPoint[] coords;
    
    public RouteListEntry(int image, String name, boolean checked, int color, GeoPoint[] coords) {
        this.image = image;
        this.name = name;
        this.checked = checked;
        this.color = color;
        this.coords = coords;
    }
}
