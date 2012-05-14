package de.androidlab.trackme.listeners;

import de.androidlab.trackme.activities.TrackMeActivity;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

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
