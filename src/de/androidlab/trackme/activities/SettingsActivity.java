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
        
        // ----- General Tab -----
        TabHost.TabSpec general = tabHost.newTabSpec("tab1");
        general.setIndicator("General", res.getDrawable(R.drawable.ic_tab_settings));
        general.setContent(new Intent(SettingsActivity.this, SettingsGeneralTabActivity.class));
        tabHost.addTab(general);      

        // ----- Privacy Tab -----
        TabHost.TabSpec privacy = tabHost.newTabSpec("tab2");
        privacy.setIndicator("Privacy", res.getDrawable(R.drawable.ic_tab_privacy));
        privacy.setContent(new Intent(SettingsActivity.this, SettingsPrivacyTabActivity.class));
        tabHost.addTab(privacy);

        // Set active tab
        tabHost.setCurrentTab(0);
    }
}
