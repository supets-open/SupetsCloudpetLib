package com.supets.pet.theme.statsbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AnyRes;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.supets.pet.theme.R;

import java.lang.reflect.Method;

public class StatusBarUtil {

    @TargetApi(19)
    public static void translucentConfig(View parentView, @AnyRes int bgColor, @AnyRes int barColor,
                                         TranslucentLinearLayout.TranslucentType mode) {
        if (parentView != null && parentView instanceof ITranslucentBar) {

            if (mode == null) {
                return;
            }

            //对activity有效
            if (mode == TranslucentLinearLayout.TranslucentType.BackColorMode) {
                ((ITranslucentBar) parentView).setFitsSystemFlag(true);//不需要添加View，留出系统空间给状态栏
                ((ITranslucentBar) parentView).setBackgroundPicture(bgColor);


                if (Build.VERSION.SDK_INT >= 21) {
                    ((Activity) parentView.getContext()).getWindow()
                            .setStatusBarColor(ResourcesCompat.getColor(parentView.getContext().getResources()
                                    , bgColor, null));
                } else {
                    if (Build.VERSION.SDK_INT >= 19) {
                        ((Activity) parentView.getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }
                }
            }

            //对activity，fragment有效
            else if (mode == TranslucentLinearLayout.TranslucentType.StatusBarColorMode) {
                ((ITranslucentBar) parentView).setFitsSystemFlag(false);
//                ((ITranslucentBar) parentView).setStausBarView(0xff, barColor);
                ((ITranslucentBar) parentView).setStausBarView(barColor);

                if (Build.VERSION.SDK_INT >= 19) {
                    ((Activity) parentView.getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
            }
            //对activity，fragment有效
            else if (mode == TranslucentLinearLayout.TranslucentType.BackImageStatusBarColorMode) {
                ((ITranslucentBar) parentView).setFitsSystemFlag(false);
                ((ITranslucentBar) parentView).setBackgroundPicture(bgColor);
//                ((ITranslucentBar) parentView).setStausBarView(0x00, barColor);
                ((ITranslucentBar) parentView).setStausBarView(barColor);

                if (Build.VERSION.SDK_INT >= 19) {
                    ((Activity) parentView.getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }

            }
            else  if (mode== TranslucentLinearLayout.TranslucentType.None){  //全屏，无Bar
                ((ITranslucentBar) parentView).setFitsSystemFlag(false);
                if (Build.VERSION.SDK_INT >= 19) {
                    ((Activity) parentView.getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
            }
            //对activity有效
            else if (mode == TranslucentLinearLayout.TranslucentType.BackImageMode) {
                ((ITranslucentBar) parentView).setFitsSystemFlag(true);//不需要添加View，留出系统空间给状态栏
                ((ITranslucentBar) parentView).setBackgroundPicture(bgColor);

                if (Build.VERSION.SDK_INT >= 19) {
                    ((Activity) parentView.getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }

            }

            transportStatus((Activity) parentView.getContext());

        }
    }

    public static void transportStatus(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (ViewConfiguration.get(context).hasPermanentMenuKey()) {//是否存在底部导航
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

}
