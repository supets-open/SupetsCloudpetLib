package com.supets.pet.uiwidget;

import android.view.View;
import android.widget.AbsListView;

public class ListViewOnScrollHelper {
    
    private int mLastFirstVisibleItem;
    private int mLastTop;
    //private int scrollPosition;
    private int lastHeight;
    
    private OnScrollUpDownListener mListener;

    public ListViewOnScrollHelper(OnScrollUpDownListener mListener) {
        this.mListener = mListener;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        View firstChild = view.getChildAt(0);
        if (firstChild == null) {
            return;
        }
        int top = firstChild.getTop();
        int height = firstChild.getHeight();
        int delta;
        int skipped = 0;
        if (mLastFirstVisibleItem == firstVisibleItem) {
            delta = mLastTop - top;
        } else if (firstVisibleItem > mLastFirstVisibleItem) {
            skipped = firstVisibleItem - mLastFirstVisibleItem - 1;
            delta = skipped * height + lastHeight + mLastTop - top;
        } else {
            skipped = mLastFirstVisibleItem - firstVisibleItem - 1;
            delta = skipped * -height + mLastTop - (height + top);
        }
        //scrollPosition += -delta;
        //boolean exact = skipped > 0;
        if (mListener != null && Math.abs(delta) > 5) {
            if(delta > 0) {
                mListener.onScrollUp(view, firstVisibleItem, visibleItemCount, totalItemCount);
            } else {
                mListener.onScrollDown(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
            //mListener.onScrollUpDownChanged(-delta, scrollPosition, exact);
        }
        mLastFirstVisibleItem = firstVisibleItem;
        mLastTop = top;
        lastHeight = firstChild.getHeight();
    }
    
    public static interface OnScrollUpDownListener {
        void onScrollUp(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
        
        void onScrollDown(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }
}
