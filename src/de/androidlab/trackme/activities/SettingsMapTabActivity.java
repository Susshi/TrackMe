package de.androidlab.trackme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import de.androidlab.trackme.R;
import de.androidlab.trackme.data.MapData;

public class SettingsMapTabActivity extends Activity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_map_tab);
        
        setupShowAll();
        setupShowFriends();
        setupFollow();
        setupShowLegend();
        setupUpdate();
        setupTraffic();
        setupSatellite();
        setupAntialiasing();
        setupStrokeWidth();
    }

    private void setupStrokeWidth() {
        EditText editStroke = (EditText)findViewById(R.id.settings_map_edit_strokewidth);
        editStroke.setText(String.valueOf(MapData.defaultStrokeWidth));
        editStroke.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                MapData.defaultStrokeWidth = Integer.parseInt(((EditText)v).getText().toString());
                return false;
            }
        });
    }

    private void setupAntialiasing() {
        CheckBox AABox = (CheckBox)findViewById(R.id.settings_map_checkbox_antialiasing);
        AABox.setChecked(MapData.defaultAntialiasing);
        AABox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapData.defaultAntialiasing = isChecked;
            }
        });     
    }

    private void setupSatellite() {
        CheckBox satBox = (CheckBox)findViewById(R.id.settings_map_checkbox_satellite);
        satBox.setChecked(MapData.defaultSatellite);
        satBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapData.defaultSatellite = isChecked;
            }
        });       
    }

    private void setupTraffic() {
        CheckBox trafficBox = (CheckBox)findViewById(R.id.settings_map_checkbox_traffic);
        trafficBox.setChecked(MapData.defaultTraffic);
        trafficBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapData.defaultTraffic = isChecked;
            }
        });
    }

    private void setupUpdate() {
        CheckBox updateBox = (CheckBox)findViewById(R.id.settings_map_checkbox_update);
        updateBox.setChecked(MapData.defaultUpdate);
        updateBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapData.defaultUpdate = isChecked;
            }
        });
    }

    private void setupFollow() {
        CheckBox followBox = (CheckBox)findViewById(R.id.settings_map_checkbox_follow);
        followBox.setChecked(MapData.defaultFollow);
        followBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapData.defaultFollow = isChecked;
            }
        });
    }
    
    private void setupShowLegend() {
        CheckBox legendBox = (CheckBox)findViewById(R.id.settings_map_checkbox_legend);
        legendBox.setChecked(MapData.defaultShowLegend);
        legendBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapData.defaultShowLegend = isChecked;
            }
        });
    }

    private void setupShowFriends() {
        RadioButton friendsBtn = (RadioButton)findViewById(R.id.settings_map_radio_friends);
        if (MapData.defaultSetting == R.id.mapview_radio_friends) {
            friendsBtn.setChecked(true);
        }
        friendsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapData.defaultSetting = R.id.mapview_radio_friends;
            }
        });
    }

    private void setupShowAll() {
        RadioButton allBtn = (RadioButton)findViewById(R.id.settings_map_radio_all);
        if (MapData.defaultSetting == R.id.mapview_radio_all) {
            allBtn.setChecked(true);
        }
        allBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapData.defaultSetting = R.id.mapview_radio_all;
            }
        });
    }
    
    protected void onResume() {
    	super.onResume();
        EditText editStroke = (EditText)findViewById(R.id.settings_map_edit_strokewidth);
        editStroke.setText(String.valueOf(MapData.defaultStrokeWidth));
        editStroke.setSelection(editStroke.getText().length());
        CheckBox AABox = (CheckBox)findViewById(R.id.settings_map_checkbox_antialiasing);
        AABox.setChecked(MapData.defaultAntialiasing);
        CheckBox satBox = (CheckBox)findViewById(R.id.settings_map_checkbox_satellite);
        satBox.setChecked(MapData.defaultSatellite);
        CheckBox trafficBox = (CheckBox)findViewById(R.id.settings_map_checkbox_traffic);
        trafficBox.setChecked(MapData.defaultTraffic);
        CheckBox updateBox = (CheckBox)findViewById(R.id.settings_map_checkbox_update);
        updateBox.setChecked(MapData.defaultUpdate);
        CheckBox followBox = (CheckBox)findViewById(R.id.settings_map_checkbox_follow);
        followBox.setChecked(MapData.defaultFollow);
        CheckBox legendBox = (CheckBox)findViewById(R.id.settings_map_checkbox_legend);
        legendBox.setChecked(MapData.defaultShowLegend);
        RadioButton friendsBtn = (RadioButton)findViewById(R.id.settings_map_radio_friends);
        if (MapData.defaultSetting == R.id.mapview_radio_friends) {
            friendsBtn.setChecked(true);
        }
        RadioButton allBtn = (RadioButton)findViewById(R.id.settings_map_radio_all);
        if (MapData.defaultSetting == R.id.mapview_radio_all) {
            allBtn.setChecked(true);
        }
    }
    
    protected void onPause() {
        super.onPause();
        MapData.storeInPreferences(getSharedPreferences("TrackMeActivity", 0).edit());
    }
}
