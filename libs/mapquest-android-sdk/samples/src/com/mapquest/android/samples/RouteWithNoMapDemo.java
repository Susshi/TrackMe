package com.mapquest.android.samples;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;
import com.mapquest.android.maps.ServiceResponse.Info;

/**
 * This is a simple demo on how to use the RouteManger to generate a route and narrative without using a map.
 * 
 */
public class RouteWithNoMapDemo extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_no_map_demo);

		// find the objects we need to interact with
		final WebView itinerary = (WebView) findViewById(R.id.itinerary);

		//grab refs to UI objects we are using
		final Button createRouteButton = (Button) findViewById(R.id.createRouteButton);
		final Button clearButton = (Button) findViewById(R.id.clearButton);
		final EditText start = (EditText) findViewById(R.id.startTextView);
		final EditText end = (EditText) findViewById(R.id.endTextView);

		
		//When not using a mapActivity you can create routes by passing in 
		//your api key to the RouteManager
		String apiKey=this.getString(R.string.api_key);
		final RouteManager routeManager = new RouteManager(this,apiKey);
		routeManager.setItineraryView(itinerary);
		routeManager.setRouteCallback(new RouteManager.RouteCallback() {
			@Override
			public void onError(RouteResponse routeResponse) {
				//got an error, show a toast with the error code and messages
				Info info = routeResponse.info;
				Toast.makeText(
				        getApplicationContext(),
						info.messages.toString(), Toast.LENGTH_LONG).show();
				createRouteButton.setEnabled(true);
			}

			@Override
			public void onSuccess(RouteResponse routeResponse) {
				//We got a route, lets get the UI setup
				clearButton.setVisibility(View.VISIBLE);
				createRouteButton.setEnabled(true);
				itinerary.setVisibility(View.VISIBLE);
			}
		});
		
		// attach the clear route listener
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				routeManager.clearRoute();
				clearButton.setVisibility(View.GONE);
				itinerary.setVisibility(View.GONE);
			}
		});

		// create an onclick listener for the instructional text
		createRouteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				hideSoftKeyboard(view);
				createRouteButton.setEnabled(false);

				//get the start and end locations
				String startAt = getText(start);
				String endAt = getText(end);

				//create the route
				routeManager.createRoute(startAt, endAt);
			}
		});
		
	}
	
	/**
	 * Utility method for getting the text of an EditText, if no text was entered the hint is returned
	 * @param editText
	 * @return
	 */
	private String getText(EditText editText){
		String s = editText.getText().toString();
		if("".equals(s)) s=editText.getHint().toString();
		return s;
	}
	
	/**
	 * Hides the softkeyboard
	 * @param v
	 */
	private void hideSoftKeyboard(View v){
		//hides soft keyboard
		final InputMethodManager imm = (InputMethodManager)getSystemService(
				Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
}
