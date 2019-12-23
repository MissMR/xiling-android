package com.xiling.module.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiling.R;
import com.xiling.module.category.adapter.CategoryProductListAdapter;
import com.xiling.module.category.adapter.LeftCategoryAdapter;
import com.xiling.module.search.SearchActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Category;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICategoryService;
import com.xiling.shared.util.RvUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/6/7.
 */
public class NewCategoryFragment extends BaseFragment {

    @BindView(R.id.searchLayout)
    TextView mSearchLayout;
    @BindView(R.id.linkToBeShopkeeper)
    LinearLayout mLinkToBeShopkeeper;
    @BindView(R.id.rvLeft)
    RecyclerView mRvLeft;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private LeftCategoryAdapter mLeftCategoryAdapter;
    private ICategoryService mCategoryService;
    private CategoryProductListAdapter mProductListAdapter;
    private String mCategoryId;

    public static NewCategoryFragment newInstance() {
        Bundle args = new Bundle();

        NewCategoryFragment fragment = new NewCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_category, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mCategoryService = ServiceManager.getInstance().createService(ICategoryService.class);
        APIManager.startRequest(
                mCategoryService.getTopCategory(1, 100),
                new BaseRequestListener<PaginationEntity<Category, Object>>(mRefreshLayout) {
                    @Override
                    public void onSuccess(PaginationEntity<Category, Object> result) {
                        mLeftCategoryAdapter.addItems(result.list);
                        if (result.list.size() > 0) {
                            String id = result.list.get(0).id;
                            mLeftCategoryAdapter.setSelect(id);
                            EventBus.getDefault().post(new EventMessage(Event.changeCategory, id));
                        }
                    }
                });
    }

    private void initView() {
        mLeftCategoryAdapter = new LeftCategoryAdapter(getContext());
        RvUtils.configRecycleView(getContext(), mRvLeft);
        mRvLeft.setAdapter(mLeftCategoryAdapter);

        mProductListAdapter = new CategoryProductListAdapter();
        RvUtils.configRecycleView(getContext(), mRecyclerView, mProductListAdapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProductList(true);
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                SkuInfo skuInfo = (SkuInfo) adapter.getData().get(position);
//                DDProductDetailActivity.start(getContext(), skuInfo.s);
            }
        });
    }

    private void getProductList(boolean isRefresh) {
        if (isRefresh) {
            mProductListAdapter.setNewData(null);
        }

        APIManager.startRequest(
                mCategoryService.getSkuListByCategory(
                        mCategoryId,
                        mProductListAdapter.getData().size() / Constants.PAGE_SIZE + 1,
                        Constants.PAGE_SIZE
                )
                , new BaseRequestListener<PaginationEntity<SkuInfo, Object>>(mRefreshLayout) {
                    @Override
                    public void onSuccess(PaginationEntity<SkuInfo, Object> result) {
                        super.onSuccess(result);
                        mProductListAdapter.addData(result.list);
                        if (result.list.size() < Constants.PAGE_SIZE) {
                            mProductListAdapter.loadMoreEnd();
                        } else {
                            mProductListAdapter.loadMoreComplete();
                        }
                    }
                }
        );
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void selectCategory(EventMessage message) {
        if (message.getEvent() == Event.changeCategory) {
            EventBus.getDefault().removeStickyEvent(message);
            String categoryId = String.valueOf(message.getData());
            if (!categoryId.equalsIgnoreCase(mCategoryId)) {
                mCategoryId = categoryId;
                getProductList(true);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.searchLayout)
    public void onSearch() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }
}
