package de.androidlab.trackme.activities;

import de.androidlab.trackme.R;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import de.androidlab.trackme.map.RouteListEntry;
import de.androidlab.trackme.map.RouteListAdapter;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

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
                // TODO generate Result
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
        // Fetch Data to show
        RouteListEntry[] data = fetchData();
        // Add Data to list
        lv.setAdapter(new RouteListAdapter(this, R.layout.routelist_entry, data));
        // Register Listeners
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkbox = (CheckBox)view.findViewById(R.id.routelist_entry_checkbox);
                getListView().setItemChecked(position, !checkbox.isChecked());
                checkbox.setChecked(!checkbox.isChecked());
            }
        });
        
    }

    // TODO just test data
    private RouteListEntry[] fetchData() {
        RouteListEntry[] data = new RouteListEntry[3];
        data[0] = new RouteListEntry(R.drawable.ic_launcher, "Ich", false);
        data[1] = new RouteListEntry(R.drawable.ic_launcher, "Du", false);
        data[2] = new RouteListEntry(R.drawable.ic_launcher, "Er", false);
        return data;
    }
}
