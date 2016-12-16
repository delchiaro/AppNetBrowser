package com.nagash.appwebbrowser.model.webapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nagash on 07/10/16.
 */

public class FavouriteAppsManager
{
    private static FavouriteAppsManager instance = null;

    public static FavouriteAppsManager getInstance(Activity activity) {
        if(instance == null)
            return instance = new FavouriteAppsManager(activity);
        else return instance;
    }

    private final static String SHARED_PREF_KEY = "FAVORITES_APP_LIST";
    Set<String> favoriteAppIDs;
    SharedPreferences sharedPref;

    private FavouriteAppsManager(Activity activity) {
       this.sharedPref =  activity.getPreferences(activity.MODE_PRIVATE);
        favoriteAppIDs = new HashSet<>();
    }

    public void addToFavorites(@NonNull WebApp webApp) {
        favoriteAppIDs.add(webApp.getId());
    }
    public void removeFromFavorites(@NonNull WebApp webApp) {
        favoriteAppIDs.remove(webApp.getId());
    }
    public void setFavorite(@NonNull WebApp webApp, boolean isFavorite) {
        if(isFavorite)
            addToFavorites(webApp);
        else removeFromFavorites(webApp);
    }
    public void toggleFavorite(@NonNull WebApp webApp) {
        if(favoriteAppIDs.contains(webApp.getId()))
            favoriteAppIDs.remove(webApp.getId());
        else favoriteAppIDs.add(webApp.getId());
    }

    public boolean isFavorite(@NonNull WebApp webApp) {
        return favoriteAppIDs.contains(webApp.getId());
    }


    public void savePreferences() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(SHARED_PREF_KEY, this.favoriteAppIDs);
        editor.commit();
    }

    public void loadPreferences() {

        this.favoriteAppIDs = sharedPref.getStringSet(SHARED_PREF_KEY, new HashSet<String>());

    }



    public Collection<WebApp> filterFavoritesApps(Collection<WebApp> allWebApps) {
        loadPreferences();
        Set<WebApp> favorites = new HashSet<>();

        for(WebApp w : allWebApps)
            if(isFavorite(w)) favorites.add(w);

        return favorites;
    }
}
