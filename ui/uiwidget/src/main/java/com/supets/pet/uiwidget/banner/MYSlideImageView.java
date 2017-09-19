package com.supets.pet.uiwidget.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.supets.pet.supetsbaseui.R;

import java.util.List;

public class MYSlideImageView extends FrameLayout implements Callback {

    private static final long DEFAULT_INTERVAL = 5000;

    private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private int rowCount = 1;

    private Strategy mStrategy;
    private boolean mLoopSlide;
    private boolean mAutoPlay;
    private long mPlayInterval = DEFAULT_INTERVAL;
    private Handler mHandler;

    private List<? extends Object> mData;
    private SlideImageAdapter mAdapter;

    public MYSlideImageView(Context context) {
        this(context, null);
    }

    public MYSlideImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MYSlideImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mViewPager = new ViewPager(context);
        mIndicator = new CirclePageIndicator(context);

        mIndicator.setDotSpacing(com.supets.commons.utils.UIUtils.dp2px(6));
        mIndicator.setSnap(true);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .WRAP_CONTENT);
        addView(mViewPager, params);

        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.px20);
        addView(mIndicator, params);

        mAdapter = new SlideImageAdapter();
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);


    }

    public void setRowCount(int count) {
        if (count > 0)
            rowCount = count;
    }

    public void setAutoPlay(boolean autoPlay) {
        mAutoPlay = autoPlay;
    }

    public void setLoopSlide(boolean loopSlide) {
        mLoopSlide = loopSlide;
    }

    public void setPlayInterval(long interval) {
        mPlayInterval = interval;
    }

    public void setStrategy(Strategy strategy) {
        mStrategy = strategy;
    }

    public void setData(List<? extends Object> data) {
        setData(data, 0);
    }

    public void setData(List<? extends Object> data, int current) {
        mData = data;

        mAdapter.clearCache();
        mViewPager.setAdapter(mAdapter);
        int item = (mAutoPlay || mLoopSlide) ? current + mAdapter.getRealCount() * 1000 : current;
        mIndicator.setCurrentItem(item);

        onResume();
    }

    public int getDataCount() {
        return mData == null ? 0 : mData.size();
    }

    public void onResume() {
        if (mHandler == null) {
            mHandler = new Handler(this);
        }
        onPause();
        if (!mAutoPlay || mAdapter.getCount() <= 1) {
            return;
        }
        mHandler.sendEmptyMessageDelayed(0, mPlayInterval);
        isremove = false;
    }

    private boolean isremove = false;

    public void onPause() {
        isremove = true;
        if (mHandler != null) {
            mHandler.removeMessages(0);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        int next = mViewPager.getCurrentItem() + 1;
        next %= mAdapter.getCount();
        mIndicator.setCurrentItem(next);
        if (!isremove) {
            mHandler.sendEmptyMessageDelayed(0, mPlayInterval);
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                onPause();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onResume();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = 0;
        int heightSize = 0;

        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);

        heightSize = heightSize <= 0 ? widthSize : heightSize;

        int count = getChildCount();
        int maxHeight = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            int childHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
            child.measure(childWidthSpec, childHeightSpec);
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
        }

        heightSize = heightSpecMode == MeasureSpec.EXACTLY ? heightSize : maxHeight;

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onPause();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onResume();
    }

    public interface Strategy {
        View instantiateItem(Context context, Object object, int position);
    }

    public static abstract class InfinityPagerAdapter extends PagerAdapter {
        public abstract int getRealCount();
    }

    public class SlideImageAdapter extends InfinityPagerAdapter {

        private SparseArray<View> mCache = new SparseArray<>();

        public void clearCache() {
            mCache.clear();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mStrategy == null) {
                return null;
            }

            int newPosition = getRealPosition(position);
            View view = mCache.get(newPosition);
            if (view == null || view.getParent() != null) {
                Object result = null;
                if (rowCount > 1) {
                    int end = newPosition * rowCount + rowCount;
                    result = mData.subList(newPosition * rowCount, end >= mData.size() ? mData
                            .size() : end);
                } else
                    result = mData.get(newPosition);
                view = mStrategy.instantiateItem(getContext(), result, newPosition);
                mCache.put(newPosition, view);
            }

            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            container.addView(view, params);

            return view;
        }

        @Override
        public int getCount() {
            int count = getRealCount();
            return count <= 1 ? count : (mAutoPlay || mLoopSlide ? Integer.MAX_VALUE : count);
        }

        private int getRealPosition(int position) {
            return position % getRealCount();
        }

        @Override
        public int getRealCount() {
            int realCount = 0;
            if (mData != null && mData.size() > 0) {
                realCount = mData.size() % rowCount == 0 ? mData.size() / rowCount : mData.size()
                        / rowCount + 1;
            }
            return realCount;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
