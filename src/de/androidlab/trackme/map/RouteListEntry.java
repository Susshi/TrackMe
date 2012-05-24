package de.androidlab.trackme.map;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.LineOverlay;

public class RouteListEntry {
    public String id;
	public int image;
    public String name;
    public int color;
    public boolean isFriend;
    public boolean checked;
    public GeoPoint[] coords;
    public LineOverlay line;
    
    public RouteListEntry(String id, int image, String name, int color, boolean isFriend, boolean checked, GeoPoint[] coords) {
        this.id = id;
    	this.image = image;
        this.name = name;
        this.color = color;
        this.isFriend = isFriend;
        this.checked = checked;
        this.coords = coords;
    }
}
