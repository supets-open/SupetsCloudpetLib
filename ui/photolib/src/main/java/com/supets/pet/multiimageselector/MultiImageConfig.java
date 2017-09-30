package com.supets.pet.multiimageselector;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public  class MultiImageConfig {

    public static final String EXTRA_SELECT_MODE = "select_count_mode";

    public static final int MODE_SINGLE = 0;//单选模式
    public static final int MODE_MULTI = 1;//多选模式
    public static final int MODE_PREVIEW = 2;//预览模式

    //模式定义
    @IntDef({MODE_SINGLE, MODE_MULTI, MODE_PREVIEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface  PhotoMode {}


    public static final int MaxNum=4;

    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";//预览结果
    public static final String EXTRA_BIGPIC_CURR_POSITION = "currposition";
    public static final String EXTRA_RESULT = "select_result";
    public static final String EXTRA_CAMERA_RESULT = "camera_result";

}
