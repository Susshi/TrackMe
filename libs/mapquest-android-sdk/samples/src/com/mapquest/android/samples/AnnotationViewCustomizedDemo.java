package com.mapquest.android.samples;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;

/**
 * This demo shows how to use a customized annotation view for 
 * overlayItems 
 */
public class AnnotationViewCustomizedDemo extends SimpleMap {
        
	private AnnotationView customizedAnnotation;
	RelativeLayout customInnerView;
	
	ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
	int[] colors = {Color.parseColor("#E67f2020"), 
			Color.parseColor("#E6a97d1f"),
			Color.parseColor("#E6347c1a"),
			Color.parseColor("#E61a417c"),
			Color.parseColor("#E6641a7c")};
	
	TextView customTitle;
	
	@Override
	protected void init() {
		// init our map
        setupMapView(new GeoPoint(40662546, -73961166), 10);
		
		// add the overlay
        addOverlay();
        
        // initialize the annotation to be shown later 
    	customizedAnnotation = new AnnotationView(map);
    	// customize the annotation
    	float density = map.getContext().getResources().getDisplayMetrics().density;
    	customizedAnnotation.setBubbleRadius((int)(12*density+0.5f));
    	// make the annotation not animate
    	customizedAnnotation.setAnimated(false);
    	
    	// init our custom innerView from an xml file
    	LayoutInflater li = (LayoutInflater)map.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customInnerView = (RelativeLayout) li.inflate(R.layout.custom_inner_view, null);
        customTitle = (TextView) customInnerView.findViewById(R.id.title);
        
        // now use the customInnerView as the annotation's innerView
        customizedAnnotation.setInnerView(customInnerView);
    }
	
	/**
     * Add an ItemizedOverlay to the MapView
     */
    private void addOverlay() {
        Drawable icon = getResources().getDrawable(R.drawable.location_marker);
    	final DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(icon);
    	
    	// add items with a title and a snippet
    	items.add(new OverlayItem(new GeoPoint(40758002, -73985633), "Manhattan", ""));
    	items.add(new OverlayItem(new GeoPoint(40650006, -73950005), "Brooklyn", ""));
    	items.add(new OverlayItem(new GeoPoint(40730004, -73823494), "Queens", ""));
    	items.add(new OverlayItem(new GeoPoint(40849998, -73866702), "Bronx", ""));
    	items.add(new OverlayItem(new GeoPoint(40583332, -74150015), "Staten Island", ""));
    	for(int i=0; i < items.size(); i++){
    		overlay.addItem(items.get(i));
    	}
    	
    	// add a tap listener
    	overlay.setTapListener(new ItemizedOverlay.OverlayTapListener() {
			@Override
			public void onTap(GeoPoint pt, MapView mapView) {
				// when tapped, show the annotation for the overlayItem
				int lastTouchedIndex = overlay.getLastFocusedIndex();
				if(lastTouchedIndex>-1){
					OverlayItem tapped = overlay.getItem(lastTouchedIndex);
					showCustomAnnotation(tapped);
				}
			}
		});
    	
    	map.getOverlays().add(overlay);
    	map.invalidate();
    }
    
    /**
     * This will show the annotation view and use a custom innerView
     * @param item
     */
    private void showCustomAnnotation(OverlayItem item){
    	// set title on the custom inner view
    	customTitle.setText(item.getTitle());
    	
    	// change the annotation color based on the overlay shown
		int colorIndex = items.indexOf(item);
		customizedAnnotation.setBackgroundColor(colors[colorIndex]);
		
		// show the customized annotation
		customizedAnnotation.showAnnotationView(item);
    }
}