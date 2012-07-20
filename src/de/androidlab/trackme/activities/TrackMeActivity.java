package de.androidlab.trackme.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import de.androidlab.trackme.R;
import de.androidlab.trackme.data.MapData;
import de.androidlab.trackme.data.SettingsData;
import de.androidlab.trackme.db.LocationDatabase;
import de.androidlab.trackme.dtn.LocalDTNClient;
import de.androidlab.trackme.listeners.TrackMeLocationListener;

public class TrackMeActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tMgr.getLine1Number();
		boolean makeClickable = true;
		if(phoneNumber == null || phoneNumber.isEmpty())
		{	
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("You have to have your own phone number set to use this App. Please insert a SIM-Card and start again! Otherwise this App will be useless for you!")
						.setCancelable(false)
						.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
			AlertDialog alert = builder.create();
			alert.show();		
			makeClickable = true;
			return;
		}
        
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
        
        if(!makeClickable)
        {
        	mapBtn.setClickable(false);
        	creditsBtn.setClickable(false);
        	settingsBtn.setClickable(false);
        }

        MapData.init(this, getSharedPreferences("TrackMeActivity", 0));
        SettingsData.init(getSharedPreferences("TrackMeActivity", 0));
        
        // Setup / open database
        db.init(getBaseContext());
        
        // DTN starten - nur wenn nicht bereits passiert
        dtnclient = new LocalDTNClient(db);
        dtnclient.init(getApplicationContext(), "de.androidlab.trackme");
        
        // Setup location callbacks
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        TrackMeLocationListener locationListener = new TrackMeLocationListener(db, getBaseContext());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, SettingsData.default_location_update_min_time * 1000, SettingsData.default_location_update_min_distance, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, SettingsData.default_location_update_min_time * 1000, SettingsData.default_location_update_min_distance, locationListener);

        // bereits gestartet
        initialized = true;
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if(dtnclient != null) dtnclient.close(this);
    	if(db != null) db.close();
    }
    
    public LocalDTNClient dtnclient;
    public static LocationDatabase db = new LocationDatabase();
    public static boolean initialized = false;
}