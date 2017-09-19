package com.supets.pet.uiwidget.SnapScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class SnapTopScrollView extends ScrollView  implements SnapPageLayout.SnapPageContent {
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    public SnapTopScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onScrollListener != null) {
            onScrollListener.onScroll(x, y, oldx, oldy);
        }
    }

    private OnJDScrollListener onScrollListener;

    public OnJDScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public void setOnJDScrollListener(OnJDScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface OnJDScrollListener {
        void onScroll(int x, int y, int oldx, int oldy);
    }

	@Override
	public boolean isAtTop() {
		return true;
	}

	@Override
	public boolean isAtBottom() {
		int scrollY = getScrollY();
        int height = getHeight();
        int scrollViewMeasuredHeight = getChildAt(0).getMeasuredHeight();

        if ((scrollY + height) >= scrollViewMeasuredHeight) {
            return true;
        }
        return false;
	}
}
