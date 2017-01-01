package com.nagash.appwebbrowser.controller.webAppController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
import com.nagash.appwebbrowser.utils.BlueUtility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        eddystoneSettings.advertiseLostProximity(true);
        eddystoneSettings.advertiseOnEmptyList(true);
        eddystoneSettings.advertiseNewProximity(true);

    }

    public void setOnUpdateListener(WebAppListener listener) {
        this.webAppListeners.add(listener);
    }


    private Collection<GeofenceObject<WebApp>> geofenceObjects;
    private Map<String, WebApp> webAppBeaconMap = new HashMap<>();

    private EddystoneProximityManager.Settings eddystoneSettings;


    public void start(List<WebApp> allApps, Activity activity) {
        for(WebApp app : allApps)
            webAppBeaconMap.put(app.getUID(), app);

        if(BlueUtility.isBleSupported()) {
            eddystoneProximityManager = new EddystoneProximityManager(EddystoneScanner.getInstance(activity), eddystoneSettings);
            eddystoneProximityManager.addListener(this);
            eddystoneProximityManager.start();
        }

//        eddystoneProximityManager.getScanner().

        nearbyGeofenceManager.addAll(allApps, new GeoEvent().enableAllEvent());
        nearbyGeofenceManager.startScan(NEARBY_SCAN_FREQUENCY);
        //proximityGeofenceManager.startScan(PROXIMITY_SCAN_FREQUENCY);
    }



    public void stop() {
        if (BlueUtility.isBleSupported()) {
            if(eddystoneProximityManager!=null)
                eddystoneProximityManager.stop();
        }
        if(nearbyGeofenceManager != null)
            nearbyGeofenceManager.stopScan();
    }






    // * * * * * * * * * * * * EDDYSTONE LISTENER  * * * * * * * * * * * *
    @Override public void onBeaconNotInProximity(Set<Eddystone> paramSet)   {}
    @Override public void onBeaconProximity(Set<Eddystone> paramSet)        {}
    @Override public void onLostBeaconProximity(Set<Eddystone> paramSet)    {
        // update proximity list! (remove items, re-add the item in the nearby list)
        boolean changes = false;
        for( Eddystone e : paramSet) {
            WebApp app = webAppBeaconMap.get(e.namespace + e.instance);
            app.setUserNearBeacon(false);
//            appsBeaconRemovedInLastScan.add( app );
            appsBeacon.remove( app );
//            appsNearby.add(app);
            changes = true;
        }

        if( changes )
            callListeners(appsBeacon, appsProxy, appsNearby);
    }
    @Override public void onNewBeaconProximity(Set<Eddystone> paramSet)     {
        // update proximity list! (add items, higher priority over Geofence events! Remove the same item from nearby if present, until out of beacon proximity)
        boolean changes = false;
        for( Eddystone e : paramSet) {
            WebApp app = webAppBeaconMap.get(e.namespace + e.instance);
            app.setUserNearBeacon(true);
            appsBeacon.add( app );
           // appsBeaconRemovedInLastScan.remove( app );
           // appsNearby.remove(app);
            changes = true;
        }
        if( changes )
            callListeners(appsBeacon, appsProxy, appsNearby);


    }


    private LinkedHashSet<WebApp> appsBeaconRemovedInLastScan = new LinkedHashSet<>();
    private LinkedHashSet<WebApp> appsBeacon = new LinkedHashSet<>();
    private Collection<WebApp> appsNearby = new LinkedHashSet<>();
    private Collection<WebApp> appsProxy = new LinkedHashSet<>();


    boolean localizAppsChanges = false;

    // * * * * * * * * * * * * GEOFENCE LISTENER  * * * * * * * * * * * *
    private GeofenceListener<WebApp>  nearbyGeofencListener = new GeofenceListener<WebApp>() {

        @Override
        public void onGeofenceScanTask(TriggeredGeofenceableContainer<WebApp> triggered) {
            nearbyGeofenceManager.stopScan();
            if(triggered.isAtLeastOneEntering()) {
                localizAppsChanges = true;
                proximityGeofenceManager.addAll(triggered.enteringSet,  new GeoEvent().enableEnteringEvent().enableExitingEvent());
            }
            if(triggered.isAtLeastOneExiting()) {
                localizAppsChanges = true;
                proximityGeofenceManager.removeAll(triggered.exitingSet);
            }

            appsNearby = triggered.inSet;

            proximityGeofenceManager.startScan(PROXIMITY_SCAN_FREQUENCY);
        }


    };
    private GeofenceListener<WebApp>  proxyGeofencListener = new GeofenceListener<WebApp>() {

        @Override
        public void onGeofenceScanTask(TriggeredGeofenceableContainer<WebApp> triggered) {
            proximityGeofenceManager.stopScan();
            if(triggered.isAtLeastOneEntering() || triggered.isAtLeastOneExiting()) {
                localizAppsChanges = true;
            }

            appsProxy = triggered.inSet;

            //if(localizAppsChanges)
            callListeners(appsBeacon, appsProxy, appsNearby);



            // reset localizAppsChanges flag befor start new scan loop (scan loop: nearby scan -> proxy scan )
            localizAppsChanges = false;
            nearbyGeofenceManager.startScan(NEARBY_SCAN_FREQUENCY);
        }


    };


    private void callListeners(Collection appsBeacon, Collection appsProxy, Collection appsNearby) {
        for (WebAppListener l : webAppListeners)
            l.onWebAppUpdate(Collections.unmodifiableCollection(appsBeacon), Collections.unmodifiableCollection(appsProxy), Collections.unmodifiableCollection(appsNearby));

    }


//
//
//    public static SortedSet<WebApp> getNearbyAppsNotInProximity(@Nullable SortedSet<WebApp> appsProxy,@Nullable SortedSet<WebApp> appsNearby ) {
//        if(appsProxy == null) return appsNearby;
//        if(appsNearby == null) return null;
//
//        SortedSet<WebApp> nearbyNotInproximitySet = new TreeSet<>(appsNearby);
//        //if(appsProxy != null)
//            nearbyNotInproximitySet.removeAll(appsProxy);
//        return nearbyNotInproximitySet;
//    }
//
//    public static SortedSet<WebApp> getProximityAppsNotInBeacons(@Nullable Collection<WebApp> appsBeacon,@Nullable SortedSet<WebApp> appsProxy ) {
//        if(appsBeacon == null) return appsProxy;
//        if(appsProxy == null) return null;
//
//        SortedSet<WebApp> proxyNotInBeacons = new TreeSet<>(appsProxy);
//        //if(appsBeacon != null)
//            proxyNotInBeacons.removeAll(appsBeacon);
//        return proxyNotInBeacons;
//    }

}
