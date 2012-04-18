package com.mapquest.android.samples;

import com.mapquest.android.Geocoder;

public class MapQuestGeocoderDemo extends GeocoderDemo {

    Geocoder geocoder;
    
    /**
     * Returns the layout id to use.
     * @return layout id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.simple_geocode_map;
    }
    
    /**
     * Construct and return the MapQuest geocoder instance.
     * @return geocoder the geocoder instance
     */
    @Override
    protected Geocoder getGeocoder() {
        if (geocoder == null) {
            geocoder = new Geocoder(this, getString(R.string.api_key));
        }
        return geocoder;
    }

    
}
