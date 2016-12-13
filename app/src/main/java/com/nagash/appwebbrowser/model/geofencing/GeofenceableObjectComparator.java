package com.nagash.appwebbrowser.model.geofencing;

import android.location.Location;

import java.util.Comparator;

/**
 * Created by nagash on 23/09/16.
 */
public class GeofenceableObjectComparator implements Comparator<Geofenceable>
{
     Location myLocation;

    GeofenceableObjectComparator(Location myLocation)
    {
        this.myLocation = myLocation;
    }

    @Override
    public int compare(Geofenceable geofenceableObject, Geofenceable t2) {
        float dist1 = geofenceableObject.getLocation().distanceTo(this.myLocation);
        float dist2 = geofenceableObject.getLocation().distanceTo(this.myLocation);
        if(dist1<dist2)
            return 1;
        else if(dist1==dist2)
            return 0;
        else return -1;    }
}
