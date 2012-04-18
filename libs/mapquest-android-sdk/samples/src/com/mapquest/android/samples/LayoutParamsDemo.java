package com.mapquest.android.samples;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;

/**
 * A sample app to showcase using layout params to place position child views inside MapView
 *
 */
public class LayoutParamsDemo extends SimpleMap {
	
    private ImageView viewRelativeToMapXml;
    private ImageView viewRelativeToMapViewXml;
    
    private ImageView viewRelativeToMap;
    private ImageView viewRelativeToMapView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /** Called when the activity is first created. */
    @Override
    protected void init() {
        setupMapView(new GeoPoint(38.0,-104.0), 6);
        
        setupChildViewsFromXml();
        createChildViews();
    }
	
    @Override
    protected int getLayoutId() {
        return R.layout.simple_layout_params;
    }

    /**
     * Setup views from layout xml and attach events.
     */
    private void setupChildViewsFromXml() {
        viewRelativeToMapXml = (ImageView) findViewById(R.id.viewRelativeToMap);
        
        //setup alignment, alignment options cannot be configured through xml, so setting it up here.
        MapView.LayoutParams lp = (MapView.LayoutParams) viewRelativeToMapXml.getLayoutParams();
        lp.alignment = MapView.LayoutParams.BOTTOM_CENTER;
        
        viewRelativeToMapXml.setOnClickListener(new OnClickListener() {
           
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Child view constructed from layout xml positioned relative to map",
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        viewRelativeToMapViewXml = (ImageView) findViewById(R.id.viewRelativeToMapView);
        
        viewRelativeToMapViewXml.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Child view constructed from layout xml and positioned relative to MapView",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Creates two child views programmatically with different position modes.
     */
    private void createChildViews() {
        viewRelativeToMap = new ImageView(this);
        
        viewRelativeToMap.setImageDrawable(getResources().getDrawable(R.drawable.monstertruck));
        //creating layout param options for positioning relative to the map
        MapView.LayoutParams lp = new MapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, new GeoPoint(39.0,-102.0),
                MapView.LayoutParams.LEFT | MapView.LayoutParams.CENTER_VERTICAL);
        //add the view to the parent
        map.addView(viewRelativeToMap, lp);
        
        viewRelativeToMap.setOnClickListener(new OnClickListener() {
           
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Child view constructed programattically and positioned relative to map",
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        viewRelativeToMapView =  new ImageView(this);
        
        viewRelativeToMapView.setImageDrawable(getResources().getDrawable(R.drawable.icon));
        //creating layout param options for position relative to the MapView.
        lp = new MapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 50, 200,
                MapView.LayoutParams.CENTER);
        
        map.addView(viewRelativeToMapView, lp);
        
        viewRelativeToMapView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Child view constructed programattically and positioned relative to MapView",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}