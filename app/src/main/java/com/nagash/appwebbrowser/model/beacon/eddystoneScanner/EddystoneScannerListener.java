package com.nagash.appwebbrowser.model.beacon.eddystoneScanner;

import com.estimote.sdk.eddystone.Eddystone;
import java.util.List;
import java.util.Set;

public abstract interface EddystoneScannerListener
{
  public abstract void onLostConnectedBeacons(Set<Eddystone> paramSet);
  
  public abstract void onNewConnectedBeacons(Set<Eddystone> paramSet);
  
  public abstract void onScanBeacons(List<Eddystone> paramList);
}
