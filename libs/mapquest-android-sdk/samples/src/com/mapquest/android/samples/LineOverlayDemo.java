package com.mapquest.android.samples;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.Toast;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.LineOverlay;
import com.mapquest.android.maps.MapView;

public class LineOverlayDemo extends SimpleMap {
   
	
    @Override
    protected void init() {
        super.init();
        setupMapView(new GeoPoint(39.74,-104.985), 7);
        
        showLineOverlay();
        
        showLineOverlayWithPoints();
    }
	
	/**
	 * Construct a simple line overlay to display on the map and add a OverlayTapListener
	 * to respond to tap events.
	 * 
	 */
	private void showLineOverlay() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setAlpha(100);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);

		// sample line data
		List<GeoPoint> routeData = new ArrayList<GeoPoint>();
		routeData.add(new GeoPoint(39.369609, -104.837982));
		routeData.add(new GeoPoint(39.432109, -104.879158));
		routeData.add(new GeoPoint(39.556461, -104.870552));
		routeData.add(new GeoPoint(39.672660, -104.929023)); 
		routeData.add(new GeoPoint(39.734790, -105.015029));
		routeData.add(new GeoPoint(39.946441, -104.988610)); 
		routeData.add(new GeoPoint(40.165721, -105.102523));
		
        LineOverlay line = new LineOverlay(paint);
        line.setData(routeData);
        line.setKey("Line #1");

		line.setTapListener(new LineOverlay.OverlayTapListener() {			
			@Override
			public void onTap(GeoPoint gp, MapView mapView) {
				Toast.makeText(getApplicationContext(), "Line Tap!", Toast.LENGTH_SHORT).show();			
			}
		});
        this.map.getOverlays().add(line);
        //this.map.getController().z
        this.map.invalidate();
	}
	
	/**
	 * Construct a simple line overlay to display on the map with the individual
	 * points shown.  Also, add a OverlayTouchEventListener to respond to touch
	 * events.
	 * 
	 * @param showPoints
	 */
	private void showLineOverlayWithPoints() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setAlpha(100);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);

		// sample line data
		List<GeoPoint> routeData = new ArrayList<GeoPoint>();
		routeData.add(new GeoPoint(39.264166, -103.691704));
		routeData.add(new GeoPoint(39.711421, -104.226601));
		routeData.add(new GeoPoint(39.739940, -104.984656));
		routeData.add(new GeoPoint(39.742579, -105.512688)); 
		routeData.add(new GeoPoint(39.574477, -106.096336));
		routeData.add(new GeoPoint(39.640075, -106.373054)); 
		routeData.add(new GeoPoint(39.064523, -108.549721));
		
        LineOverlay line = new LineOverlay(paint);
        line.setData(routeData);
        line.setShowPoints(true, null);
        line.setKey("Line #2");

		line.setTouchEventListener(new LineOverlay.OverlayTouchEventListener() {			
			@Override
			public void onTouch(MotionEvent evt, MapView mapView) {
				Toast.makeText(getApplicationContext(), "Line Touch!", Toast.LENGTH_SHORT).show();				
			}
		});
        this.map.getOverlays().add(line);
        this.map.invalidate();
	}	
}