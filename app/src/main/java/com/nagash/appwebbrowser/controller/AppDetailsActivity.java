package com.nagash.appwebbrowser.controller;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.fragments.details.WebAppDetailsFragment;
import com.nagash.appwebbrowser.model.webapp.FavouriteAppsManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;

/**
 * Created by nagash on 16/12/16.
 */

public class AppDetailsActivity extends AppCompatActivity {

    private WebApp webApp = null;
    private boolean favoriteToggle = false;
    private FavouriteAppsManager favouriteAppsManager;

//    public WebAppDetailsFragment() {
//        super();
//        setBackButton(MainFragment.BackButton.VISIBLE);
//        favouriteAppsManager = FavouriteAppsManager.getInstance(getActivity());
//    }

//    public WebAppDetailsFragment setParentFragmentStyle(MainFragment parentFragment) {
//        setColorID(parentFragment.getColorID());
//        setColorDarkID(parentFragment.getColorDarkID());
//        return this;
//    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }
    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override protected void onStart() {
    }
    @Override protected void onStop() {
        super.onStop();
    }
    @Override protected void onResume() {    // Same as onPause - need to call onHostResume on our ReactInstanceManager
        super.onResume();
    }
    @Override protected void onPause() {    // Any activity that uses the ReactFragment or ReactActivty Needs to call onHostPause() on the ReactInstanceManager
        super.onPause();
    }






    public AppDetailsActivity setWebApp(WebApp webApp) {
        this.webApp = webApp;
        updateView();
        return this;
    }


    public void updateView() {
        if(webApp != null)
        {


            EditText etID           = (EditText) findViewById(R.id.etAppInfoID);
            EditText etName         = (EditText) findViewById(R.id.etAppInfoName);
            EditText etVersionCode  = (EditText) findViewById(R.id.etAppInfoVersionCode);
            EditText etVersionName  = (EditText) findViewById(R.id.etAppInfoVersionName);

            EditText etLatitude     = (EditText) findViewById(R.id.etAppGpsLatitude);
            EditText etLongitude    = (EditText) findViewById(R.id.etAppGpsLongitude);

            EditText etEddyNamespace = (EditText) findViewById(R.id.etAppBeaconNamespace);
            EditText etEddyInstance  = (EditText) findViewById(R.id.etAppBeaconInstance);
            EditText etIBeaconMajor = (EditText)  findViewById(R.id.etAppBeaconMajor);
            EditText etIBeaconMinor = (EditText)  findViewById(R.id.etAppBeaconMinor);
            EditText etIBeaconUUID = (EditText)   findViewById(R.id.etAppBeaconUUID);



            etID.setText(webApp.getId());
            etName.setText(webApp.getName());
            etVersionCode.setText(new Integer(webApp.getVersionCode()).toString());
            etVersionName.setText(webApp.getVersionName());

            etLatitude.setText(webApp.getLatitude() != null ? webApp.getLatitude().toString() : "");
            etLongitude.setText(webApp.getLongitude() != null ? webApp.getLongitude().toString() : "");

            etEddyNamespace.setText(webApp.getUID_namespace());
            etEddyInstance.setText(webApp.getUID_instance());

            setTitle(webApp.getName());
            // TODO: getToolbar().setTitle(title);
            loadFavorite();



        }
    }






    MenuItem favoriteMenuItem = null;
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app_details, menu);
        favoriteMenuItem = menu.getItem(0);
        updateFavoriteIcon();
        return true;
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
            favouriteAppsManager.setFavorite(webApp, this.favoriteToggle);
            favouriteAppsManager.savePreferences();
        }
    }
    private void loadFavorite() {
        if(webApp!=null && webApp.getId()!= null)
        {
            favouriteAppsManager.loadPreferences();
            favoriteToggle = favouriteAppsManager.isFavorite(webApp);
            updateFavoriteIcon();
        }
    }
}
