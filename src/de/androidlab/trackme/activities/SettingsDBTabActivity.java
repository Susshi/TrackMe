package de.androidlab.trackme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import de.androidlab.trackme.R;
import de.androidlab.trackme.data.MapData;
import de.androidlab.trackme.data.SettingsData;

public class SettingsDBTabActivity extends Activity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_db_tab);
        
        setupUpdateTime();
        setupUpdateDistance();
        setupRefreshTime();
        setupLifetime();
    }
    
    private void setupLifetime() {
        EditText editLifetime = (EditText)findViewById(R.id.settings_db_route_ttl);
        editLifetime.setText(String.valueOf(SettingsData.default_route_lifetime));
        editLifetime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            	SettingsData.default_route_lifetime = Long.parseLong(((EditText)v).getText().toString());
                return false;
            }
        });
	}

	private void setupUpdateTime() {
        EditText editUpdateIntervall = (EditText)findViewById(R.id.settings_db_update_intervall);
        editUpdateIntervall.setText(String.valueOf(SettingsData.default_location_update_min_time));
        editUpdateIntervall.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            	SettingsData.default_location_update_min_time = Long.parseLong(((EditText)v).getText().toString());
                return false;
            }
        });
	}

	private void setupUpdateDistance() {
        EditText editUpdateDistance = (EditText)findViewById(R.id.settings_db_min_distance);
        editUpdateDistance.setText(String.valueOf(SettingsData.default_location_update_min_distance));
        editUpdateDistance.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            	SettingsData.default_location_update_min_distance = Long.parseLong(((EditText)v).getText().toString());
                return false;
            }
        });
	}

	private void setupRefreshTime() {
        EditText editRefreshTime = (EditText)findViewById(R.id.settings_db_max_time);
        editRefreshTime.setText(String.valueOf(SettingsData.default_max_refresh_time));
        editRefreshTime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            	SettingsData.default_max_refresh_time = Long.parseLong(((EditText)v).getText().toString());
                return false;
            }
        });
	}
    
    protected void onResume() {
    	super.onResume();
        EditText editLifetime = (EditText)findViewById(R.id.settings_db_route_ttl);
        editLifetime.setText(String.valueOf(SettingsData.default_route_lifetime));
        editLifetime.setSelection(editLifetime.getText().length());
        EditText editUpdateIntervall = (EditText)findViewById(R.id.settings_db_update_intervall);
        editUpdateIntervall.setText(String.valueOf(SettingsData.default_location_update_min_time));
        editUpdateIntervall.setSelection(editUpdateIntervall.getText().length());
        EditText editUpdateDistance = (EditText)findViewById(R.id.settings_db_min_distance);
        editUpdateDistance.setText(String.valueOf(SettingsData.default_location_update_min_distance));
        editUpdateDistance.setSelection(editUpdateDistance.getText().length());
        EditText editRefreshTime = (EditText)findViewById(R.id.settings_db_max_time);
        editRefreshTime.setText(String.valueOf(SettingsData.default_max_refresh_time));
        editRefreshTime.setSelection(editRefreshTime.getText().length());
    }
    
    protected void onPause() {
        super.onPause();
        SettingsData.storeInPreferences(getSharedPreferences("TrackMeActivity", 0).edit());
    }
}
