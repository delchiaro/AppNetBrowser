package com.nagash.appwebbrowser.controller.webAppController;

import android.content.Context;

import com.estimote.sdk.eddystone.Eddystone;
import com.nagash.appwebbrowser.model.beacon.eddystoneProximity.EddystoneProximityListener;
import com.nagash.appwebbrowser.model.beacon.eddystoneProximity.EddystoneProximityManager;
import com.nagash.appwebbrowser.model.beacon.eddystoneScanner.EddystoneScanner;
import com.nagash.appwebbrowser.model.geofencing.GeoEvent;
import com.nagash.appwebbrowser.model.geofencing.GeofenceListener;
import com.nagash.appwebbrowser.model.geofencing.GeofenceManager;
import com.nagash.appwebbrowser.model.geofencing.GeofenceObject;
import com.nagash.appwebbrowser.model.geofencing.TriggeredGeofenceableContainer;
import com.nagash.appwebbrowser.model.geofencing.options.GeofenceOptions;
import com.nagash.appwebbrowser.model.geofencing.options.ScannerIntervalMode;
import com.nagash.appwebbrowser.model.localization.LocationManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
    private static final int NEARBY_SCAN_FREQUENCY = 1000;
    private static final int PROXIMITY_SCAN_FREQUENCY = 1000;


    private List<WebAppListener> webAppListeners = new ArrayList<>();
    private GeofenceManager<WebApp> nearbyGeofenceManager = null;
    private GeofenceManager<WebApp> proximityGeofenceManager = null;
    private EddystoneProximityManager eddystoneProximityManager;

    private GeofenceOptions nearbyGeofenceOptions;
    private GeofenceOptions proximityGeofenceOptions;

    public WebAppController(LocationManager locationManager) {
        nearbyGeofenceOptions = new GeofenceOptions();
        nearbyGeofenceOptions.setExtraRadius(3000000);
        nearbyGeofenceOptions.useExtraRadius();
        //nearbyGeofenceOptions.advertiseOnEmptyList();
        nearbyGeofenceOptions.setScannerIntervalMode(ScannerIntervalMode.timerBased);
        nearbyGeofenceManager = new GeofenceManager<>(locationManager, nearbyGeofencListener, nearbyGeofenceOptions);

        proximityGeofenceOptions = new GeofenceOptions();
        proximityGeofenceOptions.setExtraRadius(100);
        proximityGeofenceOptions.useExtraRadius();
        proximityGeofenceOptions.setScannerIntervalMode(ScannerIntervalMode.timerBased);
        //proximityGeofenceOptions.advertiseOnEmptyList();
        proximityGeofenceManager = new GeofenceManager<>(locationManager, proxyGeofencListener, proximityGeofenceOptions);

        eddystoneSettings = new EddystoneProximityManager.Settings();
        eddystoneSettings.advertiseInProximity(true);
        eddystoneSettings.advertiseConnectedOutProximity(true);
    }

    public void setOnUpdateListener(WebAppListener listener) {
        this.webAppListeners.add(listener);
    }


    private Collection<GeofenceObject<WebApp>> geofenceObjects;
    private Map<String, WebApp> webAppBeaconMap = new HashMap<>();

    private EddystoneProximityManager.Settings eddystoneSettings;


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




    private SortedSet<WebApp> nearbySet = null;
    private SortedSet<WebApp> proximitySet = null;


    boolean changes = false;

    // * * * * * * * * * * * * GEOFENCE LISTENER  * * * * * * * * * * * *
    private GeofenceListener<WebApp>  nearbyGeofencListener = new GeofenceListener<WebApp>() {

        @Override
        public void onGeofenceScanTask(TriggeredGeofenceableContainer<WebApp> triggered) {
            nearbyGeofenceManager.stopScan();
            if(triggered.isAtLeastOneEntering()) {
                changes = true;
                proximityGeofenceManager.addAll(triggered.enteringSet,  new GeoEvent().enableEnteringEvent().enableExitingEvent());
            }
            if(triggered.isAtLeastOneExiting()) {
                changes = true;
                proximityGeofenceManager.removeAll(triggered.exitingSet);
            }

            if(triggered.isAtLeastOneIn()) {
                nearbySet = triggered.inSet;
            }
            proximityGeofenceManager.startScan(PROXIMITY_SCAN_FREQUENCY);
        }


    };
    private GeofenceListener<WebApp>  proxyGeofencListener = new GeofenceListener<WebApp>() {

        @Override
        public void onGeofenceScanTask(TriggeredGeofenceableContainer<WebApp> triggered) {
            proximityGeofenceManager.stopScan();
            if(triggered.isAtLeastOneEntering() || triggered.isAtLeastOneExiting()) {
                changes = true;
                proximitySet = triggered.inSet;
            }

            if(changes)
                for(WebAppListener l : webAppListeners)
                    l.onWebAppUpdate(proximitySet, nearbySet);


            // reset changes flag befor start new scan loop (scan loop: nearby scan -> proxy scan )
            changes = false;
            nearbyGeofenceManager.startScan(NEARBY_SCAN_FREQUENCY);
        }


    };





    public static SortedSet<WebApp> getNearbyNotInProximity( SortedSet<WebApp> proximitySet, SortedSet<WebApp> nearbySet ) {
        SortedSet<WebApp> nearbyNotInproximitySet = new TreeSet<>(nearbySet);
        if(proximitySet != null)
            nearbyNotInproximitySet.removeAll(proximitySet);
        return nearbyNotInproximitySet;
    }

}
