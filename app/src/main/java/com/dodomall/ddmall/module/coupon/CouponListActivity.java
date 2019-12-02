package com.dodomall.ddmall.module.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.coupon.adapter.CouponListAdapter;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.bean.Coupon;
import com.dodomall.ddmall.shared.component.NoData;
import com.dodomall.ddmall.shared.constant.Key;
import com.dodomall.ddmall.shared.decoration.SpacesItemDecoration;
import com.dodomall.ddmall.shared.manager.PageManager;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.coupon
 * @since 2017-06-20
 */
public class CouponListActivity extends BaseActivity implements PageManager.RequestListener {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.confirmBtn)
    protected TextView mConfirmBtn;
    @BindView(R.id.moreBtn)
    protected TextView mMoreBtn;

    @BindView(R.id.noDataLayout)
    NoData noDataLayout;

    private CouponListAdapter mCouponListAdapter;
    private boolean isSelectCoupon = false;
    //    private ICouponService mCouponService;
    private PageManager mPageManager;
    private Coupon mCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list_layout);
        ButterKnife.bind(this);
        getIntentData();
        initData();
        initView();
    }

    private void initData() {
//        mCouponService = ServiceManager.getInstance().createService(ICouponService.class);
    }

    private void initView() {
        showHeader();
        setTitle(isSelectCoupon ? "选择优惠券" : "优惠券");

        mMoreBtn.setVisibility(isSelectCoupon ? View.GONE : View.VISIBLE);
        mConfirmBtn.setVisibility(!isSelectCoupon ? View.GONE : View.VISIBLE);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageManager.onRefresh();
            }
        });

        noDataLayout.setImgRes(R.mipmap.no_data_coupon);
        noDataLayout.setTextView("暂无优惠券");

        mCouponListAdapter = new CouponListAdapter(this, isSelectCoupon);
        mRecyclerView.setAdapter(mCouponListAdapter);
        try {
            mPageManager = PageManager.getInstance()
                    .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
                    .setRecyclerView(mRecyclerView)
                    .setItemDecoration(new SpacesItemDecoration(ConvertUtil.dip2px(15), true))
                    .setRequestListener(this);
            mPageManager.build(this);
        } catch (PageManager.PageManagerException e) {
            ToastUtil.error("PageManager 初始化失败");
        }
        if (isSelectCoupon) {
            mRefreshLayout.setEnabled(false);
        } else {
            mPageManager.setSwipeRefreshLayout(mRefreshLayout);
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            isSelectCoupon = false;
        } else if (intent.getExtras() == null) {
            isSelectCoupon = false;
        } else {
            String action = getIntent().getExtras().getString("action");
            mCoupon = (Coupon) getIntent().getExtras().get("coupon");
            isSelectCoupon = action != null && action.equals(Key.SELECT_COUPON);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPageManager.onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void nextPage(final int page) {
        if (isSelectCoupon) {
            getSelectData(page);
        } else {
            getAllData(page);
        }

    }

    private void getAllData(final int page) {

        //屏蔽老的优惠券界面的协议请求

        mPageManager.setLoading(false);
        mRefreshLayout.setRefreshing(false);

//        APIManager.startRequest(mCouponService.getMyCouponList(page), new BaseRequestListener<PaginationEntity<Coupon, Object>>(mRefreshLayout) {
//
//            @Override
//            public void onSuccess(PaginationEntity<Coupon, Object> result) {
//                if (page == 1) {
//                    mCouponListAdapter.removeAllItems();
//                }
//                mPageManager.setLoading(false);
//                mPageManager.setTotalPage(result.totalPage);
//                mCouponListAdapter.addItems(result.list);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                mPageManager.setLoading(false);
//                mRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onComplete() {
//                super.onComplete();
//                mPageManager.setLoading(false);
//                mRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    private void getSelectData(int page) {
//        HashMap<String, Integer> products = (HashMap<String, Integer>) getIntent().getExtras().get("products");
//        if (products == null) {
//            return;
//        }
//        HashMap<String, Object> params = new HashMap<>();
//        List<SkuAmount> skuAmounts = new ArrayList<>();
//        for (String skuId : products.keySet()) {
//            skuAmounts.add(new SkuAmount(skuId, products.get(skuId)));
//        }
//        params.put("products", skuAmounts);
//        APIManager.startRequest(mCouponService.getCouponListForOrder(APIManager.buildJsonBody(params)), new BaseRequestListener<List<Coupon>>(mRefreshLayout) {
//            @Override
//            public void onSuccess(List<Coupon> result) {
//                mPageManager.setLoading(false);
//                mPageManager.setTotalPage(1);
//                if (mCoupon != null) {
//                    for (Coupon coupon : result) {
//                        coupon.isSelected = mCoupon.couponId.equalsIgnoreCase(coupon.couponId);
//                    }
//                }
//                mCouponListAdapter.setItems(result);
//            }
//        });
    }

    @OnClick(R.id.confirmBtn)
    protected void selectCoupon() {
        Coupon couponSelected = null;
        for (Coupon coupon : mCouponListAdapter.getItems()) {
            if (coupon.isSelected) {
                couponSelected = coupon;
                break;
            }
        }

        Intent intent = getIntent();
        intent.putExtra("coupon", couponSelected);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.moreBtn)
    protected void viewCouponCenter() {
        startActivity(new Intent(CouponListActivity.this, CouponCenterActivity.class));
    }
}

