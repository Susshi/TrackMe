package de.androidlab.trackme.activities;

import de.androidlab.trackme.R;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class SettingsGeneralTabActivity extends Activity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_general_tab);
            
        // Home Button Events
        Button homeBtnGeneral = (Button)findViewById(R.id.settings_general_btn_home);             
        homeBtnGeneral.setOnClickListener(new HomeButtonListener(this));     
        // Back Button Events
        Button backBtnGeneral = (Button)findViewById(R.id.settings_general_btn_back);             
        backBtnGeneral.setOnClickListener(new BackButtonListener(this));
    }
}
