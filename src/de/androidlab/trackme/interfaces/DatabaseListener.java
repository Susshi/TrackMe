package de.androidlab.trackme.interfaces;

import java.util.Vector;

import android.util.Pair;

import com.google.android.maps.GeoPoint;

public interface DatabaseListener {
	public void onDatabaseChange(Vector<Pair<String, GeoPoint[]>> newData);
}
