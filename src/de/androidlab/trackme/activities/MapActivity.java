package de.androidlab.trackme.activities;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import de.androidlab.trackme.map.RouteListEntry;
import de.androidlab.trackme.map.tasks.UpdateLegendTask;
import de.androidlab.trackme.map.tasks.UpdateRoutesTask;

public class MapActivity extends com.google.android.maps.MapActivity implements DatabaseListener {
	
	// TODO Testdaten entfernen
	private static Vector<Pair<String, GeoPoint[]>> testInput1 = new Vector<Pair<String, GeoPoint[]>>();
	private static Vector<Pair<String, GeoPoint[]>> testInput2 = new Vector<Pair<String, GeoPoint[]>>();
	private static Vector<Pair<String, GeoPoint[]>> testInput3 = new Vector<Pair<String, GeoPoint[]>>();
	private static int testCounter = 3;
	static {
		testInput1.add(new Pair<String, GeoPoint[]>("ddfe163345d338193ac2bdc183f8e9dcff904b43", // Hash of 01
		                                            new GeoPoint[]{new GeoPoint((int) (51.206883*1E6),(int) (10.319823*1E6)),
		                                                           new GeoPoint((int) (55.028022*1E6), (int) (-2.907716*1E6))}));
        testInput1.add(new Pair<String, GeoPoint[]>("bcac9d1d8eab3713ae489224d0130c9468e7a0e3", // Hash of 02
                                                    new GeoPoint[]{new GeoPoint((int) (53.225768*1E6),(int) (-8.093263*1E6)),
                                                                   new GeoPoint((int) (55.429013*1E6), (int) (9.265136*1E6))}));
        testInput1.add(new Pair<String, GeoPoint[]>("3ea6c91e241f256e5e3a88ebd647372022323a53", // Hash of 03
                                                    new GeoPoint[]{new GeoPoint((int) (46.589069*1E6),(int) (2.365722*1E6)),
                                                                   new GeoPoint((int) (39.605688*1E6), (int) (-3.610841*1E6))}));
        
        testInput2.add(new Pair<String, GeoPoint[]>("ddfe163345d338193ac2bdc183f8e9dcff904b43", // Hash of 01
                                                    new GeoPoint[]{new GeoPoint((int) (51.206883*1E6),(int) (10.319823*1E6)),
                                                                   new GeoPoint((int) (55.028022*1E6), (int) (-2.907716*1E6)),
                                                                   new GeoPoint((int) (60.028022*1E6), (int) (-7.907716*1E6)),}));
        testInput2.add(new Pair<String, GeoPoint[]>("3ea6c91e241f256e5e3a88ebd647372022323a53", // Hash of 03
                                                    new GeoPoint[]{new GeoPoint((int) (53.225768*1E6),(int) (-8.093263*1E6)),
                                                                   new GeoPoint((int) (55.429013*1E6), (int) (9.265136*1E6))}));
        testInput2.add(new Pair<String, GeoPoint[]>("798f861ee74f6ff83ccbc9c53b419941d0080e50", // Hash of 04
                                                    new GeoPoint[]{new GeoPoint((int) (46.589069*1E6),(int) (2.365722*1E6)),
                                                                   new GeoPoint((int) (39.605688*1E6), (int) (-3.610841*1E6)),
                                                                   new GeoPoint((int) (35.605688*1E6), (int) (3.610841*1E6))}));
	}
	// TODO Testdaten Ende
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

        // Home Button Events
        Button homeBtn = (Button)findViewById(R.id.mapview_btn_home);             
        homeBtn.setOnClickListener(new HomeButtonListener(this));
        
        // Back Button Events
        Button backBtn = (Button)findViewById(R.id.mapview_btn_back);             
        backBtn.setOnClickListener(new BackButtonListener(this));
        
        // Refresh Button
        setupRefreshButton(this);
       
        // Force update Button
        setupForceUpdateButton(this);
        
        // Edit defaults Button
        setupEditDefaultsButton();
        
        // Toggle Buttons
        setupToggleLegendButton();
        setupToggleSettingsButton();
        
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
        
        // Restore old state
        restoreOldData();
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
            ((RadioButton)findViewById(MapData.defaultSetting)).performClick();
            break;   
        }
        
        if (MapData.traffic == true) {
            ((ToggleButton)findViewById(R.id.mapview_checkbox_traffic)).setChecked(true);
            map.setTraffic(true);
        }
        if (MapData.satellite == true) {
            ((ToggleButton)findViewById(R.id.mapview_checkbox_satellite)).setChecked(true);
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
    
    private void setupToggleLegendButton() {
        ToggleButton legendBtn = (ToggleButton)findViewById(R.id.mapview_btn_legend);
        legendBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    findViewById(R.id.mapview_legend).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.mapview_legend).setVisibility(View.GONE);
                }
            }
        }); 
    }
    
    private void setupToggleSettingsButton() {
        ToggleButton settingsBtn = (ToggleButton)findViewById(R.id.mapview_btn_settings);
        settingsBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    findViewById(R.id.mapview_settings).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.mapview_settings).setVisibility(View.GONE);
                }
            }
        }); 
    }

    private void setupRefreshButton(MapActivity mapActivity) {
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
        Button refreshBtn = (Button)findViewById(R.id.mapview_btn_refresh);
        refreshBtn.setOnClickListener(new RefreshListener(mapActivity));
    }
    
    private void setupMapLegend() {
        legend = (ListView)findViewById(R.id.mapview_legend_list);
        new UpdateLegendTask(this, map, legend).execute(MapData.data);
    }
    
    private void setupMapView() {
        this.map = (MapView) findViewById(R.id.mapview_view_map);
        map.setBuiltInZoomControls(true);
        map.getController().setCenter(new GeoPoint((int) (51.6998*1E6), (int) (5.485839*1E6))); // TODO remove after testing
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
