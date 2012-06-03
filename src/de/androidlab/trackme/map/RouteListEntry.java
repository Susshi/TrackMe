package de.androidlab.trackme.map;

import android.graphics.Bitmap;
import android.util.Pair;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

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
    
    public void centerToRoute(MapView map) {
        MapController controller = map.getController();
        if (coords.length == 1) { // animate to the location
            controller.animateTo(coords[0]);
        } else {
            // find the lat, lon span
            int minLatitude = Integer.MAX_VALUE;
            int maxLatitude = Integer.MIN_VALUE;
            int minLongitude = Integer.MAX_VALUE;
            int maxLongitude = Integer.MIN_VALUE;

            // Find the boundaries of the item set
            for (GeoPoint item : coords) {
                int lat = item.getLatitudeE6(), lon = item.getLongitudeE6();

                maxLatitude = Math.max(lat, maxLatitude);
                minLatitude = Math.min(lat, minLatitude);
                maxLongitude = Math.max(lon, maxLongitude);
                minLongitude = Math.min(lon, minLongitude);
            }

            // leave some padding from corners
            // such as 0.1 for hpadding and 0.2 for vpadding
            double hpadding = 0.1, vpadding = 0.2;
            maxLatitude = maxLatitude + (int)((maxLatitude-minLatitude)*hpadding);
            minLatitude = minLatitude - (int)((maxLatitude-minLatitude)*hpadding);

            maxLongitude = maxLongitude + (int)((maxLongitude-minLongitude)*vpadding);
            minLongitude = minLongitude - (int)((maxLongitude-minLongitude)*vpadding);

            // Calculate the lat, lon spans from the given pois and zoom
            controller.zoomToSpan(Math.abs(maxLatitude - minLatitude),
                                  Math.abs(maxLongitude - minLongitude));

            // Animate to the center of the cluster of points
            controller.animateTo(new GeoPoint((maxLatitude + minLatitude) / 2,
                                              (maxLongitude + minLongitude) / 2));
        }
    }
}
