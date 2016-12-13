//package com.nagash.appwebbrowser.controller.fragments.list.listTypes;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import com.nagash.appwebbrowser.R;
//import com.nagash.appwebbrowser.controller.fragments.WebAppListAdapter;
//import com.nagash.appwebbrowser.controller.fragments.list.AppListFragment;
//import com.nagash.appwebbrowser.model.connection.CentralConnection;
//import com.nagash.appwebbrowser.model.webapp.FavoriteAppsManager;
//import com.nagash.appwebbrowser.model.webapp.WebApp;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by nagash on 15/09/16.
// */
//public class FavouriteListFragment extends Fragment {
//
//
//    // Views for Loading:
//    ListView listView = null;
//    View fragmentListLayout = null;
//
//    AppListFragment appListFragment;
//
//    public FavouriteListFragment(AppListFragment appListFragment) {
//        this.appListFragment = appListFragment;
//    }
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_list_favourites, container, false);
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState)
//    {
//        super.onActivityCreated(savedInstanceState);
//        listView              = (ListView)   getActivity().findViewById(R.id.app_favouriteListView);
//        fragmentListLayout    =              getActivity().findViewById(R.id.fragment_favouriteList_layout);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        updateAppList();
//    }
//
//    public void updateAppList()
//    {
//        final List<WebApp> appList = CentralConnection.instance().getCachedWebAppList();
//        if(appList != null && appList.isEmpty() == false && isAdded() )
//        {
//            FavoriteAppsManager favoriteAppsManager = new FavoriteAppsManager(getActivity());
//            final List<WebApp> favorites = new ArrayList<>(favoriteAppsManager.filterFavoritesApps(appList));
//
//            // Create the adapter to convert the array to views
//            WebAppListAdapter adapter = new WebAppListAdapter(appListFragment.getMainActivity(), favorites);
//
//            // Attach the adapter to a ListView
//            listView.setAdapter(adapter);
//            listView.setItemsCanFocus(false);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    // Snackbar.make(fragmentListLayout, "Item Selected!", Snackbar.LENGTH_LONG).show();
//                    appListFragment.showDetailsFragment(favorites.get(i));
//                }
//            });
//
//            listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    //Snackbar.make(fragmentListLayout, "Item Selected!", Snackbar.LENGTH_LONG).show();
//                    appListFragment.showDetailsFragment(favorites.get(i));
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//        }
//
//    }
//
//
//}
