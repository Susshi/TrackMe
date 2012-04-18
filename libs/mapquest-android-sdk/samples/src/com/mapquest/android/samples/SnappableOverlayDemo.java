package com.mapquest.android.samples;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.Overlay;

public class SnappableOverlayDemo extends MapActivity {
    
	protected MapView myMap;
	private SnappableMyLocationOverlay myLocationOverlay;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_map);
        
        this.setupMapView();
        this.setupMyLocation();
    }

    /**
     * This will set up a MapQuest map with a MyLocation Overlay
     */
	private void setupMapView() {
		this.myMap = (MapView) findViewById(R.id.map);
		
		// enable the zoom controls
		myMap.setBuiltInZoomControls(true);
        myMap.displayZoomControls(true);
	}
	
	private void setupMyLocation() {
		this.myLocationOverlay = new SnappableMyLocationOverlay(this, myMap);
		
		myLocationOverlay.enableMyLocation();
		
		myLocationOverlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				GeoPoint currentLocation = myLocationOverlay.getMyLocation(); 
				myMap.getController().animateTo(currentLocation);
				myMap.getController().setZoom(14);
				myMap.getOverlays().add(myLocationOverlay);
			}
		});
	}
	
	@Override
	protected void onResume() {
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableCompass();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	public boolean isRouteDisplayed() {
		return false;
	}
	

	/**
	 * This is a simple example of subclassing the MyLocation Overlay that implements the Overlay.Snappable interface.
	 * When implementing Overlay.Snappable you can focus on a POI where the users is attempting to pinch zoom in or out.  this 
	 * allows you to be a bit more accurate with the zoom behavoir when you are expecting a zoom into a poi for the user to focus on
	 * @author jsypher
	 *
	 */
	public class SnappableMyLocationOverlay extends MyLocationOverlay implements Overlay.Snappable {

		public SnappableMyLocationOverlay(Context context, MapView mapView) {
			super(context, mapView);
		}

		@Override
		public boolean onSnapToItem(int x, int y, Point snapPoint, MapView mapView) { 
			//we are always assuming the center 
			mapView.getProjection().toPixels(myLocationOverlay.getMyLocation(), snapPoint);
			return true;
		}
		
	}
	
	
}