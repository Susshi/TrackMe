package de.androidlab.trackme.map;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import de.androidlab.trackme.R;

public class RouteListAdapter extends android.widget.ArrayAdapter<RouteListEntry> {

    private Context context = null;
    private int layoutResourceID = -1;
    private List<RouteListEntry> entries = null;
    
    public RouteListAdapter(Context context, int layoutResourceID, List<RouteListEntry> entries) {
        super(context, layoutResourceID, entries.toArray(new RouteListEntry[entries.size()]));
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
        RouteListEntry entry = entries.get(position);
        ((ImageView)row.findViewById(R.id.routelist_entry_image)).setImageBitmap(entry.image);
        ((TextView)row.findViewById(R.id.routelist_entry_name)).setText(entry.name);
        ((CheckBox)row.findViewById(R.id.routelist_entry_checkbox)).setChecked(entry.isChecked);
        row.findViewById(R.id.routelist_root).setBackgroundColor(entry.color);
        return row;
    }
    
}
