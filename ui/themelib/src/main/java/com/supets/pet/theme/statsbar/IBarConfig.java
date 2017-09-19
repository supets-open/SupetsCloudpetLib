package com.supets.pet.theme.statsbar;

import android.support.annotation.AnyRes;

public interface IBarConfig {

    boolean isSupport=true;

    @AnyRes
    int getBarColor();

    @AnyRes
    int getBackColor();

    TranslucentLinearLayout.TranslucentType getTranslucentType();

}
