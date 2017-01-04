package com.nagash.appwebbrowser.controller;

import android.app.Application;
import android.content.Intent;

import com.facebook.react.BuildConfig;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.github.xinthink.rnmk.ReactMaterialKitPackage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nagash on 04/01/17.
 */


public class MainApplication extends Application  implements ReactApplication {



    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        protected boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new ReactMaterialKitPackage()
            );
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // do stuff (prefs, etc)

        // start the initial Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

       // SoLoader.init(this, /* native exopackage */ false);

    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }


}
