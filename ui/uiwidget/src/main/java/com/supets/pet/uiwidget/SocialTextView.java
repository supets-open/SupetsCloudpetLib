package com.supets.pet.uiwidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.supets.commons.widget.MiaTextView;

/**
 * 两种写法
 * 1 使用SpanUtils构建SpanableString，然后setText方法
 *   setText(SpannableString)
 * 2 先设置文本内容，setText(String),然后在设置Span，
 * setSpan(int start, int length, MiYaClickSpan span)
 */

public class SocialTextView extends MiaTextView {
	private ClickableSpan mPressedSpan;

	public SocialTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SocialTextView(Context context) {
		super(context);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		text = text == null ? "" : text;
		super.setText(text, BufferType.SPANNABLE);
	}

	public void setSpan(int start, int length, Object span) {
		String content = String.valueOf(getText());
		int contentLength = content.length();
		if (contentLength <= start || contentLength < (start + length)) {
			return;
		}
		Spannable spans = (Spannable) getText();
		spans.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = false;

		TextView widget = this;
		Object text = widget.getText();
		if (text instanceof Spanned) {
			Spannable buffer;
			try {
				buffer = (Spannable) text;
			} catch (ClassCastException cce) {
				cce.printStackTrace();
				return false;
			}

			int action = event.getAction();

			if (action == MotionEvent.ACTION_DOWN) {
				mPressedSpan=null;
				mPressedSpan = getPressedSpan(widget, buffer, event);
				if (mPressedSpan != null) {
					Selection.setSelection(buffer, buffer.getSpanStart(mPressedSpan), buffer.getSpanEnd(mPressedSpan));
					invalidateClickSpan(true, widget);
					result = true;
				}
			} else if (action == MotionEvent.ACTION_MOVE) {
				ClickableSpan touchedSpan = getPressedSpan(widget, buffer, event);
				if (mPressedSpan != null && touchedSpan != mPressedSpan) {
					invalidateClickSpan(false, widget);
					mPressedSpan = null;
					Selection.removeSelection(buffer);
				}
			} else {
				if (mPressedSpan != null) {
					if (action == MotionEvent.ACTION_UP) {
						mPressedSpan.onClick(widget);
					}
					invalidateClickSpan(false, widget);
				}
				mPressedSpan = null;
				Selection.removeSelection(buffer);
			}
		}

		// Log.i("SocialView", event.getAction()+" "+ "result:" + result );
		if (result) {
			return result;
		}
		return super.onTouchEvent(event);
	}

	private void invalidateClickSpan(boolean pressed, TextView widget) {
		if (mPressedSpan instanceof MiYaClickSpan) {
			((MiYaClickSpan) mPressedSpan).isPressed = pressed;
		}

		widget.invalidate();
	}

	private ClickableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
		try {
			int x = (int) event.getX();
			int y = (int) event.getY();

			x -= textView.getTotalPaddingLeft();
			y -= textView.getTotalPaddingTop();

			x += textView.getScrollX();
			y += textView.getScrollY();

			Layout layout = textView.getLayout();
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line, x);

			// 点击空白区域 不处理
			int textLength = textView.getText().length();
			if (off >= textLength) {
				return null;
			}

			ClickableSpan[] link = spannable.getSpans(off, off, ClickableSpan.class);
			ClickableSpan touchedSpan = null;
			if (link.length > 0) {
				touchedSpan = link[0];
			}
			return touchedSpan;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mPressedSpan=null;
	}

}
