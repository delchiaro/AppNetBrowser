package com.nagash.appwebbrowser.model.localization;

import android.location.Location;

/**
 * Created by Nagash on 03/01/2015.
 */
public interface LocationEventListener
{
    public void onLocationChanged(Location location);
    public void onConnected(Location myLastLocation);
    public void onConnectionSuspended();
    public void onConnectionFailed();
}
