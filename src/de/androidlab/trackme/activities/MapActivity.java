package de.androidlab.trackme.activities;

import de.androidlab.trackme.R;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MapActivity extends Activity {
    
    private final static int SHOWROUTESREQUEST = 0;
    
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
        // List Button Events
        Button listBtn = (Button)findViewById(R.id.mapview_btn_list);             
        listBtn.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View v) {
                startActivityForResult(new Intent(MapActivity.this, RouteListActivity.class), SHOWROUTESREQUEST);
            }
        });
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
        case SHOWROUTESREQUEST: if (resultCode == Activity.RESULT_OK)
                                    processShowRoutesResult(data);
                                break;
        }
    }

    private void processShowRoutesResult(Intent data) {
        // TODO Auto-generated method stub
        Log.d("RouteListActivity", "Test");
    }
}
