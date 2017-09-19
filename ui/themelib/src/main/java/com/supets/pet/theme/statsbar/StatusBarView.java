package com.supets.pet.theme.statsbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatusBarView extends TextView {

    public StatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        adjustHeight();
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        adjustHeight();
    }


    public StatusBarView(Context context) {
        super(context);
        adjustHeight();
    }

    public void setTranslucentColor(int alpha, @ColorRes int resId) {
        int color = getResources().getColor(resId);
        setBackgroundColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }

    public void setTranslucentColor2(@DrawableRes int resId) {
        setBackgroundResource(resId);
    }

    public boolean isSupportTranslucent() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && IBarConfig.isSupport;
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

    public void adjustHeight() {
        if (isSupportTranslucent()) {
            int height = getStatusBarHeight();
            Log.v("getStatusBarHeight", String.valueOf(height));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    height);
            setLayoutParams(params);
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }
}
