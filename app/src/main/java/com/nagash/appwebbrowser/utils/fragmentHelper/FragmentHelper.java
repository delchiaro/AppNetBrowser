package com.nagash.appwebbrowser.utils.fragmentHelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.nagash.appwebbrowser.utils.fragmentHelper.Exceptions.RemovedNotAddedFragmentException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nagash on 07/10/16.
 */



public class FragmentHelper<F extends Fragment> {

    //Map<Fragment, Boolean>  loadedFragments = new HashMap<>();

    Set<F> loadedFragments = new HashSet<>();

    F activeFragment = null;
    int containerViewID;
    android.support.v4.app.FragmentManager fragmentManager;

    Integer customEnterAnimID = null;
    Integer customExitAnimID  = null;

    public FragmentHelper(int containerViewID, android.support.v4.app.FragmentManager fragmentManager) {
        this.containerViewID = containerViewID;
        this.fragmentManager = fragmentManager;
    }

    public FragmentHelper setCustomAnim(int customEnterAnimID, int customExitAnimID) {
        this.customEnterAnimID = customEnterAnimID;
        this.customExitAnimID = customExitAnimID;
        return this;
    }

//    public FragmentHelper add(Fragment fragment) {
//        loadedFragments.put(fragment, false);
//        return this;
//    }

    public F getActiveFragment() {
        return activeFragment;
    }


    public void hideFragment(F fragment)
    {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if(loadedFragments.contains(fragment) == false) {
            ft.add(containerViewID, fragment);
            loadedFragments.add(fragment);
        }
        ft.hide(fragment).commit();
    }

    public void activateFragment(F fragment)
    {
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if(customEnterAnimID != null && customEnterAnimID != null  /* &&   activeFragment != null*/) // if no frag is active, disable animation
            ft.setCustomAnimations(customEnterAnimID, customExitAnimID);

        if(loadedFragments.contains(fragment) ) {
            ft.show(fragment);
        }
        else
        {
            ft.add(containerViewID, fragment);
            //ft.show(fragment);
            loadedFragments.add(fragment);
        }

        if(activeFragment != null) {
            ft.hide(activeFragment);
        }

        ft.commit();
        activeFragment = fragment;
    }

    public void removeFragment(F fragment)
    {
        if(loadedFragments.contains(fragment) )
        {
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit();
            loadedFragments.remove(fragment);
        }
        else throw new RemovedNotAddedFragmentException();
        if(activeFragment == fragment)
            activeFragment = null;
    }

}



