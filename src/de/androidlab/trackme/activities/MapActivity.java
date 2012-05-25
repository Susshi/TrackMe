package de.androidlab.trackme.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import de.androidlab.trackme.map.ColorGenerator;
import de.androidlab.trackme.map.HelloItemizedOverlay;
import de.androidlab.trackme.map.LineOverlay;
import de.androidlab.trackme.map.RouteListEntry;
import de.androidlab.trackme.R;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ZoomControls;

public class MapActivity extends com.google.android.maps.MapActivity {
    
	// TODO Testdaten entfernen
	public static RouteListEntry[] data = new RouteListEntry[20];
	static {
		ColorGenerator cg = new ColorGenerator(data.length);
		for (int i = 0; i < data.length; i++) {
			data[i] = new RouteListEntry("0", R.drawable.ic_launcher, "" + i, cg.getNewColor(), false, false ,null);
		}
	}
	
    private final static int SHOWROUTESREQUEST = 0;
    private CompoundButton lastActive = null;
    private MapView map;
    private MyLocationOverlay myLocationOverlay;
    
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
        
        setupCustomRadioButton();
        setupFriendsRadioButton();
        setupAllRadioButton();
        
        setupMapView();
        setupMyLocation();
    }
   
//    private void updateRoutes() {
//        for (RouteListEntry e : data) {
//            if (e.isChecked == true) {
//                drawRoute(e);
//            }
//        }  
//    }

//    private void drawRoute(RouteListEntry e) {
//        if (e.coords != null) {
//            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            paint.setColor(e.color);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(3);
//            LineOverlay line = new LineOverlay();
//            line.setData(Arrays.asList(e.coords));
//            map.getOverlays().add(line);
//            e.line = line;
//        }
//    }
    
//    private void removeRoute(RouteListEntry e) {
//        map.getOverlays().remove(e.line);
//        e.line = null;
//    }

    protected void onStart() {
        super.onStart();
        // TODO update the route data
        //updateRoutes();
    }
    
    @Override
    protected void onResume() {
      super.onResume();
      myLocationOverlay.enableMyLocation();
      myLocationOverlay.enableCompass();
    }
    
    @Override
    protected void onPause() {
      super.onPause();
      myLocationOverlay.disableCompass();
      myLocationOverlay.disableMyLocation();
    }

    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return true;
    }
    
    private void setupCustomRadioButton() {
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
    }

    private void setupFriendsRadioButton() {
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
    }

    private void setupAllRadioButton() {
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
    
    private void setupMapView() {
        this.map = (MapView) findViewById(R.id.mapview_view_map);
        map.setBuiltInZoomControls(true);
        map.getController().setCenter(new GeoPoint((int)(38.7217*1E6),(int)(-112.386036*1E6)));
      }
    
    private void setupMyLocation() {
        this.myLocationOverlay = new MyLocationOverlay(this, map);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
          @Override
          public void run() {
            GeoPoint currentLocation = myLocationOverlay.getMyLocation();
            map.getController().animateTo(currentLocation);
            map.getOverlays().add(myLocationOverlay);
          }
        });
      }
   
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
        case SHOWROUTESREQUEST: if (resultCode == Activity.RESULT_OK) {
                                    // TODO process result
                                    lastActive = (RadioButton)findViewById(R.id.mapview_radio_custom);
                                } else {
                                    lastActive.setChecked(true);
                                }
                                break;
        }
    }

}
