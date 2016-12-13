package com.nagash.appwebbrowser.model.connection;

import android.os.AsyncTask;

import com.nagash.appwebbrowser.model.webapp.WebApp;
import com.nagash.appwebbrowser.model.webapp.WebAppList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import protobuf.app.App;

/**
 * Created by nagash on 31/08/16.
 */
class AppListDownloader extends AsyncTask<String,Void,List<WebApp> > {

    private final AppListDownloadHandler _handler;

    public AppListDownloader(AppListDownloadHandler handler) {
        this._handler = handler;
    }


    @Override
    protected List<WebApp> doInBackground(String... urls) {
        InputStream input = null;
        App.AppList list;
        try {
            URL myUrl = new URL(urls[0]);
            URLConnection urlConn = myUrl.openConnection();
            urlConn.setConnectTimeout(6000);
            input=urlConn.getInputStream();

            //input = new URL(urls[0]).openStream();
            list = App.AppList.parseFrom(input);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        WebAppList webAppList = new WebAppList(list);
        return webAppList.getAppList();
    }



    protected void onPostExecute(List<WebApp> webAppList) {
        _handler.onAppListDownloaded(webAppList);
    }


}



