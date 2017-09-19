package com.supets.commons.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class InputMethodUtils {

	// 打开关闭切换
	public static void toggleSoftInputMethod(Context context) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {

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

}
