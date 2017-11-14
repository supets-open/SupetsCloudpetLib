package com.supets.pet.theme.statsbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class StatusBarView extends TextView {

    public StatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        controlVisible();
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        controlVisible();
    }

    public StatusBarView(Context context) {
        super(context);
        controlVisible();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getStatusBarHeight();
        Log.v("getStatusBarHeight", String.valueOf(height));
        setMeasuredDimension(width, height);
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

    private void controlVisible() {
        if (isSupportTranslucent()) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }

}
