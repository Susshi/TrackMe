package com.mapquest.android.samples;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Annotation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;

/**
 * This demo shows how to enable the default annotation view for an
 * overlayItem 
 */
public class AnnotationViewDemo extends SimpleMap {
        
	AnnotationView annotation;
	
	@Override
	protected void init() {
        setupMapView(new GeoPoint(40732270,-74013985), 13);
		
		// add the overlay
        addOverlay();
        
        // initialize the annotation to be shown later 
    	annotation = new AnnotationView(map);
    	// add an onclick listener to the annotationView
    	annotation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(map.getContext(), 
						"Clicked the "+annotation.getOverlayItem().getTitle()+" annotation", 
						Toast.LENGTH_SHORT).show();
			}
		});
    }
	
	/**
     * Add an ItemizedOverlay to the MapView and an annotationView
     */
    private void addOverlay() {
        Drawable icon = getResources().getDrawable(R.drawable.location_marker);
    	final DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(icon);
    	
    	// add items with a title and a snippet
    	OverlayItem item1 = new OverlayItem(new GeoPoint(40720640, -73995171), "New York, NY", "The Big Apple");
    	overlay.addItem(item1);
    	OverlayItem item2 = new OverlayItem(new GeoPoint(40743900, -74032800), "Hoboken, NJ", "The Mile Square City");
    	overlay.addItem(item2);
    	
    	// add a tap listener
    	overlay.setTapListener(new ItemizedOverlay.OverlayTapListener() {
			@Override
			public void onTap(GeoPoint pt, MapView mapView) {
				// when tapped, show the annotation for the overlayItem
				int lastTouchedIndex = overlay.getLastFocusedIndex();
				if(lastTouchedIndex>-1){
					OverlayItem tapped = overlay.getItem(lastTouchedIndex);
					annotation.showAnnotationView(tapped);
				}
			}
		});
    	
    	map.getOverlays().add(overlay);
    	map.invalidate();
    }
    
    
}