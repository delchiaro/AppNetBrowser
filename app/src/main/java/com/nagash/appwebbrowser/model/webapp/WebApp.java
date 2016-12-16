package com.nagash.appwebbrowser.model.webapp;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.nagash.appwebbrowser.model.beacon.EddystoneBeacon;
import com.nagash.appwebbrowser.model.geofencing.Geofenceable;

import protobuf.app.App;

/**
 * Created by nagash on 22/09/16.
 */
public class WebApp implements Geofenceable, Parcelable {

    private static final int DEFAULT_PROXIMITY_RADIUS = 30;

    private final EddystoneBeacon eddystoneBeacon;

    private final String appId;
    private final String appName;
    private final String indexFile;
    private final String versionName;
    private final int    versionCode;
    private final int    proximityRadius;
    // TODO: insert proximityRadius in protobuf

    private final String mainClass;

    private final Location location;


    public WebApp(@NonNull App.WebApp protoWebApp)
    {
        this.appId = protoWebApp.getAppId();
        this.appName = protoWebApp.getAppName();
        this.versionName = protoWebApp.getVersionName();
        this.versionCode = protoWebApp.getVersionCode();
        this.proximityRadius = DEFAULT_PROXIMITY_RADIUS;

        if(protoWebApp.getIndexFileAndroid() != null && protoWebApp.getIndexFileAndroid().equals("") == false)
            this.indexFile = protoWebApp.getIndexFileAndroid();
        else this.indexFile = protoWebApp.getIndexFile();

        if(protoWebApp.getMainAndroidClass() != null && protoWebApp.getMainAndroidClass().equals("") == false)
            this.mainClass = protoWebApp.getMainAndroidClass();
        else this.mainClass = protoWebApp.getMainClass();


        this.eddystoneBeacon = new EddystoneBeacon(protoWebApp.getBeacon() );

        if(protoWebApp.getLat().equals("") == false && protoWebApp.getLong().equals("") == false)
        {
            location = new Location("webApp " + protoWebApp.getAppName());
            location.setLatitude(Double.parseDouble(protoWebApp.getLat()));
            location.setLongitude(Double.parseDouble(protoWebApp.getLong()));
        }
        else location = null;
    }



    public final EddystoneBeacon getEddystone() {
        return eddystoneBeacon;
    }
    public final String getUID_instance() {
        return eddystoneBeacon.getUidInstance();
    }
    public final String getUID_namespace() {
        return eddystoneBeacon.getUidNamsepace();
    }
    public final String getUID() {
        return eddystoneBeacon.getUID();
    }
    public final String getURL() {
        return eddystoneBeacon.getURL();
    }
    public final String getTLM() {
        return eddystoneBeacon.getTLM();
    }


    public final String getId() {
        return appId;
    }
    public final String getName() {
        return appName;
    }
    public final int getVersionCode() {
        return versionCode;
    }
    public final String getVersionName() {
        return versionName;
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
        return proximityRadius;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebApp webApp = (WebApp) o;
        return appId.equals(webApp.appId);

    }

    @Override
    public int hashCode() {
        return appId.hashCode();
    }












    // * * * * * * * * PARCELABLE INTERFACE * * * * * * * *
    @Override public int describeContents() {
        return hashCode();
    }
    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(eddystoneBeacon, i);
        parcel.writeString(appId);
        parcel.writeString(appName);
        parcel.writeString(indexFile);
        parcel.writeString(versionName);
        parcel.writeInt(versionCode);
        parcel.writeInt(proximityRadius);
        parcel.writeString(mainClass);
        parcel.writeParcelable(location, i);
    }
    protected WebApp(Parcel in) {
        eddystoneBeacon = in.readParcelable(EddystoneBeacon.class.getClassLoader());
        appId = in.readString();
        appName = in.readString();
        indexFile = in.readString();
        versionName = in.readString();
        versionCode = in.readInt();
        proximityRadius = in.readInt();
        mainClass = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<WebApp> CREATOR = new Creator<WebApp>() {
        @Override public WebApp createFromParcel(Parcel in) { return new WebApp(in); }
        @Override public WebApp[] newArray(int size) { return new WebApp[size]; }
    };




//        @Override
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
