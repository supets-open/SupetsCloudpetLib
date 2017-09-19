package com.supets.pet.uiwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class KeyboardListenRelativeLayout extends RelativeLayout {

	public static final byte KEYBOARD_STATE_SHOW = -3;
	public static final byte KEYBOARD_STATE_HIDE = -2;
	public static final byte KEYBOARD_STATE_INIT = -1;

	private boolean mHasInit = false;
	private boolean mHasKeyboard = false;
	private int mHeight;

	private IOnKeyboardStateChangedListener onKeyboardStateChangedListener;

	public KeyboardListenRelativeLayout(Context context) {
		super(context);
	}

	public KeyboardListenRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public KeyboardListenRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setOnKeyboardStateChangedListener(IOnKeyboardStateChangedListener onKeyboardStateChangedListener) {
		this.onKeyboardStateChangedListener = onKeyboardStateChangedListener;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!mHasInit) {
			mHasInit = true;
			mHeight = b;
			if (onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_INIT);
				Log.v("key", "KEYBOARD_STATE_INIT");
			}
		} else {
			mHeight = mHeight < b ? b : mHeight;
		}

		if (mHasInit && mHeight > b && !mHasKeyboard) {
			mHasKeyboard = true;
			if (onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_SHOW);
				Log.v("key", "KEYBOARD_STATE_SHOW");
			}
		}
		if (mHasInit && mHasKeyboard && mHeight == b) {
			mHasKeyboard = false;
			if (onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_HIDE);
				Log.v("key", "KEYBOARD_STATE_HIDE");
			}
		}
	}

	public interface IOnKeyboardStateChangedListener {
		public void onKeyboardStateChanged(int state);
	}
}