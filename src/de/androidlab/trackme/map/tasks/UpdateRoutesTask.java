package de.androidlab.trackme.map.tasks;

import java.util.List;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.google.android.maps.MapView;

import de.androidlab.trackme.activities.MapActivity;
import de.androidlab.trackme.map.LineOverlay;
import de.androidlab.trackme.map.RouteListEntry;

public class UpdateRoutesTask extends AsyncTask<List<RouteListEntry>, Pair<Boolean, RouteListEntry>, Pair<Integer, Integer>> {
    
    private MapActivity mapActivity;
    private MapView map;
    private int strokeWidth;
    private boolean antialiasing;
    
    public UpdateRoutesTask(MapActivity mapActivity, MapView map, int strokeWidth, boolean antialiasing) {
        this.mapActivity = mapActivity;
        this.map = map;
        this.strokeWidth = strokeWidth;
        this.antialiasing = antialiasing;
    }
    
    @Override
    // first = toRemove, second = toAdd, third = data
    protected Pair<Integer, Integer> doInBackground(List<RouteListEntry>... params) {
        if (params.length == 3) {
            int newEntries = params[1].size();
            int oldEntries = params[0].size();
            // Remove
            for (RouteListEntry e : params[0]) {
                Pair<Boolean, RouteListEntry> route = new Pair<Boolean, RouteListEntry>(true, e);
                publishProgress(route);
            }
            params[2].removeAll(params[0]);
            params[0].clear();
            
            // Draw new and redraw old
            params[2].addAll(params[1]);
            params[1].clear();   
            for (RouteListEntry e : params[2]) {
                if (e.isChecked == true) {
                    Pair<Boolean, RouteListEntry> route = new Pair<Boolean, RouteListEntry>(false, e);
                    publishProgress(route);
                } else {
                    Pair<Boolean, RouteListEntry> route = new Pair<Boolean, RouteListEntry>(true, e);
                    publishProgress(route);
                }
            }
            return new Pair<Integer, Integer>(newEntries, oldEntries);
        } else {
            throw new IllegalArgumentException("3 parameter needed");
        }
    }
    
    protected void onProgressUpdate(Pair<Boolean, RouteListEntry>... routes) {
        for (Pair<Boolean, RouteListEntry> route : routes) {
            if (route.first == true) {
                removeRoute(route.second);
            } else {
                drawRoute(route.second);
            }
            map.invalidate();
        }
    }
    
    protected void onPostExecute(Pair<Integer, Integer> result) {
        if (result.first > 0 || result.second > 0) {
            String toast = result.first > 0 ? result.first + " entries are new" : "";
            toast += result.second > 0 ? "\n" + result.second + " entries are obsolete" : "";
            Toast.makeText(mapActivity, toast, Toast.LENGTH_SHORT).show();
        }
    }
    
    private void drawRoute(RouteListEntry e) {
        Paint paint = new Paint();
        paint.setColor(e.color);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(antialiasing);
        LineOverlay line = new LineOverlay(e.coords, paint, map.getProjection());
        e.line = line;
        map.getOverlays().add(e.line);
    }

    private void removeRoute(RouteListEntry e) {
        if (e.line != null) {
            map.getOverlays().remove(e.line);
            e.line = null;  
        }
    }
    

}
