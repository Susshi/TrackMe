package com.mapquest.android.samples;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;

/**
 * Simple base class for common things used through out the demos
 *
 */
public class CustomLoadingTile extends MapActivity {
    
	protected MapView map; 
	
	/** 
	 * Called when the activity is first created. 
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        
       init();
    }
    
    /**
     * Initialize the view.
     */
    protected void init() {
    	this.setupMapView(new GeoPoint(38.0,-104.0), 5);
    }

    /**
     * This will set up a basic MapQuest map with zoom controls
     */
	protected void setupMapView(GeoPoint pt, int zoom) {
		this.map = (MapView) findViewById(R.id.map);
		
		// set a custom loading tile from resources
		Bitmap tile = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
		map.setLoadingTile(tile);

		// set the zoom level
		map.getController().setZoom(zoom);
		
		// set the center point
		map.getController().setCenter(pt);
		
		// enable the zoom controls
		map.setBuiltInZoomControls(true);
		
	}

	/**
	 * Get the id of the layout file.
	 * @return
	 */
	protected int getLayoutId() {
	    return R.layout.simple_map;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
}