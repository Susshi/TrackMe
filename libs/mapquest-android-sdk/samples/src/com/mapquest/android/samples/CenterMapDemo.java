package com.mapquest.android.samples;

import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.mapquest.android.maps.GeoPoint;


public class CenterMapDemo extends SimpleMap {
	
	private static final String TAG = "centermapdemo";
	
	/**
	 * 
	 */
    @Override
    protected void init() {
        
        setupMapView(new GeoPoint(39.73987,-104.992586), 16);
        
        //MapView map = (MapView)findViewById(R.id.map);
        
        Button button = (Button)findViewById(R.id.centerButton);
        button.setOnClickListener(new View.OnClickListener() {						
			public void onClick(View v) {
				map.getController().setCenter(new GeoPoint(55.321332,61.826935));
			}
		});
    }

    /**
     * 
     */
	protected int getLayoutId() {
	    return R.layout.center_map;
	}

}