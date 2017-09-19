package com.supets.pet.uiwidget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止左右滚动
 */
public class MYViewPager extends ViewPager {

    private boolean mEnableScrolled = false;

    public MYViewPager(Context context) {
        this(context, null);
    }

    public MYViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void enableLeftRightScroll(boolean isScroll) {
        this.mEnableScrolled = isScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableScrolled) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mEnableScrolled) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

}