package de.androidlab.trackme.activities;

import de.androidlab.trackme.map.ColorGenerator;
import de.androidlab.trackme.R;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MapActivity extends Activity {
    
    private final static int SHOWROUTESREQUEST = 0;
    private CompoundButton lastActive = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);
        
        // Home Button Events
        Button homeBtn = (Button)findViewById(R.id.mapview_btn_home);             
        homeBtn.setOnClickListener(new HomeButtonListener(this));
        
        // Back Button Events
        Button backBtn = (Button)findViewById(R.id.mapview_btn_back);             
        backBtn.setOnClickListener(new BackButtonListener(this));
        
        // Radiobutton Custom Events
        RadioButton customBtn = (RadioButton)findViewById(R.id.mapview_radio_custom);
        customBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) lastActive = buttonView;
            }
        });
        customBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(MapActivity.this, RouteListActivity.class), SHOWROUTESREQUEST);
			}
        });
        
        // Radiobutton Friends Events
        RadioButton friendsBtn = (RadioButton)findViewById(R.id.mapview_radio_friends);
        friendsBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if (!isChecked) lastActive = buttonView;
            }
        });
        friendsBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			    // TODO
			}
        });
        
        // Radiobutton All Events
        RadioButton allBtn = (RadioButton)findViewById(R.id.mapview_radio_all);
        allBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if (!isChecked) lastActive = buttonView;
            }
        });
        allBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO
			}
        });
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
        case SHOWROUTESREQUEST: if (resultCode == Activity.RESULT_OK) {
                                    processShowRoutesResult(data);
                                    lastActive = (RadioButton)findViewById(R.id.mapview_radio_custom);
                                } else {
                                    lastActive.setChecked(true);
                                }
                                break;
        }
    }

    private void processShowRoutesResult(Intent data) {
        // TODO
    }
}
