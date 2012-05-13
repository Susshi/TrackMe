package de.androidlab.trackme.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class HomeButtonListener implements View.OnClickListener {

    private Activity activity;
    
    public HomeButtonListener(Activity activity) {
        this.activity = activity;
    }
    
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent); 
    }

}
