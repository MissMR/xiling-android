package com.xiling.ddui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.ddui.activity.OrderListActivit;
import com.xiling.ddui.activity.XLSettingActivity;
import com.xiling.ddui.adapter.MineServiceAdapter;
import com.xiling.ddui.bean.UserCostomBean;
import com.xiling.ddui.bean.UserInComeBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.address.AddressListActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.OrderCount;
import com.xiling.shared.component.ItemWithIcon;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.contract.IOrderService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.xiling.ddui.fragment.OrderFragment.ORDER_STATUS;
import static com.xiling.shared.Constants.ORDER_IS_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.Constants.ORDER_WAIT_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;

/**
 * A simple {@link Fragment} subclass.
 */
public class XLMineFragment extends BaseFragment implements OnRefreshListener {

    INewUserService iNewUserService;
    IOrderService mOrderService;

    Unbinder unbinder;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.avatarIv)
    ImageView avatarIv;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_user_ordinary)
    ImageView ivUserOrdinary;
    @BindView(R.id.ll_user_vip)
    LinearLayout llUserVip;
    @BindView(R.id.rel_custom)
    RelativeLayout relCustom;
    @BindView(R.id.recycler_service)
    RecyclerView recyclerService;
    MineServiceAdapter serviceAdapter;
    List<MineServiceAdapter.ServiceBean> serviceBeanList = new ArrayList<>();
    @BindView(R.id.tv_incomeDay)
    TextView tvIncomeDay;
    @BindView(R.id.tv_incomeMonth)
    TextView tvIncomeMonth;
    @BindView(R.id.tv_incomeTotal)
    TextView tvIncomeTotal;
    @BindView(R.id.tv_custom)
    TextView tvCustom;
    @BindView(R.id.tv_custom_order)
    TextView tvCustomOrder;
    @BindView(R.id.orider_wait_pay)
    ItemWithIcon oriderWaitPay;
    @BindView(R.id.orider_wait_ship)
    ItemWithIcon oriderWaitShip;
    @BindView(R.id.orider_wait_received)
    ItemWithIcon oriderWaitReceived;
    @BindView(R.id.orider_closed)
    ItemWithIcon oriderClosed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xl_mine, null, false);
        unbinder = ButterKnife.bind(this, view);
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        initView();
        requestUserInfo();
        return view;
    }

    private void initView() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);

        recyclerService.setLayoutManager(new GridLayoutManager(mContext, 4));
        serviceAdapter = new MineServiceAdapter();
        recyclerService.setAdapter(serviceAdapter);

        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_address, "管理地址"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_coupon, "优惠券"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_policy, "政策"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_help, "帮助与客服"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_rule, "规则中心"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_activite, "活动报名"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.drawable.icon_paihang, "排行榜"));

        serviceAdapter.setNewData(serviceBeanList);

        serviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0: // 管理地址
                        Intent intent = new Intent(mContext, AddressListActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            requestUserInfo();
        }
    }

    /**
     * 获取用户信息
     */
    private void requestUserInfo() {
        getOrderCount();
        UserManager.getInstance().checkUserInfo(new UserManager.OnCheckUserInfoLisense() {
            @Override
            public void onCheckUserInfoSucess(NewUserBean newUserBean) {
                refreshLayout.finishRefresh();
                if (newUserBean != null) {
                    GlideUtils.loadHead(mContext, avatarIv, newUserBean.getHeadImage());
                    tvName.setText(newUserBean.getNickName());
                    tvPhone.setText(newUserBean.getPhone());
                    if (newUserBean.getRole().getRoleLevel() == 10) {
                        //普通会员
                        ivUserOrdinary.setVisibility(View.VISIBLE);
                        llUserVip.setVisibility(View.GONE);
                        relCustom.setVisibility(View.GONE);
                    } else {
                        // 高级会员
                        ivUserOrdinary.setVisibility(View.GONE);
                        llUserVip.setVisibility(View.VISIBLE);
                        relCustom.setVisibility(View.VISIBLE);
                        requestVipData();
                    }


                }
            }

            @Override
            public void onCheckUserInfoFail() {
                refreshLayout.finishRefresh();
            }
        });
    }

    /**
     * 获取订单状态数量
     */
    private void getOrderCount() {
        APIManager.startRequest(mOrderService.getOrderCount(), new BaseRequestListener<List<OrderCount>>() {

            @Override
            public void onSuccess(List<OrderCount> result) {
                super.onSuccess(result);

                for (OrderCount orderCount : result) {
                    switch (orderCount.getOrderStatus()) {
                        case 10:
                            oriderWaitPay.setBadge(orderCount.getOrderCount());
                            break;
                        case 20:
                            oriderWaitShip.setBadge(orderCount.getOrderCount());
                            break;
                        case 30:
                            oriderWaitReceived.setBadge(orderCount.getOrderCount());
                            break;
                    }
                }


            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }


        });
    }

    private void requestVipData() {
        APIManager.startRequest(iNewUserService.getUserIncome(), new BaseRequestListener<UserInComeBean>() {
            @Override
            public void onSuccess(UserInComeBean result) {
                super.onSuccess(result);
                if (result != null) {
                    tvIncomeDay.setText(NumberHandler.reservedDecimalFor2(result.getIncomeDay()));
                    tvIncomeMonth.setText(NumberHandler.reservedDecimalFor2(result.getIncomeMonth()));
                    tvIncomeTotal.setText(NumberHandler.reservedDecimalFor2(result.getIncomeTotal()));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        });


        APIManager.startRequest(iNewUserService.getMyCostom(), new BaseRequestListener<UserCostomBean>() {
            @Override
            public void onSuccess(UserCostomBean result) {
                super.onSuccess(result);
                if (result != null) {
                    tvCustom.setText(result.getClientCount() + "");
                    tvCustomOrder.setText(result.getClientOrderCount() + "");
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        });

    }


    @Override
    protected boolean isNeedLogin() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestUserInfo();
    }

    @OnClick({R.id.iv_setting, R.id.orider_wait_pay, R.id.orider_wait_ship, R.id.orider_wait_received, R.id.orider_closed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                startActivity(new Intent(mContext, XLSettingActivity.class));
                break;
            case R.id.orider_wait_pay:
                jumpOrderList(ORDER_WAIT_PAY);
                break;
            case R.id.orider_wait_ship:
                jumpOrderList(ORDER_WAIT_SHIP);
                break;
            case R.id.orider_wait_received:
                jumpOrderList(ORDER_WAIT_RECEIVED);
                break;
            case R.id.orider_closed:
                jumpOrderList(ORDER_IS_RECEIVED);
                break;
        }
    }

    private void jumpOrderList(String status) {
        Intent intent = new Intent(mContext, OrderListActivit.class);
        intent.putExtra(ORDER_STATUS, status);
        startActivity(intent);
    }


}
