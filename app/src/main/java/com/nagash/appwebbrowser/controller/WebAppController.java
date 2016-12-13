package com.nagash.appwebbrowser.controller;

import android.content.Context;

import com.estimote.sdk.eddystone.Eddystone;
import com.nagash.appwebbrowser.model.beacon.eddystoneProximity.EddystoneProximityListener;
import com.nagash.appwebbrowser.model.beacon.eddystoneProximity.EddystoneProximityManager;
import com.nagash.appwebbrowser.model.beacon.eddystoneScanner.EddystoneScanner;
import com.nagash.appwebbrowser.model.geofencing.GeoEvent;
import com.nagash.appwebbrowser.model.geofencing.GeofenceListener;
import com.nagash.appwebbrowser.model.geofencing.GeofenceManager;
import com.nagash.appwebbrowser.model.geofencing.GeofenceObject;
import com.nagash.appwebbrowser.model.geofencing.options.GeofenceOptions;
import com.nagash.appwebbrowser.model.geofencing.options.ScannerIntervalMode;
import com.nagash.appwebbrowser.model.localization.LocationManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by nagash on 11/12/16.
 */

/*
    This class manage the list of applications in the nearby of the user.
    The
 */
public class WebAppController
        implements
        EddystoneProximityListener

{
    private static final int NEARBY_SCAN_FREQUENCY = 10000;
    private static final int PROXIMITY_SCAN_FREQUENCY = 5000;

    GeofenceManager<WebApp> nearbyGeofenceManager = null;
    GeofenceManager<WebApp> proximityGeofenceManager = null;
    EddystoneProximityManager eddystoneProximityManager;

    GeofenceOptions nearbyGeofenceOptions;
    GeofenceOptions proximityGeofenceOptions;

    public WebAppController(LocationManager locationManager) {
        nearbyGeofenceOptions = new GeofenceOptions();
        nearbyGeofenceOptions.setExtraRadius(300000);
        nearbyGeofenceOptions.useExtraRadius();
        nearbyGeofenceOptions.advertiseOnEmptyList();
        nearbyGeofenceOptions.setScannerIntervalMode(ScannerIntervalMode.timerBased);
        nearbyGeofenceManager = new GeofenceManager<>(locationManager, nearbyGeofencListener, nearbyGeofenceOptions);

        proximityGeofenceOptions = new GeofenceOptions();
        proximityGeofenceOptions.setScannerIntervalMode(ScannerIntervalMode.timerBased);
        proximityGeofenceOptions.advertiseOnEmptyList();
        proximityGeofenceManager = new GeofenceManager<>(locationManager, proxyGeofencListener, proximityGeofenceOptions);

        eddystoneSettings = new EddystoneProximityManager.Settings();
        eddystoneSettings.advertiseInProximity(true);
        eddystoneSettings.advertiseConnectedOutProximity(true);

    }


    Collection<GeofenceObject<WebApp>> geofenceObjects;
    Map<String, WebApp> webAppBeaconMap = new HashMap<>();

    EddystoneProximityManager.Settings eddystoneSettings;


    public void start(List<WebApp> allApps, Context context) {
        start(allApps, EddystoneScanner.getInstance(context));
    }
    public void start(List<WebApp> allApps, EddystoneScanner scanner) {
        for(WebApp app : allApps)
            webAppBeaconMap.put(app.getUID(), app);
        eddystoneProximityManager = new EddystoneProximityManager(scanner, eddystoneSettings);
        eddystoneProximityManager.start();

        nearbyGeofenceManager.addAll(allApps, new GeoEvent().enableAllEvent());
        nearbyGeofenceManager.startScan(NEARBY_SCAN_FREQUENCY);
        //proximityGeofenceManager.startScan(PROXIMITY_SCAN_FREQUENCY);

    }







    // * * * * * * * * * * * * EDDYSTONE LISTENER  * * * * * * * * * * * *
    @Override public void onBeaconNotInProximity(Set<Eddystone> paramSet)   {}
    @Override public void onBeaconProximity(Set<Eddystone> paramSet)        {}
    @Override public void onLostBeaconProximity(Set<Eddystone> paramSet)    {
        // update proximity list! (remove items, re-add the item in the nearby list)
        for( Eddystone e : paramSet) {
            WebApp app = webAppBeaconMap.get(e.namespace + e.instance);
            proximitySet.remove( app );
        }
    }
    @Override public void onNewBeaconProximity(Set<Eddystone> paramSet)     {
        // update proximity list! (add items, higher priority over Geofence events! Remove the same item from nearby if present, until out of beacon proximity)
        for( Eddystone e : paramSet) {
            WebApp app = webAppBeaconMap.get(e.namespace + e.instance);
            proximitySet.add( app );
        }
    }




    SortedSet<WebApp> nearbySet = null;
    SortedSet<WebApp> proximitySet = null;
    Set<WebApp> nearbyNotInproximitySet = new HashSet<>();



    // * * * * * * * * * * * * GEOFENCE LISTENER  * * * * * * * * * * * *
    GeofenceListener<WebApp>  nearbyGeofencListener = new GeofenceListener<WebApp>() {
        @Override public void onGeofenceIn(SortedSet<WebApp> triggeredGeofences) {
            nearbyGeofenceManager.stopScan();
            //  update nearby list (re-ordering or add items)
            nearbySet = triggeredGeofences;
            nearbyNotInproximitySet = nearbySet;
            nearbyNotInproximitySet.removeAll(proximitySet);
            proximityGeofenceManager.startScan(PROXIMITY_SCAN_FREQUENCY);
        }
        @Override public void onGeofenceOut(SortedSet<WebApp> triggeredGeofences) {}
        @Override public void onGeofenceEntering(SortedSet<WebApp> triggeredGeofences) {
            proximityGeofenceManager.addAll(triggeredGeofences, new GeoEvent().enableEnteringEvent().enableExitingEvent());
        }
        @Override public void onGeofenceExiting(SortedSet<WebApp> triggeredGeofences) {
            proximityGeofenceManager.removeAll(triggeredGeofences);

            //  update nearby list (ignore re-ordering, only remove items)
            if(nearbySet != null && triggeredGeofences != null)
                nearbySet.removeAll(triggeredGeofences);
        }
    };

    GeofenceListener<WebApp>  proxyGeofencListener = new GeofenceListener<WebApp>() {
        @Override public void onGeofenceIn(SortedSet<WebApp> triggeredGeofences) {
            proximityGeofenceManager.stopScan();
            nearbyGeofenceManager.startScan(NEARBY_SCAN_FREQUENCY);
        }
        @Override public void onGeofenceOut(SortedSet<WebApp> triggeredGeofences) {}
        @Override public void onGeofenceEntering(SortedSet<WebApp> triggeredGeofences) {
            //  update proximity list! (add items)
            if(triggeredGeofences != null)
               proximitySet.addAll(triggeredGeofences);
        }
        @Override public void onGeofenceExiting(SortedSet<WebApp> triggeredGeofences) {
            // update proximity list! (remove items)
            if(triggeredGeofences != null)
                proximitySet.removeAll(triggeredGeofences);
        }
    };

}
