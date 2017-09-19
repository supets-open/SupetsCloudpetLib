package com.supets.pet.uiwidget.recyclelib;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.supets.coredata.MYData;

public class SupetRecyclerViewHolder2 extends RecyclerView.ViewHolder {

    public SupetRecyclerView2.SupetRecyclerViewAdapter mDRecyclerViewAdapter;
    public SupetRecyclerAdapter2 mDBaseRecyclerViewAdapter;
    private View mWholeView;


    public SupetRecyclerViewHolder2(View itemView, SupetRecyclerAdapter2 mDBaseRecyclerViewAdapter) {
        super(itemView);
        mWholeView=itemView;
        this.mDRecyclerViewAdapter = mDBaseRecyclerViewAdapter.getmDRecyclerViewAdapter();
        this.mDBaseRecyclerViewAdapter = mDBaseRecyclerViewAdapter;
    }

    public View getWholeView() {
        return mWholeView;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T view(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    public <T extends MYData> void setData(T data, int position) {

    }

    /**
     * 获取点击的item的position
     * @return
     */
    public int getAdapterItemPosition() {
        int oldPosition =getAdapterPosition();

        if(mDRecyclerViewAdapter==null){
            return oldPosition;
        }

        if (mDRecyclerViewAdapter.isHeader(oldPosition) || mDRecyclerViewAdapter.isFooter(oldPosition)) {
            return -1;
        } else {
            return oldPosition - mDRecyclerViewAdapter.getHeaderViewsCount();
        }
    }
}
