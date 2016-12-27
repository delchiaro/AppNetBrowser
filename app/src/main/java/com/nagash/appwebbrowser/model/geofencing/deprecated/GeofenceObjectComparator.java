package com.nagash.appwebbrowser.model.geofencing.deprecated;

import android.location.Location;

import com.nagash.appwebbrowser.model.geofencing.GeofenceObject;

import java.util.Comparator;

/**
 * Created by nagash on 23/09/16.
 */
public class GeofenceObjectComparator implements Comparator<GeofenceObject>
{
     Location myLocation;

    GeofenceObjectComparator(Location myLocation)
    {
        this.myLocation = myLocation;
    }

    @Override
    public int compare(GeofenceObject geofenceObject, GeofenceObject t2) {
        float dist1 = geofenceObject.distanceTo(this.myLocation);
        float dist2 = geofenceObject.distanceTo(this.myLocation);
        if(dist1<dist2)
            return 1;
        else if(dist1==dist2)
            return 0;
        else return -1;
    }
}
