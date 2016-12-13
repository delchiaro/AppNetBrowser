package com.nagash.appwebbrowser.controller;

/**
 * Created by nagash on 09/10/16.
 */

public class MainFragmentBuilder {
    private MainFragment build = null;
    MainFragmentBuilder() { build = new MainFragment(); }


    public MainFragmentBuilder setTitle(String title) { build.title = title; build.titleID = null; return this; }
    public MainFragmentBuilder setTitle(int stringID) { build.titleID = stringID;  build.title = null; return this; }

    public MainFragmentBuilder setColor(int colorID)  { build.colorID = colorID; return this; }
    public MainFragmentBuilder setColorDark(int colorID)  { build.colorDarkID = colorID; return this; }
    public MainFragmentBuilder setBackButton(MainFragment.BackButton backButton) { build.backButton = backButton; return this; }

    public MainFragmentBuilder setHasActionmenu() { build.setHasOptionsMenu(true); return this; }
    public MainFragment build(){
        return this.build;
    }
}