package com.mapquest.android.samples;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.widget.Toast;

import com.mapquest.android.intent.MapQuestAppLauncher;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.Overlay;

public class NavigationIntentDemo extends SimpleMap {
	
	/**
	 * Initialize the map.
	 */
    @Override
    protected void init() {
        setupMapView(new GeoPoint(38.0,-104.0), 6);
        
        TouchOverlay overlay = new TouchOverlay();
        map.getOverlays().add(overlay);
    }
	

    /**
     * Crude way to launch navigation to various locations based on clicking on the map.
     * 
     * @author rhouston
     *
     */
    private class TouchOverlay extends Overlay {
    	
    	@Override
    	public boolean onTouchEvent(MotionEvent event, final MapView mapView) {
    		
    		if(event.getAction() == MotionEvent.ACTION_UP) {
    			final GeoPoint p = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY());
    			Toast.makeText(getApplicationContext(), p.getLatitude() + "," + p.getLongitude(), Toast.LENGTH_LONG).show();
    			
    	        AlertDialog.Builder builder = new AlertDialog.Builder(mapView.getContext());
    	        builder.setTitle("Navigation Intent");
    	        builder.setMessage("Would you like to navigate to:    [" + p.toString() + "] ");
    	        
    	        builder.setCancelable(true);
    	        
    	        builder.setPositiveButton("Ok", new Dialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String label = "My Place";
						//MapQuestAppLauncher.launchNavigation(mapView.getContext(), p, label);
						MapQuestAppLauncher.launchNavigation(mapView.getContext(), p, label, 
								MapQuestAppLauncher.MapType.SAT, MapQuestAppLauncher.MapLayer.TRAFFIC);
					}
    	        	
    	        });

    	        
    	        builder.setNegativeButton("Cancel", new Dialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
    	        	
    	        });
    	        builder.create();
    	        builder.show();
    		}
    		
    		return false;
    	}
    }
}