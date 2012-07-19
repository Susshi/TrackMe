package de.androidlab.trackme.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import de.androidlab.trackme.R;

public class SettingsActivity extends TabActivity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        // Create Tab functionality
        Resources res = getResources();
        TabHost tabHost = getTabHost();

        // ----- Map Tab -----
        TabHost.TabSpec map = tabHost.newTabSpec("tab1");
        map.setIndicator("Map", res.getDrawable(R.drawable.ic_menu_map));
        map.setContent(new Intent(SettingsActivity.this, SettingsMapTabActivity.class));
        tabHost.addTab(map);
        
        // ----- DB Tab -----
        TabHost.TabSpec db = tabHost.newTabSpec("tab2");
        db.setIndicator("Database", res.getDrawable(R.drawable.ic_menu_settings));
        db.setContent(new Intent(SettingsActivity.this, SettingsDBTabActivity.class));
        tabHost.addTab(db);
        
        // ----- DTN Tab -----
        TabHost.TabSpec general = tabHost.newTabSpec("tab3");
        general.setIndicator("DTN", res.getDrawable(R.drawable.ic_menu_settings));
        general.setContent(new Intent(SettingsActivity.this, SettingsDTNTabActivity.class));
        tabHost.addTab(general);

        // Set active tab
        tabHost.setCurrentTab(0);
    }
}
