package com.nagash.appwebbrowser.model.geofencing;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by nagash on 03/10/16.
 */

class TriggeredGeofenceableList<T extends Geofenceable> {
    public SortedSet<T> triggeredInGeofences;
    public SortedSet<T> triggeredOutGeofences;
    public  SortedSet<T> triggeredEnteringGeofences;
    public  SortedSet<T> triggeredExitingGeofences;

    public TriggeredGeofenceableList(GeofenceableObjectComparator comparator) {
        triggeredInGeofences    = new TreeSet<>(comparator);
        triggeredOutGeofences   = new TreeSet<>(comparator);
        triggeredEnteringGeofences = new TreeSet<>(comparator);
        triggeredExitingGeofences = new TreeSet<>(comparator);
    }


    public boolean isAtLeastOneIn() {
        if(triggeredInGeofences.size() > 0) return true;
        else return false;
    }

    public boolean isAtLeastOneOut() {
        if(triggeredOutGeofences.size() > 0) return true;
        else return false;
    }

    public boolean isAtLeastOneEntering() {
        if(triggeredEnteringGeofences.size() > 0) return true;
        else return false;
    }

    public boolean isAtLeastOneExiting() {
        if(triggeredExitingGeofences.size() > 0) return true;
        else return false;
    }


    public boolean isAtLeastOneTriggered() {
        if(triggeredEnteringGeofences.size() > 0 || triggeredExitingGeofences.size() > 0
                || triggeredInGeofences.size() > 0 || triggeredOutGeofences.size() > 0)
            return true;
        else return false;
    }

}

