package de.androidlab.trackme.map.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import android.os.AsyncTask;
import android.widget.ListView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import de.androidlab.trackme.R;
import de.androidlab.trackme.activities.MapActivity;
import de.androidlab.trackme.data.ContactInfo;
import de.androidlab.trackme.data.MapData;
import de.androidlab.trackme.map.MapLegendListAdapter;
import de.androidlab.trackme.map.RouteListEntry;

public class UpdateLegendTask extends AsyncTask<List<RouteListEntry>, Void, List<RouteListEntry>> {

    private MapView map;
    private ListView legend;
    private MapActivity mapActivity;
    private static Semaphore mutex = new Semaphore(1);
    
    public UpdateLegendTask(MapActivity mapActivity, MapView map, ListView legend) {
        this.mapActivity = mapActivity;
        this.map = map;
        this.legend = legend;
    }
    
    @Override
    protected List<RouteListEntry> doInBackground(List<RouteListEntry>... data) {
    	try {
			mutex.acquire();
	        List<RouteListEntry> checkedOnly = new ArrayList<RouteListEntry>(MapData.data.size());
	        List<RouteListEntry> anonymous = new ArrayList<RouteListEntry>(MapData.data.size());
	        for (List<RouteListEntry> dataset : data) {
	            // TODO find a more efficient way to not display unchecked elements in legend
	            for (RouteListEntry e : dataset) {
	            	if (e.coords.length > 0) {
		                if (e.isChecked == true) {
		                    if (e.isFriend == true) {
		                        checkedOnly.add(e);
		                    } else {
		                        anonymous.add(e);
		                    }
		                }
	            	}
	            }
	            checkedOnly.addAll(anonymous);
	        }
	        return checkedOnly;
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	return null;
    }
    
    protected void onPostExecute(List<RouteListEntry> result) {
        legend.setAdapter(new MapLegendListAdapter(mapActivity, R.layout.maplegend_entry, map, result));
        legend.invalidate();
        mutex.release();
    }
}
