package com.nagash.appwebbrowser.controller;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.webAppController.WebAppController;
import com.nagash.appwebbrowser.utils.BlueUtility;
import com.nagash.appwebbrowser.model.connection.AppListDownloadHandler;
import com.nagash.appwebbrowser.model.connection.CentralConnection;
import com.nagash.appwebbrowser.model.localization.LocationManager;
import com.nagash.appwebbrowser.model.localization.LocationEventListener;
import com.nagash.appwebbrowser.model.webapp.FavouriteAppsManager;
import com.nagash.appwebbrowser.model.webapp.WebApp;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

/**
 * Created by nagash on 14/09/16.
 */
public class MainActivity
        extends AppCompatActivity

        implements
        DefaultHardwareBackBtnHandler,
        AppListDownloadHandler,
        LocationEventListener
{

    private static final boolean SHOW_GPS_UPDATE_TOAST = false;
    private static final boolean SHOW_BEACON_DEBUG_BUTTON = false;



    private static MainActivity mainActivity = null;
    protected static MainActivity getMainActivity() {
        return mainActivity;
    }
    private static void setMainActivity(MainActivity mainActivity) {
        MainActivity.mainActivity = mainActivity;
    }
    // * * * * * Managers * * * * *

    FragmentsController fragCtrl = new FragmentsController(this);
    CentralConnection centralConnection = CentralConnection.instance();
    LocationManager locationManager = new LocationManager(this, this);
    //GeofenceManager<WebApp> geofenceManager = new GeofenceManager(locationManager, this, EmptyListAdvertiseOptions.ADVERTISE_ON_EMPTY_LIST);






    // * * * * * UI COMPONENTS * * * * *
    private BottomBar  bottomBar  = null;
    private Toolbar    toolbar    = null;

    public BottomBar   getBottomBar()      { return bottomBar;     }
    public Toolbar     getToolbar()        { return toolbar;       }

    private RelativeLayout mainFragmentContainer;
    private RelativeLayout mainLayout;




    //  TODO: embed fab in classs
    private enum FabBeaconStatus { Hidden, Visible }
    private FabBeaconStatus fabBeaconStatus = FabBeaconStatus.Hidden;
    private FloatingActionButton fabBeacon = null;
    private WebApp nearestBeaconFabApp = null;
    public  FloatingActionButton getFabBeacon()      { return fabBeacon;  }
    public void setNearestBeaconFabApp(WebApp app) {
        if(app != null) {
            this.fabBeaconStatus = FabBeaconStatus.Visible;
            nearestBeaconFabApp = app;
            updateFabVisibility();
        }
        else if(nearestBeaconFabApp != null){
            this.fabBeaconStatus = FabBeaconStatus.Hidden;
            updateFabVisibility();
            nearestBeaconFabApp = null;
        }
    }
    public  void updateFabVisibility()  {
        if(fragCtrl.getMainMode() == MainMode.WEBAPP) {
            fabBeacon.hide();
        }
        else
        {
            if(fabBeaconStatus == FabBeaconStatus.Visible)
            {
                if(nearestBeaconFabApp != null && nearestBeaconFabApp == activeWebApp) {
                    //fabBeacon.setImageResource(R.drawable.ic_cast_connected_black_24dp);
                    fabBeacon.hide();
                }
                else{
                    //fabBeacon.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    fabBeacon.show();
                }
            }
            else if(fabBeaconStatus == FabBeaconStatus.Hidden)
                fabBeacon.hide();

        }
    }


    public void showBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    public void hideBackButton() {
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    public void setToolbarColor(int resourcePrimaryColorID, int resourcePrimaryDarkColorID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(resourcePrimaryDarkColorID));
            getToolbar().setElevation(0);
        }
        getToolbar().setBackground(new ColorDrawable(getResources().getColor(resourcePrimaryColorID)));

        // TODO: usare questa funzione per settare il colore delle icone della bottombar (problema: setta anche il colore di sfondo e lo setta in ritardo)
        //getBottomBar().setActiveTabColor(getResources().getColor(resourcePrimaryColorID));
    }




    // * * * * * NOTIFICATIONS - BROADCAST RECEIVER - FULLSCREEN * * * * *
    private final static int     FULLSCREEN_NOTIFICATION_ID = 13;
    private final static String  FULLSCREEN_KEY = "com.nagash.appwebbrowser.EXIT_FULLSCREEN";
    BroadcastReceiver broadcastReceiver;
    private boolean fullScreen = false;
    private NotificationManager mNotificationManager = null;






    public MainMode getMode() { return fragCtrl.getMainMode(); }


    public void showAppDetails(WebApp webApp) {
        fragCtrl.showAppDetils(webApp);
    }






    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * LOCATION  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override public void onLocationChanged(Location location) {
        if(SHOW_GPS_UPDATE_TOAST)
            Toast.makeText(this, "Location changed: " + location.getLatitude() + ", " + location.getLongitude() + "\nAccuracy: " + location.getAccuracy()
                , Toast.LENGTH_SHORT).show();
    }
    @Override public void onConnected(Location myLastLocation) {
    }
    @Override public void onConnectionSuspended() {}
    @Override public void onConnectionFailed() {}

    public LocationManager getLocationManager() { return locationManager; }




//    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//     * * * * * * * * * * * * * * * GEOFENCE  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//    @Override public void onGeofenceIn(SortedSet<GeofenceObject<WebApp>> triggeredGeofences) {
//
//        if(triggeredGeofences.size() == 0)
//        {
//            this.proximityApp = null;
//            fabBeaconStatus = FabBeaconStatus.Hidden;
//        }
//        else
//        {
//            //Toast.makeText(this, "GeofenceObject Triggered!", Toast.LENGTH_SHORT).show();
//            final GeofenceObject<WebApp> nearest = triggeredGeofences.first();
//            this.proximityApp = nearest.getManagedObject();
//            fabBeaconStatus = FabBeaconStatus.Visible;
//
//        }
//        updateFabVisibility();
//    }
//    @Override public void onGeofenceOut(SortedSet<GeofenceObject<WebApp>> triggeredGeofences)     {}
//    @Override public void onGeofenceEntering(SortedSet<GeofenceObject<WebApp>> triggeredGeofences)   {}
//    @Override public void onGeofenceExiting(SortedSet<GeofenceObject<WebApp>> triggeredGeofences)    {}
//





    /* * * * *  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * APPLIST DOWNLOADER  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public  void onAppListDownloaded(List<WebApp> webAppList) {

        if(webAppList != null){
            // initContentViewLoaded();
            fragCtrl.getNearbyListFragment().onAppsDownloaded();

            fragCtrl.getMapFragment().setMarkers(webAppList);
            startWebAppController(webAppList);

//            Collection<GeofenceObject<WebApp>> geoObjs = GeofenceObject.createGeofenceObjects(webAppList, new GeoEvent().enableInEvent());
//            geofenceManager.setGeofenceObjects( geoObjs );
//            geofenceManager.startScan(5000);
        }

        else fragCtrl.getNearbyListFragment().onServerNotAvailable();
    }
    private  void connectToLinkServer() throws CentralConnection.DoubleConnectionException {
        fragCtrl.getNearbyListFragment().setStatusLoading();
        centralConnection.startAppListDownload(this);
    }
    public   void retryConnection() {
        try { connectToLinkServer(); }
        catch (CentralConnection.DoubleConnectionException e) { e.printStackTrace(); }
    }
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * *  WEBAPP MANAGEMENT  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private WebAppController webAppController       = null;
    private WebApp               activeWebApp           = null;
    public void startAppFragment(WebApp webApp) {
        //Get the reference to the ReactInstanceManager
        fragCtrl.changeMode(MainMode.WEBAPP);
        activeWebApp = webApp;
        fragCtrl.getWebAppContainerFragment().startApp(webApp);
        bottomBar.selectTabWithId(R.id.tab_webapp);

    }
    public void closeWebApp() {
        fragCtrl.getWebAppContainerFragment().exitFullscreen();
        fragCtrl.getWebAppContainerFragment().closeApp();
        activeWebApp = null;
    }
    // public WebApp                   getProximityApp()   { return proximityApp;  }
    // public WebApp                   getActiveWebApp()   { return activeWebApp;  }

    public void setNearbyAppsCounter(int counter) {
        BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_nearby);
        if(counter <= 0)
            nearby.removeBadge();
        else nearby.setBadgeCount(counter);
    }
    public void setRunningAppsCounter(int counter) {
        BottomBarTab webAppTab = bottomBar.getTabWithId(R.id.tab_webapp);
        if(counter <= 0)
            webAppTab.removeBadge();
        else webAppTab.setBadgeCount(counter);
    }

    private void initWebAppController() {
        webAppController = new WebAppController(locationManager);
        webAppController.setOnUpdateListener(fragCtrl.getNearbyListFragment());
    }
    private void startWebAppController(List<WebApp> downloadedApps) {
        if(downloadedApps != null)
            webAppController.start(downloadedApps, this);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */












    private void initContentView() {
        setContentView(R.layout.activity_main);
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mainFragmentContainer = (RelativeLayout) findViewById(R.id.main_fragment_container);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        toolbar   = (Toolbar) findViewById(R.id.toolbar);
        fabBeacon = (FloatingActionButton) findViewById(R.id.fab_proximity);
        fabBeacon.hide();


        fabBeaconStatus = FabBeaconStatus.Hidden;

        fabBeacon.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        if(nearestBeaconFabApp != null) {
                            startAppFragment(nearestBeaconFabApp);
                        }

//                        fabBeaconStatus = FabBeaconStatus.Hidden;
//                        fabBeacon.hide();
                    }
                });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {


            @Override public void onTabSelected(@IdRes int tabId) {

                switch(tabId) {
                    case R.id.tab_nearby:
                        fragCtrl.changeMode(MainMode.NEARBY);
                        break;

                    case R.id.tab_favourites:
                        fragCtrl.changeMode(MainMode.FAVOURITES);
                        break;

                    case R.id.tab_map:
                        fragCtrl.changeMode(MainMode.MAP);
                        break;

                    case R.id.tab_webapp:
                        fragCtrl.changeMode(MainMode.WEBAPP);
                        break;
                }
            }
        });
//        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
//            @Override
//            public void onTabReSelected(@IdRes int tabId) {
//
//                View yourView = bottomBar.getTabWithId(tabId);
//                switch (tabId) {
//                    case R.id.tab_nearby:
//                        SimpleTooltip t = new SimpleTooltip.Builder(getBaseContext())
//                                .anchorView(yourView)
//                                .text("FIlter Radius")
//                                .gravity(Gravity.TOP)
//                                .animated(false)
//                                .transparentOverlay(true)
//                                .contentView(R.layout.tooltip_filter_slider)
//                                .build();
//                        t.show();
//
//                        break;
//
//                    case R.id.tab_map:
//
//                        break;
//
//                    case R.id.tab_webapp:
//
//                        break;
//                }
//            }
//        });

        fragCtrl.changeMode(MainMode.NEARBY);
        fragCtrl.earlyInitMapFragment();

//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
//            @Override public void onPageSelected(int position) { bottomBar.selectTabAtPosition(position); }
//            @Override public void onPageScrollStateChanged(int state) {}
//        });


        // Manage fullscreen notification:
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(FULLSCREEN_KEY);
//        broadcastReceiver = new FullscreenBroadcastreceiver();
//        registerReceiver(broadcastReceiver, filter);
    }

    public void activateMode(MainMode mode) {
        switch (mode)
        {
            case NEARBY:
                bottomBar.selectTabWithId(R.id.tab_nearby);
                break;
            case FAVOURITES:
                bottomBar.selectTabWithId(R.id.tab_favourites);
                break;
            case MAP:
                bottomBar.selectTabWithId(R.id.tab_map);
                break;
            case WEBAPP:
                bottomBar.selectTabWithId(R.id.tab_webapp);
                break;
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * MANAGE ACTIVITY LIFECYCLE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(BlueUtility.isLocationEnabled(this) == false)
            BlueUtility.requestGps(this);

        if(BlueUtility.isBleSupported())
            if(BlueUtility.isBluetoothEnabled() == false)
                BlueUtility.requestBluetoothAccess(this);

        initContentView();
        locationManager.setHighAccuracy();
        locationManager.onCreate(savedInstanceState);
        // initEddy();
        initWebAppController();

        MainActivity.setMainActivity(this);
        try{ connectToLinkServer(); }
        catch (CentralConnection.DoubleConnectionException e) { e.printStackTrace(); }



    }
    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        locationManager.onSaveInstanceState(outState);
    }
    @Override protected void onStart() {
        super.onStart();

        if(isFullScreen())
            showFullscreenNotification();



        if(centralConnection.getCachedWebAppList() != null && webAppController != null)
            webAppController.start(centralConnection.getCachedWebAppList(), this);
        locationManager.onStart();

    }
    @Override protected void onStop() {
        super.onStop();
        locationManager.onStop();
        if(webAppController != null)
            webAppController.stop();

        if(isFullScreen()) {
            // TODO: this is a workaround for a bug - when entering in a webApp, pressing home, re-open app, pressing back,
            // will close AppWeb Browser instead of exiting fullscreen or close the app.
            fragCtrl.getWebAppContainerFragment().exitFullscreen();
            hideFullscreenNotification();
        }
    }
    @Override protected void onResume() {    // Same as onPause - need to call onHostResume on our ReactInstanceManager
        super.onResume();
        if (fragCtrl.getWebAppContainerFragment() != null && fragCtrl.getWebAppContainerFragment().getReactInstanceManager() != null) {
            fragCtrl.getWebAppContainerFragment().getReactInstanceManager() .onHostResume(this, this);
        }
        locationManager.onResume();
        FavouriteAppsManager.getInstance(this).loadPreferences();
    }
    @Override protected void onPause() {    // Any activity that uses the ReactFragment or ReactActivty Needs to call onHostPause() on the ReactInstanceManager
        super.onPause();
        if (fragCtrl.getWebAppContainerFragment() != null && fragCtrl.getWebAppContainerFragment().getReactInstanceManager()  != null) {
            fragCtrl.getWebAppContainerFragment().getReactInstanceManager() .onHostPause();
        }
        locationManager.onPause();
        FavouriteAppsManager.getInstance(this).savePreferences();
    }
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */



    @Override public void onBackPressed() {
        if( fragCtrl.onMainBackPressed() == false )
            super.onBackPressed();
    }
    @Override public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }







    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
        * * * * * * * * * * * * * * * MANAGE FULLSCREEN MODE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    int topBackup = 0;
    int bottomBackup = 0;
    boolean bottomBarHidden = false;
    boolean toolbarHidden = false;
    public void hideBottomBar() {
        if(bottomBarHidden) return;

        getBottomBar().animate().translationY( pxFromDp(130) ).setDuration(600).start();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceHeight = displayMetrics.heightPixels;
        bottomBackup = mainFragmentContainer.getBottom();
        mainFragmentContainer.setBottom(deviceHeight);
        mainFragmentContainer.invalidate();
        mainFragmentContainer.refreshDrawableState();

        bottomBarHidden = true;
    }
    public void showBottomBar() {
        if(!bottomBarHidden) return;
        getBottomBar().animate().translationY(0).setDuration(600).start();
        mainFragmentContainer.setTop(topBackup);
        mainFragmentContainer.setBottom(bottomBackup);
        mainFragmentContainer.requestLayout();
        bottomBarHidden = false;
    }

    public void hideToolbar() {
        if(toolbarHidden)return;
        getToolbar().animate().translationY( -pxFromDp(60) ).setDuration(600).start();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        topBackup = mainFragmentContainer.getTop();
        mainFragmentContainer.setTop(0);
        mainFragmentContainer.requestLayout();
        toolbarHidden = true;
    }
    public void showToolbar() {
        if(!toolbarHidden) return;
        hideFullscreenNotification();
        getToolbar().animate().translationY(0+statusBarHeight).setDuration(600).start();
        mainFragmentContainer.setTop(topBackup);
        mainFragmentContainer.invalidate();
        toolbarHidden = false;
    }

//    public void setFullScreen(boolean on) {
//        if(fullScreen && on || !fullScreen && !on) return;
//        if(!fullScreen && on) { // ENTERING FULLSCREEN
//            showFullscreenNotification();
//            hideBottomBar();
//            hideToolbar();
//            fullScreen = true;
//        }
//        else if(fullScreen && !on) { // EXITING FULLSCREEN
//            hideFullscreenNotification();
//            showBottomBar();
//            showToolbar();
//            fullScreen = false;
//        }
//    }

    public void setFullScreen(boolean on) {
        if(fullScreen && on || !fullScreen && !on) return;
        if(!fullScreen && on) { // ENTERING FULLSCREEN
            //.setSystemUiVisibility(flags);
//            Rect rectangle = new Rect();
//            getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
//            statusBarHeight = rectangle.top;

            statusBarHeight = getStatusBarHeight();
            showFullscreenNotification();
            getToolbar().animate().translationY( -pxFromDp(100) ).setDuration(600).start();
            getBottomBar().animate().translationY( pxFromDp(80) ).setDuration(600).start();


            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            int deviceWidth = displayMetrics.widthPixels;
            int deviceHeight = displayMetrics.heightPixels;

//            Rect rectangle = new Rect();
//            getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
//            statusBarHeight = rectangle.top;
            topBackup = mainFragmentContainer.getTop();
            bottomBackup = mainFragmentContainer.getBottom();
            mainFragmentContainer.setTop(0);
            int bottom = deviceHeight-(statusBarHeight);
            mainFragmentContainer.setBottom(bottom);
            mainFragmentContainer.invalidate();
//            getWindow().addFlags(flags);
            fullScreen = true;
        }
        else if(fullScreen && !on) { // EXITING FULLSCREEN
            hideFullscreenNotification();
            getToolbar().animate().translationY(0).setDuration(600).start();
            getBottomBar().animate().translationY(0).setDuration(600).start();
            mainFragmentContainer.setTop(topBackup);
            mainFragmentContainer.setBottom(bottomBackup);
            mainFragmentContainer.invalidate();
//            getWindow().clearFlags(flags);
            fullScreen = false;
        }
    }

    private void hideFullscreenNotification() {
        if(mNotificationManager != null)
            mNotificationManager.cancel(FULLSCREEN_NOTIFICATION_ID);
    }
    private void showFullscreenNotification() {

//        Intent action1Intent = new Intent(this, MainActivity.NotificationActionService.class).setAction(FULLSCREEN_KEY);
//        PendingIntent action1PendingIntent = PendingIntent.getService(this, 0, action1Intent, PendingIntent.FLAG_ONE_SHOT);


        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent nid = new Intent(this, NotificationActionService.class);
        // If you were starting a service, you wouldn't using getActivity() here
        PendingIntent ci = PendingIntent.getService(this, FULLSCREEN_NOTIFICATION_ID, nid, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_up_down_arrows_white_24dp)
                        .setContentTitle("WebApp in fullscreen mode")
                        .setContentText("Tap to exit fullscreen mode")
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setPriority(Notification.PRIORITY_MAX)
                        //.addAction(new NotificationCompat.Action(R.drawable.ic_up_down_arrows_white_24dp, "Exit Fullscreen", action1PendingIntent))
                        .setContentIntent(ci);

        mNotificationManager.notify(FULLSCREEN_NOTIFICATION_ID, notificationBuilder.build());
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(FULLSCREEN_NOTIFICATION_ID, notificationBuilder.build());
//        notificationBuilder.setContentIntent(action1PendingIntent);
    }
    public static class NotificationActionService extends IntentService {
        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }


        @Override
        public void onStart(Intent intent, int startId) {
            super.onStart(intent, startId);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            //if (action.equals(FULLSCREEN_KEY)) {
            if (getMainActivity() != null) {
                getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getMainActivity().fragCtrl.getWebAppContainerFragment().exitFullscreen();
                    }
                });
            }
            NotificationManagerCompat.from(this).cancel(FULLSCREEN_NOTIFICATION_ID);
           // }
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        //if((boolean)intent.getExtras().get("EXIT_FULLSCREEN"))
        setFullScreen(false);
    }

    public boolean isFullScreen() {
        return fullScreen;
    }




    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * MANAGE ACTIVITY MENÚ  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    // Called Once
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onSupportNavigateUp(){
        onBackPressed(); //replace onOptionItemSelected( android.R.id.home )
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
            default:
                //fragCtrl.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);

    }
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */










    /* * * * * * * * * * * * * * *  UTILS  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private float dpFromPx(float px) {
        return px / getResources().getDisplayMetrics().density;
    }

    private float pxFromDp(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    int statusBarHeight = -1;
    public int getStatusBarHeight() {
        if(statusBarHeight == -1) {
            statusBarHeight = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
            return statusBarHeight;
        }
        else return statusBarHeight;
    }

}

