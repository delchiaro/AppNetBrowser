package com.nagash.appwebbrowser.model.geofencing.deprecated;

import com.nagash.appwebbrowser.model.geofencing.GeofenceObject;
import com.nagash.appwebbrowser.model.geofencing.Geofenceable;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by nagash on 03/10/16.
 */

class TriggeredGeofenceList<T extends Geofenceable> {
    public SortedSet<GeofenceObject<T>> triggeredInGeofences;
    public SortedSet<GeofenceObject<T>> triggeredOutGeofences;
    public  SortedSet<GeofenceObject<T>> triggeredEnterGeofences;
    public  SortedSet<GeofenceObject<T>> triggeredExitGeofences;

    public TriggeredGeofenceList(GeofenceObjectComparator comparator) {
        triggeredInGeofences    = new TreeSet<>(comparator);
        triggeredOutGeofences   = new TreeSet<>(comparator);
        triggeredEnterGeofences = new TreeSet<>(comparator);
        triggeredExitGeofences  = new TreeSet<>(comparator);
    }


    public boolean isAtLeastOneIn() {
        if(triggeredInGeofences.size() > 0) return true;
        else return false;
    }

    public boolean isAtLeastOneOut() {
        if(triggeredOutGeofences.size() > 0) return true;
        else return false;
    }

    public boolean isAtLeastOneEnter() {
        if(triggeredEnterGeofences.size() > 0) return true;
        else return false;
    }

    public boolean isAtLeastOneExit() {
        if(triggeredExitGeofences.size() > 0) return true;
        else return false;
    }


    public boolean isAtLeastOneTriggered() {
        if(triggeredEnterGeofences.size() > 0 || triggeredExitGeofences.size() > 0
                || triggeredInGeofences.size() > 0 || triggeredOutGeofences.size() > 0)
            return true;
        else return false;
    }

}

