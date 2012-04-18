package com.mapquest.android.samples;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.Projection;

/**
 * Demonstrates ItemizedOverlay snappablility to an item when zooming in
 * 
 */
public class SnappableItemizedOverlayDemo extends SimpleMap {

    
    @Override
    protected void init() {
        setupMapView(new GeoPoint(39544541,-99141968), 3);
		
		// add the overlay
        addOverlay();
    }
	

    /**
     * Add an ItemizedOverlay to the MapView.
     * 
     */
    private void addOverlay() {
        Drawable icon = getResources().getDrawable(R.drawable.location_marker);
        SnappableItemizedOverlay overlay = new SnappableItemizedOverlay(icon);
    	
    	// add points for the top 10 US cities by population and their nicknames
    	OverlayItem it1 = new OverlayItem(new GeoPoint(40720640,-73995171), "New York, NY", "The Big Apple");
    	overlay.addItem(it1);
    	
    	OverlayItem it2 = new OverlayItem(new GeoPoint(34052571,-118242607), "Los Angeles, CA", "City of Angels");
    	overlay.addItem(it2);
 
    	OverlayItem it3 = new OverlayItem(new GeoPoint(41883796,-87632637), "Chicago, IL", "The Windy City");
    	overlay.addItem(it3);
    	
    	OverlayItem it4 = new OverlayItem(new GeoPoint(29763688,-95363579), "Houston, TX", "Space City");
    	overlay.addItem(it4);
    	
    	OverlayItem it5 = new OverlayItem(new GeoPoint(39952303,-75164528), "Philadelphia, PA", "City of Brotherly Love");
    	overlay.addItem(it5);
    	
    	OverlayItem it6 = new OverlayItem(new GeoPoint(33449114,-112073097), "Phoenix, AZ", "Valley of the Sun");
    	overlay.addItem(it6);
    	
    	OverlayItem it7 = new OverlayItem(new GeoPoint(29424553,-98493309), "San Antonio, TX", "Something to Remember");
    	overlay.addItem(it7);
    	
    	OverlayItem it8 = new OverlayItem(new GeoPoint(32716153,-117156334), "San Diego, CA", "Americas Finest City");
    	overlay.addItem(it8);
    	
    	OverlayItem it9 = new OverlayItem(new GeoPoint(32783720,-96800041), "Dallas, TX", "The Big D");
    	overlay.addItem(it9);
    	
    	OverlayItem it10 = new OverlayItem(new GeoPoint(37340052,-121893501), "San Jose, CA", "Capital of Silicon Valley");
    	overlay.addItem(it10);
    	
    	// add a focus change listener
    	overlay.setOnFocusChangeListener(new ItemizedOverlay.OnFocusChangeListener() {
			@Override
			public void onFocusChanged(ItemizedOverlay overlay, OverlayItem newFocus) {
				// when focused item changes, recenter map and show info
				map.getController().animateTo(newFocus.getPoint());
				Toast.makeText(getApplicationContext(), newFocus.getTitle() + ": " + 
						newFocus.getSnippet(), Toast.LENGTH_SHORT).show();
				
			}    		
    	});
    	
    	map.getOverlays().add(overlay);
    	map.invalidate();
    }

    
    
    private class SnappableItemizedOverlay extends DefaultItemizedOverlay implements Overlay.Snappable{

		public SnappableItemizedOverlay(Drawable defaultMarker) {
			super(defaultMarker);
		}

		/**
		 * In this implementation we will snap to closest SNAPPABLE item within 50 pixels of the zoom center point, you can
		 * use any algorithm you want decide if you want to snap to it or not.
		 */
		@Override
		public boolean onSnapToItem(int x, int y, Point snapPoint, MapView mapView) {
						
			OverlayItem closestItem=null;//holder for closest point
			Point closestItemPoint = null; //pixel coords on map of closest Item
			int closestPixelDistance=0;//sum of x+y used to see what point was closer
			
			Projection projection = mapView.getProjection();
			
			//loop through the items and see if we have something to snap to
			for(int i=0;i<size();i++){
				OverlayItem item=getItem(i);
				Point point =projection.toPixels(item.getPoint(), new Point()); 				
				int totalPixelDistance=Math.abs(point.x-x)+Math.abs(point.y-y);

				//see if the item is in the 50 px range
				if( (totalPixelDistance<=50)){
					
					//if we do not have a closest yet, use this one
					if(closestItem==null){
						closestItem=item;
						closestItemPoint=point;
						closestPixelDistance=totalPixelDistance;
						
					}else{
						//see if this one is closer
						if( totalPixelDistance<closestPixelDistance){
							closestItem=item;
							closestItemPoint=point;
							closestPixelDistance=totalPixelDistance;
						}
					}
				}
			
			}
			
			//if we found an item lets snap to it
			if(closestItem!=null){
				snapPoint.x=closestItemPoint.x;
				snapPoint.y=closestItemPoint.y;
				Toast.makeText(getApplicationContext(), "Snapping to:"+closestItem.getTitle(), Toast.LENGTH_SHORT).show();
				return true;
			}
			
			return false;
		}		
    	
    }
    
}