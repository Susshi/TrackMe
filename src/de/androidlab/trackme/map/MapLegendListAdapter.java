package de.androidlab.trackme.map;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.MapView;

import de.androidlab.trackme.R;

public class MapLegendListAdapter extends android.widget.ArrayAdapter<RouteListEntry> {

    private Context context = null;
    private int layoutResourceID = -1;
    private List<RouteListEntry> entries = null;
    private MapView map = null;
    
    public MapLegendListAdapter(Context context, int layoutResourceID, MapView map, List<RouteListEntry> entries) {
        super(context, layoutResourceID, entries.toArray(new RouteListEntry[entries.size()]));
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.entries = entries;
        this.map = map;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final RouteListEntry entry = entries.get(position);
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, null);
        }
            ((ImageView)row.findViewById(R.id.maplegend_image)).setImageBitmap(entry.image);
            ((TextView)row.findViewById(R.id.maplegend_name)).setText("(" + entry.coords.length + ")" + entry.name);
            row.findViewById(R.id.maplegend_root).setBackgroundColor(entry.color);
            row.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    entry.centerToRoute(map);
                    return false;
                }
            });
        return row;
    }
    
}
