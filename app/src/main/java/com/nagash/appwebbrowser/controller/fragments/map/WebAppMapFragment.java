package com.nagash.appwebbrowser.controller.fragments.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.MainFragment;
import com.nagash.appwebbrowser.model.webapp.WebApp;

/**
 * Created by nagash on 09/10/16.
 */

public class WebAppMapFragment
        extends     AMapFragment<WebApp>
        implements  GoogleMap.OnMarkerClickListener,
                    GoogleMap.OnMapClickListener
{


    private static final int ACTIONBAR_TITLE_ID = R.string.map_fragment_actionbar_title;

    Marker markerWebApp = null;
    WebApp selectedWebApp = null;

    public WebAppMapFragment() {
        super();
        setColorID(R.color.colorMapPrimary);
        setColorDarkID(R.color.colorMapPrimaryDark);
        setTitle(ACTIONBAR_TITLE_ID);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // * * * * * EASY FRAGMENT EVENTS/LIFECYCLE * * * * *


    @Override public void onFragmentShown() {
        super.onFragmentShown();
        if(selectedWebApp != null) {
            activateMarkerMode(selectedWebApp);
            animateFAB_UP();
        }
        else activateMapMode();
    }
    @Override public void onFragmentHidden() {
        super.onFragmentHidden();
        if(selectedWebApp !=null)
        {
            animateFAB_DOWN();
        }
    }

    MenuItem infoMenuItem = null;
    MenuItem playMenuItem = null;




    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(selectedWebApp != null) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu_map_marker, menu);
            infoMenuItem =  menu.findItem(R.id.menu_item_marker_info);
            playMenuItem = menu.findItem(R.id.menu_item_marker_play);
        }
        else{
            inflater.inflate(R.menu.menu_map, menu);
            infoMenuItem = playMenuItem = null;
        }
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_item_marker_info:
                getMainActivity().showAppDetails(selectedWebApp);

                break;

            case R.id.menu_item_marker_play:
                getMainActivity().startAppFragment(selectedWebApp);
                break;
        }
        return false;
    }






    // * * * * * MAP EVENTS * * * * *
    @Override public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        //googleMap.setOnInfoWindowCloseListener(this);
        googleMap.setOnMapClickListener(this);
        getGoogleMap().setOnMarkerClickListener(this);
        //getGoogleMap().getUiSettings().setMapToolbarEnabled(true);
    }

    @Override
    protected BitmapDescriptor getDefaultMarkerIcon() {
//        return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//
        return BitmapDescriptorFactory.defaultMarker(171);
//        return BitmapDescriptorFactory.defaultMarker(174);

    }

    private CharSequence titleBackup = null;
    @Override public boolean onMarkerClick(Marker marker) {
        if(marker != null) {
//            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(
//                    "http://maps.google.com/maps?q=loc:51.5, 0.125"));
//            startActivity(intent);
            getGoogleMap().getUiSettings().setMapToolbarEnabled(true);
            markerWebApp = marker;
            selectedWebApp = getMarkersMap().get(marker);
            animateFAB_UP();
            activateMarkerMode(selectedWebApp);
        }
        else onBackPressed();

        return false; // return false to show google map toolbar
    }
    @Override public void onMapClick(LatLng latLng) {
        onBackPressed();
    }
    @Override public boolean onBackPressed() {
        if(selectedWebApp != null) {
            //markerWebApp.hideInfoWindow(); // dont works with toolbar :((
            getGoogleMap().getUiSettings().setMapToolbarEnabled(false); // only way to hide toolbar with back pressed. But is not animated.

            markerWebApp = null;
            selectedWebApp = null;
            animateFAB_DOWN();

            activateMapMode();
            return true;
        }
        else return false;
    }


    private void animateFAB_UP() {
        getMainActivity().getFabProximity().animate()
                .translationY( -140 )
                .setDuration(600)
                .start();
    }
    private void animateFAB_DOWN() {
        getMainActivity().getFabProximity().animate()
                .translationY( 0 )
                .setDuration(600)
                .start();
    }

    private void activateMarkerMode(WebApp webApp) {
        getMainActivity().invalidateOptionsMenu();
        super.setColorsID(R.color.colorMapMarker, R.color.colorMapMarkerDark);
        super.setTitle(webApp.getName() != null ? webApp.getName() : webApp.getId());
        super.setBackButton(BackButton.VISIBLE);
        super.updateActionBar();
    }




    private void activateMapMode(){


        getMainActivity().invalidateOptionsMenu();
        super.setColorsID(R.color.colorMapPrimary, R.color.colorMapPrimaryDark);
        super.setTitle(ACTIONBAR_TITLE_ID);
        super.setBackButton(BackButton.HIDDEN);
        super.updateActionBar();


    }
}
