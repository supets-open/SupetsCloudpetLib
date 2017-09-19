package com.supets.pet.uiwidget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import com.rockerhieu.emojicon.EmojiconEditText;

public class CommentEditText extends EmojiconEditText implements TextWatcher {
	private int MAX140 = 140;
	private static final int MAXLINES = 4;
	private EditBackListener listenr;
	private int beforeLine = 0;
	private int afterLine = 0;
	private int beforeNum = 0;

	public void setFontNum(int maxsize) {
		MAX140 = maxsize;
	}

	public CommentEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CommentEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CommentEditText(Context context) {
		super(context);
		init();
	}

	private void init() {
		addTextChangedListener(this);
		setMaxLines(MAXLINES);
		setUseSystemDefault(false);
		setEmojiconSize((int) getTextSize());
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {

		if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (listenr != null && listenr.OnBackPressed()) {
				return true;
			}
		}

		return false;
	}

	public EditBackListener getEditBackListener() {
		return listenr;
	}

	public void setEditBackListener(EditBackListener listenr) {
		this.listenr = listenr;
	}

	public interface EditBackListener {
		 boolean OnBackPressed();

		 boolean OnLineChange(boolean isup);

		 boolean OnFontOver();

		 void OnVisibleSend(boolean visible);
	}

	public static  class SimpleEditBackListener implements EditBackListener {

		@Override
		public boolean OnBackPressed() {
			return false;
		}

		@Override
		public boolean OnLineChange(boolean isup) {
			return false;
		}

		@Override
		public boolean OnFontOver() {
			return false;
		}

		@Override
		public void OnVisibleSend(boolean visible) {

		}
	}

	@Override
	public void afterTextChanged(Editable editText) {
		afterLine = this.getLineCount();
		// 限制最大输入行数
		if (beforeLine != afterLine) {
			if (listenr != null) {
				// 行数发生变化时候调用
				listenr.OnLineChange(beforeLine < afterLine);
			}
		}
		String input = editText.toString();
		int length = input.length();
		if (length > MAX140) {
			this.setText(input.substring(0, MAX140));
			this.setSelection(MAX140);
			// 字数超过140
			if (listenr != null) {
				listenr.OnFontOver();
			}
		}

		if (length > 0) {
			if (listenr != null) {
				listenr.OnVisibleSend(true);
			}
		} else {
			if (listenr != null) {
				listenr.OnVisibleSend(false);
			}
		}

		if (length < beforeNum) {
			Log.v("CommentEditText", "delete mode");
			return;
		}
		super.onTextChanged(null, 0, 0, 0);

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		beforeLine = this.getLineCount();
		beforeNum = arg0.length();

	}

	@Override
	public void onTextChanged(CharSequence text, int start, int lengthBefore,
			int lengthAfter) {

	}
}
