package com.nagash.appwebbrowser.model.geofencing;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by nagash on 03/10/16.
 */

public class TriggeredGeofenceableContainer<T extends Geofenceable> {
    public SortedSet<T> inSet;
    public SortedSet<T> outSet;
    public SortedSet<T> enteringSet;
    public SortedSet<T> exitingSet;

    public TriggeredGeofenceableContainer(GeofenceableObjectComparator comparator) {
        inSet = new TreeSet<>(comparator);
        outSet = new TreeSet<>(comparator);
        enteringSet = new TreeSet<>(comparator);
        exitingSet = new TreeSet<>(comparator);
    }


    public boolean isAtLeastOneIn() {
        if(inSet.size() > 0) return true;
        else return false;
    }

    public boolean isAtLeastOneOut() {
        if(outSet.size() > 0) return true;
        else return false;
    }

    public boolean isAtLeastOneEntering() {
        if(enteringSet.size() > 0) return true;
        else return false;
    }

    public boolean isAtLeastOneExiting() {
        if(exitingSet.size() > 0) return true;
        else return false;
    }


    public boolean isAtLeastOneTriggered() {
        if(enteringSet.size() > 0 || exitingSet.size() > 0
                || inSet.size() > 0 || outSet.size() > 0)
            return true;
        else return false;
    }

}

