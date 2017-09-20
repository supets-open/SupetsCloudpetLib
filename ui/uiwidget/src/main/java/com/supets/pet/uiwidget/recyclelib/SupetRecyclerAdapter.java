package com.supets.pet.uiwidget.recyclelib;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.supets.coredata.MYData;

import java.util.ArrayList;
import java.util.List;


public abstract class SupetRecyclerAdapter {

    public abstract int getItemViewType(int position);

//    public interface ItemOnClickListener{
//        void onclick(View view, int position);
//        void onItemLongClick(View view, int position);
//    }

//    ItemOnClickListener mItemOnClickListener;
//
//    public ItemOnClickListener getItemOnClickListener() {
//        return mItemOnClickListener;
//    }
//
//    public void setItemOnClickListener(ItemOnClickListener mItemOnClickListener) {
//        this.mItemOnClickListener = mItemOnClickListener;
//    }

    private List<MYData> mDatas = new ArrayList<>();

    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    private SupetRecyclerView.SupetRecyclerViewAdapter mDRecyclerViewAdapter;

    private int itemHeight;

    public int getItemHeight() {
        return itemHeight;
    }

    public SupetRecyclerView.SupetRecyclerViewAdapter getmDRecyclerViewAdapter() {
        return mDRecyclerViewAdapter;
    }

    public void setRealRecyclerViewAdapter(SupetRecyclerView.SupetRecyclerViewAdapter mDRecyclerViewAdapter) {
        this.mDRecyclerViewAdapter = mDRecyclerViewAdapter;
    }

    public SupetRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public <T extends MYData> void addHomePage(List<T> data) {
        mDatas.clear();
        if (data != null) {
            this.mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }

    public <T extends MYData> void addNextPage(List<T> data) {
        if (data != null) {
            int itemCount = data.size();
            int postionStart = getItemCount();
            this.mDatas.addAll(data);
            notifyItemRangeInserted(postionStart, itemCount);
        }
    }

    public <T extends MYData> void addNextPage(T  data) {
        if (data != null) {
            int itemCount = 1;
            int postionStart = getItemCount();
            this.mDatas.add(data);
            notifyItemRangeInserted(postionStart, itemCount);
        }
    }

    public SupetRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final SupetRecyclerViewHolder holder = onCreateViewHolder(parent, viewType, this);
        holder.getWholeView().getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        itemHeight = holder.getWholeView().getMeasuredHeight();
                        return true;
                    }
                }
        );
        return holder;
    }


    public void onBindViewHolder(SupetRecyclerViewHolder holder, int position) {
        holder.setData(mDatas.get(position), position);
    }

    public int getItemCount() {
        return mDatas.size();
    }

    public boolean isFullSpan(int position) {
        return false;
    }


    public abstract SupetRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType, SupetRecyclerAdapter dBaseRecyclerViewAdapter);


    public void notifyDataSetChanged() {
        mDRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void notifyItemChanged(int position) {
        position = mDRecyclerViewAdapter.getHeaderViewsCount() + position;
        mDRecyclerViewAdapter.notifyItemChanged(position);
    }

    public void notifyItemInserted(int position) {
        position = mDRecyclerViewAdapter.getHeaderViewsCount() + position;
        mDRecyclerViewAdapter.notifyItemInserted(position);
    }

    public void notifyItemRemoved(int position) {
        position = mDRecyclerViewAdapter.getHeaderViewsCount() + position;
        mDRecyclerViewAdapter.notifyItemRemoved(position);
    }

    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        positionStart = mDRecyclerViewAdapter.getHeaderViewsCount() + positionStart;
        mDRecyclerViewAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        positionStart = mDRecyclerViewAdapter.getHeaderViewsCount() + positionStart;
        mDRecyclerViewAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        positionStart = mDRecyclerViewAdapter.getHeaderViewsCount() + positionStart;
        mDRecyclerViewAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    public List<MYData> getData() {
        return mDatas;
    }

    public MYData getData(int position) {
        return mDatas.get(position);
    }

    public boolean isEmpty() {
        return mDatas.isEmpty();
    }
}
