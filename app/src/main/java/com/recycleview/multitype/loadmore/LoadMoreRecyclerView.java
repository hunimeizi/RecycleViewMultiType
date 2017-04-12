package com.recycleview.multitype.loadmore;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.recycleview.multitype.R;
import com.recycleview.multitype.utils.AppUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class LoadMoreRecyclerView extends RecyclerView implements OnScrollBottomListener {
    /**
     * 加载更多UI
     */
    ILoadMoreView mLoadMoreView;

    /**
     * 加载更多方式，默认滑动到底部加载更多
     */
    LoadMoreMode mLoadMoreMode = LoadMoreMode.SCROLL;
    /**
     * 加载更多lock
     */
    private boolean mLoadMoreLock;
    /**
     * 是否可以加载跟多
     */
    boolean mHasLoadMore;
    /**
     * 是否加载失败
     */
    private boolean mHasLoadFail;

    /**
     * 加载更多事件回调
     */
    private OnLoadMoreListener mOnLoadMoreListener;

    /**
     * emptyview
     */
    private View mEmptyView;

    /**
     * 没有更多了是否隐藏loadmoreview
     */
    private boolean mNoLoadMoreHideView;

    private FooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private boolean mAddLoadMoreFooterFlag;

    private int mLastVisibleItemPosition;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mHeaderAndFooterRecyclerViewAdapter = new FooterRecyclerViewAdapter();
        super.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadMoreView);

        if (a.hasValue(R.styleable.LoadMoreView_loadMoreMode)) {
            mLoadMoreMode =
                    LoadMoreMode.mapIntToValue(a.getInt(R.styleable.LoadMoreView_loadMoreMode, 0x01));
        } else {
            mLoadMoreMode = LoadMoreMode.SCROLL;
        }

        if (a.hasValue(R.styleable.LoadMoreView_noLoadMoreHideView)) {
            mNoLoadMoreHideView = a.getBoolean(R.styleable.LoadMoreView_noLoadMoreHideView, false);
        } else {
            mNoLoadMoreHideView = false;
        }

        if (a.hasValue(R.styleable.LoadMoreView_loadMoreView)) {
            try {
                String loadMoreViewName = a.getString(R.styleable.LoadMoreView_loadMoreView);
                Class clazz = Class.forName(loadMoreViewName);
                Constructor c = clazz.getConstructor(Context.class);
                ILoadMoreView loadMoreView = (ILoadMoreView) c.newInstance(context);
                mLoadMoreView = loadMoreView;
            } catch (Exception e) {
                e.printStackTrace();
                mLoadMoreView = new DefaultLoadMoreView(context);
            }
        } else {
            mLoadMoreView = new DefaultLoadMoreView(context);
        }

        mLoadMoreView.getFooterView().setOnClickListener(new OnMoreViewClickListener());

        setHasLoadMore(false);
        a.recycle();
        addOnScrollListener(new RecyclerViewOnScrollListener());
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        //        super.setAdapter(adapter);
        try {
            adapter.unregisterAdapterDataObserver(mDataObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.registerAdapterDataObserver(mDataObserver);
        mHeaderAndFooterRecyclerViewAdapter.setAdapter(adapter);
    }

    @Override
    public void onScorllBootom() {
        if (mHasLoadMore && mLoadMoreMode == LoadMoreMode.SCROLL) {
            executeLoadMore();
        }
    }

    /**
     * 设置recyclerview emptyview
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 设置LoadMoreView
     */
    public void setLoadMoreView(ILoadMoreView loadMoreView) {
        if (mLoadMoreView != null) {
            try {
                removeFooterView(mLoadMoreView.getFooterView());
                mAddLoadMoreFooterFlag = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mLoadMoreView = loadMoreView;
        mLoadMoreView.getFooterView().setOnClickListener(new OnMoreViewClickListener());
    }

    /**
     * 设置加载更多模式
     */
    public void setLoadMoreMode(LoadMoreMode mode) {
        mLoadMoreMode = mode;
    }

    /**
     * 设置没有更多数据了，是否隐藏fooler view
     */
    public void setNoLoadMoreHideView(boolean hide) {
        this.mNoLoadMoreHideView = hide;
    }

    /**
     * 没有很多了
     */
    public void showNoMoreUI() {
        mLoadMoreLock = false;
        mLoadMoreView.showNoMore();
    }

    /**
     * 显示失败UI
     */
    public void showFailUI() {
        mHasLoadFail = true;
        mLoadMoreLock = false;
        mLoadMoreView.showFail();
    }

    /**
     * 显示默认UI
     */
    void showNormalUI() {
        mLoadMoreLock = false;
        mLoadMoreView.showNormal();
    }

    /**
     * 显示加载中UI
     */
    public void showLoadingUI() {
        mHasLoadFail = false;
        mLoadMoreLock = false;
        mLoadMoreView.showLoading();
    }

    /**
     * 隐藏loading
     */
    public void goneLoadingUI() {
        mLoadMoreLock = false;
        mLoadMoreView.goneLoading();
    }
    /**
     * 显示查看更多按钮
     */
    public void visibleMoreBtn() {
        mLoadMoreView.VisibleMoreBtn();
    }

    /**
     * 隐藏查看更多按钮
     */
    public void goneMoreBtn() {
        mLoadMoreView.goneMoreBtn();
    }

    public void onClickLoadMore(){
        mLoadMoreView.onClickLoadMoreView();
    }
    public void setViewTAG(int id,String tag){
        mLoadMoreView.setViewTag(id,tag);
    }
    /**
     * 是否有更多
     */
    public void setHasLoadMore(boolean hasLoadMore) {
        mHasLoadMore = hasLoadMore;
        if (!mHasLoadMore) {
            showNoMoreUI();
            if (mNoLoadMoreHideView) {
                removeFooterView(mLoadMoreView.getFooterView());
                mAddLoadMoreFooterFlag = false;
            }
        } else {
            if (!mAddLoadMoreFooterFlag) {
                mAddLoadMoreFooterFlag = true;
                addFooterView(mLoadMoreView.getFooterView());
            }
            showNormalUI();
        }
    }

    /**
     * 设置加载更多事件回调
     */
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mOnLoadMoreListener = loadMoreListener;
    }

    /**
     * 完成加载更多
     */
    public void onLoadMoreComplete() {
        if (mHasLoadFail) {
            showFailUI();
        } else if (mHasLoadMore) {
            //showNormalUI();
            mLoadMoreLock = false;
        }
    }

    /**
     * 添加footer view
     */
    public void addFooterView(View footerView) {
        mHeaderAndFooterRecyclerViewAdapter.addFooterView(footerView);
    }

    public void removeFooterView(View footerView) {
        mHeaderAndFooterRecyclerViewAdapter.removeFooter(footerView);
    }

    public void addHeadeView(View headerView) {
        mHeaderAndFooterRecyclerViewAdapter.addHeaderView(headerView);
    }

    public ArrayList<View> getHeadeView() {
        return mHeaderAndFooterRecyclerViewAdapter.getHeaderViews();
    }


    /**
     * 点击more view加载更多
     */
    class OnMoreViewClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (!AppUtils.isNetworkAvailable(view.getContext())) {
                Toast.makeText(view.getContext(), R.string.isw_loading_view_net_error, Toast.LENGTH_SHORT)
                        .show();
            }
            if (mLoadMoreView.getFooterView() instanceof DefaultLoadMoreView) {
                if (((DefaultLoadMoreView) mLoadMoreView.getFooterView()).getTvMessage()
                        .equals(view.getContext().getString(R.string.isw_loading_view_no_me))) {
                    return;
                }
            }
            if (mHasLoadMore) {
                executeLoadMore();
            }
        }
    }

    /**
     * 加载更多
     */
    void executeLoadMore() {
        if (!mLoadMoreLock && mHasLoadMore) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.loadMore();
            }
            mLoadMoreLock = true;//上锁
            showLoadingUI();
        }
    }

    /**
     * 设置OnItemClickListener
     */
    public void setOnItemClickListener(FooterRecyclerViewAdapter.OnItemClickListener listener) {
        mHeaderAndFooterRecyclerViewAdapter.setOnItemClickListener(listener);
    }

    /**
     * 设置OnItemLongClickListener
     */
    public void setOnItemLongClickListener(
            FooterRecyclerViewAdapter.OnItemLongClickListener listener) {
        mHeaderAndFooterRecyclerViewAdapter.setOnItemLongClickListener(listener);
    }

    /**
     * 滚动到底部自动加载更多数据
     */
    private class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {

        /**
         * 最后一个的位置
         */
        private int[] lastPositions;

        /**
         * 最后一个可见的item的位置
         */
        private int lastVisibleItemPosition;

        /**
         * 当前滑动的状态
         */
        private int currentScrollState = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition =
                        ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager =
                        (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
            mLastVisibleItemPosition = lastVisibleItemPosition;
            if (onRecyclerViewListener != null) {
                onRecyclerViewListener.onScroll(dy);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            currentScrollState = newState;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if ((visibleItemCount > 0
                    && currentScrollState == RecyclerView.SCROLL_STATE_IDLE
                    && (lastVisibleItemPosition) >= totalItemCount - 1)) {
                onScorllBootom();
            }
        }
        /**
         * 取数组中最大值
         */
        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }

            return max;
        }
    }

    /**
     * 刷新数据时停止滑动,避免出现数组下标越界问题
     */
    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    setVisibility(View.VISIBLE);
                }
            }

            dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_CANCEL, 0, 0, 0));
        }
    };

    public int getRvLastPosition() {
        return mLastVisibleItemPosition;
    }

    public interface OnRecyclerViewListener {

        void onScroll(int dy);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
}
