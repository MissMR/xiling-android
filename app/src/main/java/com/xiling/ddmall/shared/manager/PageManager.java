package com.xiling.ddmall.shared.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.xiling.ddmall.shared.decoration.ListDividerDecoration;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.manager
 * @since 2017-06-10
 */
public class PageManager implements SwipeRefreshLayout.OnRefreshListener {

    private static final int VISIBLE_THRESHOLD = 1;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private RequestListener mListener;
    private Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;
    private int mPage;
    private int mTotalPage;
    private boolean isLoading = false;
    private boolean mShowItemDecoration = true;
    private RecyclerView.ItemDecoration mDecoration;
    private View mNoDataLayout;

    private PageManager() {
    }

    public static PageManager getInstance() {
        return new PageManager();
    }

    public PageManager setRecyclerView(@NonNull RecyclerView recyclerView) throws PageManagerException {
        mRecyclerView = recyclerView;
        if (recyclerView.getAdapter() == null) {
            throw new PageManagerException("PayAdapter Can not be null");
        }
        return this;
    }

    public PageManager setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        return this;
    }


    public PageManager setItemDecoration(RecyclerView.ItemDecoration decoration) {
        mDecoration = decoration;
        mShowItemDecoration = true;
        return this;
    }

    public PageManager showItemDecoration(boolean show) {
        mShowItemDecoration = show;
        return this;
    }

    public PageManager setSwipeRefreshLayout(@NonNull SwipeRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        return this;
    }

    public PageManager setRequestListener(@NonNull RequestListener listener) {
        mListener = listener;
        return this;
    }

    public PageManager build(@NonNull Context context) throws PageManagerException {
        setContext(context);
        init();
        return this;
    }

    private void init() throws PageManagerException {
        if (mLayoutManager == null) {
            throw new PageManagerException("LayoutManager Can not be null");
        }
        if (mRecyclerView == null) {
            throw new PageManagerException("RecyclerView Can not be null");
        }
        if (mListener == null) {
            throw new PageManagerException("RequestListener Can not be null");
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (mShowItemDecoration) {
            if (mDecoration == null) {
                mDecoration = new ListDividerDecoration(mContext);
            }
            mRecyclerView.addItemDecoration(mDecoration);
        }

        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener(this);
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mTotalPage == 0 || isLoading) {
                        return;
                    }

                    int lastVisibleItemPosition;
                    if (mLayoutManager instanceof GridLayoutManager) {
                        lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                    } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                        int[] into = new int[((StaggeredGridLayoutManager) mLayoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(into);
                        lastVisibleItemPosition = findMax(into);
                    } else {
                        lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                    }
                    if (mTotalPage > mPage &&
                            mLayoutManager.getChildCount() > 0 &&
                            lastVisibleItemPosition >= mLayoutManager.getItemCount() - 1
                            && mLayoutManager.getItemCount() > mLayoutManager.getChildCount()
                            ) {
                        setLoading(true);
                        mPage++;
                        mListener.nextPage(mPage);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (mTotalPage == 0 || isLoading) {
//                    return;
//                }
//                if (mTotalPage > mPage && dy > 0) {
//                    int totalItemCount = mLayoutManager.getItemCount();
//                    int lastVisibleItem = 0;
//                    if (mLayoutManager instanceof LinearLayoutManager) {
//                        lastVisibleItem =  ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();
//                    }else if(mLayoutManager instanceof GridLayoutManager){
//                        lastVisibleItem =  ((GridLayoutManager)mLayoutManager).findLastVisibleItemPosition();
//                    }
//                    if (lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount) {
//                        setLoading(true);
//                        mPage++;
//                        mListener.nextPage(mPage);
//                    }
//                }
//            }
//        });
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public void onRefresh() {
        setLoading(true);
        mPage = 1;
        setTotalPage(0);
        mListener.nextPage(mPage);
    }

    private void setContext(Context context) {
        mContext = context;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setTotalPage(int totalPage) {
        this.mTotalPage = Math.max(0, totalPage);
    }

    public PageManager setNoDataLayout(View noDataLayout) {
        mNoDataLayout = noDataLayout;
        return this;
    }

    public interface RequestListener {
        void nextPage(int page);
    }

    public class PageManagerException extends Exception {
        PageManagerException(String message) {
            super(message);
        }
    }
}
