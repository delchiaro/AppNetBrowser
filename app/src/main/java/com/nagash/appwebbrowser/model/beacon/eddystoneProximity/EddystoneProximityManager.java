package com.nagash.appwebbrowser.model.beacon.eddystoneProximity;

import com.estimote.sdk.eddystone.*;
import com.nagash.appwebbrowser.model.beacon.eddystoneScanner.*;
import android.content.*;
import com.estimote.sdk.*;
import java.util.*;

public class EddystoneProximityManager implements EddystoneScannerListener
{
    public static final double DISTANCE_ALL = Double.MAX_VALUE;
    public static final double DISTANCE_NEAR = 0.5;
    public static final long serialVersionUID = 0L;

    public double proximityDistance = DISTANCE_NEAR;
    private List<EddystoneProximityListener> listeners;
    private Set<Eddystone> proximityBeacons;
    public EddystoneScanner scanner;
    public Settings settings;

    public EddystoneProximityManager(final Context context, final Settings settings) {
        this(EddystoneScanner.getInstance(context), settings);
    }

    public EddystoneProximityManager(final EddystoneScanner scanner, final Settings settings) {
        // this.proximityDistance = DISTANCE_NEAR;
        this.listeners = new ArrayList<>();
        this.proximityBeacons = new HashSet<>();
        this.settings = settings;
        this.scanner = scanner;
        scanner.getEventSettings().eventEmptyScan(settings.proximityEventOnEmpty).eventScan(true);
        scanner.addProximityListener(this);
    }


    public static Settings generator() {
        return new Settings();
    }

    private static double getDistance(final Eddystone eddystone) {
        return Utils.computeAccuracy(eddystone);
    }

    private boolean isInProximity(final Eddystone eddystone) {
        if (Utils.computeAccuracy(eddystone) < this.proximityDistance)
            return true;
        else return false;
    }

    public void addListener(final EddystoneProximityListener eddystoneProximityListener) {
        this.listeners.add(eddystoneProximityListener);
    }

    public EddystoneScanner getScanner() {
        return this.scanner;
    }

    public void onLostConnectedBeacons(final Set<Eddystone> set) {}

    public void onNewConnectedBeacons(final Set<Eddystone> set) {}

    public void onScanBeacons(final List<Eddystone> list) {

        List<Eddystone> eddyList;
        if ((eddyList = list) == null) {
            eddyList = new ArrayList<>(0);
        }

        final HashSet<Eddystone> proximityBeacons = new HashSet<>(eddyList.size());
        final HashSet<Eddystone> notProximityBeacons = new HashSet<>(eddyList.size());

        for (final Eddystone eddystone : eddyList)
        {
            if (this.isInProximity(eddystone))
                proximityBeacons.add(eddystone);
            else notProximityBeacons.add(eddystone);
        }

        if (this.settings.newProximityEvent)
        {
            final HashSet<Eddystone> newProximityBeacons = new HashSet<>(proximityBeacons.size());

            for (final Eddystone eddystone : proximityBeacons)
                if (!this.proximityBeacons.contains(eddystone))
                    newProximityBeacons.add(eddystone);

            if (newProximityBeacons.size() > 0)
                for(EddystoneProximityListener listener: this.listeners )
                    listener.onNewBeaconProximity(newProximityBeacons);
        }

        if (this.settings.lostProximiyEvent)
        {
            final HashSet<Eddystone> lostProximityBeacons = new HashSet<>(this.proximityBeacons.size());

            for (final Eddystone eddystone : this.proximityBeacons)
                if (!proximityBeacons.contains(eddystone))
                    lostProximityBeacons.add(eddystone);

            if (lostProximityBeacons.size() > 0)
                for(EddystoneProximityListener listener: this.listeners )
                    listener.onLostBeaconProximity(lostProximityBeacons);
        }


        if (this.settings.inProximityEvent)
        {
            if (proximityBeacons.size() > 0)
                for(EddystoneProximityListener listener: this.listeners )
                    listener.onBeaconProximity(proximityBeacons);

            else
                for(EddystoneProximityListener listener: this.listeners )
                    listener.onBeaconProximity(null);
        }

        if (this.settings.connectedOutProximityEvent && notProximityBeacons.size() > 0)
            for(EddystoneProximityListener listener: this.listeners )
                listener.onBeaconNotInProximity(notProximityBeacons);

        this.proximityBeacons = (Set<Eddystone>)proximityBeacons;

    }

    public void setDisconnectHisteresis(final int disconnectHisteresis) {
        this.scanner.setDisconnectHisteresis(disconnectHisteresis);
    }

    public void setProximityDistance(final double proximityDistance) {
        this.proximityDistance = proximityDistance;
    }

    public void setScanPeriod(final long n, final long n2) {
       this.scanner.setScanPeriod(n, n2);
    }

    public void start() {
        this.scanner.startScan();
    }

    public void stop() {
        this.scanner.stopScan();
    }

    public static class Settings
    {
        public boolean connectedOutProximityEvent;
        public boolean inProximityEvent;
        public boolean lostProximiyEvent;
        public boolean newProximityEvent;
        public boolean proximityEventOnEmpty;

        public Settings() {
            this.proximityEventOnEmpty = false;
            this.inProximityEvent = true;
            this.connectedOutProximityEvent = false;
            this.newProximityEvent = false;
            this.lostProximiyEvent = false;
        }


        public Settings advertiseConnectedOutProximity(final boolean connectedOutProximityEvent) {
            this.connectedOutProximityEvent = connectedOutProximityEvent;
            return this;
        }

        public Settings advertiseInProximity(final boolean inProximityEvent) {
            this.inProximityEvent = inProximityEvent;
            return this;
        }

        public Settings advertiseLostProximity(final boolean lostProximiyEvent) {
            this.lostProximiyEvent = lostProximiyEvent;
            return this;
        }

        public Settings advertiseNewProximity(final boolean newProximityEvent) {
            this.newProximityEvent = newProximityEvent;
            return this;
        }

        public Settings advertiseOnEmptyList(final boolean proximityEventOnEmpty) {
            this.proximityEventOnEmpty = proximityEventOnEmpty;
            return this;
        }

        public EddystoneProximityManager build(final Context context) {
             return new EddystoneProximityManager(context, this);
        }
    }
}
