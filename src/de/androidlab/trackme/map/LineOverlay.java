package de.androidlab.trackme.map;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class LineOverlay extends Overlay {
    
    public GeoPoint[] points;
    public Paint paint;
    public Projection projection;

    public LineOverlay(GeoPoint[] points, Paint paint, Projection projection) {
        this.points = points;
        this.paint = paint;
        this.projection = projection;
    }
    
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);
        if (points.length > 0) {
            Point pointNew = null;
            Point pointOld = projection.toPixels(points[0], null);
            for (int i = 1; i < points.length; i++) {
                pointNew = projection.toPixels(points[i], null);
                canvas.drawLine(pointOld.x, pointOld.y, pointNew.x, pointNew.y, paint);
            }
        }
    }
}
