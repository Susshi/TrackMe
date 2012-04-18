package com.mapquest.android.samples;

import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.mapquest.android.maps.GeoPoint;


public class AnimateMapDemo extends SimpleMap {
	
	private static final String TAG = "mapanimatedemo";
	
    @Override
    protected void init() {

        //near Dickson, Russia 
        setupMapView(new GeoPoint(72.448168, 82.560168), 20); 
        
        
        //MapView map = (MapView)findViewById(R.id.map);
        
        Button button = (Button)findViewById(R.id.animateButton);
        button.setOnClickListener(new View.OnClickListener() {						
			public void onClick(View v) {
				// to Okhotsk, Russia
				map.getController().animateTo(new GeoPoint(59.655597,142.75426)); 
			}
		});
    }

	protected int getLayoutId() {
	    return R.layout.map_animate;
	}

}
