package com.dodomall.ddmall.module.coupon;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.coupon.adapter.CouponCenterAdapter;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Coupon;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.decoration.SpacesItemDecoration;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.PageManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICouponService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.coupon
 * @since 2017-07-04
 */
public class CouponCenterActivity extends BaseActivity implements PageManager.RequestListener {
    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    private CouponCenterAdapter mCouponCenterAdapter;
    private ICouponService mCouponService;
    private PageManager mPageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_list_layout);
        ButterKnife.bind(this);
        showHeader();
        setTitle("领券中心");
        mCouponService = ServiceManager.getInstance().createService(ICouponService.class);
        mCouponCenterAdapter = new CouponCenterAdapter(this);
        mRecyclerView.setAdapter(mCouponCenterAdapter);
        try {
            mPageManager = PageManager.getInstance()
                    .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
                    .setRecyclerView(mRecyclerView)
                    .setItemDecoration(new SpacesItemDecoration(ConvertUtil.dip2px(15), true))
                    .setRequestListener(this)
                    .setSwipeRefreshLayout(mRefreshLayout)
                    .build(this);
        } catch (PageManager.PageManagerException e) {
            ToastUtil.error("PageManager 初始化失败");
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
        APIManager.startRequest(mCouponService.getPlatformCouponList(page), new BaseRequestListener<PaginationEntity<Coupon, Object>>(mRefreshLayout) {

            @Override
            public void onSuccess(PaginationEntity<Coupon, Object> result) {
                if (page == 1) {
                    mCouponCenterAdapter.removeAllItems();
                }
                mPageManager.setLoading(false);
                mPageManager.setTotalPage(result.totalPage);
                mCouponCenterAdapter.addItems(result.list);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mPageManager.setLoading(false);
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mPageManager.setLoading(false);
                mRefreshLayout.setRefreshing(false);
            }
        });
    }
}
