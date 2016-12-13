package com.nagash.appwebbrowser.model.beacon.eddystoneProximity;

import com.estimote.sdk.eddystone.Eddystone;
import java.util.Set;

public abstract interface EddystoneProximityListener
{
  public abstract void onBeaconNotInProximity(Set<Eddystone> paramSet);
  
  public abstract void onBeaconProximity(Set<Eddystone> paramSet);
  
  public abstract void onLostBeaconProximity(Set<Eddystone> paramSet);
  
  public abstract void onNewBeaconProximity(Set<Eddystone> paramSet);
}
