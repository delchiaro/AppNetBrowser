package com.nagash.appwebbrowser.model.localization;

import android.location.Location;

/**
 * Created by nagash on 02/10/16.
 */
public interface Localizable {

    Location getLocation();
    float distanceTo(Location myLocation);

    // to use default methods we need min api 16 :(
//    default float distanceTo(Location location) {
//        return getLocation().distanceTo(location);
//    }

}
