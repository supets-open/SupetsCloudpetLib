package com.supets.pet.uiwidget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.supets.pet.supetsbaseui.R;


@SuppressWarnings("ALL")
public class MYGroupWidgetTab extends RelativeLayout {

    private LinearLayout tabContainer;
    private TabItemView[] views;
    private String[] titles;

    private UnderlinePageIndicator mTabs;
    private int mTabNums = 0;
    private int fontseleter = R.drawable.color_float_tab_text;

    public MYGroupWidgetTab(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MYGroupWidgetTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MYGroupWidgetTab(Context context) {
        super(context);
        init();
    }

    View tabRootView;

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.tab_item_container,
                this);
        mTabs = (UnderlinePageIndicator) findViewById(R.id.indicator);
        tabContainer = (LinearLayout) findViewById(R.id.tab_item_container);
        tabRootView = findViewById(R.id.tabRootView);
    }

    private ViewPager mViewPager;

    public void initData(String[] title, ViewPager pViewPager) {
        this.mViewPager = pViewPager;
        this.titles = title;
        this.mTabNums = title.length;
        this.views = new TabItemView[mTabNums];
        setTabContainer();
        setPagerAndTabs();
        views[0].getmTabText().setSelected(true);
    }

    public void manuChangePager(int index) {
        mTabs.setCurrentItem(index);
        views[index].getmTabText().setSelected(true);
    }

    private void setTabContainer() {
        for (int i = 0; i < mTabNums; i++) {
            views[i] = new TabItemView(getContext());
            views[i].resetWidth(mTabNums);
            views[i].setTabText(titles[i]);

            views[i].getmTabText().setTextColor(
                    getResources().getColorStateList(fontseleter));
            views[i].getmTabItem().setTag(i);
            views[i].setTabItemClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Log.v("tag", arg0 + "");
                    mTabs.setCurrentItem((Integer) arg0.getTag());// 不同会调用切换事件,同不会

                    if (mOnTabClickListener != null) {
                        mOnTabClickListener.onClickTab((Integer) arg0.getTag(), views[(Integer) arg0.getTag()].getmTabText().isSelected());
                    }

                    views[(Integer) arg0.getTag()].getmTabText().setSelected(true);
                }

            });
            tabContainer.addView(views[i], i);
        }
    }

    private void setPagerAndTabs() {

        mTabs.setVisibility(View.VISIBLE);
        mTabs.setFades(false);// 指示器没有谈出动画
        // 不同TAB切换时候
        mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < mTabNums; i++) {
                    views[i].getmTabText().setSelected(arg0 == i);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mTabs.setViewPager(mViewPager);
    }

    public void setMaxTab(int tabsNum) {
        mTabs.resetWidth(tabsNum);
    }

    public TabItemView getCurrentTabItemView(int current) {
        return views[current];
    }

    private OnTabClickListener mOnTabClickListener;

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.mOnTabClickListener = onTabClickListener;
    }

    public interface OnTabClickListener {
        void onClickTab(int position, boolean isSelected);
    }
}