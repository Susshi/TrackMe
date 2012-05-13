package de.androidlab.trackme.activities;

import de.androidlab.trackme.R;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class SettingsPrivacyTabActivity extends Activity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_privacy_tab);
        
        // Home Button Events
        Button homeBtnPrivacy = (Button)findViewById(R.id.settings_privacy_btn_home);             
        homeBtnPrivacy.setOnClickListener(new HomeButtonListener(this));
        // Back Button Events
        Button backBtnPrivacy = (Button)findViewById(R.id.settings_privacy_btn_back);             
        backBtnPrivacy.setOnClickListener(new BackButtonListener(this));
    }
}
