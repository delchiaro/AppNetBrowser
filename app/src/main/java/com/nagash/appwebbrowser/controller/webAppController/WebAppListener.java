package com.nagash.appwebbrowser.controller.webAppController;

import android.support.annotation.NonNull;

import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by nagash on 13/12/16.
 */

public interface WebAppListener {

    void onWebAppUpdate(@NonNull Collection<WebApp> beaconApps,@NonNull Collection<WebApp> proximityApps ,@NonNull Collection<WebApp> nearbyApps);


}
