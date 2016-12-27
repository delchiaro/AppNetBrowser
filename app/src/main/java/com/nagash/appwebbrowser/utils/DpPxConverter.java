package com.nagash.appwebbrowser.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by nagash on 24/10/16.
 */

public class DpPxConverter {

    static Context context;
    DpPxConverter(Context context){
        this.context = context;
    }

    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = context.getResources().getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = context.getResources().getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    private int DpToPx(int dp){
        return Math.round(dp*(context.getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));

    }

    private int PxToDp(int px){
        return Math.round(px/(context.getResources().getSystem().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }

    private float dpFromPx(float px)
    {
        return px / context.getResources().getDisplayMetrics().density;
    }

    private float pxFromDp(float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}