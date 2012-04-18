package com.mapquest.android.samples;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mapquest.android.maps.BoundingBox;
import com.mapquest.android.maps.RectangleOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;

/**
 * Simple Demo class for putting a rectangle overlay on a map.
 *
 */
public class RectangleOverlayDemo extends SimpleMap {
	
	private float rotation = 0f;
	
    @Override
    protected void init() {
        //setupMapView(new GeoPoint(39.9,-104.83), 10);
        setupMapView(new GeoPoint(39.73996,-104.98486), 10);
        
        RelativeLayout container = (RelativeLayout)findViewById(R.id.container);
        
        // add a button to rotate the map
        Button button = new Button(this);
        button.setText("RotateMap");
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				rotation = rotation + 45.f;
				map.getController().setMapRotation(rotation);
				
			}
		});
        container.addView(button);
 
        showRectangleOverlay();
    }
	
	/**
	 * Create an RectangleOverlay with a BoundingBox.
	 * 
	 */
	private void showRectangleOverlay() {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAlpha(50);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        
        BoundingBox boundingBox = new BoundingBox(new GeoPoint(39.73996,-104.98486), new GeoPoint(39.5189, -104.7617));
		RectangleOverlay rect = new RectangleOverlay(boundingBox, paint);
		rect.setTapListener(new RectangleOverlay.OverlayTapListener() {			
			@Override
			public void onTap(GeoPoint p, MapView mapView) {
				Toast.makeText(getApplicationContext(), "Rectangle Tapped!", Toast.LENGTH_SHORT).show();				
			}
		});
		this.map.getOverlays().add(rect);
		this.map.invalidate();
	}

}