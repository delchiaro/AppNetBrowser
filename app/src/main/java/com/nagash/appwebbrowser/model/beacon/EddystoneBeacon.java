package com.nagash.appwebbrowser.model.beacon;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.estimote.sdk.connection.settings.Eddystone;
import com.facebook.infer.annotation.SuppressFieldNotNullable;

import javax.annotation.concurrent.NotThreadSafe;

import protobuf.app.App;

/**
 * Created by root on 16/12/16.
 */

public class EddystoneBeacon implements Parcelable {

    private String UID_instance = null;
    private String UID_namespace = null;
    private String TLM = null;
    private String URL = null;


    public EddystoneBeacon(App.EddystoneBeacon protobufEddy) {
        if(protobufEddy != null)
        {
            if(protobufEddy.hasTLM()) TLM = protobufEddy.getTLM();
            if(protobufEddy.hasUIDInstance()) UID_instance = protobufEddy.getUIDInstance();
            if(protobufEddy.hasUIDNamespace())  UID_namespace = protobufEddy.getUIDNamespace();
            if(protobufEddy.hasURL()) URL = protobufEddy.getURL();
        }
    }

    public EddystoneBeacon(@NonNull String UID_namespace,@NonNull String UID_instance) {
        this.UID_instance = UID_instance;
        this.UID_namespace = UID_namespace;
    }
    public EddystoneBeacon(@NonNull String URL) {
        this.URL = URL;
    }

    public EddystoneBeacon(@NonNull String namespace,@NonNull String instance, String TLM, String URL) {
        this(namespace, instance);
        if(TLM != null) this.TLM = TLM;
        if(URL != null) this.URL = URL;
    }



    public String getUidInstance() {
        return UID_instance;
    }
    public String getUidNamsepace() {
        return UID_namespace;
    }
    public String getUID() {
        return UID_namespace + UID_instance;
    }
    public String getTLM() {
        return TLM;
    }
    public String getURL() {
        return URL;
    }

    public boolean hasUID() {
        return ( UID_instance != null && UID_namespace != null);
    }
    public boolean hasTLM() {
        return TLM != null;
    }
    public boolean hasURL() {
        return URL != null;
    }








    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EddystoneBeacon that = (EddystoneBeacon) o;

        if (UID_instance != null ? !UID_instance.equals(that.UID_instance) : that.UID_instance != null)
            return false;
        if (UID_namespace != null ? !UID_namespace.equals(that.UID_namespace) : that.UID_namespace != null)
            return false;
        if (TLM != null ? !TLM.equals(that.TLM) : that.TLM != null) return false;
        return URL != null ? URL.equals(that.URL) : that.URL == null;

    }

    @Override public int hashCode() {
        int result = UID_instance != null ? UID_instance.hashCode() : 0;
        result = 31 * result + (UID_namespace != null ? UID_namespace.hashCode() : 0);
        result = 31 * result + (TLM != null ? TLM.hashCode() : 0);
        result = 31 * result + (URL != null ? URL.hashCode() : 0);
        return result;
    }






    // * * * * * * * * PARCELABLE INTERFACE * * * * * * * *
    protected EddystoneBeacon(Parcel in) {
        UID_namespace=in.readString();
        UID_instance=in.readString();
        TLM=in.readString();
        URL=in.readString();
    }
    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(UID_namespace);
        parcel.writeString(UID_instance);
        parcel.writeString(TLM);
        parcel.writeString(URL);
    }
    public static final Creator<EddystoneBeacon> CREATOR = new Creator<EddystoneBeacon>() {
        @Override public EddystoneBeacon createFromParcel(Parcel in) {
            return new EddystoneBeacon(in);
        }
        @Override public EddystoneBeacon[] newArray(int size) {
            return new EddystoneBeacon[size];
        }
    };
    @Override public int describeContents() {
        return hashCode();
    }
}
