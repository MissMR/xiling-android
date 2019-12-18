package com.xiling.ddmall.module.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.category.adapter.NewCategoryAdapter;
import com.xiling.ddmall.module.search.SearchActivity;
import com.xiling.ddmall.shared.basic.BaseFragment;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.Category;
import com.xiling.ddmall.shared.bean.api.PaginationEntity;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.bean.event.MsgCategory;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICategoryService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.category
 * @since 2017-06-16
 */
public class CategoryFragment extends BaseFragment {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.searchLayout)
    LinearLayout mSearchLayout;
    @BindView(R.id.ivNoData)
    ImageView mIvNoData;
    @BindView(R.id.tvNoData)
    TextView mTvNoData;
    @BindView(R.id.tvGoMain)
    TextView mTvNoDataBtn;
    @BindView(R.id.layoutNodata)
    LinearLayout mLayoutNodata;
    @BindView(R.id.leftContainer)
    FrameLayout mLeftContainer;

    private NewCategoryAdapter mAdapter;
    private ICategoryService mCategoryService;
    private String mCategoryId = "";
    private boolean isLoaded = false;
    private List<Category> mDatas = new ArrayList<>();
    private static final int PAGE_SIZE = 30;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category_layout, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        mCategoryService = ServiceManager.getInstance().createService(ICategoryService.class);
        initViews();
        return rootView;
    }

    void initViews() {
        initNoData();

        mAdapter = new NewCategoryAdapter(mDatas);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mAdapter);
        isLoaded = true;
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("categoryId", mDatas.get(position).id);
                getActivity().startActivity(intent);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(true);
            }
        });

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.leftContainer, getCategoryLeftFragment());
        fragmentTransaction.commit();
    }

    private GridLayoutManager getLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setAutoMeasureEnabled(true);
        return gridLayoutManager;
    }


    private void initNoData() {
        mIvNoData.setImageResource(R.mipmap.no_data_normal);
        mTvNoData.setText("这个页面去火星了");
        mTvNoDataBtn.setText("刷新看看");
        mTvNoDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MsgCategory(MsgCategory.ACTION_RE_QUEST));
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded() && !isLoaded) {
        }
    }

    private LeftCategoryFragment getCategoryLeftFragment() {
        LeftCategoryFragment leftCategoryFragment = new LeftCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("categoryId", mCategoryId);
        leftCategoryFragment.setArguments(bundle);
        return leftCategoryFragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getData(boolean isRefresh) {
        LogUtils.e("开始刷新" + mCategoryId);
        if (mCategoryId == null || mCategoryId.isEmpty()) {
            return;
        }
        if (isRefresh) {
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
        }
        int page = mDatas.size() / PAGE_SIZE + 1;
        APIManager.startRequest(
                mCategoryService.getCategory(mCategoryId, page, PAGE_SIZE),
                new BaseRequestListener<PaginationEntity<Category, Object>>(mRefreshLayout) {
                    @Override
                    public void onSuccess(PaginationEntity<Category, Object> result) {
                        mDatas.addAll(result.list);
                        if (result.list.size() < PAGE_SIZE) {
                            mAdapter.loadMoreEnd();
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mAdapter.loadMoreFail();

                    }
                });
    }

    @OnClick(R.id.searchLayout)
    protected void goToSearch() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectCategory(EventMessage message) {
        if (message.getEvent() == Event.changeCategory) {

            String categoryId = String.valueOf(message.getData());
            LogUtils.e("收到数据 id" + categoryId);
            if (com.blankj.utilcode.utils.StringUtils.isEmpty(mCategoryId) || !categoryId.equals(mCategoryId)) {
                mCategoryId = categoryId;
                getData(true);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(MsgCategory msgCategory) {
        switch (msgCategory.getAction()) {
            case MsgCategory.ACTION_SHOW_NODATA:
                mLayoutNodata.setVisibility(View.VISIBLE);
                break;
            case MsgCategory.ACTION_GONE_NODATA:
                mLayoutNodata.setVisibility(View.GONE);
                break;
            default:
        }
    }


}
