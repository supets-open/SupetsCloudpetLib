package com.supets.pet.uiwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.supets.pet.supetsbaseui.R;

/**
 * Created by b.s.lee on 2016/3/18.
 * <p/>
 * module   name:
 * module action:
 */
public class SplitBar extends LinearLayout {

    private View mTopLine;
    private View mMiddleLine;
    private View mBottomLine;

    public SplitBar(Context context) {
        super(context);
        initView();
    }

    public SplitBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SplitBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SplitBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.common_widget_split_bar, this);
        setOrientation(VERTICAL);
        mTopLine = findViewById(R.id.topLine);
        mMiddleLine = findViewById(R.id.middleLine);
        mBottomLine = findViewById(R.id.bottomLine);
        setShowMode(SplitBarMode.all);
    }

    public enum SplitBarMode {
        line, middle, all, mid_bot, top_mid
    }

    public void setShowMode(SplitBarMode mode) {
        boolean isTop = (mode == SplitBarMode.line || SplitBarMode.all == mode || SplitBarMode.top_mid == mode);
        mTopLine.setVisibility(isTop ? View.VISIBLE : View.GONE);
        boolean isBottom = (mode == SplitBarMode.all || SplitBarMode.mid_bot == mode);
        mBottomLine.setVisibility(isBottom ? View.VISIBLE : View.GONE);
        boolean isMiddle = (mode == SplitBarMode.mid_bot || SplitBarMode.all == mode
                || SplitBarMode.top_mid == mode || SplitBarMode.middle == mode);
        mMiddleLine.setVisibility(isMiddle ? View.VISIBLE : View.GONE);
    }

}
