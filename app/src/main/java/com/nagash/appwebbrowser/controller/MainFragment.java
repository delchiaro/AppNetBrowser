package com.nagash.appwebbrowser.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by nagash on 15/09/16.
 */


public class MainFragment extends Fragment {


    public static MainFragmentBuilder builder() { return new MainFragmentBuilder(); }

    public enum BackButton { VISIBLE, HIDDEN }


    boolean actionbarUpToDate = true;

    String  title = null;
    Integer titleID = null;

    Integer colorID = null;
    Integer colorDarkID = null;

    BackButton backButton = BackButton.HIDDEN;


    public MainActivity getMainActivity() {
        return (MainActivity)getActivity();
    }


    protected MainFragment(){}
    public MainFragment(MainFragment styleCopy){
        this.title = styleCopy.title;
        this.titleID = styleCopy.titleID;
        this.colorID = styleCopy.colorID;
        this.colorDarkID = styleCopy.colorDarkID;
        this.backButton = styleCopy.backButton;
        this.setHasOptionsMenu(styleCopy.hasOptionsMenu());
    }
    public MainFragment(MainFragmentBuilder builder){
        this(builder.build());
    }


    private final void actionbarToUpdate() {
        actionbarUpToDate=false;
    }

    public MainFragment setBackButton( BackButton backButton ) {
        this.backButton = backButton; actionbarToUpdate(); return this;
    }
    public MainFragment setColorID(int colorID) {
        this.colorID = colorID; actionbarToUpdate(); return this;
    }
    public MainFragment setColorDarkID(int colorDarkID) {
        this.colorDarkID = colorDarkID; actionbarToUpdate(); return this;
    }
    public MainFragment setColorsID(int colorID, int colorDarkID) {
        setColorID(colorID);
        return setColorDarkID(colorDarkID);
    }

    public MainFragment setTitle(String title) {
        this.title = title;
        this.titleID = null;
        actionbarToUpdate();
        return this;
    }
    public MainFragment setTitle(int titleID) {
        if(isInContext()) {
            this.title = getString(titleID);
            this.titleID = null;
        }
        else{
            this.titleID = titleID;
            this.title = null;
        }
        actionbarToUpdate();
        return this;
    }

    public Integer getColorID() { return colorID; }
    public Integer getColorDarkID() { return colorDarkID; }





    public boolean isInContext() {
        return this.getActivity() != null;
    }
    public void updateColors() {
        if(!isInContext()) return;
        if(colorID != null)
        {
            if (colorDarkID != null) setActionbarColor(colorID, colorDarkID);
            else setActionbarColor(colorID, colorID);
        }
    }
    public void updateTitle() {
        if(!isInContext()) return;
        if(titleID != null)
        {
            title = getResources().getString(titleID);
            titleID=null;
        }
        if(title != null)
            setActionbarTitle(title);
    }
    public void updateBackButton() {
        if(!isInContext()) return;
        if(backButton != null)
            setActionbarBackButton(this.backButton);
    }
    public void updateActionBar() {
        if(actionbarUpToDate == true) return;
        if(!isInContext()) return;
        updateColors();
        updateTitle();
        updateBackButton();
        actionbarUpToDate = true;
    }



    private void setActionbarColor(int primaryID, int primaryDarkID) {
        getMainActivity().setToolbarColor(primaryID, primaryDarkID);
    }
    private void setActionbarTitle(String title) {
//        getMainActivity().getSupportActionBar().setTitle(title);
        getMainActivity().getToolbar().setTitle(title);
    }
    private void setActionbarBackButton(BackButton backButton) {
        if(backButton == BackButton.VISIBLE) getMainActivity().showBackButton();
        else if(backButton == BackButton.HIDDEN) getMainActivity().hideBackButton();
    }



    // * * * * EVENTS * * * *
    public void onFragmentShown() {
        actionbarUpToDate = false;
        updateActionBar();
    }
    public void onFragmentHidden() {}
    public void onFragmentAdded() {}
    public void onFragmentRemoved() {}

    public boolean onBackPressed() {
        return false;
    }



    @Override public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden==false) onFragmentShown();
        else onFragmentHidden();
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // updateActionBar();
        onFragmentAdded();
        onFragmentShown();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        onFragmentRemoved();
    }


    //    public abstract boolean onCreateOptionMenu(Menu menu);
//    public abstract boolean  onOptionsItemSelected(MenuItem item);




    /* * * * * * * * * * * * * * *  UTILS  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public float dpFromPx(float px) {
        return px / getResources().getDisplayMetrics().density;
    }

    public float pxFromDp(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}