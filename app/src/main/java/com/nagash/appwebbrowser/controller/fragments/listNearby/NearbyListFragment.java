package com.nagash.appwebbrowser.controller.fragments.listNearby;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.MainFragment;
import com.nagash.appwebbrowser.controller.fragments.WebAppListAdapter;
import com.nagash.appwebbrowser.model.connection.CentralConnection;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.List;

/**
 * Created by nagash on 15/09/16.
 */
public class NearbyListFragment extends MainFragment
{



    // Views for Loading:
    ProgressBar progressBarLinkServer = null;
    Button btnConnect = null;
    TextView tvConnectionStatus = null;
    ListView listView = null;
    View fragmentListLayout = null;



    enum State { LOADING, CANT_CONNECT, CONNECTED }

    State state = State.LOADING;


    private static final int ACTIONBAR_TITLE_ID = R.string.nearby_fragment_actionbar_title;

    public NearbyListFragment() {
        super(MainFragment.builder()
                .setColor(R.color.colorNearbyPrimary)
                .setColorDark(R.color.colorNearbyPrimaryDark)
                .setTitle(ACTIONBAR_TITLE_ID));
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
            btnConnect.setVisibility(View.GONE);
            updateAppList();
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
        View view = inflater.inflate(R.layout.fragment_list_nearby, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        progressBarLinkServer = (ProgressBar)getActivity().findViewById(R.id.progressBarLinkServer);
        btnConnect            = (Button)     getActivity().findViewById(R.id.btnConnect);
        tvConnectionStatus    = (TextView)   getActivity().findViewById(R.id.tvAppList);
        listView              = (ListView)   getActivity().findViewById(R.id.app_listView);
        fragmentListLayout    =              getActivity().findViewById(R.id.fragment_list_layout);
        btnConnect.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { getMainActivity().retryConnection();}  });
        refreshState();

        //listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }






    public void updateAppList()
    {
        final List<WebApp> appList = CentralConnection.instance().getCachedWebAppList();
        if(appList != null && appList.isEmpty() == false)
        {
            // Create the adapter to convert the array to views
            WebAppListAdapter adapter = new WebAppListAdapter(getMainActivity(), appList);

            // Attach the adapter to a ListView
            listView.setAdapter(adapter);
            listView.setItemsCanFocus(false);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   // Snackbar.make(fragmentListLayout, "Item Selected!", Snackbar.LENGTH_LONG).show();
                    getMainActivity().showAppDetails(appList.get(i));
                }
            });


            listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Snackbar.make(fragmentListLayout, "Item Selected!", Snackbar.LENGTH_LONG).show();
                    getMainActivity().showAppDetails(appList.get(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

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
