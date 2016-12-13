package com.nagash.appwebbrowser.model.geofencing.options;

/**
 * Created by nagash on 11/12/16.
 */

public interface IGeofenceOptions {

    ScannerIntervalMode getScannerIntervalMode();

    int getExtraRadius();
    boolean isUsingExtraRadius();
    boolean isUsingOnlyExtraRadius() ;
    boolean isAdvertiseOnEmptyList();


//    public int getScannerUpdateFrequency();

}
