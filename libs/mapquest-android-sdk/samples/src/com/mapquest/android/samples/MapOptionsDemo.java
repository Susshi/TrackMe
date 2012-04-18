package com.mapquest.android.samples;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.mapquest.android.maps.Configuration;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;

/**
 * This demo shows how to use different map view options including:
 * 	- satellite maps
 *  - hybrid maps
 *  - traffic 
 *
 */
public class MapOptionsDemo extends MapActivity {
    
	protected MapView map; 
	private RadioButton mapButton;
	private RadioButton satelliteButton;
	private ToggleButton trafficToggle;
	private ToggleButton labelsToggle;
	private Configuration config;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(getLayoutId());
        
    	init();
    }
    
    protected void init() {
    	this.setupMapView(new GeoPoint(39.740145, -104.984871), 12);
    	this.config = map.getConfiguration();
    	
    	this.mapButton = (RadioButton)findViewById(R.id.mapButton);
    	this.satelliteButton = (RadioButton)findViewById(R.id.satelliteButton);
    	this.trafficToggle = (ToggleButton)findViewById(R.id.trafficToggle);
    	this.labelsToggle = (ToggleButton)findViewById(R.id.labelsToggle);
    	
    	this.initMapOptions();
    }

    /**
     * This will set up a basic MapQuest map with zoom controls
     */
	protected void setupMapView(GeoPoint pt, int zoom) {
		this.map = (MapView) findViewById(R.id.map);

		// set the zoom level
		map.getController().setZoom(zoom);
		
		// set the center point
		map.getController().setCenter(pt);
		
		// enable the zoom controls
		map.setBuiltInZoomControls(true);
	}
	
	/**
	 * Handle changes to the checked state of the radio buttons
	 */
	private OnClickListener radio_listener = new OnClickListener() {
	    public void onClick(View v) {
	    	
			config.setSatelliteLabeled(labelsToggle.isChecked());
			config.setSatellite(satelliteButton.isChecked());

			//see if the labels toggle should be shown
			if((v.getId() == satelliteButton.getId())){
				labelsToggle.setVisibility(View.VISIBLE);
			}else if(v.getId() == mapButton.getId()){
				labelsToggle.setVisibility(View.GONE);
			}
			
		}
	};
	
	
	/**
	 * Handle changes to the checked state of the toggle buttons
	 */
	private OnCheckedChangeListener check_listener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        	// if this was the traffic toggle, set the traffic to be shown
    		map.setTraffic(trafficToggle.isChecked());

        	// if this was the labels toggle, set the labels to be shown or not
        	config.setSatelliteLabeled(labelsToggle.isChecked());
		}
	};
	
	/**
	 * Init controls for map view controls 
	 */
	private void initMapOptions() {
		mapButton.setOnClickListener(radio_listener);
		satelliteButton.setOnClickListener(radio_listener);
		trafficToggle.setOnCheckedChangeListener(check_listener);
		labelsToggle.setOnCheckedChangeListener(check_listener);
	}

	protected int getLayoutId() {
	    return R.layout.map_options;
	}

	@Override
	public boolean isRouteDisplayed() {
		return false;
	}
	
}