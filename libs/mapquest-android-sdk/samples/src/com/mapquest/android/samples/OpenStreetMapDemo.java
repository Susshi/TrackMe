package com.mapquest.android.samples;

import android.os.Bundle;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;

public class OpenStreetMapDemo extends SimpleMap {
    
	protected MapView map; 
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        super.setupMapView(new GeoPoint(38.0, -104.0), 7);
    }

	@Override
    protected int getLayoutId() {
	    // in our simple_osm_map layout xml file, we have a MapView object
        // with no apiKey parameter present. This will default the map to 
        // use OpenStreetMap data
        return R.layout.simple_osm_map;
    }

	@Override
	public boolean isRouteDisplayed() {
		return false;
	}
	
}