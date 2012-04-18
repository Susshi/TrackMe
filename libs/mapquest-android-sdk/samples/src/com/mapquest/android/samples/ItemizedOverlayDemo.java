package com.mapquest.android.samples;

import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.OverlayItem;

/**
 * This class demonstrates the use of the DefaultItemizedOverlay class.  The demo creates
 * an overlay with points for the 10 largest cities in the United States.  When one of the
 * cities is clicked, the map recenters on the city and displays a toast message with the
 * city name and nickname.
 * 
 */
public class ItemizedOverlayDemo extends SimpleMap {
        
	@Override
	protected void init() {
        setupMapView(new GeoPoint(39544541,-99141968), 3);
		
		// add the overlay
        addOverlay();
    }
	

    /**
     * Add an ItemizedOverlay to the MapView.
     * 
     */
    private void addOverlay() {
        Drawable icon = getResources().getDrawable(R.drawable.location_marker);
    	DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(icon);
    	
    	// add points for the top 10 US cities by population and their nicknames
    	OverlayItem it1 = new OverlayItem(new GeoPoint(40720640,-73995171), "New York, NY", "The Big Apple");
    	overlay.addItem(it1);
    	
    	OverlayItem it2 = new OverlayItem(new GeoPoint(34052571,-118242607), "Los Angeles, CA", "City of Angels");
    	overlay.addItem(it2);
 
    	OverlayItem it3 = new OverlayItem(new GeoPoint(41883796,-87632637), "Chicago, IL", "The Windy City");
    	overlay.addItem(it3);
    	
    	OverlayItem it4 = new OverlayItem(new GeoPoint(29763688,-95363579), "Houston, TX", "Space City");
    	overlay.addItem(it4);
    	
    	OverlayItem it5 = new OverlayItem(new GeoPoint(39952303,-75164528), "Philadelphia, PA", "City of Brotherly Love");
    	overlay.addItem(it5);
    	
    	// use a different marker with different alignment for this point
    	OverlayItem it6 = new OverlayItem(new GeoPoint(33448261,-112075768), "Phoenix, AZ", "Valley of the Sun");
    	it6.setMarker(Overlay.setAlignment(getResources().getDrawable(R.drawable.monstertruck), Overlay.CENTER_HORIZONTAL | Overlay.CENTER_VERTICAL));
    	overlay.addItem(it6);
    	
    	OverlayItem it7 = new OverlayItem(new GeoPoint(29424553,-98493309), "San Antonio, TX", "Something to Remember");
    	overlay.addItem(it7);
    	
    	OverlayItem it8 = new OverlayItem(new GeoPoint(32716153,-117156334), "San Diego, CA", "Americas Finest City");
    	overlay.addItem(it8);
    	
    	OverlayItem it9 = new OverlayItem(new GeoPoint(32783720,-96800041), "Dallas, TX", "The Big D");
    	overlay.addItem(it9);
    	
    	OverlayItem it10 = new OverlayItem(new GeoPoint(37340052,-121893501), "San Jose, CA", "Capital of Silicon Valley");
    	overlay.addItem(it10);
    	
    	OverlayItem it11 = new OverlayItem(new GeoPoint(47517201,3164063), "France", "France");
    	overlay.addItem(it11);
    	
    	OverlayItem it12 = new OverlayItem(new GeoPoint(29228890,120937500), "PRC", "PRC");
    	overlay.addItem(it12);
    	
    	// add a focus change listener
    	overlay.setOnFocusChangeListener(new ItemizedOverlay.OnFocusChangeListener() {
			@Override
			public void onFocusChanged(ItemizedOverlay overlay, OverlayItem newFocus) {
				// when focused item changes, recenter map and show info
				map.getController().animateTo(newFocus.getPoint());
				Toast.makeText(map.getContext().getApplicationContext(), newFocus.getTitle() + ": " + 
						newFocus.getSnippet(), Toast.LENGTH_SHORT).show();
				
			}    		
    	});
    	
    	map.getOverlays().add(overlay);
    	map.invalidate();
    	
    	// test setting the focus on a specific item
    	overlay.setFocus(it6);
    }

}