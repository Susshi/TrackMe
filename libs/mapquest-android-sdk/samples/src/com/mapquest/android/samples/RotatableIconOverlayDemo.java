package com.mapquest.android.samples;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.RotatableIconOverlay;

public class RotatableIconOverlayDemo extends SimpleMap {
	
	private static final String TAG = "rotatableiconoverlaydemo";
	
	private static final String START_TEXT = "Click Icon to Start Rotation";
	private static final String STOP_TEXT = "Click Icon to Stop Rotation";
	
	private static final int ANIMATION_FINISHED = 0;
	
	long duration = 10000;
	
	private RotatableIconOverlay rio;
	boolean running = false;
	
	private TextView instructionalText;

        
    @Override
    protected void init() {
        
        setupMapView(new GeoPoint(39.0595,-107.1006), 13);
        
        // add instructional text
        instructionalText = new TextView(this);
        instructionalText.setTextColor(Color.BLACK);
        instructionalText.setTextSize(20);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 10;
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		instructionalText.setText(START_TEXT);
		instructionalText.setLayoutParams(params);
		
		RelativeLayout container = (RelativeLayout)findViewById(R.id.container);
		container.addView(instructionalText);
		
		// add the icon
        showRotatableIcon();
    }
	
	/**
	 * Add a rotatable icon to the map.  Start/Stop a rotation animation when the icon is tapped.
	 * 
	 */
	private void showRotatableIcon() {

        Drawable icon = getResources().getDrawable(R.drawable.monstertruck);
        rio = new RotatableIconOverlay(new GeoPoint(39.0595,-107.1006), icon, Overlay.CENTER);
        rio.setRotation(45.0f);
        rio.setTapListener(new RotatableIconOverlay.OverlayTapListener() {
        	@Override
        	public void onTap(GeoPoint p, MapView mapView) {
				if(!running) {
					Animator anim = new Animator();
					anim.start();
					instructionalText.setText(STOP_TEXT);
				} else {
					running = false;
				}
        	}
        });
		this.map.getOverlays().add(rio);
		this.map.invalidate();

	}
	
	/**
	 * Create a handler to modify the instructional text when the animation is finished.
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == ANIMATION_FINISHED) {
				instructionalText.setText(START_TEXT);
			}
		}
	};
	
	/**
	 * A very primitive animation routine to demonstrate the rotation aspect of the icon.
	 */
	private void animate() {
		Log.d(TAG, "Start animate");
		running = true;
		
		
		long startTime = System.currentTimeMillis();
		long elapsed = 0;
		
		while(elapsed < duration && running) {
			float angle = (elapsed/1000.f)*360.f;				
			rio.setRotation(angle);
			this.map.postInvalidate();
			
			elapsed = System.currentTimeMillis() - startTime;
		}
		
		running = false;
		handler.sendEmptyMessage(ANIMATION_FINISHED);
		Log.d(TAG, "Stopped animation");
		
	}
	
	private class Animator extends Thread {
		public void run() {
			animate();
		}
	};


}