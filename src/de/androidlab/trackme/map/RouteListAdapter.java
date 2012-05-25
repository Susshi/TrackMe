package de.androidlab.trackme.map;

import java.util.Collections;

import de.androidlab.trackme.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CheckBox;

public class RouteListAdapter extends ArrayAdapter<RouteListEntry> {

    private Context context = null;
    private int layoutResourceID = -1;
    private RouteListEntry[] entries = null;
    
    public RouteListAdapter(Context context, int layoutResourceID, RouteListEntry[] entries) {
        super(context, layoutResourceID, entries);
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.entries = entries;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, null);
        }
        RouteListEntry entry = entries[position];
        ((ImageView)row.findViewById(R.id.routelist_entry_image)).setImageResource(entry.image);
        ((TextView)row.findViewById(R.id.routelist_entry_name)).setText(entry.name);
        ((CheckBox)row.findViewById(R.id.routelist_entry_checkbox)).setChecked(entry.isChecked);
        row.findViewById(R.id.routelist_root).setBackgroundColor(entry.color);
        return row;
    }
    
}
