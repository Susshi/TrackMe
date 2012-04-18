package com.mapquest.android.samples;

import com.mapquest.android.maps.BoundingBox;
import com.mapquest.android.maps.CircleOverlay;
import com.mapquest.android.maps.DrawableOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.TrackballGestureDetector;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * This demo class how to use {@link com.mapquest.android.maps.TrackballGestureDetector} to analyze trackball events.
 *
 */
public class TrackballGestuerDetectorDemo extends SimpleMap {
	
    
    @Override
    protected void init() {
        setupMapView(new GeoPoint(39.8,-105.0), 10);
        
        addTrackballOverlay();
        Toast.makeText(getApplicationContext(), "On trackball, move to scroll the map, tap to zoom in and double tap to zoom out", Toast.LENGTH_SHORT).show();
    }
	
	/**
	 * Constructs the overlay object to detect trackball events and trigger map operations.
	 * 
	 */
	private void addTrackballOverlay() {
        TrackballOverlay trackballOverlay = new TrackballOverlay();
		this.map.getOverlays().add(trackballOverlay);
		this.map.invalidate();
	}
	
	/**
	 * Handles the basic mapping operation using trackball gestures.
	 * Double tap to zoom in. Single tap to zoom out and and scroll to pan
	 *
	 */
	private class TrackballOverlay extends Overlay {

	    TrackballGestureDetector trackballGestureDetector;
	    
	    public TrackballOverlay() {
	        trackballGestureDetector = new TrackballGestureDetector();
	        trackballGestureDetector.registerTapCallback(new Runnable() {

                @Override
                public void run() {
                    //zoom in if it is tap event.
                    map.getController().zoomIn();
                }
	            
	        });
	    }
        @Override
        public boolean onTrackballEvent(MotionEvent evt, MapView mapView) {
            
            trackballGestureDetector.analyze(evt);
            if (trackballGestureDetector.isDoubleTap()) {
                mapView.getController().zoomOut();
            } else if (trackballGestureDetector.isScroll()) {
                mapView.getController().scrollBy(-(int)trackballGestureDetector.scrollX(),
                        -(int)trackballGestureDetector.scrollY());
            }
            //return true to avoid further processing of the event.
            return true;
        }
	    
	}
	
	
}