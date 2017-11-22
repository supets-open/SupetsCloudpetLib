package com.supets.commons.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.supets.commons.utils.inputmethod.ReferenceCleaner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InputMethodUtils {

    // 打开关闭切换
    public static void toggleSoftInputMethod(Context context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSoftInputMode(Activity activity, int flag) {
        activity.getWindow().setSoftInputMode(flag);
    }

    public static void showSoftInputMethod(Context context, View view) {
        InputMethodManager iMM = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        iMM.showSoftInput(view, 0);
    }

    public static void hideSoftInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isSoftKeyboardActive(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager.isActive(view);
    }

    public static boolean isSoftKeyboardActive(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager.isActive();
    }

    public static void InitKeyBorad(boolean isflag, Activity context) {
        if (isflag) {
            context.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                            | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } else {
            context.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                            | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    /**
     * 解决输入法导致的内存泄漏
     * 参考：http://blog.csdn.net/sodino/article/details/32188809
     * <p>
     * 在Activity.onDestory()方法中执行以上方法即可解决.
     * <p>
     * public  void onDestroy() {
     * super.ondestroy();
     * fixInputMethodManagerLeak(this);
     * }
     *
     * @param destContext 要销毁的Activity
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 解决输入法导致的内存泄漏
     * 参考：http://www.jianshu.com/p/aa2555628b17
     * <p>
     * 在退出使用InputMethodManager的Activity时，
     * 调用fixFocusedViewLeak方法即可解决。
     *
     * @param application
     */
    public static void fixFocusedViewLeak(Application application) {

        // Don't know about other versions yet.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 || Build.VERSION.SDK_INT > 23) {
            return;
        }

        final InputMethodManager inputMethodManager =
                (InputMethodManager) application.getSystemService(Context.INPUT_METHOD_SERVICE);

        final Field mServedViewField;
        final Field mHField;
        final Method finishInputLockedMethod;
        final Method focusInMethod;
        try {
            mServedViewField = InputMethodManager.class.getDeclaredField("mServedView");
            mServedViewField.setAccessible(true);
            mHField = InputMethodManager.class.getDeclaredField("mServedView");
            mHField.setAccessible(true);
            finishInputLockedMethod = InputMethodManager.class.getDeclaredMethod("finishInputLocked");
            finishInputLockedMethod.setAccessible(true);
            focusInMethod = InputMethodManager.class.getDeclaredMethod("focusIn", View.class);
            focusInMethod.setAccessible(true);
        } catch (Exception unexpected) {
            Log.e("IMMLeaks", "Unexpected reflection exception", unexpected);
            return;
        }

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityDestroyed(Activity activity) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @RequiresApi(api = 12)
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ReferenceCleaner cleaner = new ReferenceCleaner(inputMethodManager, mHField, mServedViewField,
                        finishInputLockedMethod);
                View rootView = activity.getWindow().getDecorView().getRootView();
                ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
                viewTreeObserver.addOnGlobalFocusChangeListener(cleaner);
            }
        });
    }
}
