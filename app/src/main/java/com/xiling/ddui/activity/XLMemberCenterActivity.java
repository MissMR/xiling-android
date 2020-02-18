package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiling.R;
import com.xiling.ddui.adapter.MackAdapter;
import com.xiling.ddui.adapter.PrivilegeAdapter;
import com.xiling.ddui.bean.MemberCenterInfo;
import com.xiling.ddui.service.IMemberService;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * pt
 * 会员中心
 */
public class XLMemberCenterActivity extends BaseActivity {
    IMemberService iMemberService;
    @BindView(R.id.recycler_make)
    RecyclerView recyclerMake;
    MackAdapter mackAdapter;
    List<MackAdapter.MackBean> mackBeanList = new ArrayList<>();

    @BindView(R.id.recycler_privilege)
    RecyclerView recyclerPrivilege;
    PrivilegeAdapter privilegeAdapter;
    List<PrivilegeAdapter.PrivilegeBean> privilegeBeanList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlmember_center);
        ButterKnife.bind(this);
        makeStatusBarTranslucent();
        iMemberService = ServiceManager.getInstance().createService(IMemberService.class);
        initView();
        getMemberCenterInfo();
    }


    private void initView() {
        mackAdapter = new MackAdapter();
        recyclerMake.setLayoutManager(new LinearLayoutManager(context));
        recyclerMake.setAdapter(mackAdapter);

        privilegeAdapter = new PrivilegeAdapter();
        recyclerPrivilege.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerPrivilege.setAdapter(privilegeAdapter);

        mackBeanList.add(new MackAdapter.MackBean(R.drawable.icon_invitation, "邀请好友成为VIP会员", "邀请好友、兑换好礼", "去邀请"));
        mackBeanList.add(new MackAdapter.MackBean(R.drawable.icon_invitation, "邀请好友成为黑卡会员", "多档优惠低至5.5折", "去邀请"));
        mackBeanList.add(new MackAdapter.MackBean(R.drawable.icon_place_order, "下单订货赚成长值", "350元=1成长值", "去订货"));
        mackAdapter.setNewData(mackBeanList);

        privilegeBeanList.add(new PrivilegeAdapter.PrivilegeBean(R.drawable.icon_privilege_brand, "品牌直供", "正品保证"));
        privilegeBeanList.add(new PrivilegeAdapter.PrivilegeBean(R.drawable.icon_privilege_discount, "专享折扣", "终身享受优惠"));
        privilegeBeanList.add(new PrivilegeAdapter.PrivilegeBean(R.drawable.icon_privilege_keeper, "专属管家", "一对一服务"));
        privilegeBeanList.add(new PrivilegeAdapter.PrivilegeBean(R.drawable.icon_privilege_events, "专场活动", "热销货品"));
        privilegeAdapter.setNewData(privilegeBeanList);

    }

    /**
     * 获取会员中心信息
     */
    private void getMemberCenterInfo() {
        //   ToastUtil.showLoading(this);
        UserManager.getInstance().checkUserInfo(new UserManager.OnCheckUserInfoLisense() {
            @Override
            public void onCheckUserInfoSucess(NewUserBean newUserBean) {
                APIManager.startRequest(iMemberService.getCenterInfo(), new BaseRequestListener<MemberCenterInfo>() {
                    @Override
                    public void onSuccess(MemberCenterInfo result) {
                        super.onSuccess(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.error(e.getMessage());
                    }
                });
            }

            @Override
            public void onCheckUserInfoFail() {

            }
        });


    }


}
