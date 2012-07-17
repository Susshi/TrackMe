package de.androidlab.trackme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import de.androidlab.trackme.R;
import de.androidlab.trackme.data.MapData;
import de.androidlab.trackme.data.SettingsData;

public class SettingsDTNTabActivity extends Activity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_dtn_tab);
        
        setupDTTL();
        setupPND();
        setupPTTL();
        setupRTT();
    }
    
    private void setupRTT() {
        EditText editRTT = (EditText)findViewById(R.id.settings_dtn_rtt);
        editRTT.setText(String.valueOf(SettingsData.default_retransmission_time));
        editRTT.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                	SettingsData.default_retransmission_time = Integer.parseInt(((EditText)v).getText().toString());
                }
                return false;
            }
        });
    }
    
    private void setupPND() {
        EditText editPND = (EditText)findViewById(R.id.settings_dtn_pnd);
        editPND.setText(String.valueOf(SettingsData.default_presence_notification_delay));
        editPND.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                	SettingsData.default_presence_notification_delay = Integer.parseInt(((EditText)v).getText().toString());
                }
                return false;
            }
        });
    }
    
    private void setupPTTL() {
        EditText editPTTL = (EditText)findViewById(R.id.settings_dtn_pttl);
        editPTTL.setText(String.valueOf(SettingsData.default_presence_ttl));
        editPTTL.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                	SettingsData.default_presence_ttl = Integer.parseInt(((EditText)v).getText().toString());
                }
                return false;
            }
        });
    }
    
    private void setupDTTL() {
        EditText editDTTL = (EditText)findViewById(R.id.settings_dtn_dttl);
        editDTTL.setText(String.valueOf(SettingsData.default_data_ttl));
        editDTTL.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                	SettingsData.default_data_ttl = Integer.parseInt(((EditText)v).getText().toString());
                }
                return false;
            }
        });
    }
    
    protected void onPause() {
        super.onPause();
        SettingsData.storeInPreferences(getSharedPreferences("TrackMeActivity", 0).edit());
    }
}
