package com.mapquest.android.samples;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;
import com.mapquest.android.maps.ServiceResponse.Info;

/**
 * This demo is to give you and idea on how to implement customized route itinerary
 */
public class RouteItineraryDemo extends SimpleMap{
	
	@Override
	protected int getLayoutId() {
		return R.layout.custom_route_itinerary_demo;
	}

	@Override
	protected void init() {
		super.init();

		//find the objects we need to interact with
		final MapView mapView = (MapView)findViewById(R.id.map);
		final RelativeLayout mapLayout=(RelativeLayout)findViewById(R.id.mapLayout);
		final RelativeLayout itineraryLayout=(RelativeLayout)findViewById(R.id.itineraryLayout);
		final Button createRouteButton=(Button)findViewById(R.id.createRouteButton);
		final Button showItineraryButton=(Button)findViewById(R.id.showItineraryButton);
		final Button showMapButton=(Button)findViewById(R.id.showMapButton);
		final Button clearButton=(Button)findViewById(R.id.clearButton);
		final EditText start=(EditText)findViewById(R.id.startTextView);
		final EditText end=(EditText)findViewById(R.id.endTextView);
		final RouteItineraryView itinerary=(RouteItineraryView)findViewById(R.id.itinerary);
		
		
		final RouteManager routeManager=new RouteManager(this);
		routeManager.setMapView(mapView);
		routeManager.setDebug(true);
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
				itinerary.setRouteResponse(routeResponse);
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
				hideSoftKeyboard(view);
				createRouteButton.setEnabled(false);
				
				String startAt=getText(start);
				String endAt=getText(end);

				routeManager.createRoute(startAt, endAt);
				
			}
		});

	}
	
	

}
