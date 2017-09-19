package com.supets.pet.theme.statsbar;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;

public interface ITranslucentBar {

    /***
     * 实现图片，颜色填充到状态栏，需要设置fitSysterm=true
     *
     * @param resId
     */
    void setBackgroundPicture(int resId);

    void setBackgroundPicture(Drawable resId);

    /**
     * 实现颜色填充到状态栏，需要设置fitSysterm=true
     *
     * @param alpha
     * @param resId
     */
    void setTranslucentColor(int alpha, @ColorRes int resId);

    /**
     * 实现背景图片填充，状态栏颜色跟随标题栏颜色变化
     * fitSysterm=false
     *
     * @param alpha
     * @param colors
     */
    void setStausBarView(int alpha, @ColorRes int colors);
    void setStausBarView(int colors);

    /**
     * 是否占满全屏
     *
     * @param fitsSystemFlag
     */
    void setFitsSystemFlag(boolean fitsSystemFlag);


    /**
     * 支持沉侵式
     *
     * @return
     */
    boolean isSupportTranslucent();
}
