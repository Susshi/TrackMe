package de.androidlab.trackme.activities;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import de.androidlab.trackme.map.ColorGenerator;
import de.androidlab.trackme.map.ContactInfo;
import de.androidlab.trackme.map.LineOverlay;
import de.androidlab.trackme.map.RouteListEntry;
import de.androidlab.trackme.R;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ZoomControls;

public class MapActivity extends com.google.android.maps.MapActivity {
	
	// TODO Testdaten entfernen
	private static Vector<Pair<String, GeoPoint[]>> testInput1 = new Vector<Pair<String, GeoPoint[]>>();
	private static Vector<Pair<String, GeoPoint[]>> testInput2 = new Vector<Pair<String, GeoPoint[]>>();
	private static Vector<Pair<String, GeoPoint[]>> testInput3 = new Vector<Pair<String, GeoPoint[]>>();
	private static int testCounter = 3;
	static {
	    // Deutschland nach England
		testInput1.add(new Pair<String, GeoPoint[]>("01601111111", 
		                                            new GeoPoint[]{new GeoPoint((int) (51.206883*1E6),(int) (10.319823*1E6)),
		                                                           new GeoPoint((int) (55.028022*1E6), (int) (-2.907716*1E6))}));
		// Irland nach Dänemark
        testInput2.add(new Pair<String, GeoPoint[]>("01602222222", 
                                                    new GeoPoint[]{new GeoPoint((int) (53.225768*1E6),(int) (-8.093263*1E6)),
                                                                   new GeoPoint((int) (55.429013*1E6), (int) (9.265136*1E6))}));
        // Frankreich nach Spanien
        testInput3.add(new Pair<String, GeoPoint[]>("01603333333", 
                                                    new GeoPoint[]{new GeoPoint((int) (46.589069*1E6),(int) (2.365722*1E6)),
                                                                   new GeoPoint((int) (39.605688*1E6), (int) (-3.610841*1E6))}));
	}
	// TODO Testdaten Ende
    private final int SHOWROUTESREQUEST = 0;
    private CompoundButton lastActive;
    private MapView map;
    private MyLocationOverlay myLocationOverlay;
    private static ColorGenerator colorGenerator = new ColorGenerator(10);
    public static List<RouteListEntry> data = new ArrayList<RouteListEntry>();
    private List<RouteListEntry> toRemove = new LinkedList<RouteListEntry>();
    private List<RouteListEntry> toAdd = new LinkedList<RouteListEntry>();
    private boolean updateDisabled = false;
    
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
    
    private void update() {
        updateData();
        updateRoutes();
    }
    
    private void updateData() {
        // TODO call correct function instead of testdata
        Vector<Pair<String, GeoPoint[]>> newData = null;
        switch(testCounter++%3) {
        case 0: newData = testInput1;
                break;
        case 1: newData = testInput2;
                break;
        case 2: newData = testInput3;
                break;
        }
        toAdd.clear();
        toRemove.clear();
        List<RouteListEntry> visited = new LinkedList<RouteListEntry>();
        for (Pair<String, GeoPoint[]> input : newData) {
            //Search for an existing entry for this ID
            boolean newDataEntry = true;
            for (RouteListEntry e : data) {
                if (e.id.equals(input.first)) {
                    newDataEntry = false;
                    e.coords = input.second;
                    visited.add(e);
                    break;
                    
                }
            }
            if (newDataEntry == true) {
                // Not found -> Search for this ID in the users phone book
                ContactInfo contact = new ContactInfo(this, input.first);
                RouteListEntry e = new RouteListEntry(contact, colorGenerator.getNewColor(), false, input.second);
                toAdd.add(e);
            }
            // Remove those entries which were not visited since these are old
            for (RouteListEntry e : data) {
                if (visited.contains(e) == false) {
                    toRemove.add(e);
                }
            }
        }
    }
   
    private void updateRoutes() {
        for (RouteListEntry e : data) {
            if (e.isChecked == true) {
                drawRoute(e);
            } else {
                removeRoute(e);
            }
        }
        for (RouteListEntry e : toRemove) {
            removeRoute(e);
        }
        data.removeAll(toRemove);
        toRemove.clear();
        data.addAll(toAdd);
        toAdd.clear();      
        map.invalidate();
    }

    private void drawRoute(RouteListEntry e) {
        removeRoute(e);
        Paint paint = new Paint();
        paint.setColor(e.color);
        paint.setStrokeWidth(3); // TODO Define in Settings
        paint.setAntiAlias(true); // TODO Define in settings
        LineOverlay line = new LineOverlay(e.coords, paint, map.getProjection());
        map.getOverlays().add(line);
        e.line = line;
    }
    
    private void removeRoute(RouteListEntry e) {
        if (e.line != null) {
            map.getOverlays().remove(e.line);
            e.line = null;  
        }
    }

    public void onStart() {
        super.onStart();
        if (!updateDisabled) {
            update();
        } else {
            updateDisabled = false;
        }
    }
    
    @Override
    public void onResume() {
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
                updateDisabled = true;
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
                for (RouteListEntry e : data) {
                    if (e.isFriend == true) {
                        e.isChecked = true;
                    } else {
                        e.isChecked = false;
                    }
                }
                updateRoutes();
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
                for (RouteListEntry e : data) {
                    e.isChecked = true;
                }
                updateRoutes();
            }
        });
    }
    
    private void setupMapView() {
        this.map = (MapView) findViewById(R.id.mapview_view_map);
        map.setBuiltInZoomControls(true);
        map.getController().setCenter(new GeoPoint((int) (51.6998*1E6), (int) (5.485839*1E6)));
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
                                    updateRoutes();
                                    lastActive = (RadioButton)findViewById(R.id.mapview_radio_custom);
                                } else {
                                    lastActive.setChecked(true);
                                }
                                break;
        }
    }

}
