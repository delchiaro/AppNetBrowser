package com.nagash.appwebbrowser.model.geofencing;

import android.location.Location;

import com.nagash.appwebbrowser.model.localization.Localizable;

/**
 * Created by nagash on 23/09/16.
 */
public interface Geofenceable extends Localizable {

    public int getProximityRadius();
    public Location getLocation();

}
