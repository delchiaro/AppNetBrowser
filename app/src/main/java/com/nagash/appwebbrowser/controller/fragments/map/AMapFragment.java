package com.nagash.appwebbrowser.controller.fragments.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.MainFragment;
import com.nagash.appwebbrowser.model.localization.Localizable;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nagash on 15/09/16.
 */
public class AMapFragment<L extends Localizable> extends MainFragment
        implements OnMapReadyCallback
{


    private final static String TAG = "AMapFragment";

    private GoogleMap          googleMap  = null;
    private List<L>            localizables = null;
    private Map<Marker, L>     markersMap = new HashMap<>();

    public AMapFragment() {
        super();
    }

    SupportMapFragment mMapFragment = null;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: begin");

        Log.d(TAG, "onCreate: SupportMapFragment.newInstance() done");
        Log.d(TAG, "onCreate: end");

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: begin");
        // FragmentManager fm = getChildFragmentManager();
//         mMapFragment = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.google_map));
        if(mMapFragment == null) {
            mMapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.map_container, mMapFragment, "map");
            fragmentTransaction.commit();
            mMapFragment.getMapAsync(AMapFragment.this);
        }
        Log.d(TAG, "onActivityCreated: fragmentTransaction.commit() done");
//        new AsyncTask<Void, Void, Void>() {
//            @Override protected Void doInBackground(Void... voids) {
//                return null;
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        Log.d(TAG, "onActivityCreated: getMapAsync() done");
        Log.d(TAG, "onActivityCreated: end");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: begin");
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, "onMapReady: permission checked");
        googleMap.setMyLocationEnabled(true);
        Log.d(TAG, "onMapReady: location enabled");
        updateGoogleMapMarkers();
        Log.d(TAG, "onMapReady: markers updated");
        Log.d(TAG, "onMapReady: end");

    }




    private Marker addMarker(@NonNull Location location) {
        return addMarker(location.getLatitude(), location.getLongitude());
    }
    private Marker addMarker(double lat, double lng) {
        return addMarker(new LatLng(lat, lng));
    }
    private Marker addMarker(@NonNull LatLng latlng) {
        MarkerOptions mo = new MarkerOptions();
        mo.position(latlng);
        if(googleMap != null)
            return googleMap.addMarker(mo);
        else return null;
    }



    protected GoogleMap         getGoogleMap()      { return googleMap; }
    protected List<L>           getLocalizables()   { return localizables; }
    protected Map<Marker, L>    getMarkersMap()     { return markersMap; }



    public void setMarkers(@NonNull Collection<? extends L> localiz) {
        this.localizables = new ArrayList<>(localiz.size());
        for( L l : localiz )
            if(l.getLocation() != null)
                localizables.add(l);

        updateGoogleMapMarkers();
    }

    public void updateGoogleMapMarkers() {
        if(googleMap != null && localizables != null)
            for(L l : localizables)
               markersMap.put(addMarker(l.getLocation()), l);
    }


    public boolean isMapReady() {
        return this.googleMap != null;
    }
    public void setCamera(LatLng newLatLng, float newZoom){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, newZoom));
    }
    public void setCamera(LatLng newLatLng){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
    }
    public void zoomOnLatLng(LatLng latLng, float zoom) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
    public void goOnLatLng(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }


}
