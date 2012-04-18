package com.mapquest.android.samples;

import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.mapquest.android.maps.BoundingBox;
import com.mapquest.android.maps.DrawableOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.Overlay;

/**
 * This demo class shows overlaying drawable resource on the map using {@link com.mapquest.android.maps.DrawableOverlay}
 *
 */
public class DrawableOverlayDemo extends SimpleMap {
	
    @Override
    protected void init() {
        setupMapView(new GeoPoint(39.8,-105.0), 10);
        
        showDrawableOverlay();
    }
	
	/**
	 * Overlays the mapquest logo with an assumed bounding box.
	 */
	private void showDrawableOverlay() {
        Drawable drawable = getResources().getDrawable(R.drawable.mqlogo);
        BoundingBox bbox = new BoundingBox(new GeoPoint(39.844927, -105.110526),new GeoPoint(39.734442, -104.833237));
		DrawableOverlay drawableOverlay = new DrawableOverlay(drawable, bbox);
		drawableOverlay.setTapListener(new Overlay.OverlayTapListener() {			
			@Override
			public void onTap(GeoPoint p, MapView mapView) {
				Toast.makeText(getApplicationContext(), "Drawable Tapped!", Toast.LENGTH_SHORT).show();				
			}
		});
		this.map.getOverlays().add(drawableOverlay);
		this.map.invalidate();
	}
	
}