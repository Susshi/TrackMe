package com.mapquest.android.samples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * SampleLauncher is a simple ListActivity that lets you pick different 
 * sample activities to launch for demonstration purposes
 */
public class SampleLauncher extends ListActivity {

	// available demo apps by class:
	private Class[] samples={
	        //simple map
	        SimpleMap.class,
	        OpenStreetMapDemo.class,
	        //overlay demos
	        CircleOverlayDemo.class,
	        DrawableOverlayDemo.class,
	        LineOverlayDemo.class,
	        PolygonOverlayDemo.class,
	        EllipseOverlayDemo.class,
	        RectangleOverlayDemo.class,
	        RotatableIconOverlayDemo.class,
	        MyLocationMap.class,
	        CustomizedMyLocationMap.class,
	        SnappableOverlayDemo.class,
	        ItemizedOverlayDemo.class,
	        SnappableItemizedOverlayDemo.class,
	        //mapview options demos
	        MapOptionsDemo.class,
	        LayoutParamsDemo.class,
	        FocalPointDemo.class,
	        //geocoder demos
	        MapQuestGeocoderDemo.class,
	        OpenStreetMapGeocoderDemo.class,
	        //route demos
	        RouteDemo.class,
	        RouteOpenStreetMapDemo.class,
	        RouteDemoInternational.class,
	        RouteWithNoMapDemo.class,
	        RouteItineraryDemo.class,
	        RouteAmbiguitiesDemo.class,
	        RouteMultiPointDemo.class,
	        RouteOptionsDemo.class,
	        //misc demos
	        NavigationIntentDemo.class,
	        TrackballGestureDetectorDemo.class,
	        AnnotationViewDemo.class,
	        AnnotationViewCustomizedDemo.class,
	        MapEventDemo.class,
	        AnimateMapDemo.class,
	        CenterMapDemo.class,
	        CustomLoadingTile.class
	};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        ListView lv = this.getListView();
        lv.setAdapter(new ArrayAdapter<String>(
        		this, 
        		android.R.layout.simple_list_item_1, 
        		getStringArray()));
    }
    
    /**
     * Gets the class' names as an array for the list to show
     */
    private String[] getStringArray() {
    	String[] sampleNames = new String[samples.length];
    	for(int i=0; i < samples.length; i++) {
    		sampleNames[i] = samples[i].getSimpleName();
    	}
    	return sampleNames;
    }
    
    /**
     * When you click a list item, launch the sample activity at it's index
     */
    public void onListItemClick(ListView parent, View v, int position, long id){
		Intent myIntent = new Intent(SampleLauncher.this, samples[position]);
		this.startActivity(myIntent);
	}
}
