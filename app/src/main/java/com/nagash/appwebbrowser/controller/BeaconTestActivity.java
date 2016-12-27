package com.nagash.appwebbrowser.controller;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.estimote.sdk.eddystone.Eddystone;
import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.model.beacon.eddystoneProximity.EddystoneProximityListener;
import com.nagash.appwebbrowser.model.beacon.eddystoneProximity.EddystoneProximityManager;
//import com.nagash.appwebbrowser.model.beacon.deprecated.eddystone.EddyHelper;
import com.nagash.appwebbrowser.model.beacon.eddystoneScanner.EddystoneScanner;
import com.nagash.appwebbrowser.model.beacon.eddystoneScanner.EddystoneScannerListener;

import java.util.List;
import java.util.Set;

public class BeaconTestActivity extends AppCompatActivity implements EddystoneProximityListener, EddystoneScannerListener {

    EddystoneProximityManager eddyProximity;
    EddystoneScanner          eddyScanner;

    RelativeLayout contentLayout;
    CoordinatorLayout beaconLayout;
    FloatingActionButton fab;

    EddystoneScanner eddyScanner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentLayout = (RelativeLayout) findViewById(R.id.content_beacon_layout);
        beaconLayout = (CoordinatorLayout) findViewById(R.id.beacon_layout);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        eddyScanner = EddystoneScanner.getInstance(this);
        eddyScanner.getEventSettings() .eventLostConnection(true) .eventNewConnection(true);
        eddyScanner.addProximityListener(this);

        EddystoneProximityManager.Settings proxySettings = new EddystoneProximityManager.Settings()
                .advertiseConnectedOutProximity(true)
                .advertiseInProximity(true)
                .advertiseLostProximity(true)
                .advertiseNewProximity(true)
                .advertiseOnEmptyList(true);

        eddyProximity = new EddystoneProximityManager(eddyScanner, proxySettings);
        eddyProximity.addListener(this);
        eddyProximity.setProximityDistance(0.3);

    }


    @Override
    protected void onStart() {
        super.onStart();
        //eddyProximity.start();
        eddyScanner.startScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //eddyProximity.stop();
        eddyScanner.stopScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }





    @Override
    public void onNewBeaconProximity(Set<Eddystone> newProxyBeacons) {
        Log.i("BeaconTestActivity", "Nuovi beacon vicini: " + newProxyBeacons.size());
        //Snackbar.make(beaconLayout, "Nuovo beacon vicino!", 300).show();
    }

    @Override
    public void onLostBeaconProximity(Set<Eddystone> newFarBeacons) {
        Log.i("BeaconTestActivity", "Beacon allontanati: " + newFarBeacons.size());
        //Snackbar.make(beaconLayout, "Beacon perso", 300).show();;
    }

    @Override
    public void onBeaconProximity(Set<Eddystone> proxyBeacons) {

        if(proxyBeacons == null)
            Log.i("BeaconTestActivity", "Nessun beacon vicino");

        else
            Log.i("BeaconTestActivity", "Beacon vicini: " + proxyBeacons.size());
        //Snackbar.make(beaconLayout, "Beacon vicino!", 300).show();
    }

    @Override
    public void onBeaconNotInProximity(Set<Eddystone> farBeacons) {
        Log.i("BeaconTestActivity", "Beacon lontani: " + farBeacons.size());
        /// Snackbar.make(beaconLayout, "Nessun beacon vicino", 300).show();;
    }







    @Override
    public void onNewConnectedBeacons(Set<Eddystone> newBeacons) {
        Log.i("BeaconTestActivity", "Nuovi beacon connessi: " + newBeacons.size());
        //Snackbar.make(beaconLayout, "Nuovo beacon connesso!", 700).show();;
    }

    @Override
    public void onLostConnectedBeacons(Set<Eddystone> lostBeacons) {
        Log.i("BeaconTestActivity", "Beacon disconnessi: " + lostBeacons.size());
        // Snackbar.make(beaconLayout, "Beacon disconnesso", 700).show();;
    }

    @Override
    public void onScanBeacons(List<Eddystone> scannedBeacons) {
        if(scannedBeacons == null)
        {
            Log.i("BeaconTestActivity", "Nessun beacon connesso");
            // Snackbar.make(beaconLayout, "Nessun beacon connesso", 700).show();;
        }
        else
        {
            Log.i("BeaconTestActivity", "Beacon connessi: " + scannedBeacons.size());
            //   Snackbar.make(beaconLayout, "Beacon connesso!", 700).show();
        }
    }




}