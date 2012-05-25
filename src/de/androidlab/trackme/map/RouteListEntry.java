package de.androidlab.trackme.map;

import com.google.android.maps.GeoPoint;

public class RouteListEntry {
    public String id;
	public int image;
    public String name;
    public int color;
    public boolean isFriend;
    public boolean isChecked;
    public GeoPoint[] coords;
    public LineOverlay line;
    
    public RouteListEntry(String id, int image, String name, int color, boolean isFriend, boolean checked, GeoPoint[] coords) {
        this.id = id;
    	this.image = image;
        this.name = name;
        this.color = color;
        this.isFriend = isFriend;
        this.isChecked = checked;
        this.coords = coords;
    }
}
