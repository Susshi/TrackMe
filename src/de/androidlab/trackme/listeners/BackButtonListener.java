package de.androidlab.trackme.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import de.androidlab.trackme.activities.TrackMeActivity;

public class BackButtonListener implements View.OnClickListener {

    private Activity activity;
    
    public BackButtonListener(Activity activity) {
        this.activity = activity;
    }
    
    @Override
    public void onClick(View v) {
        activity.startActivity(new Intent(activity, TrackMeActivity.class));
    }

}
