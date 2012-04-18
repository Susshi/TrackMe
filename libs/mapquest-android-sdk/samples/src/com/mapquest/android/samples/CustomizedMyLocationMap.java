package com.mapquest.android.samples;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.Overlay;

public class CustomizedMyLocationMap extends MyLocationMap {
	
	
	/**
	 * Provide a custom look to the MyLocation icon.
	 */
	@Override
	protected void setupMyLocation() {
		this.myLocationOverlay = new MyLocationOverlay(this, myMap);
		Drawable icon = getResources().getDrawable(R.drawable.star);
		this.myLocationOverlay.setMarker(icon,Overlay.CENTER_HORIZONTAL | Overlay.CENTER_VERTICAL);

    	Paint beaconAnimationOutlinePaint = new Paint(); 
    	beaconAnimationOutlinePaint.setColor(Color.GREEN); 
    	beaconAnimationOutlinePaint.setStrokeWidth(2.0f);
    	beaconAnimationOutlinePaint.setStyle(Paint.Style.STROKE);
    	beaconAnimationOutlinePaint.setAntiAlias(true);
    	beaconAnimationOutlinePaint.setAlpha(70);

    	Paint beaconAnimationFillPaint = new Paint(); 
    	beaconAnimationFillPaint.setColor(Color.GREEN); 
    	beaconAnimationFillPaint.setStrokeWidth(2.0f);
    	beaconAnimationFillPaint.setStyle(Paint.Style.FILL);
    	beaconAnimationFillPaint.setAntiAlias(true);
    	beaconAnimationFillPaint.setAlpha(30);
    	
    	myLocationOverlay.setBeaconAnimationOutlinePaint(beaconAnimationOutlinePaint);
    	myLocationOverlay.setBeaconAnimationFillPaint(beaconAnimationFillPaint);
		
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				GeoPoint currentLocation = myLocationOverlay.getMyLocation(); 
				myMap.getController().animateTo(currentLocation);
				myMap.getController().setZoom(14);
				myMap.getOverlays().add(myLocationOverlay);
				myLocationOverlay.setFollowing(true);
			}
		});
	}
	
}