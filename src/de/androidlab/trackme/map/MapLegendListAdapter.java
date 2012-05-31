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

public class MapLegendListAdapter extends android.widget.ArrayAdapter<RouteListEntry> {

    private Context context = null;
    private int layoutResourceID = -1;
    private List<RouteListEntry> entries = null;
    
    public MapLegendListAdapter(Context context, int layoutResourceID, List<RouteListEntry> entries) {
        super(context, layoutResourceID, entries.toArray(new RouteListEntry[entries.size()]));
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.entries = entries;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RouteListEntry entry = entries.get(position);
        if (entry.isChecked) {
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, null);
        }
            ((ImageView)row.findViewById(R.id.maplegend_image)).setImageBitmap(entry.image);
            ((TextView)row.findViewById(R.id.maplegend_name)).setText(entry.name);
            row.findViewById(R.id.maplegend_root).setBackgroundColor(entry.color);
        }
        return row;
    }
    
}
