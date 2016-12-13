package com.nagash.appwebbrowser.model.webapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import protobuf.app.App;

/**
 * Created by nagash on 22/09/16.
 */
public class WebAppList {
    private final App.AppList protoAppList;
    private List<WebApp> appList = new ArrayList<>();

    public WebAppList(App.AppList protoWebAppList) {
        this.protoAppList = protoWebAppList;
        for(App.WebApp protoApp : protoWebAppList.getAppList())
            this.appList.add(new WebApp(protoApp));
    }

    public List<WebApp> getAppList() {
        return Collections.unmodifiableList(appList);
    }



}
