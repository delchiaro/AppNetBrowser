package com.nagash.appwebbrowser.controller.webAppController;

import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.SortedSet;

/**
 * Created by nagash on 13/12/16.
 */

public interface WebAppListener {

    void onWebAppUpdate(SortedSet<WebApp> proximityApps , SortedSet<WebApp> nearbyApps);


}
