package de.androidlab.trackme.map;

import android.graphics.Bitmap;

import com.google.android.maps.GeoPoint;

public class RouteListEntry {
    public String id;
	public Bitmap image;
    public String name;
    public int color;
    public boolean isFriend;
    public boolean isChecked;
    public GeoPoint[] coords;
    public LineOverlay line;
    
    public RouteListEntry(String id,
                          Bitmap image,
                          String name,
                          int color,
                          boolean isFriend,
                          boolean checked,
                          GeoPoint[] coords) {
        this.id = id;
    	this.image = image;
        this.name = name;
        this.color = color;
        this.isFriend = isFriend;
        this.isChecked = checked;
        this.coords = coords;
    }
    
    public RouteListEntry(ContactInfo contact, 
                          int color, 
                          boolean checked, 
                          GeoPoint[] coords) {
        this(contact.number, 
             contact.picture, 
             contact.name, 
             color, 
             contact.isFriend, 
             checked, coords);
    }
    
    public RouteListEntry(ContactInfo contact, int color) {
        this(contact.number, 
             contact.picture,
             contact.name, color, 
             contact.isFriend, 
             false, 
             null);
    }
}
