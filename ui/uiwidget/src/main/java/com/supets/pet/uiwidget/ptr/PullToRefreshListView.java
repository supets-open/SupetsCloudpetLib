package com.supets.pet.uiwidget.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.supets.pet.supetsbaseui.R;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class PullToRefreshListView extends PullToRefreshBase implements AbsListView.OnScrollListener {

    private int DEFAULT_LOAD_MORE_REMAIN_COUNT = 4;
    private FrameLayout mContent;
    private ListView mListView;
    private int mLoadMoreRemainCount = DEFAULT_LOAD_MORE_REMAIN_COUNT;
    private AbsListView.OnScrollListener mOnScrollListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnRefreshListener mListener;

    private View mItemLoadingView;
    private OnLoadMoreViewHandlerListener mOnLoadViewHandler;

    public PullToRefreshListView(Context context) {
        this(context, null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContent = new FrameLayout(getContext());
        this.addViewForPtrFrameLayout(mContent);

        mListView = new ListView(getContext(), attrs);
        mListView.setId(android.R.id.list);
        mContent.addView(mListView);

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

        mListView.setOnScrollListener(this);

        this.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                View mContent = mListView.isShown() ? mListView :
                        mListView.getEmptyView() != null ? mListView.getEmptyView() : content;
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mContent, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //具体刷新的操作的回调
                if (mListener != null) {
                    mListener.onRefresh(PullToRefreshListView.this);
                }
                Log.v("GSonRequest", "start");
            }
        });
    }

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
            setLoadMoreRemainCount(1);
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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void setRefreshing() {
        mListView.setSelection(0);
        super.setRefreshing();
    }

    public final ListView getRefreshableView() {
        return mListView;
    }

    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mListView.setOnItemClickListener(listener);
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mListView.setOnItemLongClickListener(listener);
    }

    public void addHeaderView(View view, Object data, boolean isSelectable) {
        mListView.addHeaderView(view, data, isSelectable);
    }

    public void addHeaderView(View view) {
        mListView.addHeaderView(view);
    }

    public int getHeaderViewsCount() {
        return mListView.getHeaderViewsCount();
    }

    public void setEmptyView(View emptyView) {
        if (emptyView == null) {
            return;
        }
        if (emptyView.getParent() != null) {
            ((ViewGroup) emptyView.getParent()).removeView(emptyView);
        }
        if (emptyView.getVisibility() != VISIBLE) {
            emptyView.setVisibility(VISIBLE);
        }

        mContent.addView(emptyView);
        mListView.setEmptyView(emptyView);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener refreshListener) {
        this.mListener = refreshListener;
    }

    public final void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    public final void setLoadMoreRemainCount(int remainCount) {
        mLoadMoreRemainCount = remainCount;
    }

    public final void setOnScrollListener(AbsListView.OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int state) {
        if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && null != mOnLoadMoreListener) {
            int lastPosition = getRefreshableView().getLastVisiblePosition();
            int total = getRefreshableView().getCount() - 1;
            if (total > 0 && total - lastPosition <= mLoadMoreRemainCount && getHeader().getTop() == -getHeader().getHeight()) {
                //mOnLoadMoreListener.onLoadMore();
                onLoadNextPageEx();
            }
        }

        if (null != mOnScrollListener) {
            mOnScrollListener.onScrollStateChanged(view, state);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mOnScrollListener) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
