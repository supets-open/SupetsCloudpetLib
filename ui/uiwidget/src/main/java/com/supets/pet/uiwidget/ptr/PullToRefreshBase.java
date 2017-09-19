package com.supets.pet.uiwidget.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import java.lang.reflect.Field;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class PullToRefreshBase<T extends View> extends PtrFrameLayout {
    private PullToRefreshHeader mPtrClassicHeader;
    private float sX = -1, sY = -1;
    private boolean isDraging;
    private OnRefreshListener<T> mListener;
    private Field horizontalMoveController;

    public PullToRefreshBase(Context context) {
        super(context);
        initViews();
    }

    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PullToRefreshBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        setPreventForHorizontalVisible(this.getClass());
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
                            mListener.onRefresh(PullToRefreshBase.this);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                sX = e.getRawX();
                sY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //只处理一次拖动以及初始的时候isEnable = true的时候
                if (!isDraging) {
                    checkCanDrag(e);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isDraging = false;
                setHorizontalMoveDisable(false);
        }
        return super.dispatchTouchEvent(e);
    }

    private void checkCanDrag(MotionEvent ev) {
        float distanceX = ev.getRawX() - sX;
        float distanceY = ev.getRawY() - sY;
        if (distanceY < 100) {
            setHorizontalMoveDisable(true);
            return;
        }
        isDraging = true;
        float angle = Math.abs(distanceY / distanceX);
        angle = (float) Math.toDegrees(Math.atan(angle));
        if (angle < 30) {
            setHorizontalMoveDisable(true);
        } else {
            setHorizontalMoveDisable(false);
        }
    }

    private void setPreventForHorizontalVisible(Class clazz) {
        try {
            horizontalMoveController = clazz.getDeclaredField("mPreventForHorizontal");
            horizontalMoveController.setAccessible(true);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                setPreventForHorizontalVisible(clazz.getSuperclass());
            }
        }
    }

    private void setHorizontalMoveDisable(boolean flags) {
        try {
            horizontalMoveController.set(this, flags);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setPtrEnabled(boolean enabledPullToRefresh) {
        this.setEnabled(enabledPullToRefresh);
    }

    public  interface OnRefreshListener<T extends View> {
         void onRefresh(final PullToRefreshBase<T> refreshView);
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
