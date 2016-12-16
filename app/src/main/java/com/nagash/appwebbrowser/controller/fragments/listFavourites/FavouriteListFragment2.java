package com.nagash.appwebbrowser.controller.fragments.listFavourites;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.MainFragment;
import com.nagash.appwebbrowser.controller.fragments.listNearby.AppListCard;
import com.nagash.appwebbrowser.model.connection.CentralConnection;
import com.nagash.appwebbrowser.model.webapp.FavouriteAppsManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.view.CardViewNative;

public class FavouriteListFragment2 extends MainFragment {


    //private boolean isShowingDetailsFragment;
    public ListView listView;
    CardViewNative favouritesCardView = null;
    AppListCard favouritesAppListCard = null;


    private static final int ACTIONBAR_TITLE_ID = R.string.favourites_fragment_actionbar_title;

    public FavouriteListFragment2() {
        super();
        setColorID(R.color.colorFavouritesPrimary);
        setColorDarkID(R.color.colorFavouritesPrimaryDark);
        setTitle(ACTIONBAR_TITLE_ID);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_favourites_2, container, false);
        return view;
    }

    FavouriteAppsManager favouriteAppsManager;

    public void onActivityCreated(@Nullable Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
//

        favouriteAppsManager = FavouriteAppsManager.getInstance(getActivity());

        AppListCard.AppListItemListener itemListener = new AppListCard.AppListItemListener() {
            @Override public void onItemClick(WebApp webApp) {
                getMainActivity().showAppDetails(webApp);
            }
            @Override public void onButtonClick(WebApp webApp) {
                getMainActivity().startAppFragment(webApp);
            }
            @Override public void onSwipe(WebApp webApp) {
                //favouriteAppsManager.loadPreferences();
                favouriteAppsManager.removeFromFavorites(webApp);
                //favouriteAppsManager.savePreferences();
            }
        };

        favouritesAppListCard = new AppListCard(getActivity(), "Favourite Apps", true);
        favouritesAppListCard.init();
        favouritesAppListCard.setAppListItemlistener(itemListener);
        favouritesAppListCard.updateProgressBar(false,false);

        favouritesCardView = (CardViewNative) getActivity().findViewById(R.id.favourites_app_list_card);
        favouritesCardView.setCard(favouritesAppListCard);

        updateAppList();

    }


    public void onFragmentShown() {
        super.onFragmentShown();
        updateAppList();
    }


    public void updateAppList() {
        updateAppList(null);
    }
    public void updateAppList(Location myLocation) {

    final List cachedWebAppList = CentralConnection.instance().getCachedWebAppList();
    if (favouritesCardView!=null && cachedWebAppList != null && !cachedWebAppList.isEmpty()) {

        final ArrayList<WebApp> favorites = new ArrayList<>(favouriteAppsManager.filterFavoritesApps(cachedWebAppList));
        favouritesAppListCard.updateItems(favorites);
    }


    }

}
