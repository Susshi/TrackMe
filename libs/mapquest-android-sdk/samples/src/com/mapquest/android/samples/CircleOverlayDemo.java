package com.mapquest.android.samples;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.Toast;

import com.mapquest.android.maps.CircleOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;

public class CircleOverlayDemo extends SimpleMap {
	
    @Override
    protected void init() {
        setupMapView(new GeoPoint(38.0,-104.0), 6);
        
        showCircleOverlayPixels();
        showCircleOverlayMeters();
    }
	
	/**
	 * Create a CircleOverlay with a fixed radius in screen pixels.  The circle will be the
	 * same size regardless of zoom level.
	 * 
	 */
	private void showCircleOverlayPixels() {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAlpha(50);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
		CircleOverlay circle = new CircleOverlay(new GeoPoint(38.0,-104.0), 50, paint);
		circle.setTapListener(new CircleOverlay.OverlayTapListener() {			
			@Override
			public void onTap(GeoPoint p, MapView mapView) {
				Toast.makeText(getApplicationContext(), "Pixel Circle Tapped!", Toast.LENGTH_SHORT).show();				
			}
		});
		this.map.getOverlays().add(circle);
		this.map.invalidate();
	}
	
	/**
	 * Create a CircleOverlay with a fixed radius in meters.  The circle will change size
	 * as you zoom in or out.
	 */
	private void showCircleOverlayMeters() {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(10);
        paint.setAlpha(50);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
		CircleOverlay circle = new CircleOverlay(new GeoPoint(38.0,-104.0), 5000., paint);
		circle.setTouchEventListener(new CircleOverlay.OverlayTouchEventListener() {			
			@Override
			public void onTouch(MotionEvent evt, MapView mapView) {
				Toast.makeText(getApplicationContext(), "Meters Circle Touch!", Toast.LENGTH_SHORT).show();				
			}
		});
		this.map.getOverlays().add(circle);
		this.map.invalidate();		
	}

}