package com.supets.pet.uiwidget.recyclelib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SupetRecyclerView extends RecyclerView {

    private SupetRecyclerViewAdapter dRecyclerViewAdapter = new SupetRecyclerViewAdapter();

    public SupetRecyclerView(Context context) {
        super(context);
    }

    public SupetRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SupetRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addHeaderView(View view) {
        dRecyclerViewAdapter.addHeaderView(view);
    }

    public void addFooterView(View view) {
        dRecyclerViewAdapter.addFooterView(view);
    }

    public void setAdapter(SupetRecyclerAdapter adapter) {
        dRecyclerViewAdapter.setAdapter(adapter);
        super.setAdapter(dRecyclerViewAdapter);
    }

    public void setLayoutManager(LayoutManager layoutManager) {

        if (layoutManager instanceof SupetStaggeredGridLayoutManager) {
            SupetStaggeredGridLayoutManager dStaggeredGridLayoutManager = (SupetStaggeredGridLayoutManager) layoutManager;
            dStaggeredGridLayoutManager.setSpanSizeLookup(new SupetSpanSizeLookup(dRecyclerViewAdapter, dStaggeredGridLayoutManager.getSpanCount()));
        }

        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager dStaggeredGridLayoutManager = (GridLayoutManager) layoutManager;
            dStaggeredGridLayoutManager.setSpanSizeLookup(new SupetSpanSizeLookup(dRecyclerViewAdapter, dStaggeredGridLayoutManager.getSpanCount()));
        }

        super.setLayoutManager(layoutManager);
    }

    /**
     * 实现AddHead AddFoot 下滑加载更多的RecyclerView
     * Created by Daemon on 2015/11/10.
     */
    public static  class SupetRecyclerViewAdapter extends Adapter<ViewHolder> {

        private static final int TYPE_HEADER_VIEW = Integer.MIN_VALUE;
        private static final int TYPE_FOOTER_VIEW = Integer.MIN_VALUE + 100;

        private SupetRecyclerAdapter mInnerAdapter;

        private ArrayList<View> mHeaderViews = new ArrayList<>();
        private ArrayList<View> mFooterViews = new ArrayList<>();


        public SupetRecyclerViewAdapter() {
        }

        public void setAdapter(SupetRecyclerAdapter myAdapter) {
            if (myAdapter == null) {
                throw new RuntimeException("your adapter  not null");
            }
            this.mInnerAdapter = myAdapter;

            myAdapter.setRealRecyclerViewAdapter(this);
        }

        public SupetRecyclerAdapter getInnerAdapter() {
            return mInnerAdapter;
        }

        /**
         * addHead to RecyclerView
         *
         * @param header
         */
        public void addHeaderView(View header) {
            if (header == null) {
                throw new IllegalArgumentException("header is null");
            }
            mHeaderViews.add(header);
        }

        /**
         * addFoot to RecyclerView
         *
         * @param footer
         */
        public void addFooterView(View footer) {

            if (footer == null) {
                throw new IllegalArgumentException("footer is null");
            }

            mFooterViews.add(footer);
        }


        /**
         * 返回第一个FoView
         *
         * @return
         */
        public View getFooterView() {
            return getFooterViewsCount() > 0 ? mFooterViews.get(0) : null;
        }

        /**
         * 返回第一个HeaderView
         *
         * @return
         */
        public View getHeaderView() {
            return getHeaderViewsCount() > 0 ? mHeaderViews.get(0) : null;
        }

        public void removeHeaderView(View view) {
            mHeaderViews.remove(view);
        }

        public void removeFooterView(View view) {
            mFooterViews.remove(view);
        }

        public boolean isHeader(int position) {
            return getHeaderViewsCount() > 0 && position < getHeaderViewsCount();
        }

        public boolean isTopHeader(int position) {
            return getHeaderViewsCount() > 0 && position == 0;
        }


        public boolean isFooter(int position) {
            int lastPosition = getItemCount() - 1;
            return getFooterViewsCount() > 0 && position > lastPosition - getFooterViewsCount() && position <= lastPosition;
        }

        public boolean isLastFooter(int position) {
            int lastPosition = getItemCount() - 1;
            return getFooterViewsCount() > 0 && position == lastPosition;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int headerViewsCountCount = getHeaderViewsCount();

            if (viewType < TYPE_HEADER_VIEW + headerViewsCountCount) {
                return new SupetViewHolder(mHeaderViews.get(viewType - TYPE_HEADER_VIEW));
            } else if (viewType >= TYPE_FOOTER_VIEW && viewType < Integer.MAX_VALUE / 2) {
                return new SupetViewHolder(mFooterViews.get(viewType - TYPE_FOOTER_VIEW));
            } else {
                return mInnerAdapter.onCreateViewHolder(parent, viewType - Integer.MAX_VALUE / 2);
            }

        }


        @Override
        public int getItemViewType(int position) {

            int innerCount = mInnerAdapter.getItemCount();
            int headerViewsCountCount = getHeaderViewsCount();

            if (position < headerViewsCountCount) {
                return TYPE_HEADER_VIEW + position;
            } else if (headerViewsCountCount <= position && position < headerViewsCountCount + innerCount) {

                int innerItemViewType = mInnerAdapter.getItemViewType(position - headerViewsCountCount);
                if (innerItemViewType >= Integer.MAX_VALUE / 2) {
                    throw new IllegalArgumentException("your adapter's return value of getViewTypeCount() must < Integer.MAX_VALUE / 2");
                }
                return innerItemViewType + Integer.MAX_VALUE / 2;

            } else {
                return TYPE_FOOTER_VIEW + position - headerViewsCountCount - innerCount;
            }

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            int headerViewsCountCount = getHeaderViewsCount();

            //是数据item 就调用原本的
            if (position >= headerViewsCountCount && position < headerViewsCountCount + mInnerAdapter.getItemCount()) {
                mInnerAdapter.onBindViewHolder((SupetRecyclerViewHolder) holder, position - headerViewsCountCount);

                if (mInnerAdapter.isFullSpan(position-headerViewsCountCount)){
                    ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                    if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                        ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                    }
                }
            } else {
                ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                }
            }

        }

        @Override
        public int getItemCount() {
            return getHeaderViewsCount() + getFooterViewsCount() + mInnerAdapter.getItemCount();
        }


        public int getHeaderViewsCount() {
            return mHeaderViews.size();
        }

        public int getFooterViewsCount() {
            return mFooterViews.size();
        }


        private  class SupetViewHolder extends ViewHolder {
            public SupetViewHolder(View itemView) {
                super(itemView);
            }
        }

        public boolean isFullSpan(int position){
            return mInnerAdapter.isFullSpan(position);
        }

    }
}
