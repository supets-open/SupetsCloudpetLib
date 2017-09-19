package com.supets.pet.uiwidget;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.supets.commons.utils.UIUtils;
import com.supets.pet.supetsbaseui.R;


/**
 * Created by b.s.lee on 2015/12/31.
 * <p/>
 * module   name:
 * module action:
 */
public  class MiYaClickSpan extends ClickableSpan {
    public boolean isPressed;
    private int mColorNormal = 0xfffa4b9b; // 文字正常显示颜色
    private int mColorPressed = 0x7ffa4b9b; // 文字点击后显示颜色

    public MiYaClickSpan() {
        setClickResColor(R.color.app_color);
    }

    public MiYaClickSpan setClickResColor(int resId) {
        int color = UIUtils.getColor(resId);
        mColorNormal = Color.argb(0xff, Color.red(color), Color.green(color), Color.blue(color));
        mColorPressed = Color.argb(0x7f, Color.red(color), Color.green(color), Color.blue(color));
        return this;
    }

    public MiYaClickSpan setClickColor(int color) {
        mColorNormal = Color.argb(0xff, Color.red(color), Color.green(color), Color.blue(color));
        mColorPressed = Color.argb(0x7f, Color.red(color), Color.green(color), Color.blue(color));
        return this;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
        ds.setColor(isPressed ? mColorPressed : mColorNormal);
    }

    @Override
    public void onClick(View widget) {
        if (mListener != null) {
            mListener.onClickSpan(widget);
        }
    }

    public OnClickSpanListener mListener;

    public MiYaClickSpan setClickSpanListener(OnClickSpanListener listener) {
        this.mListener = listener;
        return this;
    }

    public interface OnClickSpanListener {
        void onClickSpan(View view);
    }
}
