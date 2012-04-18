package com.mapquest.android.samples;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.PolygonOverlay;
import com.mapquest.android.maps.MapView;

public class PolygonOverlayDemo extends SimpleMap {
   
	private float rotation = 0f;
	
    @Override
    protected void init() {
        super.init();
        setupMapView(new GeoPoint(39.7294,-104.8317), 11);

        RelativeLayout container = (RelativeLayout)findViewById(R.id.container);
        
        // add a button to rotate the map
        Button button = new Button(this);
        button.setText("Rotate Map");
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rotation = rotation + 45.f;
				map.getController().setMapRotation(rotation);
				
			}
		});        
        container.addView(button);
        
        // add the polygons
        showPolygonOverlay();        
        showPolygon2();
    }
	
	/**
	 * Construct a simple polygon overlay to display on the map and add a OverlayTapListener
	 * to respond to tap events.
	 * 
	 */
	private void showPolygonOverlay() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setAlpha(100);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);

		// sample polygon data
		List<GeoPoint> polyData = new ArrayList<GeoPoint>();
		polyData.add(new GeoPoint(39.642363, -104.872239));
		polyData.add(new GeoPoint(39.632580, -104.878033));
		polyData.add(new GeoPoint(39.624515, -104.871295));
		polyData.add(new GeoPoint(39.623854, -104.844473)); 
		polyData.add(new GeoPoint(39.602431, -104.826921));
		polyData.add(new GeoPoint(39.602431, -104.806450)); 
		polyData.add(new GeoPoint(39.610895, -104.811020));
		polyData.add(new GeoPoint(39.618036, -104.819496));
		polyData.add(new GeoPoint(39.626168, -104.822565));
		polyData.add(new GeoPoint(39.636150, -104.828895));
		polyData.add(new GeoPoint(39.649434, -104.829045));
		polyData.add(new GeoPoint(39.655778, -104.834688));
		polyData.add(new GeoPoint(39.657496, -104.841447));
		
        PolygonOverlay polygon = new PolygonOverlay(paint);
        polygon.setData(polyData);
        polygon.setKey("Polygon #1");

		polygon.setTapListener(new PolygonOverlay.OverlayTapListener() {			
			@Override
			public void onTap(GeoPoint gp, MapView mapView) {
				Toast.makeText(getApplicationContext(), "Cherry Creek State Park", Toast.LENGTH_SHORT).show();				
			}
		});
        this.map.getOverlays().add(polygon);
        this.map.invalidate();
	}
	
	/**
	 * Construct a simple polygon overlay to display on the map with the individual
	 * points shown.  Also, add a OverlayTouchEventListener to respond to touch
	 * events.
	 * 
	 * @param showPoints
	 */
	private void showPolygon2() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(100);


		// sample polygon data
		List<GeoPoint> polyData = new ArrayList<GeoPoint>();
		polyData.add(new GeoPoint(39.870206, -104.865587));
		polyData.add(new GeoPoint(39.841742, -104.898461));
		polyData.add(new GeoPoint(39.828286, -104.902495));
		polyData.add(new GeoPoint(39.813266, -104.902409)); 
		polyData.add(new GeoPoint(39.813002, -104.865888));
		polyData.add(new GeoPoint(39.798628, -104.866017)); 
		polyData.add(new GeoPoint(39.798628, -104.7911293));
		polyData.add(new GeoPoint(39.870732, -104.791258));
		polyData.add(new GeoPoint(39.870206, -104.865587));
		
        PolygonOverlay polygon = new PolygonOverlay(paint);
        polygon.setData(polyData);
        polygon.setKey("Polygon #2");

		polygon.setTapListener(new PolygonOverlay.OverlayTapListener() {			
			@Override
			public void onTap(GeoPoint gp, MapView mapView) {
				Toast.makeText(getApplicationContext(), "Rocky Mountain Arsenal", Toast.LENGTH_SHORT).show();				
			}
		});
        this.map.getOverlays().add(polygon);
        this.map.invalidate();
	}	
}