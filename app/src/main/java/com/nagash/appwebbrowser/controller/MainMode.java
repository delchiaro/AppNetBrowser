package com.nagash.appwebbrowser.controller;

/**
 * Created by nagash on 06/10/16.
 */

public enum MainMode {
    NONE, NEARBY, FAVOURITES, MAP, WEBAPP;

    public enum NearbyMode { NORMAL, APP_DETAILS }
    public enum FavouritesMode { NORMAL, APP_DETAILS }
    public enum MapMode { NORMAL, APP_DETAILS }
}