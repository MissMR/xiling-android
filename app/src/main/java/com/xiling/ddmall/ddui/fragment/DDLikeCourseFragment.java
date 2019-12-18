package com.xiling.ddmall.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.adapter.LikeCourseAdapter;
import com.xiling.ddmall.ddui.bean.CourseBean;
import com.xiling.ddmall.ddui.bean.UIEvent;
import com.xiling.ddmall.shared.basic.BaseFragment;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.api.PaginationEntity;
import com.xiling.ddmall.shared.component.NoData;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.PageManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICollectService;
import com.xiling.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DDLikeCourseFragment extends BaseFragment implements PageManager.RequestListener {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.noDataLayout)
    protected NoData mNoDataLayout;

    LikeCourseAdapter mCourseAdapter = null;

    private ICollectService mCollectService;
    private PageManager mPageManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_list_layout, container, false);
        ButterKnife.bind(this, view);

        mCollectService = ServiceManager.getInstance().createService(ICollectService.class);

        mCourseAdapter = new LikeCourseAdapter(getContext());
        mRecyclerView.setAdapter(mCourseAdapter);

        try {
            mPageManager = PageManager.getInstance()
                    .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false))
                    .setRecyclerView(mRecyclerView)
                    .setSwipeRefreshLayout(mRefreshLayout)
                    .setRequestListener(this)
                    .build(getContext());
        } catch (PageManager.PageManagerException e) {
            ToastUtil.error("PageManager 初始化失败");
        }

        mNoDataLayout.setImgRes(R.mipmap.no_data_collect);
        mNoDataLayout.setTextView("亲，你还没有喜欢的课程哦");

        //测试代码
//        ArrayList data = new ArrayList<CourseBean>();
//        data.add(new CourseBean("000", "课程0：如何学习课程", "如何学习课程会让你知道", "https://i0.hdslb.com/bfs/archive/e11435cc41ae0683a5543038770e704d89ce58eb.jpg@320w_200h.jpg", "12"));
//        data.add(new CourseBean("001", "课程1：如何学习阅读", "如何学习课程会让你知道", "https://i0.hdslb.com/bfs/archive/7f96340cb00e69f24a6cec5183747663cf3dad9a.jpg@320w_200h.jpg", "11"));
//        data.add(new CourseBean("002", "课程2：如何学习朗诵", "如何学习课程会让你知道", "https://i0.hdslb.com/bfs/archive/b1c582ad126f289c6f5837594d19d5ceb23ee929.jpg@320w_200h.jpg", "10"));
//        mCourseAdapter.addItems(data);
//        mCourseAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mPageManager.onRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnLike(UIEvent uiEvent) {
        if (uiEvent.getType() == UIEvent.Type.UnLikeLearn) {
            mPageManager.onRefresh();
        }
    }

    @Override
    public void nextPage(final int page) {
        APIManager.startRequest(mCollectService.getLikeCourseList(page), new BaseRequestListener<PaginationEntity<CourseBean, Object>>(mRefreshLayout) {
            @Override
            public void onSuccess(PaginationEntity<CourseBean, Object> result) {
                if (page == 1) {
                    mCourseAdapter.getItems().clear();
                }
                mPageManager.setLoading(false);
                mPageManager.setTotalPage(result.totalPage);
                mCourseAdapter.addItems(result.list);
                mNoDataLayout.setVisibility(result.total > 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }
}
