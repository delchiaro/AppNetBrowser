package com.nagash.appwebbrowser.model.connection;

import android.os.Environment;

import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.io.File;
import java.util.List;

/**
 * Created by nagash on 14/09/16.
 */
public class CentralConnection implements AppListDownloadHandler {

    public static final String LOCAL_USB_DEBUG_IP = "10.0.0.1";

    public static final String IP = "150.217.35.31";//"127.0.0.1"; //"192.168.0.229";// "nagash.homepc.it"; // "192.168.0.228"; //"192.168.0.223";  // "172.19.133.47";
    //public static final String IP = LOCAL_USB_DEBUG_IP
    // ;
    public static final String CENTRAL_LIST_SERVER = "http://" + IP + ":5000/list";
    public static final String CENTRAL_REPO_SERVER = "http://" + IP + ":5000/download/";


    public static final String JS_BUNDLE_LOCAL_FILENAME = "index.android.bundle";
    public static final String JS_BUNDLE_MAIN_CLASS = "Main";
    public static final String JS_BUNDLE_LOCAL_REPO =
            Environment.getExternalStorageDirectory().toString() + File.separator + "WebApp"  + File.separator ;



    public static String getCachedBundlePath(WebApp app) { return JS_BUNDLE_LOCAL_REPO + app.getId() + "/" + JS_BUNDLE_LOCAL_FILENAME; }
    public static String getCachedBundlePath(String appId) { return JS_BUNDLE_LOCAL_REPO + appId  + "/" +  JS_BUNDLE_LOCAL_FILENAME; }
    public static String getRemoteBundleURL(String appId) { return CENTRAL_REPO_SERVER + appId  + "/" +  JS_BUNDLE_LOCAL_FILENAME; }



    private AppListDownloadHandler appListDownloadHandler = null;
    private List<WebApp> cachedWebAppList = null;


    public List<WebApp> getCachedWebAppList() {
        return cachedWebAppList;
    }

    public void startAppListDownload(AppListDownloadHandler handler) throws DoubleConnectionException {
        if(appListDownloadHandler == null)
            appListDownloadHandler = handler;
        else throw new DoubleConnectionException();
        AppListDownloader appListDownloader = new AppListDownloader(this);
        appListDownloader.execute(CENTRAL_LIST_SERVER);
    }


    @Override
    public void onAppListDownloaded(List<WebApp> webAppList) {
        cachedWebAppList = webAppList;
        appListDownloadHandler.onAppListDownloaded(webAppList);
        appListDownloadHandler = null;
    }


    private CentralConnection() {}

    private static CentralConnection _singletone = null;
    public static CentralConnection instance() {
        if(_singletone != null)
            return _singletone;
        else return _singletone = new CentralConnection();
    }


    public class DoubleConnectionException extends  Exception {}
}
