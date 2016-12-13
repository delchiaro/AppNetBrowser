package com.nagash.appwebbrowser.model.geofencing;

import android.location.Location;
import android.os.AsyncTask;

import com.nagash.appwebbrowser.model.geofencing.options.EmptyListAdvertiseOptions;
import com.nagash.appwebbrowser.model.geofencing.options.IGeofenceOptions;

import java.util.Set;

/**
 * Created by nagash on 23/09/16.
 */


public class GeofenceScanTask<T extends Geofenceable>  extends AsyncTask<Set<GeofenceObject<T>>, String, TriggeredGeofenceableList<T>> {


//    private boolean useExtraRadius = false;
//    private boolean useOnlyExtraRadius = false;
//    private int extraRadius = 0;


    private GeofenceManager geofenceManager;
    private Location myLocation;
    private EmptyListAdvertiseOptions advOptions;
    private IGeofenceOptions options;

    public GeofenceScanTask(GeofenceManager manager, Location myLocation, IGeofenceOptions options) {
        this.geofenceManager = manager;
        this.myLocation = myLocation;
        this.options = options;
    }



    @Override
    protected  TriggeredGeofenceableList<T> doInBackground(Set<GeofenceObject<T>>... list)
    {
        TriggeredGeofenceableList<T> tgl = new TriggeredGeofenceableList(new GeofenceableObjectComparator(myLocation));


        Set<GeofenceObject<T>> set = list[0];
        if(set == null) return null;

        for(GeofenceObject<T> geoObj : set)
        {
            GeoStatus newGeoStatus;
            geoObj.getProximityRadius();
            if(options.isUsingExtraRadius()) {
                if (options.isUsingOnlyExtraRadius())
                    newGeoStatus = geoObj.testLocationStatus_extraRadiusOnly(myLocation, options.getExtraRadius());
                else
                    newGeoStatus = geoObj.testLocationStatus_extraRadius(myLocation, options.getExtraRadius());
            }
            else newGeoStatus = geoObj.testLocationStatus(myLocation);

            GeoStatus oldGeoStatus = geoObj.getGeoStatus();
            GeoEvent regEvent = geoObj.getRegisteredEvent();

            switch (newGeoStatus)
            {
                case IN:
                    if ( regEvent.eventIn()  )
                    {
                        geoObj.setTriggeredEvent(new GeoEvent().enableInEvent());
                        tgl.triggeredInGeofences.add(geoObj.getManagedObject());
                    }
                    else if (regEvent.eventEntering()
                            && (oldGeoStatus == GeoStatus.OUT || oldGeoStatus == GeoStatus.INIT)) {
                        geoObj.setTriggeredEvent(new GeoEvent().enableEnteringEvent() );
                        tgl.triggeredEnteringGeofences.add(geoObj.getManagedObject());
                    }
                    geoObj.setGeoStatus(GeoStatus.IN);
                    break;



                case OUT:
                    if (regEvent.eventOut() )
                    {
                        geoObj.setTriggeredEvent(new GeoEvent().enableOutEvent());
                        tgl.triggeredOutGeofences.add(geoObj.getManagedObject());
                    }
                    else if ( new GeoEvent().eventExiting() && (oldGeoStatus == GeoStatus.IN || oldGeoStatus == GeoStatus.INIT))
                    {
                        geoObj.setTriggeredEvent(new GeoEvent().enableExitingEvent() );
                        tgl.triggeredExitingGeofences.add(geoObj.getManagedObject());

                        // TODO: THIS IS TO FIX THE CRAZY NO SENSE BUG O_O
                        // For jumps to the last instruction of this context (else if {..}) after the last iteration
                        int a = 2;
                        int b = 3;
                        b = a;

                    }
                    geoObj.setGeoStatus(GeoStatus.OUT);
                    break;


                default: break;

            }
        }
        return tgl;

    }


    public boolean advertiseOnEmptyList() {
        return advOptions == EmptyListAdvertiseOptions.ADVERTISE_ON_EMPTY_LIST;
    }


    @Override
    protected void onPostExecute(TriggeredGeofenceableList<T> tgl) {
        super.onPostExecute(tgl);
        if(advertiseOnEmptyList() || tgl.isAtLeastOneTriggered() )
            geofenceManager.onGeofenceScanTaskFinished(tgl);
    }
}
