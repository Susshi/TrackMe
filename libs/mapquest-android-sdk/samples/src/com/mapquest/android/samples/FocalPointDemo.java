package com.mapquest.android.samples;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.RotatableIconOverlay;
import com.mapquest.android.maps.TrackballGestureDetector;

/**
 * This class demonstrates how to use focal point and map rotation using location fixes.
 * You will need to enable GPS prior to testing this demo.
 */
public class FocalPointDemo extends SimpleMap {
	
	private static final String TAG = "focalpointdemo";
	
	long duration = 10000;
	
	private ExtendedMyLocationOverlay myLocationOverlay;
	boolean running = false;
	
        
    @Override
    protected void init() {
        
        setupMapView(new GeoPoint(39.0595,-107.1006), 13);
        addMyLocationOverlay();
        
        final ViewTreeObserver vto = this.map.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            
            @Override
            public void onGlobalLayout() {
                //set the focal little above the screen to simulate navigation mode
                int x = map.getWidth() >> 1;
                int y = (int) (map.getHeight() * .7);
                map.setFocalPoint(new Point(x, y));
                ViewTreeObserver obs = map.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
            }
        });
        
        Toast.makeText(getApplicationContext(), "Make sure you have enabled gps option under the settings", Toast.LENGTH_SHORT).show();
    }
	
	private void addMyLocationOverlay() {

	    myLocationOverlay = new ExtendedMyLocationOverlay(this, this.map);
		this.map.getOverlays().add(myLocationOverlay);
		this.map.invalidate();

	}

	/**
     * This class extends the MyLocation to auto center map to new location fix and rotate map to the direction of bearing. 
     *
     */
    private static class ExtendedMyLocationOverlay extends MyLocationOverlay {

        MapView map;
        Location lastFix = null;
        public ExtendedMyLocationOverlay(Context context, MapView mapView) {
            super(context, mapView);
            this.map= mapView;
            //lets register for receiving gps fixes
            enableMyLocation();
        }
        
        @Override
        public void onLocationChanged(Location location) {
            if (super.getLastFix() != null) {
                //duplicate the instance, MyLocationOverlay reuses Location instance inside getLastFix()
                if(lastFix == null) {
                    lastFix = new Location(location.getProvider());
                }
                lastFix.set(super.getLastFix());
            }
            super.onLocationChanged(location);
            Location newFix = super.getLastFix();
            GeoPoint gp = new GeoPoint(newFix.getLatitude(), newFix.getLongitude());
            map.getController().setCenter(gp);
            if (lastFix != null) {
                GeoPoint oldgp = new GeoPoint(lastFix.getLatitude(), lastFix.getLongitude());
                if (!oldgp.equals(gp) ) {
                    float rotation = lastFix.bearingTo(newFix);
                    map.getController().animateRotation(-rotation);
                }
            }
        }

        @Override
        public void destroy() {
            super.destroy();
            lastFix = null;
            map = null;
        }

        
    }
    
}