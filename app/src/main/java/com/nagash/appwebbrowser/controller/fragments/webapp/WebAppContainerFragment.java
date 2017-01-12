package com.nagash.appwebbrowser.controller.fragments.webapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.model.connection.AppBundleDownloadHandler;
import com.nagash.appwebbrowser.model.connection.AppBundleDownloader;
import com.nagash.appwebbrowser.model.connection.CentralConnection;
import com.nagash.appwebbrowser.controller.MainFragment;
import com.nagash.appwebbrowser.controller.fragments.webapp.reactnative.ReactFragment;
import com.nagash.appwebbrowser.model.webapp.FavouriteAppsManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.io.File;

import protobuf.app.App;

/**
 * Created by nagash on 14/09/16.
 */
public class WebAppContainerFragment
        extends     MainFragment
        implements  AppBundleDownloadHandler
{

    private ProgressBar progressBarWebApp   = null;
    private TextView    textViewWebApp      = null;

    private boolean         runningWebApp     = false;
    private boolean         downloadingWebApp = false;
    private boolean         loadingWebApp     = false;

    private WebApp          webApp          = null;
    private ReactFragment   reactFragment   = null;

    AppBundleDownloader downloader = null;

    public WebAppContainerFragment() {
        super();
        setColorID(R.color.colorWebAppPrimary);
        setColorDarkID(R.color.colorWebAppPrimaryDark);
        setTitle(R.string.webapp_fragment_actionbar_title);

        this.progressBarWebApp = null;
        this.textViewWebApp = null;
        this.runningWebApp = false;
        this.webApp = null;
        this.reactFragment = null;
        this.webAppFragmentMainLayout = null;
        this.fragmentContainer = null;
        this.isFullscreen = false;
        this.menuItemFavourites = null;
        this.menuItemFullscreen = null;
        this.menuItemClose = null;
        this.favoriteToggle = false;
        this.topBackup = 0;
        this.bottomBackup = 0;

    }





    @Override public void onFragmentShown() {
        super.onFragmentShown();
        getMainActivity().getFabBeacon().hide();
    }


    public ReactFragment getReactFragment() {
        return reactFragment;
    }

    public ReactInstanceManager getReactInstanceManager() {
        if(reactFragment != null)
            return reactFragment.getReactInstanceManager();
        else return null;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_container_webapp, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBarWebApp = (ProgressBar) getActivity().findViewById(R.id.progressBarWebApp);
        textViewWebApp    = (TextView) getActivity().findViewById(R.id.textWebApp);
        this.fragmentContainer = (FrameLayout)this.getActivity().findViewById(R.id.fragment_container);
        this.webAppFragmentMainLayout = (RelativeLayout)this.getActivity().findViewById(R.id.webapp_fragment_main_layout);

        showAppNotLoaded();

        if(webApp != null )
            startApp();

        this.favouriteAppsManager = FavouriteAppsManager.getInstance(this.getActivity());
        this.setHasOptionsMenu(true);

    }

    public void showAppDescr(App.WebApp webApp)
    {

    }

    public void showAppNotLoaded() {
        progressBarWebApp.setVisibility(View.GONE);
        textViewWebApp.setText("No app loaded");
        textViewWebApp.setVisibility(View.VISIBLE);
    }
    public void showAppDownloadin() {
        progressBarWebApp.setVisibility(View.VISIBLE);
        textViewWebApp.setText("Downloading WebApp...");
    }
    public void showAppLoading() {
        progressBarWebApp.setVisibility(View.VISIBLE);
        textViewWebApp.setText("Starting WebApp...");
    }
    public void hideAppLoading() {
        progressBarWebApp.setVisibility(View.GONE);
        textViewWebApp.setVisibility(View.GONE);
    }



    public boolean isLastVersionCached() {
        // TODO: quando scarico applicazione devo salvare su un database il riferimento ad essa, insieme al version code.
        // per adesso ritorno true se trovo l'applicazione nel cachata nel telefono, senza controllare version code
        if(webApp != null)
        {
            File file = new File(CentralConnection.getCachedBundlePath(webApp) );
            boolean fileExists = file.exists();
            if(fileExists) return true;
            else return false;
        }
        else return false; // exception
    }

    public void startApp( WebApp webApp) {
        if(runningWebApp) {
            closeApp();
        }
        this.webApp = webApp;
        super.setTitle(webApp.getName());
        startApp();
    }

    private void startApp() {
        if(getActivity() != null && webApp != null && runningWebApp == false)
        {
            //enterFullscreen(); // TODO: There is a visual bug if we force entering fullscreen before starting the app
            showAppLoading();
            boolean cached = isLastVersionCached();
            if(!cached) {
                showAppDownloadin();
                downloadingWebApp=true;
                downloader = new AppBundleDownloader(getActivity(), this);
                downloader.start(this.webApp.getId());
            }
            else runCachedApp();
        }

    }

    private void runCachedApp() {

        showAppLoading();
        loadingWebApp = true;

        reactFragment = ReactFragment.newInstance(this, webApp);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, reactFragment);
        fragmentTransaction.hide(reactFragment);
        fragmentTransaction.commit();


        reactFragment.setReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
            @Override
            public void onReactContextInitialized(ReactContext context) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.show(reactFragment);
                fragmentTransaction.commit();
                hideAppLoading();
                loadingWebApp = false;
                runningWebApp = true;
                getActivity().invalidateOptionsMenu();
                getMainActivity().setRunningAppsCounter(1);
            }
        });

    }

    @Override
    public void onAppBundleDownloaded(String appID) {
        downloadingWebApp = false;
        runCachedApp();
    }

    @Override
    public void onAppBundleDownloadFailed(String appID) {

    }

    @Override
    public void onAppBundleDownloadCanceled(String appID) {
        closeApp();
    }


    // TODO:  RECOVERED
    private FavouriteAppsManager favouriteAppsManager;
    public boolean favoriteToggle;
    public boolean isFullscreen;


    public MenuItem menuItemClose;
    public MenuItem menuItemFavourites;
    public MenuItem menuItemFullscreen;

    public int bottomBackup;
    public int topBackup;

    private RelativeLayout webAppFragmentMainLayout;

    private FrameLayout fragmentContainer;


    private void loadFavorite()
    {

        if ((this.webApp != null) && (this.webApp.getId() != null))
        {
            this.favouriteAppsManager.loadPreferences();
            this.favoriteToggle = this.favouriteAppsManager.isFavorite(this.webApp);
        }

    }

    private void saveFavorite()
    {

        if ((this.webApp != null) && (this.webApp.getId() != null))
        {
            this.favouriteAppsManager.setFavorite(this.webApp, this.favoriteToggle);
            this.favouriteAppsManager.savePreferences();
        }

    }



    public void closeApp()
    {

        if(this.runningWebApp && this.reactFragment!=null && this.webApp != null)
        {
            super.setTitle(R.string.webapp_fragment_actionbar_title);
            super.updateActionBar();
            reactFragment.close();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.hide(this.reactFragment);
            ft.remove(this.reactFragment);
            ft.commit();
            showAppNotLoaded();
            this.reactFragment = null;
            this.runningWebApp = false;
            getActivity().invalidateOptionsMenu();
            getMainActivity().setRunningAppsCounter(0);
            getMainActivity().closeWebApp();
        }


    }


    public void enterFullscreen()
    {
        this.isFullscreen = true;
        getMainActivity().setFullScreen(true);
        //getMainActivity().hideBottomBar();

        DisplayMetrics dm = new DisplayMetrics();
        // getSystemService("Context.WINDOW_SERVICE"); // use this instead???
        ((WindowManager)getActivity().getSystemService("window")).getDefaultDisplay().getMetrics(dm);
        int displayHeight = dm.heightPixels;
        int statusbarHeight= getMainActivity().getStatusBarHeight();
        int bottom = displayHeight-statusbarHeight;

        this.topBackup = this.fragmentContainer.getTop();
        this.bottomBackup = this.fragmentContainer.getBottom();
        this.fragmentContainer.setTop(0);
        this.fragmentContainer.setBottom(bottom);
        this.webAppFragmentMainLayout.setTop(0);
        this.webAppFragmentMainLayout.setBottom(bottom);
        if (this.reactFragment != null)
        {
            this.reactFragment.getReactRootView().setTop(0);
            this.reactFragment.getReactRootView().setBottom(bottom);
        }

    }


    public void exitFullscreen()
    {
        if (this.isFullscreen)
        {
            this.isFullscreen = false;
            getMainActivity().setFullScreen(false);
            //getMainActivity().showBottomBar();
            this.fragmentContainer.setTop(this.topBackup);
            this.fragmentContainer.setBottom(this.bottomBackup);
            this.webAppFragmentMainLayout.setTop(this.topBackup);
            this.webAppFragmentMainLayout.setBottom(this.bottomBackup);
            if (this.reactFragment != null)
            {
                this.reactFragment.getReactRootView().setTop(this.topBackup);
                this.reactFragment.getReactRootView().setBottom(this.bottomBackup);
            }
        }

    }

    public boolean isFullscreen()
    {
        return isFullscreen;
    }

    @Override
    public boolean onBackPressed()
    {
        if(downloadingWebApp) {
            //downloader.cancelDownload();
            // TODO: download cancel no working for now

            return true;
        }
        if(loadingWebApp)
            return true;

        if ((this.reactFragment != null) && (this.runningWebApp) ) {
            this.reactFragment.onBackPressed();
            return true;
        }

        else return false;

    }

    private void updateFavoriteIcon()
    {
        if (this.menuItemFavourites != null)
        {
            loadFavorite();
            if (this.favoriteToggle) {
                this.menuItemFavourites.setIcon(getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
            } else {
                this.menuItemFavourites.setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
            }
        }

    }


    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {

        super.onCreateOptionsMenu(menu, menuInflater);
        if (this.runningWebApp) {
            menuInflater.inflate(R.menu.menu_webapp_running, menu);
            this.menuItemFavourites = menu.findItem(R.id.menu_item_favourites);
            this.menuItemFullscreen = menu.findItem(R.id.menu_item_fullscreen);
            this.menuItemClose = menu.findItem(R.id.menu_item_close);
            this.updateFavoriteIcon();
        }

    }

    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        boolean favoriteToggle = true;
        final boolean b = false;
        boolean booleanValue = false;

        switch (menuItem.getItemId()) {
            default: {
                booleanValue = b;
                break;
            }
            case R.id.menu_item_favourites: {
                if (this.favoriteToggle) {
                    favoriteToggle = false;
                }
                this.favoriteToggle = favoriteToggle;
                this.saveFavorite();
                this.updateFavoriteIcon();
                booleanValue = b;
                break;
            }
            case R.id.menu_item_fullscreen: {
                if (!this.isFullscreen) {
                    this.enterFullscreen();
                    booleanValue = b;
                    break;
                }
                this.exitFullscreen();
                booleanValue = b;
                break;
            }
            case R.id.menu_item_close: {
                booleanValue = b;
                if (!this.runningWebApp) {
                    break;
                }
                booleanValue = b;
                if (this.reactFragment != null) {
                    this.closeApp();
                    booleanValue = b;
                    break;
                }
                break;
            }
        }
        return booleanValue;
    }

}
