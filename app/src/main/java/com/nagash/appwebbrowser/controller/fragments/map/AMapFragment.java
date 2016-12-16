package com.nagash.appwebbrowser.controller.fragments.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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



    private GoogleMap          googleMap  = null;
    private List<L>            localizables = null;
    private Map<Marker, L>     markersMap = new HashMap<>();

    public AMapFragment() {
        super();
    }







    protected GoogleMap         getGoogleMap()      { return googleMap; }
    protected List<L>           getLocalizables()   { return localizables; }
    protected Map<Marker, L>    getMarkersMap()     { return markersMap; }






    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_map, container, false);
    }



    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // FragmentManager fm = getChildFragmentManager();
        // mMapFragment = ((SupportMapFragment)fm.findFragmentById(R.id.google_map));

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_container, mMapFragment, "map");
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        // mMapFragment.getMapAsync(this);

    }

    @Override public void onMapReady(GoogleMap googleMap) {



        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        updateGoogleMapMarkers();
    }




    public boolean isMapReady() {
        return this.googleMap != null;
    }

    public void setMarkers(@NonNull Collection<? extends L> localiz) {
        this.localizables = new ArrayList<>(localiz.size());
        for( L l : localiz )
            if(l.getLocation() != null)
                localizables.add(l);

        updateGoogleMapMarkers();
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

    public void updateGoogleMapMarkers() {
        if(googleMap != null && localizables != null)
            for(L l : localizables)
               markersMap.put(addMarker(l.getLocation()), l);
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
