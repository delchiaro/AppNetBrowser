package com.nagash.appwebbrowser.model.geofencing;

import java.util.SortedSet;

/**
 * Created by nagash on 23/09/16.
 */
public interface GeofenceListener<T extends Geofenceable> {

//     * Called before the other single-event methods.
    /**
     * Always called, even if no geofence event has been triggered.
     *
     * @param triggeredContainer contains 4 SortedSet, each contains the set of Geofenceable objects
     *                           triggered for the relative event.
     */
    public void onGeofenceScanTask(TriggeredGeofenceableContainer<T> triggeredContainer);

//
//    /**
//     * 1st called event of the four single-event methods.
//     * Called when the Geofence Scan Task notice that the user is within the proximity radius of a
//     * Geofenceable Object such that in the previus Scan Task the user was outside of its proximity
//     * radius.
//     * Such a Geofenceable Object that meets these requirements, is listed in triggeredGeofences
//     * only if registered for the Entering Event in the GeofenceManager.
//     *
//     * @param triggeredGeofences Geofenceable objects triggered for Entering event, sorted by distance.
//     */
//    public void onGeofenceEntering(SortedSet<T> triggeredGeofences);
//
//    /**
//     * 2nd called event of the four single-event methods.
//     * Called when the Geofence Scan Task notice that the user is within the
//     * proximity radius of a Geofenceable Object. A Geofenceable could be triggered for this event
//     * togheter with the Entering event, and so could be present in both method parameter's lists.
//     * Such a Geofenceable Object that meets these requirements, is listed in triggeredGeofences
//     * only if registered for the In Event in the GeofenceManager.
//     *
//     * @param triggeredGeofences Geofenceable objects triggered for In event, sorted by distance.
//     */
//    public void onGeofenceIn(SortedSet<T> triggeredGeofences);
//
//    /**
//     * 3rd called event. Called when the Geofence Scan Task notice that the user is outside of the
//     * proximity radius of a Geofenceable Object such that in the previus Scan Task the user was
//     * within it.
//     * Such a Geofenceable Object that meets these requirements, is listed in triggeredGeofences only
//     * if registered for the Exiting Event in the GeofenceManager.
//     *
//     * @param triggeredGeofences Geofenceable objects triggered for Exiting event, sorted by distance.
//     */
//    public void onGeofenceExiting(SortedSet<T> triggeredGeofences);
//
//    /**
//     * 4th called event of the four single-event methods.
//     * Called when the Geofence Scan Task notice that the user is outside of
//     * the proximity radius of a Geofenceable Object. A Geofenceable could be triggered for this
//     * event togheter with the Exiting event, and so could be present in both method parameter's
//     * lists.
//     * Such a Geofenceable Object that meets these requirements, is listed in triggeredGeofences
//     * only if registered for the Out Event in the GeofenceManager.
//     *
//     * @param triggeredGeofences Geofenceable objects triggered for Out event, sorted by distance.
//     */
//    public void onGeofenceOut(SortedSet<T> triggeredGeofences);

}
