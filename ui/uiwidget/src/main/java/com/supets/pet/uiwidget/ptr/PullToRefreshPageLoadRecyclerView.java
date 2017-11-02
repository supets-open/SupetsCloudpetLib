package com.supets.pet.uiwidget.ptr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.supets.commons.widget.PageLoadingView;
import com.supets.pet.supetsbaseui.R;
import com.supets.pet.uiwidget.recyclelib.SupetRecyclerAdapter;
import com.supets.pet.uiwidget.recyclelib.SupetRecyclerView;
import com.supets.pet.uiwidget.recyclelib.SupetRecyclerViewScrollListener;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 支持load，内容，未登录，空页面，网络错误，默认有无数据支持下拉刷新。
 */
public class PullToRefreshPageLoadRecyclerView extends PullToRefreshBase<SupetRecyclerView> implements PtrHandler {

    private PageLoadingView mContent;
    private SupetRecyclerView mListView;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnRefreshListener<SupetRecyclerView> mListener;

    private View mItemLoadingView;
    private OnLoadMoreViewHandlerListener mOnLoadViewHandler;

    public PullToRefreshPageLoadRecyclerView(Context context) {
        this(context, null);
    }

    public PullToRefreshPageLoadRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshPageLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initview(attrs);
    }

    private void initview(AttributeSet attrs) {
        mContent = new PageLoadingView(getContext());
        mListView = new SupetRecyclerView(getContext(), attrs);
        mItemLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.item_loadmore, null);
        mListView.addFooterView(mItemLoadingView);


        mItemLoadingView.findViewById(R.id.content).setVisibility(View.GONE);
        mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.GONE);

        mItemLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLoadViewHandler != null) {
                    onLoadNextPageEx();
                }
            }
        });
        mListView.setHasFixedSize(true);
        mListView.setId(android.R.id.list);
        mContent.addView(mListView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mContent.setContentView(mListView);
        mListView.addOnScrollListener(mLoadMoreScrollListener);
        this.addViewForPtrFrameLayout(mContent);
        this.setPtrHandler(this);
    }

    public PageLoadingView getPageLoadingView() {
        return mContent;
    }

    public SupetRecyclerViewScrollListener mLoadMoreScrollListener = new SupetRecyclerViewScrollListener() {
        @Override
        public void onLoadNextPage(RecyclerView view) {
            onLoadNextPageEx();
        }
    };

    private void onLoadNextPageEx() {
        if (mOnLoadMoreListener != null) {
            if (mOnLoadViewHandler != null && mOnLoadViewHandler.checkIfLoadMoreData()) {
                mItemLoadingView.findViewById(R.id.content).setVisibility(View.GONE);
                mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.VISIBLE);
                mOnLoadMoreListener.onLoadMore();
            } else if (mOnLoadViewHandler != null) {
                mItemLoadingView.findViewById(R.id.content).setVisibility(View.GONE);
                mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.GONE);
            } else {
                mOnLoadMoreListener.onLoadMore();
            }
        }
    }

    public void setOnLoadViewHandler(OnLoadMoreViewHandlerListener listener) {
        this.mOnLoadViewHandler = listener;
        if (mOnLoadViewHandler != null) {
            mItemLoadingView.findViewById(R.id.content).setVisibility(View.GONE);
            mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.VISIBLE);
            mLoadMoreScrollListener.setLoadMoreNum(2);
        }
    }


    @Override
    public void setRefreshing() {
        try {
            mListView.getLayoutManager().scrollToPosition(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setRefreshing();
    }

    public void scrollToPosition(int position) {
        try {
            mListView.getLayoutManager().scrollToPosition(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefreshComplete() {
        super.onRefreshComplete();
        if (mOnLoadViewHandler != null && mOnLoadViewHandler.checkIfLoadMoreData()) {
            mItemLoadingView.findViewById(R.id.content).setVisibility(View.VISIBLE);
            mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.GONE);
        } else if (mOnLoadViewHandler != null && !mOnLoadViewHandler.checkIfLoadMoreData() && mOnLoadViewHandler.checkIfHomePage()) {
            mItemLoadingView.findViewById(R.id.content).setVisibility(View.GONE);
            mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.GONE);
        } else if (mOnLoadViewHandler != null && !mOnLoadViewHandler.checkIfLoadMoreData()) {
            mItemLoadingView.findViewById(R.id.content).setVisibility(View.INVISIBLE);
            mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.GONE);
        }
    }

    public final SupetRecyclerView getRefreshableView() {
        return mListView;
    }

    public void setAdapter(SupetRecyclerAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public void addHeaderView(View view) {
        mListView.addHeaderView(view);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener<SupetRecyclerView> refreshListener) {
        this.mListener = refreshListener;
    }

    public final void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        View visbleContent = mContent.getVisibleChildView();
        visbleContent = visbleContent == null ? mContent : visbleContent;
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, visbleContent, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        //具体刷新的操作的回调
        if (mListener != null) {
            mListener.onRefresh(this);
        }
    }

    /***********************************/

    public void subscribeReloadRefreshEvent(Object subscriber, Integer requestCode) {
        mContent.subscribeRefreshEvent(subscriber, requestCode);
    }

    public void subscribeReloadRefreshEvent(Object subscriber) {
        mContent.subscribeRefreshEvent(subscriber);
    }

    public void showContentView() {
        setPtrEnabled(true);
        mContent.showContent();
    }

    public void showNetworkError() {
        setPtrEnabled(false);
        mContent.showNetworkError();
    }

    public void showEmptyView() {
        setPtrEnabled(true);
        mContent.showEmpty();
    }

    public void addEmptyView(View view) {
        mContent.setEmptyView(view);
    }

    public void addEmptyView(int res) {
        mContent.setEmptyView(res);
    }

    public void setEmptyText(String res) {
        mContent.setEmptyText(res);
    }

    public void setEmptyText(int res) {
        mContent.setEmptyText(res);
    }

    public void showLoading() {
        mContent.showLoading();
        setPtrEnabled(false);
    }

    public void showOtherView() {
        mContent.showOtherView();
        setPtrEnabled(false);
    }

    public void addOtherView(View view) {
        mContent.addView(view);
        mContent.setOtherView(view);
    }

    public void addOtherView(int res) {
        mContent.setOtherView(res);
    }

    public boolean isContentShow() {
        return mContent.isContentShow();
    }

    public boolean isOtherShow() {
        return mContent.isOtherShow();
    }
}
