package com.xiling.ddmall.module.push;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.bean.api.PaginationEntity;
import com.xiling.ddmall.shared.decoration.ListDividerDecoration;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IProductService;
import com.xiling.ddmall.shared.util.RvUtils;

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
public class PushHotProductListActivityActivity extends BaseActivity {

    @BindView(R.id.rvProduch)
    RecyclerView mRvProduch;
    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout mLayoutRefresh;
    @BindView(R.id.screenView)
    ProductScreenView mScreenView;

    private int mType;
    public final static int TYPE_REXIAO = 1;
    public final static int TYPE_ZHUTUI = 2;
    public final static int TYPE_ZHUANDE = 3;
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
        APIManager.startRequest(
                mService.getPushHotProductList(mUrl, Constants.PAGE_SIZE, mDatas.size() / Constants.PAGE_SIZE + 1, mSort),
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
                Intent intent = new Intent(PushHotProductListActivityActivity.this, ProductPushDetailActivity.class);
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
        mType = getIntent().getIntExtra(Config.INTENT_KEY_TYPE_NAME, 0);
        switch (mType) {
            case TYPE_REXIAO:
                setTitle("热销商品");
                mUrl = "productPush/skuSaleList";
                break;
            case TYPE_ZHUTUI:
                setTitle("主推商品");
                mUrl = "productPush/skuPushList";
                mScreenView.setVisibility(View.VISIBLE);
                mScreenView.setShowModel(ProductScreenView.SHOW_SORT);
                mScreenView.setSortLisnener(new ProductScreenView.SortChangeLisnener() {
                    @Override
                    public void onChange(int sort) {
                        mSort = sort;
                        getData(true);
                    }
                });
                break;
            case TYPE_ZHUANDE:
                setTitle("赚的最多");
                mUrl = "productPush/skuMaxPriceList";
                break;
                default:
        }
    }
}
