package com.supets.pet.uiwidget.ptr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.supets.commons.utils.UIUtils;
import com.supets.pet.supetsbaseui.R;
import com.supets.pet.uiwidget.recyclelib.SupetRecyclerAdapter;
import com.supets.pet.uiwidget.recyclelib.SupetRecyclerView;
import com.supets.pet.uiwidget.recyclelib.SupetRecyclerViewScrollListener;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 无网络错误界面，只有正常和空情况。
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<SupetRecyclerView> implements PtrHandler {

    private FrameLayout mContent;
    private View mEmoptyView;
    private SupetRecyclerView mListView;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnRefreshListener<SupetRecyclerView> mListener;


    private View mItemLoadingView;
    private OnLoadMoreViewHandlerListener mOnLoadViewHandler;

    public PullToRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initview(attrs);
    }

    private void initview(AttributeSet attrs) {
        mContent = new FrameLayout(getContext());
        this.addViewForPtrFrameLayout(mContent);
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
        mContent.addView(mListView, PtrFrameLayout.LayoutParams.MATCH_PARENT, PtrFrameLayout.LayoutParams.MATCH_PARENT);

        mEmoptyView = LayoutInflater.from(getContext()).inflate(R.layout.page_view_empty, null);
        mContent.addView(mEmoptyView, PtrFrameLayout.LayoutParams.MATCH_PARENT, PtrFrameLayout.LayoutParams.MATCH_PARENT);

        showContentView();

        mListView.addOnScrollListener(mLoadMoreScrollListener);
        this.setPtrHandler(this);
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

    public void showContentView() {
        mListView.setVisibility(View.VISIBLE);
        if (mEmoptyView != null) {
            mEmoptyView.setVisibility(View.GONE);
        }
    }

    public void showEmpotyView() {
        mListView.setVisibility(View.GONE);
        if (mEmoptyView != null) {
            mEmoptyView.setVisibility(View.VISIBLE);
        }
    }

    public void showEmpotyView(int res) {
        mListView.setVisibility(View.GONE);
        if (mEmoptyView != null) {
            mEmoptyView.setVisibility(View.VISIBLE);
            TextView mTextView = (TextView) mEmoptyView.findViewById(R.id.page_view_empty_text);
            mTextView.setText(res);
        }
    }

    public void showEmpotyView(String res) {
        mListView.setVisibility(View.GONE);
        if (mEmoptyView != null) {
            mEmoptyView.setVisibility(View.VISIBLE);
            TextView mTextView = (TextView) mEmoptyView.findViewById(R.id.page_view_empty_text);
            mTextView.setText(res);
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
        View mContent = UIUtils.isSelfShown(mListView) ? mListView :
                mEmoptyView != null && UIUtils.isSelfShown(mEmoptyView) ? mEmoptyView : content;
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mContent, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        //具体刷新的操作的回调
        if (mListener != null) {
            mListener.onRefresh(this);
        }
    }

}
