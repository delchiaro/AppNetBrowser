package com.nagash.appwebbrowser.zz_deprecated.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by nagash on 14/09/16.
 */
public class DeactivatableViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    public DeactivatableViewPager(Context context) {
        super(context);
    }

    public DeactivatableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.isPagingEnabled)
            return super.onTouchEvent(event);
        else return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(this.isPagingEnabled)
            return super.onInterceptTouchEvent(event);
        else return false;
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
