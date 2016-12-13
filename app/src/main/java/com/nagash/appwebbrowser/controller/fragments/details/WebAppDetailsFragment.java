package com.nagash.appwebbrowser.controller.fragments.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.MainFragment;
import com.nagash.appwebbrowser.model.webapp.FavoriteAppsManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;

/**
 * Created by nagash on 19/09/16.
 */
public class WebAppDetailsFragment extends MainFragment {


    private WebApp webApp = null;
    private boolean favoriteToggle = false;
    private final FavoriteAppsManager favoriteAppsManager;

    public WebAppDetailsFragment(MainFragment parentFragment) {
        super(MainFragment.builder()
                .setBackButton(BackButton.VISIBLE)
                .setColor(parentFragment.getColorID())
                .setColorDark(parentFragment.getColorDarkID())
                .build() );
        favoriteAppsManager = new FavoriteAppsManager(parentFragment.getActivity());
    }






    public WebAppDetailsFragment setWebApp(WebApp webApp) {
        this.webApp = webApp;
        updateView();
        return this;
    }


    public void updateView() {
        if(webApp != null && getActivity() != null)
        {


            EditText etID           = (EditText) getActivity().findViewById(R.id.etAppInfoID);
            EditText etName         = (EditText) getActivity().findViewById(R.id.etAppInfoName);
            EditText etVersionCode  = (EditText) getActivity().findViewById(R.id.etAppInfoVersionCode);
            EditText etVersionName  = (EditText) getActivity().findViewById(R.id.etAppInfoVersionName);

            EditText etLatitude     = (EditText) getActivity().findViewById(R.id.etAppGpsLatitude);
            EditText etLongitude    = (EditText) getActivity().findViewById(R.id.etAppGpsLongitude);

            EditText etEddyNamespace = (EditText) getActivity().findViewById(R.id.etAppBeaconNamespace);
            EditText etEddyInstance  = (EditText) getActivity().findViewById(R.id.etAppBeaconInstance);
            EditText etIBeaconMajor = (EditText) getActivity().findViewById(R.id.etAppBeaconMajor);
            EditText etIBeaconMinor = (EditText) getActivity().findViewById(R.id.etAppBeaconMinor);
            EditText etIBeaconUUID = (EditText) getActivity().findViewById(R.id.etAppBeaconUUID);



            etID.setText(webApp.getId());
            etName.setText(webApp.getName());
            etVersionCode.setText(new Integer(webApp.getVersionCode()).toString());
            etVersionName.setText(webApp.getVersionName());

            etLatitude.setText(webApp.getLatitude() != null ? webApp.getLatitude().toString() : "");
            etLongitude.setText(webApp.getLongitude() != null ? webApp.getLongitude().toString() : "");

            etEddyNamespace.setText(webApp.getUID_namespace());
            etEddyInstance.setText(webApp.getUID_instance());

            super.setTitle(webApp.getName()).updateTitle();


            loadFavorite();



        }
    }



    @Override public void onFragmentShown() {
        super.onFragmentShown();
        getMainActivity().showBackButton();
    }
    @Override public void onFragmentHidden() {
        super.onFragmentHidden();
        getMainActivity().hideBackButton();
    }



    @Override   public View  onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_details, container, false);
        setHasOptionsMenu(true);
        return view;
    }
    @Override   public void  onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateView();
    }




    MenuItem favoriteMenuItem = null;
    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_app_details, menu);
        favoriteMenuItem = menu.getItem(0);
        updateFavoriteIcon();
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.favourites:
                favoriteToggle = !favoriteToggle;
                updateFavoriteIcon();
                saveFavorite();
                break;
        }
        return false;
    }

    private void updateFavoriteIcon() {
        if(favoriteMenuItem != null)
        {
            if (favoriteToggle)
                favoriteMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
            else
                favoriteMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
        }
    }




    private void saveFavorite() {
        if(webApp != null && webApp.getId() != null)
        {
            favoriteAppsManager.setFavorite(webApp, this.favoriteToggle);
            favoriteAppsManager.savePreferences();
        }
    }
    private void loadFavorite() {
        if(webApp!=null && webApp.getId()!= null)
        {
            favoriteAppsManager.loadPreferences();
            favoriteToggle = favoriteAppsManager.isFavorite(webApp);
        }
    }


}
