package com.dodomall.ddmall.module.push;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.Config;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Category;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.decoration.ListDividerDecoration;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IProductService;
import com.dodomall.ddmall.shared.util.RvUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/6 上午11:52.
 */
public class PushCategoryProductListActivity extends BaseActivity {

    @BindView(R.id.rvProduch)
    RecyclerView mRvProduch;
    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout mLayoutRefresh;
    @BindView(R.id.screenView)
    ProductScreenView mScreenView;
    @BindView(R.id.viewYinying)
    View mViewYinying;

    private String mId;
    private String mCategoryId;
    private String mTiTle;
    private ProduchPushAdapter mAdapter;
    private List<SkuInfo> mDatas;
    private IProductService mService;
    private String mUrl;
    private int mSort = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_hot_product_list_activity);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        getData(true);
    }

    private void getData(final boolean isRefresh) {
        if (mService == null) {
            mService = ServiceManager.getInstance().createService(IProductService.class);
        }
        if (isRefresh) {
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
        }
        if (mId.equals(mCategoryId)) {
            mUrl = "productPush/skuMainCategoryList";
        } else {
            mUrl = "productPush/skuCategoryList";
        }
        APIManager.startRequest(
                mService.getPushCategoryProductList(mUrl, Constants.PAGE_SIZE, mDatas.size() / Constants.PAGE_SIZE + 1, mSort, mCategoryId),
                new BaseRequestListener<PaginationEntity<SkuInfo, Object>>(mLayoutRefresh) {
                    @Override
                    public void onSuccess(PaginationEntity<SkuInfo, Object> result) {
                        super.onSuccess(result);
                        mDatas.addAll(result.list);
                        mAdapter.notifyDataSetChanged();
                        if (result.list.size() < Constants.PAGE_SIZE) {
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
                }
        );
    }

    private void initView() {
        setLeftBlack();
        setTitle(mTiTle);
        mScreenView.setVisibility(View.VISIBLE);
        mScreenView.setCategoryId(mId);
        mScreenView.setShowModel(ProductScreenView.SHOW_ALL);
        mScreenView.setYinYing(mViewYinying);
        mScreenView.setSortLisnener(new ProductScreenView.SortChangeLisnener() {
            @Override
            public void onChange(int sort) {
                mSort = sort;
                getData(true);
            }
        });
        mScreenView.setCategorySelectListener(new ProductScreenView.CategorySelectListener() {
            @Override
            public void onSelect(Category category) {
                mCategoryId = category.id;
                getData(true);
            }
        });

        mDatas = new ArrayList<>();
        mAdapter = new ProduchPushAdapter(mDatas, this);
        RvUtils.configRecycleView(this, mRvProduch, mAdapter);
        mRvProduch.addItemDecoration(new ListDividerDecoration(this));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        });
        mRvProduch.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PushCategoryProductListActivity.this, ProductPushDetailActivity.class);
                intent.putExtra(Config.INTENT_KEY_ID, mDatas.get(position).skuId);
                startActivity(intent);
            }
        });
        mLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(true);
            }
        });
    }

    private void getIntentData() {
        mId = getIntent().getStringExtra(Config.INTENT_KEY_ID);
        mTiTle = getIntent().getStringExtra(Config.INTENT_KEY_TYPE_NAME);
        mCategoryId = mId;
    }
}
