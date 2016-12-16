package com.nagash.appwebbrowser.controller.fragments.listNearby;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.MainFragment;
import com.nagash.appwebbrowser.model.localization.LocationManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.SortedSet;

import it.gmariotti.cardslib.library.view.CardViewNative;

/**
 * Created by nagash on 15/09/16.
 */
public class NearbyListFragment2 extends MainFragment
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

        nearbyAppListCard = new AppListCard(getActivity(), "Nearby Apps", false);
        nearbyAppListCard.init();
        nearbyAppListCard.setAppListItemlistener(itemListener);
        nearbyAppListCard.updateProgressBar(false,false);


        proxyAppListCard = new AppListCard(getActivity(), "Apps in your proximity", false);
        proxyAppListCard.init();
        proxyAppListCard.setAppListItemlistener(itemListener);
        proxyAppListCard.updateProgressBar(false,false);


        nearbyCardView = (CardViewNative) getActivity().findViewById(R.id.nearby_app_list_card);
        nearbyCardView.setCard(nearbyAppListCard);

        proxyCardView = (CardViewNative) getActivity().findViewById(R.id.proxy_app_list_card);
        proxyCardView.setCard(proxyAppListCard);

        btnConnect.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { getMainActivity().retryConnection();}  });
        refreshState();

        //listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }



    public void updateNarbyApps(SortedSet<WebApp> nearby, Location myLocation) {
        nearbyAppListCard.updateItems(nearby, myLocation);
        proxyAppListCard.updateProgressBar(true,true);
        nearbyAppListCard.updateProgressBar(true,true);

    }

    public void updateProxyApps(SortedSet<WebApp> proxy, Location myLocation) {
        proxyAppListCard.updateItems(proxy, myLocation);
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
