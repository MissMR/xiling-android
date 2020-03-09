package com.xiling.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.ddui.adapter.CouponAdapter;
import com.xiling.ddui.bean.CouponListBean;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.NoData;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICouponService;
import com.xiling.shared.util.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xiling.shared.Constants.PAGE_SIZE;

/**
 * @auth 逄涛
 * 优惠券列表
 */
public class XLCouponFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    private static final String STATUS = "status";
    int status = 2;
    @BindView(R.id.recycler_coupon)
    RecyclerView recyclerCoupon;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;
    CouponAdapter couponAdapter;
    ICouponService iCouponService;
    int pageOffset = 1;
    int totalPage;
    @BindView(R.id.noDataLayout)
    NoData noDataLayout;

    public static XLCouponFragment newInstance(int status) {
        XLCouponFragment fragment = new XLCouponFragment();
        Bundle args = new Bundle();
        args.putInt(STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getInt(STATUS);
        }
        iCouponService = ServiceManager.getInstance().createService(ICouponService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xlcoupon, container, false);
        unbinder = ButterKnife.bind(this, view);
        noDataLayout.setTextView("还没有优惠券");
        recyclerCoupon.setLayoutManager(new LinearLayoutManager(mContext));
        couponAdapter = new CouponAdapter(false);
        couponAdapter.setStatus(status);
        recyclerCoupon.setAdapter(couponAdapter);

        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);

        initData();
        return view;
    }

    private void initData() {
        HashMap<String, Object> map = new HashMap();
        map.put("status", status);
        map.put("pageOffset", pageOffset);
        map.put("pageSize", PAGE_SIZE);
        APIManager.startRequest(iCouponService.getCouponListPage(status + "", pageOffset + "", PAGE_SIZE + ""), new BaseRequestListener<CouponListBean>() {

            @Override
            public void onSuccess(CouponListBean result) {
                super.onSuccess(result);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();

                if (result != null) {
                    if (result.getRecords() != null) {
                        totalPage = result.getPages();
                        // 如果已经到最后一页了，关闭上拉加载
                        if (pageOffset >= totalPage) {
                            smartRefreshLayout.setEnableLoadMore(false);
                        } else {
                            smartRefreshLayout.setEnableLoadMore(true);
                        }

                        if (pageOffset == 1) {
                            couponAdapter.setNewData(result.getRecords());
                            if (result.getRecords().size() == 0) {
                                noDataLayout.setVisibility(View.VISIBLE);
                                recyclerCoupon.setVisibility(View.GONE);
                            } else {
                                noDataLayout.setVisibility(View.GONE);
                                recyclerCoupon.setVisibility(View.VISIBLE);
                            }
                        } else {
                            couponAdapter.addData(result.getRecords());
                        }
                    }
                }


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();

                ToastUtil.error(e.getMessage());
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //上拉加载
        if (pageOffset < totalPage) {
            pageOffset++;
            initData();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //下拉刷新
        pageOffset = 1;
        initData();
    }
}
