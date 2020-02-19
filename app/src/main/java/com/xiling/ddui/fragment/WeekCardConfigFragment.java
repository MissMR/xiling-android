package com.xiling.ddui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.PrivilegeAdapter;
import com.xiling.shared.basic.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * pt
 * 周卡配置fragment
 */
public class WeekCardConfigFragment extends BaseFragment {
    int weekType;
    @BindView(R.id.ll_week_card_backgroud)
    LinearLayout llWeekCardBackgroud;
    @BindView(R.id.tv_week_price)
    TextView tvWeekPrice;
    @BindView(R.id.recycler_privilege)
    RecyclerView recyclerPrivilege;
    @BindView(R.id.tv_card)
    TextView tvCard;


    Unbinder unbinder;
    int weekPrice = 0;

    PrivilegeAdapter privilegeAdapter;
    List<String> privilegeList = new ArrayList<>();


    public static WeekCardConfigFragment newInstance(int weekType) {
        WeekCardConfigFragment fragment = new WeekCardConfigFragment();
        Bundle args = new Bundle();
        args.putInt("weekType", weekType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weekType = getArguments().getInt("weekType");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_card_config, container, false);
        unbinder = ButterKnife.bind(this, view);
        privilegeList.clear();
        switch (weekType) {
            case 1:
                //VIP
                weekPrice = 199;
                tvCard.setTextColor(Color.parseColor("#EB5F76"));
                llWeekCardBackgroud.setBackgroundResource(R.drawable.bg_week_card_vip);
                privilegeList.add("全场商品订货享受8.5折优惠起");
                privilegeList.add("平台品牌商品厂家直直供");
                privilegeList.add("专属客服一对一服务");
                privilegeList.add("可参与每周频道活动、部分节假日品牌专享价");


                break;
            case 2:
                // 黑卡
                weekPrice = 299;
                tvCard.setTextColor(Color.parseColor("#F09165"));
                llWeekCardBackgroud.setBackgroundResource(R.drawable.bg_week_card_black);
                privilegeList.add("全场商品订货享受8.5折优惠起");
                privilegeList.add("平台品牌商品厂家直直供");
                privilegeList.add("专属客服一对一服务");
                privilegeList.add("可参与每周频道活动、部分节假日品牌专享价");
                privilegeList.add("最新产品功能第一时间内侧体验权");
                privilegeList.add("爆款新品早知道");

                break;
        }

        recyclerPrivilege.setLayoutManager(new LinearLayoutManager(mContext));
        privilegeAdapter = new PrivilegeAdapter();
        privilegeAdapter.setNewData(privilegeList);
        recyclerPrivilege.setAdapter(privilegeAdapter);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_purchase)
    public void onViewClicked() {
    }


}
