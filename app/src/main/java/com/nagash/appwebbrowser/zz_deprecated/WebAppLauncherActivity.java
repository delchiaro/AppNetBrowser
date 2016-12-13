package com.nagash.appwebbrowser.zz_deprecated;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.nagash.appwebbrowser.R;

import java.io.File;


/**
 * Created by nagash on 30/08/16.
 */
public class WebAppLauncherActivity extends Activity implements DefaultHardwareBackBtnHandler {


    // DEBUG URL: http://127.0.0.1:8081/index.android.bundle?platform=android   &dev=true
    private static final String TAG = "WebAppLauncherActivity";


    private static final int APP_LAUNCH_DELAY = 1000;

    public final static String KEY_JS_APP_NAME = "name";
    public final static String KEY_JS_BUNDLE_REMOTE_URL = "url";
    public final static String KEY_JS_BUNDLE_REMOTE_VERSION_CODE = "version_code";

    private static final String JS_BUNDLE_LOCAL_FILENAME = "index.android.bundle";
    private static final String JS_BUNDLE_MAIN_CLASS = "Main";
    private static final String JS_BUNDLE_LOCAL_REPO = Environment.getExternalStorageDirectory().toString() + File.separator + "WebApp"  + File.separator ;

    private String jsBundleAppID;
    private String jsBundleRemoteUrl;
    private int jsBundleRemoteVersionCode;

    private String jsBundleLocalDirPath;
    private String jsBundleLocalFilePath;



    // Olds - da eliminare:
//    public static final String JS_BUNDLE_REMOTE_URL = "https://raw.githubusercontent.com/fengjundev/React-Native-Remote-Update/master/remote/index.android.bundle";
//    public static final String JS_BUNDLE_LOCAL_PATH = Environment.getExternalStorageDirectory().toString() + File.separator + "index.android.bundle";


    private ReactInstanceManager mReactInstanceManager;
    private ReactRootView mReactRootView;

    private CompleteReceiver mDownloadCompleteReceiver;
    private long mDownloadId;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zz_deprecated_activity_launcher_webapp);
        init();



        boolean cached = isLastVersionCached();
        if(!cached)
            initAndStartDownloadManager();
        else initAndStartBundle();

    }

    public void init() {
        Intent intent = getIntent();
        jsBundleAppID = intent.getStringExtra(KEY_JS_APP_NAME);
        jsBundleRemoteUrl = intent.getStringExtra(KEY_JS_BUNDLE_REMOTE_URL);
        jsBundleRemoteUrl += File.separator + jsBundleAppID +  File.separator + "index.android.bundle";
        jsBundleRemoteVersionCode = intent.getIntExtra(KEY_JS_BUNDLE_REMOTE_VERSION_CODE, 1);
        jsBundleLocalDirPath = JS_BUNDLE_LOCAL_REPO + jsBundleAppID +  File.separator;
        jsBundleLocalFilePath = jsBundleLocalDirPath + JS_BUNDLE_LOCAL_FILENAME;
    }

    public boolean isLastVersionCached() {
        // TODO: quando scarico applicazione devo salvare su un database il riferimento ad essa, insieme al version code.
        // per adesso ritorno true se trovo l'applicazione nel cachata nel telefono, senza controllare version code
        File file = new File(jsBundleLocalFilePath);
        boolean fileExists = file.exists();
        if(fileExists)
            return true;
        else return false;
    }

    private void onBundleDownloaded() {
        File file = new File(jsBundleLocalFilePath);

        if(file == null || !file.exists())
            Log.i(TAG, "download error, check URL or network state");

        else {
            Log.i(TAG, "download success!");
            initAndStartBundle();
        }
    }


    private void initAndStartBundle() {
        ReactInstanceManager.Builder builder = ReactInstanceManager.builder();
        builder.setApplication(getApplication());
        builder.setUseDeveloperSupport(false);
        builder.setJSMainModuleName("index.android");
        builder.addPackage(new MainReactPackage());
        builder.setInitialLifecycleState(LifecycleState.RESUMED);

        File file = new File(jsBundleLocalFilePath);

        if(file != null && file.exists()) {
            builder.setJSBundleFile(jsBundleLocalFilePath);
            Log.i(TAG, "load bundle from local cache");
        }
        else {
            //builder.setBundleAssetName(JS_BUNDLE_LOCAL_FILE);
            Log.i(TAG, "Error! Bundle file not found locally.");
        }

        mReactRootView = new ReactRootView(this);
        mReactInstanceManager = builder.build();
        mReactRootView.startReactApplication(mReactInstanceManager, JS_BUNDLE_MAIN_CLASS, null);

        new Handler().postDelayed(
                new Runnable() {
                    public void run() { setContentView(mReactRootView); }
                 }
                , APP_LAUNCH_DELAY);


    }




    // DOWNLOAD MANAGER * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    private void initAndStartDownloadManager() {
        mDownloadCompleteReceiver = new CompleteReceiver();
        registerReceiver(mDownloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        //Toast.makeText(BaseReactActivity.this, "Start downloading update", Toast.LENGTH_SHORT).show();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(jsBundleRemoteUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationUri(Uri.parse("file://" + jsBundleLocalFilePath));
        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mDownloadId = dm.enqueue(request);
        Log.i(TAG, "start download remote bundle");
    }


    private class CompleteReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context context, Intent intent)
        {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if(completeDownloadId == mDownloadId)
                onBundleDownloaded();
        }
    }












    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDownloadCompleteReceiver != null)
            unregisterReceiver(mDownloadCompleteReceiver);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this, new DefaultHardwareBackBtnHandler() {
                @Override
                public void invokeDefaultOnBackPressed() {
                    finish();
                }
            });
        }
    }


















    // Hot Swap di un bundle con un altro. Rischioso!
//
//    private void hotReloadReactBundle() {
//        Log.i(TAG, "download success, reload js bundle");
//
//        Toast.makeText(WebAppLauncherActivity.this, "Downloading complete", Toast.LENGTH_SHORT).show();
//        try {
//            Class<?> RIManagerClazz = mReactInstanceManager.getClass();
//            Method method = RIManagerClazz.getDeclaredMethod("recreateReactContextInBackground",
//                    JavaScriptExecutor.class, JSBundleLoader.class);
//            method.setAccessible(true);
//            method.invoke(mReactInstanceManager,
//                    new JSCJavaScriptExecutor(),
//                    JSBundleLoader.createFileLoader(getApplicationContext(), JS_BUNDLE_LOCAL_PATH));
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    private void updateBundle() {
//
//        // Should add version check here, if bundle file
//        // is the newest , we do not need to update
//
//        File file = new File(JS_BUNDLE_LOCAL_PATH);
//        if(file != null && file.exists()){
//            Log.i(TAG, "newest bundle exists !");
//            return;
//        }
//
//        //Toast.makeText(BaseReactActivity.this, "Start downloading update", Toast.LENGTH_SHORT).show();
//
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(JS_BUNDLE_REMOTE_URL));
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//        request.setDestinationUri(Uri.parse("file://" + JS_BUNDLE_LOCAL_PATH));
//        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        mDownloadId = dm.enqueue(request);
//
//        Log.i(TAG, "start download remote bundle");
//    }


}
