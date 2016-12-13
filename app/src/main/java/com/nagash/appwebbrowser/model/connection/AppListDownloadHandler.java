package com.nagash.appwebbrowser.model.connection;

import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.List;

/**
 * Created by nagash on 14/09/16.
 */


public interface AppListDownloadHandler {
    public void onAppListDownloaded(List<WebApp> webAppList);
}