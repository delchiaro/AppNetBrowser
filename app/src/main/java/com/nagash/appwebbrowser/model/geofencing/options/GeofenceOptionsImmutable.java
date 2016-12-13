package com.nagash.appwebbrowser.model.geofencing.options;

/**
 * Created by nagash on 11/12/16.
 */

public class GeofenceOptionsImmutable implements IGeofenceOptions {
    final GeofenceOptions geofenceOptions;

    public GeofenceOptionsImmutable(GeofenceOptions options) {
        geofenceOptions = new GeofenceOptions(options);
    }


    public GeofenceOptions mutableClone() {
        return new GeofenceOptions(this);
    }

    public boolean isAdvertiseOnEmptyList() {
        return geofenceOptions.isAdvertiseOnEmptyList();
    }

    public boolean isUsingExtraRadius() {
        return geofenceOptions.isUsingExtraRadius();
    }

    public boolean isUsingOnlyExtraRadius() {
        return geofenceOptions.isUsingOnlyExtraRadius();
    }

    public int getExtraRadius() {
        return geofenceOptions.getExtraRadius();
    }

    public ScannerIntervalMode getScannerIntervalMode() {
        return geofenceOptions.getScannerIntervalMode();
    }
}
