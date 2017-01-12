package com.nagash.appwebbrowser.zz_deprecated.model.beacon.estimote.eddystone;

import android.app.Activity;
import android.content.Context;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Utils;
import com.estimote.sdk.eddystone.Eddystone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nagash on 13/10/16.
 */

public class EddystoneHelper {


        private static final int wait_time_between_scan = 1000;

    private String scanId = null;

     private final Context context;
        private final Activity activity;
        private BeaconManager beaconManager = null;


        private List<EddystoneProximityListener> proximityListeners = new ArrayList<>();
        public List<Eddystone> foundEddy;


        public EddystoneHelper(Activity activity) {
            this.context = activity;
            this.activity = activity;

            beaconManager = new BeaconManager(activity);


            final Activity myActivity = activity;


            beaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
                @Override
                public void onEddystonesFound(final List<Eddystone> eddys) {
                    myActivity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            foundEddy = eddys;
                            if (eddys != null && eddys.size() > 0)
                            {
                                int nListeners = proximityListeners.size();
                                for (int i = 0; i < nListeners; i++)
                                {
                                    proximityListeners.get(i).OnEddyProximity(eddys);
                                }
                            }
                        }
                    });

                }
            });

        }


        public void addProximityListener(EddystoneProximityListener listener) {
            this.proximityListeners.add(listener);
        }



        public void startScan()
        {
            // should be invoked in onStart()
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override public void onServiceReady() {
                    scanId = beaconManager.startEddystoneScanning();
                }
            });

        }

        public void stopScan() {
            // should be invoked in onStop()
            if(scanId != null)
                beaconManager.stopEddystoneScanning(scanId);

            beaconManager.disconnect();
        }






        public static double getDistance(Beacon beacon) {
            return Utils.computeAccuracy(beacon);
        }

        public static boolean isInProximity(Beacon beacon) {
            if(Utils.computeProximity(beacon) != Utils.Proximity.NEAR) // NEAR <= 0.5m
            {
                return false;
            }
            else return true;
        }


}
