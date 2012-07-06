package de.androidlab.trackme.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.androidlab.trackme.R;
import de.androidlab.trackme.data.MapData;
import de.androidlab.trackme.db.LocationDatabase;
import de.androidlab.trackme.listeners.HomeButtonListener;
import de.androidlab.trackme.listeners.TrackMeLocationListener;

public class TrackMeActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Map Button Events
        Button mapBtn = (Button)findViewById(R.id.main_button_map);
        mapBtn.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View v) {
                startActivity(new Intent(TrackMeActivity.this, MapActivity.class));
            }
        });
        
        // Credits Button Events
        Button creditsBtn = (Button)findViewById(R.id.main_button_credits);
        creditsBtn.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View v) {
                startActivity(new Intent(TrackMeActivity.this, CreditsActivity.class));
            }
        });
        
        // Settings Button Events
        Button settingsBtn = (Button)findViewById(R.id.main_button_settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View v) {
                startActivity(new Intent(TrackMeActivity.this, SettingsActivity.class));
            }
        });
        
        // Home Button Events
        Button homeBtn = (Button)findViewById(R.id.main_btn_home);             
        homeBtn.setOnClickListener(new HomeButtonListener(this));
        
        
        MapData.init(this, getSharedPreferences("TrackMeActivity", 0));
        
        // Setup / open database
        db = new LocationDatabase(getBaseContext());
        
        // Setup location callbacks
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        TrackMeLocationListener locationListener = new TrackMeLocationListener(db, getBaseContext());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onDestroy() {
    	db.close();
    }
    
    LocationDatabase db;
}