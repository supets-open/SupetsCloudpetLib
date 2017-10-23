package com.supets.pet.uiwidget.ptr;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class DisallowParentTouchViewPager extends ViewPager {

    private ViewGroup parent;

    public DisallowParentTouchViewPager(Context context) {
        super(context);
        parent = (ViewGroup) getParent();
    }

    public DisallowParentTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        parent = (ViewGroup) getParent();
    }

    public void setNestParent(ViewGroup parent) {
        this.parent = parent;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }
} 