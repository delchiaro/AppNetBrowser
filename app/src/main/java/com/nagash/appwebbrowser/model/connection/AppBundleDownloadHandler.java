package com.nagash.appwebbrowser.model.connection;

/**
 * Created by nagash on 14/09/16.
 */


public interface AppBundleDownloadHandler {
    public void onAppBundleDownloaded(String appID);
    public void onAppBundleDownloadFailed(String appID);
    public void onAppBundleDownloadCanceled(String appID);

}