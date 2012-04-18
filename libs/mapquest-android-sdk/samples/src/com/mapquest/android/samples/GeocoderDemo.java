
package com.mapquest.android.samples;

import java.util.List;

import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mapquest.android.Geocoder;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.Overlay;

/**
 * Abstract class to demo geocoding and reverse geocoding capabilities
 *
 */
public abstract class GeocoderDemo extends SimpleMap {

    GeocodeTask geocodeTask;

    ReverseGeocodeTask reverseGeocodeTask;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setupMapView(new GeoPoint(39.74, -104.985), 7);

        setupViews();
        setupOverlays();
        
        Toast.makeText(getApplicationContext(), "Tap map to reverse geocode", Toast.LENGTH_SHORT).show();
    }

    /**
     * An abstract method to identify the layout to use.
     * @return
     */
    protected abstract int getLayoutId();
    
    /**
     * Returns the geocoder instance.
     * @return
     */
    protected abstract Geocoder getGeocoder();
    
    /**
     * Sets up view and hooks up event handlers.
     */
    private void setupViews() {
        Button button = (Button) findViewById(R.id.mq_geocode_btn);
        
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                geocode();
            }
        });
        
        EditText mqGeocodeInput = (EditText) findViewById(R.id.mq_geocode_input);
        mqGeocodeInput.setOnKeyListener(new OnKeyListener() {
            
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        
                        geocode();
                        return true;
                    }

                return false;
            }
        });
    }

    /**
     * Add an overlay to reverse geocode on touch event.
     */
    private void setupOverlays() {
        map.getOverlays().add(new ReverseGeocodeOverlay());
    }
    
    /**
     * Execute the geocode task for location input
     */
    private void geocode() {
        if (geocodeTask == null) {
            EditText mqGeocodeInput = (EditText) findViewById(R.id.mq_geocode_input);
            String location = mqGeocodeInput.getText().toString();
            if (location.length() > 0) {
                geocodeTask = new GeocodeTask();
                geocodeTask.execute(location);
            }
        }
    }

    /**
     * An overlay class to handle the reverse geocoding on touch. 
     *
     */
    private class ReverseGeocodeOverlay extends Overlay {

        @Override
        public boolean onTap(GeoPoint p, MapView mapView) {
            if (reverseGeocodeTask == null) {
                reverseGeocodeTask = new ReverseGeocodeTask();
                reverseGeocodeTask.execute(p);
            }
            return false;
        }

    }

    /**
     * Geocode background task
     */
    private class GeocodeTask extends AsyncTask<String, Void, List<Address>> {
        protected List<Address> doInBackground(String... location) {
            try {
                return getGeocoder().getFromLocationName(location[0], 1);
            } catch (Exception e) {
                return null;
            }

        }

        protected void onPostExecute(List<Address> result) {
            if (result == null || result.size() == 0) {
                Toast.makeText(getApplicationContext(), "No match found!", Toast.LENGTH_SHORT).show();
            } else {
                Address address = result.get(0);
                map.getController().setCenter(new GeoPoint(address.getLatitude(), address.getLongitude()));
                geocodeTask = null;
            }
        }
    }
    
    /**
     * ReversesGeocode background task
     */
    private class ReverseGeocodeTask extends AsyncTask<GeoPoint, Void, List<Address>> {
        protected List<Address> doInBackground(GeoPoint... geoPoint) {
            try {
                return getGeocoder().getFromLocation(geoPoint[0].getLatitude(), geoPoint[0].getLongitude(), 1);
            } catch (Exception e) {
                return null;
            }

        }

        protected void onPostExecute(List<Address> result) {
            if (result == null || result.size() == 0) {
                Toast.makeText(getApplicationContext(), "No match found!", Toast.LENGTH_SHORT).show();
                
            } else {
                Address address = result.get(0);
                StringBuilder addressText = new StringBuilder();
                for (int i =0; i < address.getMaxAddressLineIndex(); i++) {
                    addressText.append(address.getAddressLine(i)+"\n");
                }
                Toast.makeText(getApplicationContext(), "Match found :\n" + addressText, Toast.LENGTH_SHORT).show();
                reverseGeocodeTask = null;
            }
        }
    }
}
