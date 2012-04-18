package com.mapquest.android.samples;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.Toast;

import com.mapquest.android.maps.BoundingBox;
import com.mapquest.android.maps.EllipseOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;

public class EllipseOverlayDemo extends SimpleMap {
	
    @Override
    protected void init() {
        setupMapView(new GeoPoint(38.0,-104.0), 9);
        
        showEllipseOverlay();
    }
	
	/**
	 * Create an EllipseOverlay with a BoundingBox.
	 * 
	 */
	private void showEllipseOverlay() {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAlpha(50);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        
        BoundingBox boundingBox = new BoundingBox(new GeoPoint(39.73996,-104.98486), new GeoPoint(39.5189, -104.7617));
		EllipseOverlay ellipse = new EllipseOverlay(boundingBox, paint);
		ellipse.setTapListener(new EllipseOverlay.OverlayTapListener() {			
			@Override
			public void onTap(GeoPoint p, MapView mapView) {
				Toast.makeText(getApplicationContext(), "Ellipse Tapped!", Toast.LENGTH_SHORT).show();				
			}
		});
		this.map.getOverlays().add(ellipse);
		this.map.invalidate();
	}

}