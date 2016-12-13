package com.nagash.appwebbrowser.controller.fragments.listFavourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.MainActivity;
import com.nagash.appwebbrowser.controller.MainFragment;
import com.nagash.appwebbrowser.controller.fragments.WebAppListAdapter;
import com.nagash.appwebbrowser.model.connection.CentralConnection;
import com.nagash.appwebbrowser.model.webapp.FavoriteAppsManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FavouriteListFragment  extends MainFragment
{


  public View fragmentListLayout;
  //private boolean isShowingDetailsFragment;
  public ListView listView;
  

  private static final int ACTIONBAR_TITLE_ID = R.string.favourites_fragment_actionbar_title;

  public FavouriteListFragment() {
    super(MainFragment.builder()
            .setColor(R.color.colorFavouritesPrimary)
            .setColorDark(R.color.colorFavouritesPrimaryDark)
            .setTitle(ACTIONBAR_TITLE_ID));
    this.listView = null;
    this.fragmentListLayout = null;
  }



  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_list_favourites, container, false);
    return view;
  }
  public void onActivityCreated(@Nullable Bundle paramBundle)
  {
      super.onActivityCreated(paramBundle);
      listView              = (ListView)   getActivity().findViewById(R.id.app_favourite_list_view);
      fragmentListLayout    =              getActivity().findViewById(R.id.fragment_favouriteList_layout);
      updateAppList();

  }

  
  public void onFragmentShown()
  {
      super.onFragmentShown();
      updateAppList();
  }
//
//
//  public void updateAppList()
//  {
//    final List<WebApp> appList = CentralConnection.instance().getCachedWebAppList();
//    if(appList != null && appList.isEmpty() == false && isAdded() )
//    {
//      FavoriteAppsManager favoriteAppsManager = new FavoriteAppsManager(getActivity());
//      final List<WebApp> favorites = new ArrayList<>(favoriteAppsManager.filterFavoritesApps(appList));
//
//      // Create the adapter to convert the array to views
//      WebAppListAdapter adapter = new WebAppListAdapter(getMainActivity(), favorites);
//
//      // Attach the adapter to a ListView
//      listView.setAdapter(adapter);
//      listView.setItemsCanFocus(false);
//      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//          // Snackbar.make(fragmentListLayout, "Item Selected!", Snackbar.LENGTH_LONG).show();
//          getMainActivity().showAppDetails(favorites.get(i));
//        }
//      });
//
//      listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//          //Snackbar.make(fragmentListLayout, "Item Selected!", Snackbar.LENGTH_LONG).show();
//          getMainActivity().showAppDetails(favorites.get(i));
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {
//
//        }
//      });
//    }

    public void updateAppList() {

        final List cachedWebAppList = CentralConnection.instance().getCachedWebAppList();
        if (this.listView != null && cachedWebAppList != null && !cachedWebAppList.isEmpty()) {
            final ArrayList<WebApp> favorites = new ArrayList<>(new FavoriteAppsManager(this.getActivity()).filterFavoritesApps(cachedWebAppList));
            this.listView.setAdapter( new WebAppListAdapter(this.getMainActivity(), favorites) );
            this.listView.setItemsCanFocus(false);

            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

               @Override public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                    FavouriteListFragment.this.getMainActivity().showAppDetails(favorites.get(n));
                }

            });

            this.listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                        FavouriteListFragment.this.getMainActivity().showAppDetails(favorites.get(n));
                    }
                @Override public void onNothingSelected(final AdapterView<?> adapterView) {}

            });
        }


  }

}
