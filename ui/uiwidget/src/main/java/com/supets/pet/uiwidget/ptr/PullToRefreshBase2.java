package com.supets.pet.uiwidget.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class PullToRefreshBase2<T extends View> extends FixRequestDisallowTouchEventPtrFrameLayout {
    private PullToRefreshHeader mPtrClassicHeader;
    private OnRefreshListener<T> mListener;

    public PullToRefreshBase2(Context context) {
        super(context);
        initViews();
    }

    public PullToRefreshBase2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PullToRefreshBase2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrClassicHeader = new PullToRefreshHeader(getContext());
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);
        this.setEnabled(false);

        this.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                //判断是否可以下拉刷新。一般默认设置就行
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                        //具体刷新的操作的回调
                        if (mListener != null) {
                            mListener.onRefresh(PullToRefreshBase2.this);
                        }
            }
        });
    }

    public PullToRefreshHeader getHeader() {
        return mPtrClassicHeader;
    }

    public void onRefreshComplete() {
        this.refreshComplete();
    }

    public void setOnRefreshListener(final OnRefreshListener<T> listener) {
        this.mListener = listener;
    }

    public void setRefreshing() {
        View view = this.getContentView();
        if (view instanceof ListView) {
            ((ListView) view).setSelection(0);
        } else if (view instanceof ScrollView) {
            ((ScrollView) view).fullScroll(ScrollView.FOCUS_UP);
        }
        this.setRefreshing(true);
    }

    public void addViewForPtrFrameLayout(View child) {
        super.addView(child);
        if (this.getContentView() == null) {
            this.onFinishInflate();
        }
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeRelateObject(object);
        }
    }

    public void setRefreshing(boolean doScroll) {
        this.autoRefresh(doScroll);
    }


    public void setPtrEnabled(boolean enabledPullToRefresh) {
        this.setEnabled(enabledPullToRefresh);
    }

    public  interface OnRefreshListener<T extends View> {
         void onRefresh(final PullToRefreshBase2<T> refreshView);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getIsComplete()){
            setIsComplete();
            refreshComplete();
        }
    }
}
