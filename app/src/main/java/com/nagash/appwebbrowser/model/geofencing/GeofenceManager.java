package com.nagash.appwebbrowser.model.geofencing;

import android.location.Location;
import android.support.v4.os.AsyncTaskCompat;

import com.nagash.appwebbrowser.model.localization.LocationEventListener;
import com.nagash.appwebbrowser.model.localization.LocationManager;
import com.nagash.appwebbrowser.model.geofencing.asyncTimer.AsyncTimer;
import com.nagash.appwebbrowser.model.geofencing.asyncTimer.AsyncTimerListener;
import com.nagash.appwebbrowser.model.geofencing.options.GeofenceOptions;
import com.nagash.appwebbrowser.model.geofencing.options.GeofenceOptionsImmutable;
import com.nagash.appwebbrowser.model.geofencing.options.ScannerIntervalMode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nagash on 23/09/16.
 */
public class GeofenceManager<T extends Geofenceable> implements AsyncTimerListener, LocationEventListener {

    AsyncTimer asyncTimer = null;
    GeofenceScanTask scanTask = null;

    LocationManager locationManager;
    GeofenceListener listener;

    //Set<GeofenceObject<T>> geofenceObjects = new LinkedHashSet<>();
    Map<T, GeofenceObject<T>> geofenceObjects = new HashMap<>();
    TriggeredGeofenceableContainer<T> triggeredGeofenceList = null;



    private GeofenceOptionsImmutable options = null;
    private int locationUpdates_beforeLastScanTask = 0; // used only in LocationUpdate based Scan
    private int locationUpdateBased_scanFrequency = 0;  // used only in LocationUpdate based Scan
    private boolean locationChanged = false; // used only in Timer based Scan


//    enum Event { ENTER, EXIT }



    public GeofenceManager(LocationManager locationManager, GeofenceListener listener, GeofenceOptions options) {
        this.locationManager = locationManager;
        locationManager.addListener(this);
        this.options = options.immutableClone();
        this.listener = listener;
    }
    public GeofenceManager(LocationManager locationManager, GeofenceListener listener) {
        this(locationManager, listener, new GeofenceOptions());
    }




    public void add(T newObjects, GeoEvent triggeringEvents) {
        this.geofenceObjects.put(newObjects, GeofenceObject.create(newObjects, triggeringEvents));
    }
    public void addAll(Collection<T> newObjectCollection, GeoEvent commonEvents) {
        for( T geofenceable : newObjectCollection) {
            add(geofenceable, commonEvents);
        }
    }
    public void remove(T object) {
        this.geofenceObjects.remove(object);
    }
    public void removeAll(Collection<T> collection) {
        for(T obj : collection)
            remove(obj);
    }
    public void clear() {
        this.geofenceObjects.clear();
    }



    public void addAllGeofenceObject(Collection<GeofenceObject<T>> newObjects) {
    }
    public void addGeofenceObject(GeofenceObject<T> newGeoObj ) {
        this.geofenceObjects.put(newGeoObj.getManagedObject(), newGeoObj);
    }
    public void removeGeofenceObject( GeofenceObject<T> geoobj) {
        this.geofenceObjects.remove(geoobj);
    }
    public void removeAllGeofenceObjects(Collection<GeofenceObject<T>> objects ) {
        for(GeofenceObject<T> geoobj : objects)
            this.removeGeofenceObject(geoobj);
    }
    //    public void setGeofenceObjects(Collection<? extends GeofenceObject<T>> objects) {
    //        this.geofenceObjects = new LinkedHashSet<>(objects);
    //    }




    public GeofenceOptionsImmutable getOptions() {
        return options;
    }


    public boolean isScanTaskRunning() {
        return (scanTask != null);
    }
    public boolean isPeriodicScanTaskRunning() {
        return (asyncTimer != null);
    }





    // Todo: move frequency settings in GeofenceOptions??
    public void startScan(int frequency) {
        if(options.getScannerIntervalMode() == ScannerIntervalMode.timerBased)
        {
            if (asyncTimer == null || asyncTimer.isStopping() || asyncTimer.isStopped()) {
                asyncTimer = new AsyncTimer(frequency);
                asyncTimer.addListener(this);
                AsyncTaskCompat.executeParallel(asyncTimer);
            }
        }
        else if(options.getScannerIntervalMode() == ScannerIntervalMode.locationUpdateBased) {
            locationUpdates_beforeLastScanTask = 0;
            locationUpdateBased_scanFrequency = frequency;
        }

    }
    /** Stop scanning from the next scan task occurrence.
     * If a scanningTask is running right now, this task will finish.
     */
    public void stopScan() {
        asyncTimer.stop();
    }



    /** Start the Scan Task (works only if ScannerIntervalMode is on Manual mode).
     */
    public int startManualScan() {
        // Todo: use exception or enum instead of int return code
        int ret = 0;
        if(options.getScannerIntervalMode() == ScannerIntervalMode.manual )
            ret = -10;
        else if(scanTask != null)
            ret = -1;
        else
        {
            scanTask = new GeofenceScanTask(this, locationManager.getCurrentLocation(), options);
            //scanTask.execute(this.geofenceObjects);
            AsyncTaskCompat.executeParallel(scanTask, this.geofenceObjects.values());
        }

        return ret;
    }

    @Override public void onTimerLoop() {

        if(options.getScannerIntervalMode() != ScannerIntervalMode.timerBased) return;
        if(scanTask == null && locationChanged) {
            locationChanged = false;
            scanTask = new GeofenceScanTask(this, locationManager.getCurrentLocation(), options);
            //scanTask.execute(this.geofenceObjects);

            AsyncTaskCompat.executeParallel(scanTask, this.geofenceObjects.values());
        }
        else ;// if my location is not changed, skip a timer loop (we don't start a scan task in this loop).

        return;
    }


    @Override public void onLocationChanged(Location location) {
        if(options.getScannerIntervalMode() == ScannerIntervalMode.locationUpdateBased)
        {
            locationUpdates_beforeLastScanTask++;
            if(locationUpdates_beforeLastScanTask >= locationUpdateBased_scanFrequency)
            {
                locationUpdates_beforeLastScanTask = 0;
                scanTask = new GeofenceScanTask(this, locationManager.getCurrentLocation(), options);
                //scanTask.execute(this.geofenceObjects);
                AsyncTaskCompat.executeParallel(scanTask, this.geofenceObjects);
            }
        }
        else if(options.getScannerIntervalMode() == ScannerIntervalMode.timerBased)
            locationChanged = true;

    }
    @Override public void onConnected(Location myLastLocation) {}
    @Override public void onConnectionSuspended() {}
    @Override public void onConnectionFailed() {}



    protected void onGeofenceScanTaskFinished(TriggeredGeofenceableContainer<T> tgc) {
        scanTask = null;
        listener.onGeofenceScanTask(tgc);
//        if( tgc.isAtLeastOneEntering() ) listener.onGeofenceEntering(tgc.enteringSet);
//        if( tgc.isAtLeastOneIn() )       listener.onGeofenceIn(tgc.inSet);
//        if( tgc.isAtLeastOneExiting() )  listener.onGeofenceExiting(tgc.exitingSet);
//        if( tgc.isAtLeastOneOut() )      listener.onGeofenceOut(tgc.outSet);

    }



}
