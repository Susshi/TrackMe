package com.mapquest.android.samples;

import com.mapquest.android.Geocoder;

public class OpenStreetMapGeocoderDemo extends GeocoderDemo {

    Geocoder geocoder;
    
    /**
     * Returns the layout id to use.
     * @return layout id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.simple_osm_geocode_map;
    }

    /**
     * Construct and return the Nominatim geocoder instance.
     * @return geocoder the geocoder instance
     */
    @Override
    protected Geocoder getGeocoder() {
        if (geocoder == null) {
            geocoder = new Geocoder(this);
        }
        return geocoder;
    }
}
