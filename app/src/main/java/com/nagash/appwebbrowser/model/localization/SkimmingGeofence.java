//package com.nagash.appwebbrowser.model.localization;
//
//import android.location.Location;
//
//import com.google.android.gms.maps.model.LatLng;
//import com.nagash.appwebbrowser.model.geofencing.GeoEvent;
//import com.nagash.appwebbrowser.model.geofencing.GeofenceListener;
//import com.nagash.appwebbrowser.model.geofencing.GeofenceManager;
//import com.nagash.appwebbrowser.model.geofencing.GeofenceObject;
//import com.nagash.appwebbrowser.model.geofencing.Geofenceable;
//import com.nagash.appwebbrowser.model.localization.LocationManager;
//
//import java.util.Collection;
//import java.util.SortedSet;
//
///**
// * Created by nagash on 01/10/16.
// */
//public class SkimmingGeofence implements GeofenceListener {
//    GeofenceManager geofenceManager;
//    LocationManager locationManager;
//
//    private static String SKIMMING_ID = "SKIMMING_OBJECT_ID";
//    private static int DEFAULT_SKIMMING_RADIUS = 3000; //meters
//    private static double DEFAULT_LAT = 0;
//    private static double DEFAULT_LNG = 0;
//
//
//    SkimmingRadiusGeofenceObject skimmingRadiusObject;
//
//
//    public SkimmingGeofence(LocationManager locationManager, int skimmingRadius) {
//        geofenceManager = new GeofenceManager(locationManager, this);
//        skimmingRadiusObject = new SkimmingRadiusGeofenceObject(skimmingRadius, new Location(SKIMMING_ID));
//
//        Location loc = locationManager.getCurrentLocation();
//        if(loc != null) {
//            skimmingRadiusObject.getLocation().setLatitude(loc.getLatitude());
//            skimmingRadiusObject.getLocation().setLongitude(loc.getLongitude());
//        }
//        else
//        {
//            skimmingRadiusObject.getLocation().setLatitude(DEFAULT_LAT));
//            skimmingRadiusObject.getLocation().setLongitude(DEFAULT_LNG);
//        }
//    }
//    public SkimmingGeofence(LocationManager locationManager) {
//        this(locationManager, DEFAULT_SKIMMING_RADIUS);
//    }
//
//
//    private void newSkimming(Location loc, int radius) {
//        newSkimming(new LatLng(loc.getLatitude(), loc.getLongitude()), radius);
//    }
//    private void newSkimming(LatLng latLng, int radius) {
//        skimmingRadiusObject.getLocation().setLatitude(latLng.latitude);
//        skimmingRadiusObject.getLocation().setLongitude(latLng.longitude);
//        skimmingRadiusObject.setSkimmingRadius(radius);
//    }
//
//
//
//    @Override
//    public void onGeofenceEventTriggered(SortedSet<GeofenceObject> triggeredGeofences) {
//        for(GeofenceObject o : triggeredGeofences)
//            if(o.getId().equals(SKIMMING_ID))
//                newSkimming(locationManager.getCurrentLocation(), );
//    }
//
//
//
//
//    public void addGeofenceObject(GeofenceObject newObject) {
//        geofenceManager.addGeofenceObject(newObject);
//    }
//
//    public void addGeofenceObjects(Collection<GeofenceObject> newObjects) {
//        geofenceManager.addGeofenceObjects(newObjects);
//    }
//
//    public void startScan(int frequency, Location myLocation) {
//        addGeofenceObject(new GeofenceObject(skimmingRadiusObject, SKIMMING_ID, GeoEvent.EXIT));
//        geofenceManager.startScan(frequency, myLocation);
//    }
//
//    private class SkimmingRadiusGeofenceObject implements Geofenceable
//    {
//        private Location location;
//        private int skimmingRadius;
//        public SkimmingRadiusGeofenceObject(int skimmingRadius, Location location) {
//            this.skimmingRadius = skimmingRadius;
//            this.location = location;
//        }
//
//        protected void setSkimmingRadius(int radius) { skimmingRadius = radius; }
//
//
//        @Override
//        public Location getLocation() {
//            return location;
//        }
//
//        @Override
//        public int getProximityRadius() {
//            return skimmingRadius;
//        }
//    }
//}
