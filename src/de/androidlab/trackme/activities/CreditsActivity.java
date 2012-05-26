package de.androidlab.trackme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import de.androidlab.trackme.R;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;

public class CreditsActivity extends Activity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);
        
        // Home Button Events
        Button homeBtn = (Button)findViewById(R.id.credits_btn_home);             
        homeBtn.setOnClickListener(new HomeButtonListener(this));
        // Back Button Events
        Button backBtn = (Button)findViewById(R.id.credits_btn_back);             
        backBtn.setOnClickListener(new BackButtonListener(this));
    }
}
