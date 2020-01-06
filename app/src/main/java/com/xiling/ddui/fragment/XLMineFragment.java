package com.xiling.ddui.fragment;


import android.app.Fragment;
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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.ddui.adapter.MineServiceAdapter;
import com.xiling.ddui.bean.UserCostomBean;
import com.xiling.ddui.bean.UserInComeBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class XLMineFragment extends BaseFragment implements OnRefreshListener {


    Unbinder unbinder;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout refreshLayout;

    INewUserService iNewUserService;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xl_mine, null, false);
        unbinder = ButterKnife.bind(this, view);
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
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

        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.mipmap.icon_address, "管理地址"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.mipmap.icon_coupon, "优惠券"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.mipmap.icon_policy, "政策"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.mipmap.icon_help, "帮助与客服"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.mipmap.icon_rule, "规则中心"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.mipmap.icon_activite, "活动报名"));
        serviceBeanList.add(new MineServiceAdapter.ServiceBean(R.mipmap.icon_paihang, "排行榜"));

        serviceAdapter.setNewData(serviceBeanList);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            requestUserInfo();
        }
    }

    /**
     * 获取用户信息
     */
    private void requestUserInfo() {
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
}
