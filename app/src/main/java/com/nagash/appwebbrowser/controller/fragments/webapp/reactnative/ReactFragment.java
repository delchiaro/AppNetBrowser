package com.nagash.appwebbrowser.controller.fragments.webapp.reactnative;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.nagash.appwebbrowser.controller.MainFragment;
import com.nagash.appwebbrowser.controller.fragments.webapp.WebAppContainerFragment;
import com.nagash.appwebbrowser.model.connection.CentralConnection;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.io.File;

/**
 * Created by nagash on 14/09/16.
 */
public class ReactFragment
        extends MainFragment  implements DefaultHardwareBackBtnHandler
{

    private static final String TAG = "WebAppLauncherActivity";



    private Context context;


    private String jsBundleAppID;
    private ReactRootView mReactRootView = null;
    private ReactInstanceManager mReactInstanceManager = null;
    private WebAppContainerFragment containerFragment;


    public ReactInstanceManager getReactInstanceManager() {
        return mReactInstanceManager;
    }
    public ReactRootView getReactRootView() {
        return  mReactRootView;
    }



    public static ReactFragment newInstance(final WebAppContainerFragment containerFragment, WebApp application)
    {
        ReactFragment frag = new ReactFragment();
        frag.init(containerFragment, application);
        return frag;
    }

    public ReactFragment() {}
    public ReactFragment init(final WebAppContainerFragment containerFragment, WebApp application) {
        jsBundleAppID = application.getId();
        this.containerFragment = containerFragment;
        return this;
    }
    ReactInstanceManager.ReactInstanceEventListener eventListener = null;
    private boolean eventListenerLinked = false;


    public void setReactInstanceEventListener(ReactInstanceManager.ReactInstanceEventListener reactEventListener){
        this.eventListener = reactEventListener;
    }

    private void tryLinkEventListener() {
        if(eventListener != null &&  !eventListenerLinked ) {
            mReactInstanceManager.addReactInstanceEventListener(this.eventListener);
            this.eventListenerLinked = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        ReactNativeHost host;

        ReactInstanceManager.Builder builder = ReactInstanceManager.builder();
        builder.setApplication(getActivity().getApplication());
        builder.setUseDeveloperSupport(false);
        builder.setJSMainModuleName("index.android");
        builder.addPackage(new MainReactPackage());
        builder.setDefaultHardwareBackBtnHandler(this);
        builder.setInitialLifecycleState(LifecycleState.RESUMED);

        String cachedBundlePath = CentralConnection.getCachedBundlePath(jsBundleAppID);
        File file = new File(cachedBundlePath);

        if(file != null && file.exists()) {
            builder.setJSBundleFile(cachedBundlePath);
            Log.i(TAG, "load bundle from local cache");

            mReactRootView = new ReactRootView(context);
            mReactInstanceManager = builder.build();
            tryLinkEventListener();

        }
        else {
            //builder.setBundleAssetName(JS_BUNDLE_LOCAL_FILE);
            Log.i(TAG, "Error! Bundle file not found locally.");
            return;
        }



    }


    @Override
    public ReactRootView onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tryLinkEventListener();
        return mReactRootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mReactRootView.startReactApplication(mReactInstanceManager, CentralConnection.JS_BUNDLE_MAIN_CLASS, null);
        tryLinkEventListener();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReactInstanceManager != null) {
            mReactRootView.unmountReactApplication();
            mReactInstanceManager.onHostDestroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(getActivity(), this);
        }
    }


    // TODO: RECOVERED - controlla che funzioni..

    @Override
    public void invokeDefaultOnBackPressed() {
        if (this.containerFragment.isFullscreen())
            this.containerFragment.exitFullscreen();
        else this.containerFragment.closeApp();
    }

    public boolean onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    public void close() {
        mReactInstanceManager.detachRootView(mReactRootView);
    }

}
