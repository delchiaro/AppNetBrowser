package com.nagash.appwebbrowser.model.webapp;

import android.location.Location;
import android.support.annotation.NonNull;

import com.nagash.appwebbrowser.model.geofencing.Geofenceable;

import protobuf.app.App;

/**
 * Created by nagash on 22/09/16.
 */
public class WebApp implements Geofenceable {

    private final int defaultGeofenceRadius = 30;
    private final App.WebApp protoWebApp;

    private final App.EddystoneBeacon eddystoneBeacon;


    Location location;


    public WebApp(@NonNull App.WebApp protoWebApp)
    {
        this.protoWebApp = protoWebApp;
        this.eddystoneBeacon = protoWebApp.getBeacon();

        if(protoWebApp.getLat().equals("") == false && protoWebApp.getLong().equals("") == false)
        {
            location = new Location("webApp " + protoWebApp.getAppName());
            location.setLatitude(Double.parseDouble(protoWebApp.getLat()));
            location.setLongitude(Double.parseDouble(protoWebApp.getLong()));
        }
    }


    public final String getUID_instance() {
        return eddystoneBeacon.getUIDInstance();
    }
    public final String getUID_namespace() {
        return eddystoneBeacon.getUIDNamespace();
    }
    public final String getUID() {
        return getUID_namespace() + getUID_instance();
    }
    public final String getURL() {
        return eddystoneBeacon.getURL();
    }


    public final String getId() {
        return protoWebApp.getAppId();
    }
    public final String getName() {
        return protoWebApp.getAppName();
    }
    public final int getVersionCode() {
        return protoWebApp.getVersionCode();
    }
    public final String getVersionName() {
        return protoWebApp.getVersionName();
    }

    public final int    getGeofenceRadius() {
        return defaultGeofenceRadius;
    }

    public Double getLatitude() {
        if(location != null) return location.getLatitude();
        else return null;
    }
    public Double getLongitude() {
        if(location != null) return location.getLongitude();
        else return null;
    }


    public final Location getLocation() {
        return location;
    }
    public int getProximityRadius() {
        return defaultGeofenceRadius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebApp webApp = (WebApp) o;

        return protoWebApp.getAppId().equals(webApp.protoWebApp.getAppId());

    }

    @Override
    public int hashCode() {
        return protoWebApp.getAppId().hashCode();
    }


    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        WebApp webApp = (WebApp) o;
//
//        if (!protoWebApp.getAppId().equals(webApp.protoWebApp.getAppId())) return false;
//        if (eddystoneBeacon != null ? !eddystoneBeacon.equals(webApp.eddystoneBeacon) : webApp.eddystoneBeacon != null)
//            return false;
//        return location != null ? location.equals(webApp.location) : webApp.location == null;
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result = defaultGeofenceRadius;
//        result = 31 * result + protoWebApp.getAppId().hashCode();
//        return result;
//    }
}
