package de.androidlab.trackme.activities;

import de.androidlab.trackme.R;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RouteListActivity extends Activity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routelist);
        
        // Home Button Events
        Button homeBtn = (Button)findViewById(R.id.routelist_btn_home);             
        homeBtn.setOnClickListener(new HomeButtonListener(this));
        // Back Button Events
        Button backBtn = (Button)findViewById(R.id.routelist_btn_back);             
        backBtn.setOnClickListener(new BackButtonListener(this));
        // Ok Button Events
        Button okBtn = (Button)findViewById(R.id.routelist_btn_ok);             
        okBtn.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        // Cancel Button Events
        Button cancelBtn = (Button)findViewById(R.id.routelist_btn_cancel);             
        cancelBtn.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}
