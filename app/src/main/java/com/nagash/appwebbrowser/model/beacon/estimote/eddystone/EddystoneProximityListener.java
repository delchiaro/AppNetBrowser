package com.nagash.appwebbrowser.model.beacon.estimote.eddystone;

import com.estimote.sdk.eddystone.Eddystone;

import java.util.List;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public interface EddystoneProximityListener {

    public void OnEddyProximity(List<Eddystone> proximityEddys);


}
