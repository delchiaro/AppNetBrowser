//package com.nagash.appwebbrowser.model.beacon.eddystoneScanner;
//
//import com.estimote.sdk.BeaconManager;
//import com.estimote.sdk.eddystone.Eddystone;
//import com.nagash.appwebbrowser.model.beacon.eddystoneProximity.EddystoneProximityListener;
//
//import java.util.List;
//import java.util.Set;
//
///**
// * Created by nagash on 06/12/16.
// */
//public class EddystoneScanner {
//
//    class EventSettings {
//        protected boolean eventLostConnection;
//        protected boolean eventNewConnection;
//        protected boolean eventOnEmptyScan;
//        protected boolean eventScan;
//
//        public EventSettings eventScan(boolean value)               { this.eventScan = value; return this; }
//        public EventSettings eventOnEmptyScan(boolean value)        { this.eventOnEmptyScan = value; return this; }
//        public EventSettings eventNewConnection(boolean value)      { this.eventNewConnection = value; return this; }
//        public EventSettings eventLostConnection(boolean value)     { this.eventLostConnection = value; return this; }
//    }
//
//    enum ScanningStatus {
//
//    }
//
//    private static EddystoneScanner instance; // singletone!
//
//
//    private EventSettings eventSettings;
//
//    private static final long DEFAULT_BACKGROUND_SCAN_PERIOD = 0;
//    private static final long DEFAULT_BACKGROUND_SLEEP_PERIOD = Long.MAX_VALUE;
//    private static final long DEFAULT_SCAN_PERIOD = 1000;
//    private static final long DEFAULT_SLEEP_PERIOD = 999;
//
//    private long backgroundScanPeriod = DEFAULT_BACKGROUND_SCAN_PERIOD;
//    private long backgroundSleepPeriod = DEFAULT_BACKGROUND_SLEEP_PERIOD;
//    private long scanPeriod = DEFAULT_SCAN_PERIOD;
//    private long sleepPeriod = DEFAULT_SLEEP_PERIOD;
//    private int  disconnectHisteresis;
//    private long lastScanTime;
//
//
//    ScanningStatus scanningStatus;
//
//    Set<Eddystone> connectedBeacons;
//    List<EddystoneProximityListener> proximityListeners;
//
//
//    private BeaconManager beaconManager = null;
//    int scanId;
//
//
//    public void addProximityListener(EddystoneScannerListener listener) {
//
//    }
//
//    public void getDistance(Eddystone eddystone) {}
//
//
//}
//
//
//
//
//


package com.nagash.appwebbrowser.model.beacon.eddystoneScanner;

import com.estimote.sdk.eddystone.*;
import android.content.*;
import android.util.*;
import java.util.*;
import com.estimote.sdk.*;

public class EddystoneScanner implements BeaconManager.EddystoneListener
{
    public enum ScanningStatus    { NOT_SCANNING, SCANNING, WAIT_FOR_SERVICE }
    public enum ServiceStatus    { CONNECTED, CONNECTING, DESTROYED, DISCONNECTED }



    private static final long DEFAULT_BACKGROUND_SCAN_PERIOD = 1L;
    private static final long DEFAULT_BACKGROUND_SLEEP_PERIOD = 9223372036854775806L;
    private static final long DEFAULT_SCAN_PERIOD = 1000L;
    private static final long DEFAULT_SLEEP_PERIOD = 1000L;
    private static EddystoneScanner instance;
    public static final long serialVersionUID = 0L;
    private long backgroundScanPeriod;
    private long backgroundSleepPeriod;
    private BeaconManager beaconManager;
    private Set<Eddystone> connectedBeacons;
    private int disconnectHisteresis;
    private EventSettings eventSettings;
    private Long lastScanTime;
    private List<EddystoneScannerListener> proximityListeners;
    private String scanId;
    private long scanPeriod;
    public ScanningStatus scanningStatus;
    public ServiceStatus serviceStatus;
    private long sleepPeriod;

    static {
        EddystoneScanner.instance = null;
    }

    private EddystoneScanner(final Context context) {
        this.eventSettings = new EventSettings();
        this.serviceStatus = ServiceStatus.DISCONNECTED;
        this.scanningStatus = ScanningStatus.NOT_SCANNING;
        this.scanPeriod = 1000L;
        this.sleepPeriod = 1000L;
        this.backgroundScanPeriod = 1L;
        this.backgroundSleepPeriod = 9223372036854775806L;
        this.scanId = null;
        this.beaconManager = null;
        this.disconnectHisteresis = 2;
        this.lastScanTime = null;
        this.proximityListeners = new ArrayList<EddystoneScannerListener>();
        this.connectedBeacons = new HashSet<Eddystone>();
        this.beaconManager = new BeaconManager(context);
        this.beaconManager.setEddystoneListener(this);
    }


    public static double getDistance(final Eddystone eddystone) {
        return Utils.computeAccuracy(eddystone);
    }

    public static EddystoneScanner getInstance(final Context context) {
        if (EddystoneScanner.instance == null)
            return EddystoneScanner.instance = new EddystoneScanner(context);
        else return EddystoneScanner.instance;
    }

    public static boolean isInProximity(final Eddystone eddystone) {
        if (Utils.computeProximity(eddystone) == Utils.Proximity.NEAR)
            return true;
        else return false;
    }

    public static boolean isIstanziated() {
        return EddystoneScanner.instance != null;
    }

    public void addProximityListener(final EddystoneScannerListener eddystoneScannerListener) {
        this.proximityListeners.add(eddystoneScannerListener);
    }

    public void destroyService() {
        if (this.serviceStatus != ServiceStatus.DESTROYED) {
            this.stopScan();
            this.beaconManager.disconnect();
            this.serviceStatus = ServiceStatus.DESTROYED;
        }
    }

    public void finalize() throws Throwable {
        super.finalize();
        this.destroyService();
    }

    public EventSettings getEventSettings() {
        return this.eventSettings;
    }

    public void onEddystonesFound(final List<Eddystone> list) {
        final long currentTimeMillis = System.currentTimeMillis();
        if (this.lastScanTime == null || currentTimeMillis - this.lastScanTime >= this.sleepPeriod + this.scanPeriod)
        {
            this.lastScanTime = currentTimeMillis;
            Log.i("BeaconTestActivity", "-----------------");



            List<Eddystone> eddyList = null;

            // Essere umano:
            if(list == null || list.size() == 0)
                eddyList = new ArrayList<>(0);
            else eddyList = list;
//
//            // Bestia di satana:
//            Label_0111: {
//                if (list != null) {
//                    eddyList = list;
//                    if (list.size() > 0) {
//                        break Label_0111;
//                    }
//                }
//                eddyList = new ArrayList<Eddystone>(0);
//            }



            if (this.eventSettings.eventLostConnection || this.eventSettings.eventNewConnection)
            {
                final HashSet<Eddystone> connectedBeacons = new HashSet<>(eddyList);

                if (this.eventSettings.eventNewConnection)
                {
                    final HashSet<Eddystone> connectedSet = new HashSet<>(eddyList.size());

                    for (final Eddystone eddystone : connectedSet)
                        if (!this.connectedBeacons.contains(eddystone))
                            connectedSet.add(eddystone);

                    if (connectedSet.size() > 0)
                        for(EddystoneScannerListener listener : this.proximityListeners)
                            listener.onNewConnectedBeacons(connectedSet);
                }

                if (this.eventSettings.eventLostConnection)
                {
                    final HashSet lostConnectionSet = new HashSet<Eddystone>(eddyList.size());

                    for (final Eddystone eddystone : this.connectedBeacons)
                        if (!connectedBeacons.contains(eddystone))
                            lostConnectionSet.add(eddystone);

                    if (lostConnectionSet.size() > 0)
                        for(EddystoneScannerListener listener : this.proximityListeners)
                            listener.onLostConnectedBeacons(lostConnectionSet);
                }

                this.connectedBeacons = connectedBeacons;
            }

            if (this.eventSettings.eventScan && eddyList.size() > 0 )
                for(EddystoneScannerListener listener : this.proximityListeners)
                    listener.onScanBeacons(eddyList);

            else if(this.eventSettings.eventOnEmptyScan && eddyList.size() == 0)
                for(EddystoneScannerListener listener : this.proximityListeners)
                    listener.onScanBeacons(null);
        }

    }

    public void setDisconnectHisteresis(final int disconnectHisteresis) {
        if (disconnectHisteresis >= 1) {
            this.disconnectHisteresis = disconnectHisteresis;
        }
    }

    public void setEventSettings(final EventSettings eventSettings) {
        this.eventSettings = eventSettings;
    }

    public void setScanPeriod(final long backgroundScanPeriod, final long backgroundSleepPeriod) {
        this.backgroundScanPeriod = backgroundScanPeriod;
        this.backgroundSleepPeriod = backgroundSleepPeriod;
        this.beaconManager.setBackgroundScanPeriod(this.backgroundScanPeriod, this.backgroundSleepPeriod);
    }

    public boolean startScan() {
        boolean ret = false;
        if (this.serviceStatus != ServiceStatus.DESTROYED) {
            if (this.serviceStatus == ServiceStatus.DISCONNECTED) {
                this.scanningStatus = ScanningStatus.WAIT_FOR_SERVICE;
                this.startService();
            }
            else if (this.serviceStatus == ServiceStatus.CONNECTED) {
                this.beaconManager.setBackgroundScanPeriod(this.backgroundScanPeriod, this.backgroundSleepPeriod);
                this.beaconManager.setForegroundScanPeriod(this.scanPeriod, this.sleepPeriod);
                if (this.disconnectHisteresis <= 1) {
                    this.beaconManager.setRegionExitExpiration((long)Math.ceil(this.scanPeriod * 2L + this.sleepPeriod + 30L));
                }
                else {
                    this.beaconManager.setRegionExitExpiration((long)Math.ceil(this.disconnectHisteresis * (this.scanPeriod + this.sleepPeriod)));
                }
                this.scanId = this.beaconManager.startEddystoneScanning();
                this.scanningStatus = ScanningStatus.SCANNING;
            }
            ret = true;
        }
        return ret;
    }

    public boolean startService() {
        boolean ret = false;
        if (this.serviceStatus != ServiceStatus.DESTROYED) {
            this.serviceStatus = ServiceStatus.CONNECTING;
            this.beaconManager.connect(new BeaconManager.ServiceReadyCallback() {

                public void onServiceReady() {
                    EddystoneScanner.this.serviceStatus = ServiceStatus.CONNECTED;
                    if (EddystoneScanner.this.scanningStatus == ScanningStatus.WAIT_FOR_SERVICE)
                        EddystoneScanner.this.startScan();
                }
            });
            ret = true;
        }
        return ret;
    }

    public void stopScan() {
        if (this.scanningStatus == ScanningStatus.SCANNING && this.scanId != null) {
            this.beaconManager.stopEddystoneScanning(this.scanId);
        }
        this.scanningStatus = ScanningStatus.NOT_SCANNING;
        this.scanId = null;
    }

    public static class EventSettings
    {

        public boolean eventLostConnection;
        public boolean eventNewConnection;
        public boolean eventOnEmptyScan;
        public boolean eventScan;

        public EventSettings() {
            this.eventOnEmptyScan = false;
            this.eventScan = true;
            this.eventNewConnection = true;
            this.eventLostConnection = false;
        }


        public EventSettings eventEmptyScan(final boolean eventOnEmptyScan) {
            this.eventOnEmptyScan = eventOnEmptyScan;
            return this;
        }

        public EventSettings eventLostConnection(final boolean eventLostConnection) {
            this.eventLostConnection = eventLostConnection;
            return this;
        }

        public EventSettings eventNewConnection(final boolean eventNewConnection) {
            this.eventNewConnection = eventNewConnection;
            return this;
        }

        public EventSettings eventScan(final boolean eventScan) {
            this.eventScan = eventScan;
            return this;
        }
    }



}
