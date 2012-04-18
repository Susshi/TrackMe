package com.mapquest.android.samples;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;

/**
 * Class to demonstrate map events
 *
 */
public class MapEventDemo extends MapActivity{
    
	protected MapView map; 
	protected LinearLayout actions;
	protected ScrollView scroller;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        
        init();
    }
    
    protected void init() {
    	this.setupMapView(new GeoPoint(38.0,-104.0), 5);
    	this.actions=(LinearLayout)findViewById(R.id.actions);
    	this.scroller=(ScrollView)findViewById(R.id.scroller);
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
		
		map.addMapViewEventListener(new MapView.MapViewEventListener() {
			
			@Override
			public void zoomStart(MapView mapView) {
				addEventText("zoomStart");
			}
			
			@Override
			public void zoomEnd(MapView mapView) {
				addEventText("zoomEnd");
			}
			
			@Override
			public void touch(MapView mapView) {
				addEventText("touch");
			}
			
			@Override
			public void moveStart(MapView mapView) {
				addEventText("moveStart");
			}
			
			@Override
			public void moveEnd(MapView mapView) {
				addEventText("moveEnd");
			}
			
			@Override
			public void move(MapView mapView) {
				addEventText("move");
			}
			
			@Override
			public void longTouch(MapView mapView) {
				addEventText("longTouch");
			}
			
			@Override
			public void mapLoaded(MapView mapView) {
				addEventText("mapLoaded");
			}
		});
		
	}
	
	private void addEventText(String text){
		TextView tv = new TextView(MapEventDemo.this);
		tv.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		tv.setText(text);
		actions.addView(tv);
		scroller.fullScroll(ScrollView.FOCUS_DOWN);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				scroller.fullScroll(ScrollView.FOCUS_DOWN);		
			}
		},50);
		
	}

	protected int getLayoutId() {
	    return R.layout.map_events;
	}

	@Override
	public boolean isRouteDisplayed() {
		return false;
	}
	
	/**
	 * Utility method for getting the text of an EditText, if no text was entered the hint is returned
	 * @param editText
	 * @return
	 */
	public String getText(EditText editText){
		String s = editText.getText().toString();
		if("".equals(s)) s=editText.getHint().toString();
		return s;
	}
	
	/**
	 * Hides the softkeyboard
	 * @param v
	 */
	public void hideSoftKeyboard(View v){
		//hides soft keyboard
		final InputMethodManager imm = (InputMethodManager)getSystemService(
				Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
		
	
}