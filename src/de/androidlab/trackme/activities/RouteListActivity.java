package de.androidlab.trackme.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import de.androidlab.trackme.R;
import de.androidlab.trackme.data.MapData;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import de.androidlab.trackme.map.RouteListAdapter;
import de.androidlab.trackme.map.RouteListEntry;

public class RouteListActivity extends ListActivity {
    
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
        
        // Initialize List View
        ListView lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Add Data to list
        List<RouteListEntry> sorted = new ArrayList<RouteListEntry>(MapData.data.size());
        List<RouteListEntry> anonymous = new ArrayList<RouteListEntry>(MapData.data.size());
        for (RouteListEntry e : MapData.data) {
            if (e.isFriend == true) {
                sorted.add(e);
            } else {
                anonymous.add(e);
            }
        }
        sorted.addAll(anonymous);
        lv.setAdapter(new RouteListAdapter(this, R.layout.routelist_entry, sorted));
        // Register Listeners
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RouteListEntry entry = (RouteListEntry)parent.getItemAtPosition(position);
                entry.isChecked = !entry.isChecked;
                getListView().setItemChecked(position, entry.isChecked);
                ((CheckBox)view.findViewById(R.id.routelist_entry_checkbox)).setChecked(entry.isChecked);
            }
        });
        
    }
}
