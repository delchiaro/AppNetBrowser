package com.nagash.appwebbrowser.controller.fragments.list;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.AppDetailsActivity;
import com.nagash.appwebbrowser.controller.BeaconTestActivity;
import com.nagash.appwebbrowser.controller.MainFragment;
import com.nagash.appwebbrowser.controller.fragments.list.AppListCard;
import com.nagash.appwebbrowser.controller.webAppController.WebAppController;
import com.nagash.appwebbrowser.controller.webAppController.WebAppListener;
import com.nagash.appwebbrowser.model.localization.LocationManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import it.gmariotti.cardslib.library.view.CardViewNative;

/**
 * Created by nagash on 15/09/16.
 */
public class NearbyListFragment2 extends MainFragment implements WebAppListener
{



    // Views for Loading:
    ProgressBar progressBarLinkServer = null;
    Button btnConnect = null;
    TextView tvConnectionStatus = null;
//    ListView nearbyListView = null;
//    ListView proxyListView = null;
    CardViewNative nearbyCardView = null;
    CardViewNative proxyCardView = null;
    AppListCard nearbyAppListCard = null;
    AppListCard proxyAppListCard = null;




    enum State { LOADING, CANT_CONNECT, CONNECTED }

    State state = State.LOADING;


    private static final int ACTIONBAR_TITLE_ID = R.string.nearby_fragment_actionbar_title;

    public NearbyListFragment2() {
        super();
        setColorID(R.color.colorNearbyPrimary);
        setColorDarkID(R.color.colorNearbyPrimaryDark);
        setTitle(ACTIONBAR_TITLE_ID);
    }


    public void setStatusLoading() {
        state = State.LOADING;
        if(progressBarLinkServer!= null) {
            progressBarLinkServer.setVisibility(View.VISIBLE);
            tvConnectionStatus.setText(R.string.server_list_connecting);
            btnConnect.setVisibility(View.GONE);
        }
    }
    public void setStatusCantConnect(){
        state = State.CANT_CONNECT;
        if(progressBarLinkServer!= null) {
            progressBarLinkServer.setVisibility(View.GONE);
            tvConnectionStatus.setText(R.string.server_list_not_available);
            btnConnect.setVisibility(View.VISIBLE);
        }
    }
    public void setStatusConneted(){
        state = State.CONNECTED;
        if(progressBarLinkServer!= null)
        {
            progressBarLinkServer.setVisibility(View.GONE);
            tvConnectionStatus.setText(R.string.server_list_connected);
            tvConnectionStatus.setVisibility(View.GONE);
            btnConnect.setVisibility(View.GONE);
        }

    }

    private void refreshState() {
        switch(state){
            case LOADING:       setStatusLoading();         break;
            case CANT_CONNECT:  setStatusCantConnect();     break;
            case CONNECTED:     setStatusConneted();        break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_nearby, menu);
        //MenuItem menuItemBeaconTest =  menu.findItem(R.id.start_beacon_test);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.start_beacon_test:
                Intent intent = new Intent(getActivity(), BeaconTestActivity.class);
                getActivity().startActivity(intent);
                break;
        }
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_nearby_2, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        progressBarLinkServer = (ProgressBar)getActivity().findViewById(R.id.progressBarLinkServer);
        btnConnect            = (Button)     getActivity().findViewById(R.id.btnConnect);
        tvConnectionStatus    = (TextView)   getActivity().findViewById(R.id.tvAppList);
//        nearbyListView        = (ListView)   getActivity().findViewById(R.id.listViewNearbyApps);
//        proxyListView         = (ListView)   getActivity().findViewById(R.id.listViewProxyApps);

        AppListCard.AppListItemListener itemListener = new AppListCard.AppListItemListener() {
            @Override public void onItemClick(WebApp webApp) {
                getMainActivity().showAppDetails(webApp);
            }
            @Override public void onButtonClick(WebApp webApp) {
                getMainActivity().startAppFragment(webApp);
            }
            @Override public void onSwipe(WebApp webApp) {}
        };



        proxyAppListCard = new AppListCard(getActivity(), getString(R.string.proxy_app_list_title), false);
        proxyAppListCard.init();
        proxyAppListCard.setAppListItemlistener(itemListener);
        proxyAppListCard.updateProgressBar(false,false);

        nearbyAppListCard = new AppListCard(getActivity(), getString(R.string.nearby_app_list_title), false);
        nearbyAppListCard.init();
        nearbyAppListCard.setAppListItemlistener(itemListener);
        nearbyAppListCard.updateProgressBar(false,false);


        proxyCardView = (CardViewNative) getActivity().findViewById(R.id.proxy_app_list_card);
        proxyCardView.setCard(proxyAppListCard);

        nearbyCardView = (CardViewNative) getActivity().findViewById(R.id.nearby_app_list_card);
        nearbyCardView.setCard(nearbyAppListCard);



        btnConnect.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { getMainActivity().retryConnection();}  });
        refreshState();

        //listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        setHasOptionsMenu(true);
    }






    @Override
    public    void onWebAppUpdate(@NonNull Collection<WebApp> beaconApps,@NonNull Collection<WebApp> proximityApps ,@NonNull Collection<WebApp> nearbyApps) {

        //if(isDetached()) return;
        if(getMainActivity() == null) return;

        Location currentLocation = getMainActivity().getLocationManager().getCurrentLocation();



        LinkedHashSet proxyAndBeaconList = new LinkedHashSet();
        for(WebApp app : beaconApps)
            proxyAndBeaconList.add(app);


        for(WebApp app  : proximityApps)
             proxyAndBeaconList.add(app);



        LinkedHashSet<WebApp> nearbyNotProxy = new LinkedHashSet<>(nearbyApps);
        nearbyNotProxy.removeAll(proxyAndBeaconList);

//
//
//        if(nearbyNotProxy != null && proxyNotBeacon != null)
//            for(WebApp  app: nearbyNotProxy)
//                if(proxyNotBeacon.contains(app) == false)
//                    proxyAndBeaconList.addLast(app);


        updateNarbyApps(nearbyNotProxy, currentLocation);
        updateProxyApps(proxyAndBeaconList, currentLocation);

        getMainActivity().setNearbyAppsCounter(proxyAndBeaconList.size());
        if(beaconApps.size() > 0) {
            getMainActivity().setNearestBeaconFabApp(beaconApps.iterator().next());
        }
        else getMainActivity().setNearestBeaconFabApp(null);
    }

    public void updateNarbyApps(Collection<WebApp> nearby, Location myLocation) {
        nearbyAppListCard.updateItems(nearby, myLocation);
        nearbyAppListCard.updateProgressBar(true,true);
    }

    public void updateProxyApps(Collection<WebApp> proxy, Location myLocation) {
        proxyAppListCard.updateItems(proxy, myLocation);
        proxyAppListCard.updateProgressBar(true,true);
    }


    // * * * * * DOWNLOAD SERVER * * * * *
    public void onAppsDownloaded() {
        setStatusConneted();
//        favouriteListFragment.updateAppList();
    }
    public void onServerNotAvailable() {
        setStatusCantConnect();
    }


}
