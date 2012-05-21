package de.androidlab.trackme.activities;

import de.androidlab.trackme.R;
import de.androidlab.trackme.listeners.BackButtonListener;
import de.androidlab.trackme.listeners.HomeButtonListener;
import de.androidlab.trackme.map.ColorGenerator;
import de.androidlab.trackme.map.RouteListEntry;
import de.androidlab.trackme.map.RouteListAdapter;
import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class RouteListActivity extends ListActivity {
	
	// TODO Testdaten entfernen
	private static RouteListEntry[] data = new RouteListEntry[20];
	static {
		ColorGenerator cg = new ColorGenerator(data.length);
		for (int i = 0; i < data.length; i++) {
			data[i] = new RouteListEntry(R.drawable.ic_launcher, "" + i, false, cg.getNewColor(), null);
		}
	}
    
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
        // Add Data to list
        lv.setAdapter(new RouteListAdapter(this, R.layout.routelist_entry, data));
        // Register Listeners
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RouteListEntry entry = (RouteListEntry)parent.getItemAtPosition(position);
                entry.checked = !entry.checked;
                getListView().setItemChecked(position, entry.checked);
                ((CheckBox)view.findViewById(R.id.routelist_entry_checkbox)).setChecked(entry.checked);
            }
        });
        
    }
}
