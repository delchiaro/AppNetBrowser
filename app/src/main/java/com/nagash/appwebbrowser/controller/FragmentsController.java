package com.nagash.appwebbrowser.controller;

import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.fragments.details.WebAppDetailsFragment;
import com.nagash.appwebbrowser.controller.fragments.listFavourites.FavouriteListFragment2;
import com.nagash.appwebbrowser.controller.fragments.listNearby.NearbyListFragment;
import com.nagash.appwebbrowser.controller.fragments.listFavourites.FavouriteListFragment;
import com.nagash.appwebbrowser.controller.fragments.listNearby.NearbyListFragment2;
import com.nagash.appwebbrowser.controller.fragments.map.WebAppMapFragment;
import com.nagash.appwebbrowser.controller.fragments.webapp.WebAppContainerFragment;
import com.nagash.appwebbrowser.model.webapp.WebApp;
import com.nagash.appwebbrowser.utils.fragmentHelper.FragmentHelper;

/**
 * Created by nagash on 06/10/16.
 */

public class FragmentsController
{
    MainActivity main;
    MainMode     mainMode = MainMode.NONE;

    FragmentHelper<MainFragment> fragmentHelper;

    public MainMode getMainMode() { return mainMode; }




    private NearbyListFragment2     nearbyListFragment      = null;
    private FavouriteListFragment2  favouriteListFragment   = null;
    private WebAppContainerFragment webAppContainerFragment = null;
    private WebAppMapFragment       mapFragment             = null;


    private WebAppDetailsFragment   webAppDetailsFragment_map           = null;
    private WebAppDetailsFragment   webAppDetailsFragment_favourites    = null;
    private WebAppDetailsFragment webAppDetailsFragment_nearby = null;




    public FragmentsController(MainActivity mainActivity) {
        this.main = mainActivity;
        fragmentHelper = new FragmentHelper(R.id.main_fragment_container, main.getSupportFragmentManager());
        fragmentHelper.setCustomAnim(R.anim.fade_in, R.anim.fade_out);
    }









    // return true if FragmentsController handled the event
    public boolean onMainBackPressed() {
        // if AppDetails are open, closeAppDetails() will close and return true.
        if( closeAppDetails() )
            return true;
            // let the active MainFragment manage the event (default behaviour: return false - not handle the event).
        else if(getActiveFragment()!=null) return getActiveFragment().onBackPressed();

        else return false;
    }




    public NearbyListFragment2       getNearbyListFragment() {
        return nearbyListFragment;
    }
    public WebAppContainerFragment  getWebAppContainerFragment() {
        return webAppContainerFragment;
    }
    public WebAppMapFragment        getMapFragment() {
        return mapFragment;
    }
    final public MainFragment       getActiveFragment() {
        return fragmentHelper.getActiveFragment();
    }




    public void showAppDetils(WebApp webApp) {
        if(mainMode != MainMode.WEBAPP ) {
            WebAppDetailsFragment detailsFragment;

            if (mainMode == MainMode.NEARBY)
            {
                if (webAppDetailsFragment_nearby == null)
                    webAppDetailsFragment_nearby = (new WebAppDetailsFragment()).setParentFragmentStyle(nearbyListFragment);
                detailsFragment = webAppDetailsFragment_nearby;
            }
            else if( mainMode == MainMode.FAVOURITES)
            {
                if (webAppDetailsFragment_favourites == null)
                    webAppDetailsFragment_favourites = (new WebAppDetailsFragment()).setParentFragmentStyle(favouriteListFragment);
                detailsFragment = webAppDetailsFragment_favourites;
            }
            else if (mainMode == MainMode.MAP)
            {
                if (webAppDetailsFragment_map == null)
                    webAppDetailsFragment_map = (new WebAppDetailsFragment()).setParentFragmentStyle(mapFragment);
                detailsFragment = webAppDetailsFragment_map;
            }
            else return;

            detailsFragment.setWebApp(webApp);
            fragmentHelper.activateFragment(detailsFragment);
        }

    }
    public boolean closeAppDetails() {
        // try to close AppDetails (if open). Return true if appDetails was open.

        if(mainMode == MainMode.NEARBY && webAppDetailsFragment_nearby != null) {
            fragmentHelper.removeFragment(webAppDetailsFragment_nearby);
            fragmentHelper.activateFragment(nearbyListFragment);
            webAppDetailsFragment_nearby = null;
            //main.invalidateOptionsMenu(); // this will force the mainActivity to recreate the optionMenu, calling fragmentsController.createOptionMenu()
            return true;
        }

        else if( mainMode == MainMode.FAVOURITES && webAppDetailsFragment_favourites != null) {
            fragmentHelper.removeFragment(webAppDetailsFragment_favourites);
            fragmentHelper.activateFragment(favouriteListFragment);
            webAppDetailsFragment_favourites = null;
            return true;
        }

        else if(mainMode == MainMode.MAP && webAppDetailsFragment_map != null) {
            fragmentHelper.removeFragment(webAppDetailsFragment_map);
            fragmentHelper.activateFragment(mapFragment);
            webAppDetailsFragment_map = null;
            //main.invalidateOptionsMenu(); // this will force the mainActivity to recreate the optionMenu, calling fragmentsController.createOptionMenu()
            return true;
        }
        return false;
    }




    public void earlyInitMapFragment() {
        mapFragment = new WebAppMapFragment();
        changeMode(MainMode.MAP);
        changeMode(MainMode.NEARBY);
        // fragmentHelper.hideFragment(mapFragment);
        // fragmentHelper.activateFragment(nearbyListFragment);
    }


    public void changeMode(MainMode mode) {
        if(mode == mainMode) return;
        switch(mode)
        {
            case NONE: break;

            case NEARBY:
                if (nearbyListFragment == null)
                    nearbyListFragment = new NearbyListFragment2();
                if(webAppDetailsFragment_nearby != null)
                    fragmentHelper.activateFragment(webAppDetailsFragment_nearby);
                else fragmentHelper.activateFragment(nearbyListFragment);
                break;

            case FAVOURITES:
                if(favouriteListFragment == null)
                    favouriteListFragment = new FavouriteListFragment2();
                if(webAppDetailsFragment_favourites != null)
                    fragmentHelper.activateFragment(webAppDetailsFragment_favourites);
                else fragmentHelper.activateFragment(favouriteListFragment);
                break;

            case MAP:
                if(mapFragment == null)
                    mapFragment = new WebAppMapFragment();

                if(webAppDetailsFragment_map != null)
                    fragmentHelper.activateFragment(webAppDetailsFragment_map);
                else fragmentHelper.activateFragment(mapFragment);
                break;

            case WEBAPP:
                if(webAppContainerFragment == null)
                    webAppContainerFragment = new WebAppContainerFragment();
                fragmentHelper.activateFragment(webAppContainerFragment);
                break;

        }
        mainMode = mode;
        main.updateFabVisibility();
    }

//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(getActiveFragment() != null)
//            return getActiveFragment().onOptionsItemSelected(item);
//        else return false;
//    }







}