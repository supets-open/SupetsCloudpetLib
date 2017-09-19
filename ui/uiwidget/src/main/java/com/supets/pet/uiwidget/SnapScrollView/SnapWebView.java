package com.supets.pet.uiwidget.SnapScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class SnapWebView extends WebView implements SnapPageLayout.SnapPageContent {
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    public SnapWebView(Context context) {
        super(context);
    }

    public SnapWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SnapWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean isAtTop() {
        return mTopStatus;
    }

    @Override
    public boolean isAtBottom() {
        return mBottomStatus;
    }

    private boolean mTopStatus = true;
    private boolean mBottomStatus = false;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float webcontent = getContentHeight() * getScale();
        float webnow = getHeight() + getScrollY();
        if (webcontent - webnow == 0) {
            mBottomStatus = true;
        } else {
            mBottomStatus = false;
        }

        if (t <= 0) {
            mTopStatus = true;
        } else {
            mTopStatus = false;
        }
    }

}