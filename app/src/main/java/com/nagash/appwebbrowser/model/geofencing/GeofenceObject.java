package com.nagash.appwebbrowser.model.geofencing;

import android.location.Location;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nagash on 23/09/16.
 */
public class GeofenceObject< G extends Geofenceable> implements Geofenceable
{
    private G managedObject;
    private final Location location;
    private final int radius;

    private final GeoEvent registeredEvent;

    private GeoStatus geoStatus = GeoStatus.INIT;
    private GeoEvent triggeredEvent = null;
    private GeoEvent lastTriggeredEvent = new GeoEvent().disableAllEvent();

    private Location myActualLocation;




    private GeofenceObject(G managedObject, GeoEvent registeredEvent )
    {
        this.managedObject = managedObject;
        this.location = managedObject.getLocation();
        this.radius = managedObject.getProximityRadius();
        this.registeredEvent = registeredEvent;
    }

    public Location getLocation() { return location; }
    public int getProximityRadius() { return  radius; }
    public GeoEvent getRegisteredEvent() { return registeredEvent; }

    public GeoEvent getTriggeredEvent() { return triggeredEvent; }

    public GeoStatus getGeoStatus() { return geoStatus; }



    protected GeofenceObject<G> setGeoStatus(GeoStatus geoStatus) { this.geoStatus = geoStatus; return this; }
    protected GeofenceObject<G> setTriggeredEvent(GeoEvent event) { this.triggeredEvent = event; return this;}


    public GeoStatus testLocationStatus(Location testLocation) {
        return testLocations(getLocation(), testLocation, getProximityRadius());
    }

    public GeoStatus testLocationStatus_extraRadius(Location testLocation, int extraRadius) {
        return testLocations(getLocation(), testLocation, extraRadius + getProximityRadius());

    }

    public GeoStatus testLocationStatus_extraRadiusOnly(Location testLocation, int extraRadius) {
        return testLocations(getLocation(), testLocation, extraRadius );
    }

    private static GeoStatus testLocations(Location a, Location b, int radius) {
        if(a.distanceTo(b) < radius)
            return GeoStatus.IN;
        else return GeoStatus.OUT;
    }



    /*
     *  Return an ArrayList of  GeofenceObject, one for each ManagedObject with location not null and proximity radius not negative.
     */
    public static <G extends Geofenceable> Collection<GeofenceObject<G>> createGeofenceObjects(Collection<G> managedObjects, GeoEvent registeredEvent) {
        Collection<GeofenceObject<G>> ret = new ArrayList<>(managedObjects.size());
        for (G g : managedObjects) {
            GeofenceObject<G> geoObj = create(g, registeredEvent);
            if(geoObj != null)
                ret.add(geoObj);
        }
        return ret;
    }


    /*
     *  Return a new GeofenceObject only if the ManagedObject's location is not null and proximity radius not negative.
     *  Otherwise return null.
     */
    public static <G extends Geofenceable> GeofenceObject<G> create(@NonNull G managedObject, @NonNull GeoEvent registeredEvent) {
        if(managedObject.getLocation() != null && managedObject.getProximityRadius() >= 0)
            return new GeofenceObject(managedObject, registeredEvent);
        else return null;
    }


    public static <G extends Geofenceable> Collection<G> getManagedObjects(Collection<GeofenceObject<G> > geofenceObjects) {
        Collection<G> ret = new ArrayList<>(geofenceObjects.size());
        for(GeofenceObject<G> go : geofenceObjects)
            ret.add(go.getManagedObject());
        return ret;
    }

    public G getManagedObject() {
        return managedObject;
    }


}
