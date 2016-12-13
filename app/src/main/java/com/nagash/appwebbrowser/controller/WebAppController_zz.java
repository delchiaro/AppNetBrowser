package com.nagash.appwebbrowser.controller;

import android.location.Location;

import com.estimote.sdk.eddystone.Eddystone;
import com.nagash.appwebbrowser.controller.fragments.map.WebAppMapFragment;
import com.nagash.appwebbrowser.model.beacon.eddystoneProximity.EddystoneProximityListener;
import com.nagash.appwebbrowser.model.beacon.eddystoneProximity.EddystoneProximityManager;
import com.nagash.appwebbrowser.model.connection.AppListDownloadHandler;
import com.nagash.appwebbrowser.model.connection.CentralConnection;
import com.nagash.appwebbrowser.model.localization.LocationEventListener;
import com.nagash.appwebbrowser.model.localization.LocationManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by nagash on 06/12/16.
 */




/**
 * This class have to manage the ordering by distance, providing an interface to get the ordered list.
 * Also have to manage the beacon proximity and return a list of WebApps associated to the beacons in the nearby.
 */
public class WebAppController_zz implements EddystoneProximityListener {

    Location currentLocation = null;
    List<WebApp> webAppList = null;
    Map<String, WebApp> eddystoneWebAppMap = null;
    Set<WebApp> webAppProxyBeacon = null;

    List<WebAppGpsProximity> webAppListGPS = null;
    HashSet<WebAppGpsProximity> webAppGps = null;
    // use a TreeSet webAppGpsTree.addAll(webAppGps) to sort the webAppGps list!


//    private static double webAppDistance(Location currentLocation, WebApp app) {
//        return currentLocation.distanceTo(app.getLocation());
//    }

    private EddystoneProximityManager eddystoneManager;

    public WebAppController_zz(List<WebApp> downloadedApps, EddystoneProximityManager eddystoneManager) {
        this.eddystoneManager = eddystoneManager;
        this.eddystoneManager.addListener(this);

        // We need this two events to get this manager working.
        this.eddystoneManager.getScanner().getEventSettings().eventLostConnection = true;
        this.eddystoneManager.getScanner().getEventSettings().eventNewConnection = true;

        updateAppList(downloadedApps);
    }





    private void updateAppList(List<WebApp> appList) {
        this.webAppList = appList;
        this.webAppListGPS = new ArrayList<>(webAppList.size());
        webAppProxyBeacon = new HashSet<>(webAppList.size());
        eddystoneWebAppMap = new HashMap<>(webAppList.size());

        for(WebApp  app : webAppList)
            if(app.getLocation() != null)
                this.webAppListGPS.add(new WebAppGpsProximity(app));

        for(WebApp app : webAppList)
            if(app.getUID() != null)
                eddystoneWebAppMap.put(app.getUID(), app);

        if(currentLocation!=null)
            updateSorting();
    }

    private void updateSorting() {
        if(webAppListGPS != null)
            java.util.Collections.sort(webAppListGPS);
    }

    public void updatePosition(Location myLocation) {
        this.currentLocation = myLocation;
        updateSorting();
    }




    @Override
    public void onBeaconNotInProximity(Set<Eddystone> paramSet) {}

    @Override
    public void onBeaconProximity(Set<Eddystone> paramSet) {}

    @Override
    public void onLostBeaconProximity(Set<Eddystone> paramSet) {
        for(Eddystone eddy : paramSet)
            if(webAppProxyBeacon.contains(eddy.eid + eddy.instance))
                webAppProxyBeacon.remove( eddystoneWebAppMap.get(eddy.eid+eddy.instance) );
    }

    @Override
    public void onNewBeaconProximity(Set<Eddystone> paramSet) {
        for(Eddystone eddy : paramSet)
            if(eddystoneWebAppMap.keySet().contains(eddy.eid + eddy.instance))
                webAppProxyBeacon.add(eddystoneWebAppMap.get(eddy.eid+eddy.instance));
    }




    





    private  class WebAppGpsProximity implements Comparable<WebAppGpsProximity> {
        WebApp app;

        WebAppGpsProximity(WebApp app) {
            this.app = app;
        }

        public WebApp getApp() {
            return this.app;
        }
        @Override
        public int compareTo(WebAppGpsProximity cWebApp) {
            if(currentLocation != null) {
                double d1 = this.app.getLocation().distanceTo(currentLocation);
                double d2 = cWebApp.getApp().getLocation().distanceTo(currentLocation);
                if(d1<d2) return 1;
                else if(d1>d2) return -1;
                else return 0;
            }
            else return 0;
        }
    }


}

