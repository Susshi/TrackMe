package com.mapquest.android.samples;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;
import com.mapquest.android.maps.ServiceResponse.Info;

/**
 * This demo shows how you can pass different options for your route.  The options passed 
 * are based off UI check boxes for avoiding toll roads and/or highways.  You can pass any
 * of the options defined in the directions service {@link http://www.mapquestapi.com/directions/#advancedrouting}
 * to modify how your route gets generated.
 */
public class RouteOptionsDemo extends SimpleMap{
	
	@Override
	protected int getLayoutId() {
		return R.layout.route_options_demo;
	}

	@Override
	protected void init() {
		super.init();

		//find the objects we need to interact with
		final MapView mapView = (MapView)findViewById(R.id.map);
		final WebView itinerary=(WebView)findViewById(R.id.itinerary);
				
		//grab refs to the UI elements we will be using
		final RelativeLayout mapLayout=(RelativeLayout)findViewById(R.id.mapLayout);
		final RelativeLayout itineraryLayout=(RelativeLayout)findViewById(R.id.itineraryLayout);
		final Button createRouteButton=(Button)findViewById(R.id.createRouteButton);
		final Button showItineraryButton=(Button)findViewById(R.id.showItineraryButton);
		final Button showMapButton=(Button)findViewById(R.id.showMapButton);
		final Button clearButton=(Button)findViewById(R.id.clearButton);
		final EditText start=(EditText)findViewById(R.id.startTextView);
		final EditText end=(EditText)findViewById(R.id.endTextView);
		final CheckBox avoidTollRoads=(CheckBox)findViewById(R.id.avoidTollRoads);
		final CheckBox avoidHighways=(CheckBox)findViewById(R.id.avoidHighways);
		
		//create a routeManager
		final RouteManager routeManager=new RouteManager(this);
		routeManager.setMapView(mapView);
		routeManager.setItineraryView(itinerary);
		routeManager.setRouteCallback(new RouteManager.RouteCallback() {
			@Override
			public void onError(RouteResponse routeResponse) {
				Info info=routeResponse.info;
				int statusCode=info.statusCode;
				
				StringBuilder message =new StringBuilder();
				message.append("Unable to create route.\n")
					.append("Error: ").append(statusCode).append("\n")
					.append("Message: ").append(info.messages);
				Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
				createRouteButton.setEnabled(true);
			}

			@Override
			public void onSuccess(RouteResponse routeResponse) {
				clearButton.setVisibility(View.VISIBLE);
				if(showItineraryButton.getVisibility()==View.GONE &&
						showMapButton.getVisibility()==View.GONE){
					showItineraryButton.setVisibility(View.VISIBLE);
				}
				createRouteButton.setEnabled(true);
			}
		});
		
		//attach the show itinerary listener
		showItineraryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mapLayout.setVisibility(View.GONE);
				itineraryLayout.setVisibility(View.VISIBLE);
				showItineraryButton.setVisibility(View.GONE);
				showMapButton.setVisibility(View.VISIBLE);
			}
		});
		
		//attach the show map listener
		showMapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mapLayout.setVisibility(View.VISIBLE);
				itineraryLayout.setVisibility(View.GONE);
				showMapButton.setVisibility(View.GONE);
				showItineraryButton.setVisibility(View.VISIBLE);
			}
		});

		//attach the clear route listener
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				routeManager.clearRoute();
				clearButton.setVisibility(View.GONE);
				showItineraryButton.setVisibility(View.GONE);
				showMapButton.setVisibility(View.GONE);
				mapLayout.setVisibility(View.VISIBLE);
				itineraryLayout.setVisibility(View.GONE);
			}
		});
		
		
		//create an onclick listener for the instructional text
		createRouteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {	

				createRouteButton.setEnabled(false);
				hideSoftKeyboard(view);
				
				//get the start and end locations
				String startAt = getText(start);
				String endAt = getText(end);
				
				//This is an example of building an options object to send to the 
				//route service.  you can send any valid json that the directions 
				//service accepts.  Have a look at the direction service devloper
				//documentation at http://www.mapquestapi.com/directions/
				try{
					JSONObject options=new JSONObject();
					if(avoidHighways.isChecked() || avoidTollRoads.isChecked()){
						JSONArray avoids=new JSONArray();
						if(avoidTollRoads.isChecked())avoids.put("Toll Road");
						if(avoidHighways.isChecked())avoids.put("Limited Access");
						options.put("avoids",avoids);
					}
					routeManager.setOptions(options.toString());
				}catch (Exception e) {
					//this can be thrown by the JSON lib, but should not ever happen 
					//with this simple usage
				}

				//execute the route
				routeManager.createRoute(startAt, endAt);
			}
		});

	}
	

}
