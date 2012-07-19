package de.androidlab.trackme.activities;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import de.androidlab.trackme.R;
import de.androidlab.trackme.data.ContactInfo;
import de.androidlab.trackme.data.MapData;
import de.androidlab.trackme.interfaces.DatabaseListener;
import de.androidlab.trackme.map.RouteListEntry;
import de.androidlab.trackme.map.tasks.UpdateLegendTask;
import de.androidlab.trackme.map.tasks.UpdateRoutesTask;

public class MapActivity extends com.google.android.maps.MapActivity implements DatabaseListener {
	
    private final int SHOWROUTESREQUEST = 0;
    private MapView map;
    private MyLocationOverlay myLocationOverlay;
    private List<RouteListEntry> toRemove = new LinkedList<RouteListEntry>();
    private List<RouteListEntry> toAdd = new LinkedList<RouteListEntry>();
    private ListView legend;
    private int settingsVisibility = View.GONE;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);
       
        // Force update Button
        setupForceUpdateButton(this);
        
        // Edit defaults Button
        setupEditDefaultsButton();
        
        // Radio Buttons
        setupCustomRadioButton();
        setupFriendsRadioButton();
        setupAllRadioButton();
        
        // Checkboxes
        setupFollowCheckbox();
        setupLegendCheckbox();
        setupTrafficCheckbox();
        setupSatelliteCheckbox();
        
        // Map
        setupMapView();
        setupMyLocation();
        setupMapLegend();
    }

    private void restoreOldData() {
        switch(MapData.lastActive) {
        case R.id.mapview_radio_all:     
            ((RadioButton)findViewById(R.id.mapview_radio_all)).performClick();
            break;
                                         
        case R.id.mapview_radio_friends: 
            ((RadioButton)findViewById(R.id.mapview_radio_friends)).performClick();
            break;
                                         
        case R.id.mapview_radio_custom:  
            ((RadioButton)findViewById(R.id.mapview_radio_custom)).setChecked(true);
            break;
                                         
        default: 
        	try {
        		((RadioButton)findViewById(MapData.defaultSetting)).performClick();
        	} catch(ClassCastException e) {
        		Log.e("MapActivity", "Default Setting for \"Show\" failed!");
        	}
            break;   
        }
        
        if (MapData.traffic == true) {
            ((CheckBox)findViewById(R.id.mapview_checkbox_traffic)).setChecked(true);
            map.setTraffic(true);
        }
        if (MapData.satellite == true) {
            ((CheckBox)findViewById(R.id.mapview_checkbox_satellite)).setChecked(true);
            map.setSatellite(true);
        }
        if (MapData.followActive == true) {
            ((CheckBox)findViewById(R.id.mapview_checkbox_follow)).setChecked(true);
        }
        if (MapData.showLegend == true) {
            ((CheckBox)findViewById(R.id.mapview_checkbox_legend)).setChecked(true);
        }
    }
    
    private void update(boolean byAuto, Vector<Pair<String, GeoPoint[]>> newData) {
        class UpdateThread extends Thread {
            private Vector<Pair<String, GeoPoint[]>> data;
            public UpdateThread(Vector<Pair<String, GeoPoint[]>> data) {
                this.data = data;
            }
            public void run() {
                updateData(data);
                runOnUiThread(new Runnable() {
                    public void run() {
                        updateDisplay();
                    }
                });
            }
        };
        new UpdateThread(newData).start();      
    }
    
    private void updateDisplay() {
        new UpdateRoutesTask(MapActivity.this,
                             map, 
                             MapData.defaultStrokeWidth, 
                             MapData.defaultAntialiasing)
            .execute(toRemove, toAdd, MapData.data);
        new UpdateLegendTask(MapActivity.this, map, legend)
            .execute(MapData.data); 
    }
    
    private void updateData(Vector<Pair<String, GeoPoint[]>> newData) {
        Log.d("GUI", "Updating " + newData.size() + " entries");
        int[] points = new int[newData.size()];
        int i = 0;
        for (Pair<String, GeoPoint[]> p : newData) {
        	points[i++] = p.second.length;
        }
        Log.d("GUI", "Entries have " + Arrays.toString(points) + " points");
    	toAdd.clear();
        toRemove.clear();
        List<RouteListEntry> visited = new LinkedList<RouteListEntry>();
        for (Pair<String, GeoPoint[]> input : newData) {
            //Search for an existing entry for this ID
            boolean newDataEntry = true;
            for (RouteListEntry e : MapData.data) {
                if (e.ids.contains(input.first)) {
                    newDataEntry = false;
                    e.coords = input.second;
                    visited.add(e);
                    break;
                }
            }
            if (newDataEntry == true) {
                // Not found -> Search for this ID in HashedIdentification
                ContactInfo contact = MapData.contacts.getContactInfo(input.first);
                boolean checked = false;
                boolean isFriend = true;
                if (MapData.lastActive == R.id.mapview_radio_all
                    || (MapData.lastActive == R.id.mapview_radio_friends
                       && contact != null)) {
                    checked = true;
                }
                if (contact == null) {
                    contact = MapData.contacts.getNewAnonymousContact(input.first);
                    isFriend = false;
                }
                RouteListEntry e = new RouteListEntry(contact,
                                                      MapData.colorGenerator.getNewColor(),
                                                      isFriend,
                                                      checked,
                                                      input.second);
                toAdd.add(e);
            }
        }
        
        // Remove those entries which were not visited since these are old
        for (RouteListEntry e : MapData.data) {
            if (visited.contains(e) == false) {
                toRemove.add(e);
            }
        }
    }
    
    @Override
    public void onResume() {
      super.onResume();
      TrackMeActivity.db.registerDatabaseListener(this);
      if (MapData.followActive == true) {
          myLocationOverlay.enableMyLocation();
          myLocationOverlay.enableCompass();
      }
      // Restore old state
      restoreOldData();
    }
    
    @Override
    protected void onPause() {
      super.onPause();
      TrackMeActivity.db.unregisterDatabaseListener(this);
      if (MapData.followActive == true) {
          myLocationOverlay.disableCompass();
          myLocationOverlay.disableMyLocation();
      }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return true;
    }
    
    private void setupForceUpdateButton(MapActivity mapActivity) {
        class RefreshListener implements View.OnClickListener {
            private MapActivity mapActivity;    
            public RefreshListener(MapActivity mapActivity) {
                this.mapActivity = mapActivity;
            }
            public void onClick(View v) {
                boolean updateState = MapData.defaultUpdate;
                MapData.defaultUpdate = true;
                TrackMeActivity.db.registerDatabaseListener(mapActivity);
                MapData.defaultUpdate = updateState;
            }
        }
        Button updateBtn = (Button)findViewById(R.id.mapview_btn_update);
        updateBtn.setOnClickListener(new RefreshListener(mapActivity));
    }
    
    private void setupEditDefaultsButton() {
        Button editBtn = (Button) findViewById(R.id.mapview_btn_editdefault);
        editBtn.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               startActivity(new Intent(MapActivity.this, SettingsMapTabActivity.class));
           } 
        });
    }
    
    private void setupCustomRadioButton() {
        RadioButton customBtn = (RadioButton)findViewById(R.id.mapview_radio_custom);
        customBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(MapActivity.this,
                                                  RouteListActivity.class),
                                                  SHOWROUTESREQUEST);
            }
        });    
    }

    private void setupFriendsRadioButton() {
        RadioButton friendsBtn = (RadioButton)findViewById(R.id.mapview_radio_friends);
        friendsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapData.lastActive = v.getId();
                for (RouteListEntry e : MapData.data) {
                    if (e.isFriend == true) {
                        e.isChecked = true;
                    } else {
                        e.isChecked = false;
                    }
                }
                updateDisplay();
            }
        });    
    }

    private void setupAllRadioButton() {
        RadioButton allBtn = (RadioButton)findViewById(R.id.mapview_radio_all);
        allBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapData.lastActive = v.getId();
                for (RouteListEntry e : MapData.data) {
                    e.isChecked = true;
                }
                updateDisplay();
            }
        });
    }
    
    private void setupFollowCheckbox() {
        CheckBox followBox = (CheckBox)findViewById(R.id.mapview_checkbox_follow);    
        followBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapData.followActive = isChecked;
                if (isChecked == true) {
                    myLocationOverlay.enableMyLocation();
                    myLocationOverlay.enableCompass(); 
                } else {
                    myLocationOverlay.disableMyLocation();
                    myLocationOverlay.disableCompass();    
                }
            }
        });
    }
    
    private void setupLegendCheckbox() {
        CheckBox legendBox = (CheckBox)findViewById(R.id.mapview_checkbox_legend);    
        legendBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapData.showLegend = isChecked;
                if (isChecked == true) {
                    findViewById(R.id.mapview_legend).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.mapview_legend).setVisibility(View.GONE);
                }
            }
        });
    }
    
    private void setupTrafficCheckbox() {
        CheckBox trafficBox = (CheckBox)findViewById(R.id.mapview_checkbox_traffic);
        trafficBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapData.traffic = isChecked;
                map.setTraffic(isChecked);
            }
        }); 
    }
    
    private void setupSatelliteCheckbox() {
        CheckBox satelliteBox = (CheckBox)findViewById(R.id.mapview_checkbox_satellite);
        satelliteBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapData.satellite = isChecked;
                map.setSatellite(isChecked);
            }
        }); 
    }
    
    private void setupMapLegend() {
        legend = (ListView)findViewById(R.id.mapview_legend_list);
        new UpdateLegendTask(this, map, legend).execute(MapData.data);
    }
    
    private void setupMapView() {
        this.map = (MapView) findViewById(R.id.mapview_view_map);
        map.setBuiltInZoomControls(true);
      }
    
    private void setupMyLocation() {
        this.myLocationOverlay = new MyLocationOverlay(this, map);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
          public void run() {
            GeoPoint currentLocation = myLocationOverlay.getMyLocation();
            map.getController().animateTo(currentLocation);
            map.getOverlays().add(myLocationOverlay);
          }
        });
      }
   
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
        case SHOWROUTESREQUEST: 
            if (resultCode == Activity.RESULT_OK) {
                updateDisplay();
                MapData.lastActive = R.id.mapview_radio_custom;
            } else {
                ((RadioButton)findViewById(MapData.lastActive)).setChecked(true);
            }
            break;
        }
    }

	@Override
	public void onDatabaseChange(Vector<Pair<String, GeoPoint[]>> newData) {
		if (MapData.defaultUpdate == true) {
		    update(true, newData);
		}
	}

	@Override
	public void onDatabaseChangeRaw(Vector<String> data) {

	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	            if (settingsVisibility == View.GONE) {
	                settingsVisibility = View.VISIBLE;
	            } else {
	                settingsVisibility = View.GONE;
	            }
	            findViewById(R.id.mapview_settings).setVisibility(settingsVisibility);
	            return true;
	    }
	    return super.onKeyDown(keycode, e);
	}
       

}
