package com.supets.pet.theme.statsbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.supets.pet.theme.R;

/**
 * 1 作为页面根布局，不要设置背景色，fit属性true
 * 2 纯色和图片可以在根布局设置颜色和fitSysterm=true
 */
public class TranslucentLinearLayout extends LinearLayout implements ITranslucentBar {

    private StatusBarView mStausBarView;

    public TranslucentLinearLayout(Context context) {
        super(context);
        if (isSupportTranslucent()){
            initTranslucent(context, null);
        }
    }

    public TranslucentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isSupportTranslucent()){
            initTranslucent(context, attrs);
        }
    }

    private void initTranslucent(Context context, AttributeSet attrs) {
        if (attrs != null) {

            int bgColor = android.R.color.transparent;
            int barColor = android.R.color.transparent;
            TranslucentType mode = TranslucentType.None;

            TypedArray gdhAttrs = context.obtainStyledAttributes(attrs,
                    R.styleable.TranslucentLinearLayout);
            try {
                final int indexCount = gdhAttrs.getIndexCount();
                for (int i = 0; i < indexCount; i++) {
                    final int attr = gdhAttrs.getIndex(i);
                    if (attr == R.styleable.TranslucentLinearLayout_background_color) {
                        bgColor = gdhAttrs.getResourceId(attr, android.R.color.transparent);
                    }
                    if (attr == R.styleable.TranslucentLinearLayout_statusbar_color) {
                        barColor = gdhAttrs.getResourceId(attr,
                                android.R.color.transparent);
                    }
                    if (attr == R.styleable.TranslucentLinearLayout_tanslucent_mode) {
                        mode = getTranslucentTypeFromXml(gdhAttrs, attr);
                    }
                }
                StatusBarUtil.translucentConfig(this, bgColor, barColor, mode);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                gdhAttrs.recycle();
            }
        }
    }

    public enum TranslucentType {
        StatusBarColorMode,
        BackImageMode,
        BackImageStatusBarColorMode,
        BackColorMode,
        None
    }

    @Nullable
    private static TranslucentType getTranslucentTypeFromXml(
            TypedArray gdhAttrs,
            int attrId) {
        switch (gdhAttrs.getInt(attrId, -1)) {
            case 1:
                return TranslucentType.StatusBarColorMode;
            case 2:
                return TranslucentType.BackImageMode;
            case 3:
                return TranslucentType.BackImageStatusBarColorMode;
            case 4:
                return TranslucentType.BackColorMode;
            default:
                return TranslucentType.None;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TranslucentLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isSupportTranslucent()){
            initTranslucent(context, attrs);
        }
    }

    public void setTranslucentColor(int alpha, @ColorRes int resId) {
        if (isSupportTranslucent()) {
            int color = getResources().getColor(resId);
            setBackgroundColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
        }
    }


    @Override
    public void setStausBarView(int alpha, @ColorRes int colors) {
        if (isSupportTranslucent()) {
            if (mStausBarView == null) {
                mStausBarView =new StatusBarView(getContext());
                mStausBarView.adjustHeight();
                addView(mStausBarView, 0);
            }
            mStausBarView.setTranslucentColor(alpha, colors);
        }
    }

    @Override
    public void setStausBarView(int colors) {
        if (isSupportTranslucent()) {
            if (mStausBarView == null) {
                mStausBarView =new StatusBarView(getContext());
                mStausBarView.adjustHeight();
                addView(mStausBarView, 0);
            }
            mStausBarView.setTranslucentColor2(colors);
        }
    }

    @Override
    public void setFitsSystemFlag(boolean fitsSystemFlag) {
        if (isSupportTranslucent()) {
            setFitsSystemWindows(fitsSystemFlag);
        }
    }

    @Override
    public boolean isSupportTranslucent() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&IBarConfig.isSupport;
    }

    @Override
    public void setBackgroundPicture(Drawable resId) {
        setBackgroundDrawable(resId);
    }

    @Override
    public void setBackgroundPicture(int resId) {
        setBackgroundResource(resId);
    }


}
