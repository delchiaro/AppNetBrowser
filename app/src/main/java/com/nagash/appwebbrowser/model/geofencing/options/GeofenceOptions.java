package com.nagash.appwebbrowser.model.geofencing.options;

/**
 * Created by nagash on 11/12/16.
 */

public class GeofenceOptions implements IGeofenceOptions {
    //private boolean advertiseOnEmptyList = false;

    private ScannerIntervalMode scannerIntervalMode = ScannerIntervalMode.timerBased;
//    int scannerTimerFrequency = 5000; // geofence scanning each 5000ms (if timerBased)
//    int scannerLocationUpdateFrequency = 2; // geofence scanning each 2 gps location update (if locationUpdateBased)

    private int extraRadius = 0; // User radius, radius of action of MyLocation

    // if true, GeofenceScanTask will use sum the extraRadius to the geofenceRadius of each GeofenceObject
    private boolean useExtraRadius = false;

    // if true, GeofenceScanTask will ignore the geofenceRadius of each GoefenceObject
    private boolean useOnlyExtraRadius = false;






    public GeofenceOptions(){}
    public GeofenceOptions(IGeofenceOptions copy) {
        //this.advertiseOnEmptyList = copy.isAdvertiseOnEmptyList();
        scannerIntervalMode = copy.getScannerIntervalMode(); // Todo: Enums are copied by value or ref??
        extraRadius = copy.getExtraRadius();
        useExtraRadius = copy.isUsingExtraRadius();
        useOnlyExtraRadius = copy.isUsingOnlyExtraRadius();
    }

    public GeofenceOptionsImmutable immutableClone() {
        return new GeofenceOptionsImmutable(this);
    }





    public void useExtraRadius() { useExtraRadius = true; }
    public void useOnlyExtraRadius() { useOnlyExtraRadius = true; }
    public void setExtraRadius(int extraRadius) { this.extraRadius = extraRadius; useExtraRadius(); }
    //public void advertiseOnEmptyList() { advertiseOnEmptyList = true; }
//    public void setScannerIntervalTimer(int milliseconds) {
//        scannerTimerFrequency = milliseconds;
//        scannerIntervalMode = ScannerIntervalMode.timerBased;
//    }
//    public void setScannerIntervalLocationUpdate(int gpsChanges) {
//        scannerTimerFrequency = gpsChanges;
//        scannerIntervalMode = ScannerIntervalMode.locationUpdateBased;
//    }
    public void setScannerIntervalMode( ScannerIntervalMode mode ) { scannerIntervalMode = mode; }


    public boolean isUsingExtraRadius() { return useExtraRadius; }
    public boolean isUsingOnlyExtraRadius() { return useOnlyExtraRadius; }
    public int getExtraRadius() { return extraRadius; }
    //public boolean isAdvertiseOnEmptyList() { return advertiseOnEmptyList; }


    public ScannerIntervalMode getScannerIntervalMode() {
        return scannerIntervalMode;
    }
//
//    public int getScannerUpdateFrequency() {
//        if(scannerIntervalMode == ScannerIntervalMode.timerBased)
//            return scannerTimerFrequency;
//        else return scannerLocationUpdateFrequency;
//    }

}
