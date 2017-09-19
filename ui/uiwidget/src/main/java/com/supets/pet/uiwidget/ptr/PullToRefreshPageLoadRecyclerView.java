package com.supets.pet.uiwidget.ptr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.supets.commons.widget.PageLoadingView;
import com.supets.pet.supetsbaseui.R;
import com.supets.pet.uiwidget.recyclelib.SupetRecyclerView;
import com.supets.pet.uiwidget.recyclelib.SupetRecyclerViewScrollListener;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Author:          zgl_dmw
 * Email:           2559531803@qq.com
 * Date:            2016/8/11 10:35
 * Description:     PullToRefreshPageLoadRecyclerView
 */
public class PullToRefreshPageLoadRecyclerView extends PullToRefreshBase<SupetRecyclerView> implements PtrHandler {
    private PageLoadingView mContent;
    private SupetRecyclerView mListView;
    private OnLoadMoreListener mLoadMoreListener;
    private OnRefreshListener<SupetRecyclerView> mRefreshListener;

    private View mItemLoadingView;
    private OnLoadMoreViewHandlerListener mOnLoadViewHandler;
    private SupetRecyclerViewScrollListener mLoadMoreScrollListener = new SupetRecyclerViewScrollListener() {
        @Override
        public void onLoadNextPage(RecyclerView view) {
            onLoadNextPageEx();
        }
    };

    public PullToRefreshPageLoadRecyclerView(Context context) {
        this(context, null);
    }

    public PullToRefreshPageLoadRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshPageLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        mContent = new PageLoadingView(getContext());
        mListView = new SupetRecyclerView(getContext(), attrs);

        mListView.setHasFixedSize(true);
        mListView.setId(android.R.id.list);
        mContent.addView(mListView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mContent.setContentView(mListView);
        mListView.addOnScrollListener(mLoadMoreScrollListener);
        this.addViewForPtrFrameLayout(mContent);
        this.setPtrHandler(this);
    }

    public final void setLoadMoreItemView() {
        mItemLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.item_loadmore, null);
        hideLoadingView();

        mItemLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLoadViewHandler != null) {
                    onLoadNextPageEx();
                }
            }
        });

        if (mListView.getAdapter() != null) {
            mListView.addFooterView(mItemLoadingView);
        }
    }

    private void onLoadNextPageEx() {
        if (mLoadMoreListener != null) {
            if (mItemLoadingView != null) {
                if (mOnLoadViewHandler != null) {
                    if (mOnLoadViewHandler.checkIfLoadMoreData()) {
                        showLoadingView();

                        mLoadMoreListener.onLoadMore();
                    } else {
                        hideLoadingView();
                    }
                } else {
                    mLoadMoreListener.onLoadMore();
                }
            } else {
                mLoadMoreListener.onLoadMore();
            }
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        View visbleContent = mContent.getVisibleChildView();
        visbleContent = visbleContent == null ? mContent : visbleContent;
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, visbleContent, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh(this);
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

    @Override
    public void onRefreshComplete() {
        super.onRefreshComplete();

        if (mItemLoadingView != null) {
            if (mOnLoadViewHandler != null) {
                if (mOnLoadViewHandler.checkIfHomePage()) {
                    if (mOnLoadViewHandler.checkIfLoadMoreData()) {
                        enableClickLoadMore();
                    } else {
                        invisibleLoadingView();
                    }
                } else {
                    if (mOnLoadViewHandler.checkIfLoadMoreData()) {
                        enableClickLoadMore();
                    } else {
                        invisibleLoadingView();
                    }
                }
            } else {
                hideLoadingView();
            }
        }
    }

    private void showLoadingView() {
        mItemLoadingView.findViewById(R.id.content).setVisibility(View.GONE);
        mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.VISIBLE);
    }

    private void hideLoadingView() {
        mItemLoadingView.findViewById(R.id.content).setVisibility(View.GONE);
        mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.GONE);
    }

    private void enableClickLoadMore() {
        mItemLoadingView.findViewById(R.id.content).setVisibility(View.VISIBLE);
        mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.GONE);
    }

    private void invisibleLoadingView() {
        mItemLoadingView.findViewById(R.id.content).setVisibility(View.INVISIBLE);
        mItemLoadingView.findViewById(R.id.contentload).setVisibility(View.GONE);
    }

    public final SupetRecyclerView getRecyclerView() {
        return mListView;
    }

    public final void setOnLoadViewHandler(OnLoadMoreViewHandlerListener listener) {
        this.mOnLoadViewHandler = listener;
        if (mOnLoadViewHandler != null) {
            mLoadMoreScrollListener.setLoadMoreNum(2);
        }
    }

    public final void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener<SupetRecyclerView> listener) {
        mRefreshListener = listener;
    }

    /*************************************************************************/

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
