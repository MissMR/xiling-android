package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.AuthListAdapter;
import com.xiling.ddui.bean.RealAuthBean;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实名认证
 */
public class RealAuthActivity extends BaseActivity {
    INewUserService mUserService;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_set)
    TextView btnSet;
    @BindView(R.id.empty_view)
    RelativeLayout emptyView;
    @BindView(R.id.recycler_auth)
    RecyclerView recyclerAuth;
    @BindView(R.id.rel_list)
    LinearLayout relList;
    AuthListAdapter authListAdapter;

    @BindView(R.id.rel_need_auth)
    RelativeLayout relNeedAuth;
    @BindView(R.id.iv_select_net)
    ImageView ivSelectNet;
    @BindView(R.id.btn_auth_net)
    RelativeLayout btnAuthNet;
    @BindView(R.id.iv_select_entity)
    ImageView ivSelectEntity;
    @BindView(R.id.btn_auth_entity)
    RelativeLayout btnAuthEntity;
    @BindView(R.id.iv_select_wechat)
    ImageView ivSelectWechat;
    @BindView(R.id.btn_auth_wechat)
    RelativeLayout btnAuthWechat;

    //店铺平台类型（1网站店铺 2实体门店 3微商代购）
    private int storeType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_auth);
        ButterKnife.bind(this);
        setTitle("实名认证");
        setLeftBlack();
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        getAuth();
    }

    /**
     * 获取实名认证信息
     */
    private void getAuth() {
        APIManager.startRequest(mUserService.getAuth(), new BaseRequestListener<RealAuthBean>() {
            @Override
            public void onSuccess(RealAuthBean result) {
                super.onSuccess(result);
                NewUserBean userBean = UserManager.getInstance().getUser();
                userBean.setAuthStatus(result.getAuthStatus());
                UserManager.getInstance().setUser(userBean);
                //认证状态（0，未认证，1，认证申请，2，认证通过，4，认证拒绝）
                switch (result.getAuthStatus()) {
                    case 0:
                        emptyView.setVisibility(View.GONE);
                        relNeedAuth.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        relNeedAuth.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        btnSet.setVisibility(View.GONE);
                        tvTitle.setText("周一至周五9:00-17:00（工作时\n间）提交将在2小时内审核完毕,\n非工作时间将顺延至工作时间\n处理，请耐心等待，喜领客服稍\n后将致电与您验证商户信息注意\n接听 ");
                        break;
                    case 2:
                        List<RealAuthBean> realAuthBeanList = new ArrayList<>();
                        realAuthBeanList.add(result);
                        relList.setVisibility(View.VISIBLE);
                        recyclerAuth.setLayoutManager(new LinearLayoutManager(context));
                        authListAdapter = new AuthListAdapter(realAuthBeanList);
                        recyclerAuth.setAdapter(authListAdapter);
                        break;
                    case 4:
                        emptyView.setVisibility(View.VISIBLE);
                        btnSet.setVisibility(View.VISIBLE);
                        tvTitle.setText("您的实名认证信息审核失败\n填写的身份证号与身份证的证件号不匹配 ");
                        btnSet.setText("重新提交实名认证");
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.REAL_AUTH_SUCCESS)) {
            getAuth();
        }
    }

    @OnClick({R.id.btn_auth_net, R.id.btn_auth_entity, R.id.btn_auth_wechat, R.id.btn_set, R.id.btn_go_auth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_auth_net:
                //网络店铺认证
                if (storeType == 1) {
                    return;
                }

                btnAuthNet.setBackgroundResource(R.drawable.bg_auth_select);
                ivSelectNet.setBackgroundResource(R.drawable.icon_auth_select);
                switch (storeType) {
                    case 2:
                        btnAuthEntity.setBackgroundResource(R.drawable.bg_auth_unselect);
                        ivSelectEntity.setBackgroundResource(R.drawable.icon_auth_unselect);
                        break;
                    case 3:
                        btnAuthWechat.setBackgroundResource(R.drawable.bg_auth_unselect);
                        ivSelectWechat.setBackgroundResource(R.drawable.icon_auth_unselect);
                        break;
                }
                storeType = 1;
                break;
            case R.id.btn_auth_entity:
                //实体门店认证
                if (storeType == 2) {
                    return;
                }
                btnAuthEntity.setBackgroundResource(R.drawable.bg_auth_select);
                ivSelectEntity.setBackgroundResource(R.drawable.icon_auth_select);
                switch (storeType) {
                    case 1:
                        btnAuthNet.setBackgroundResource(R.drawable.bg_auth_unselect);
                        ivSelectNet.setBackgroundResource(R.drawable.icon_auth_unselect);
                        break;
                    case 3:
                        btnAuthWechat.setBackgroundResource(R.drawable.bg_auth_unselect);
                        ivSelectWechat.setBackgroundResource(R.drawable.icon_auth_unselect);
                        break;
                }
                storeType = 2;
                break;
            case R.id.btn_auth_wechat:
                //微商代购认证
                if (storeType == 3) {
                    return;
                }
                btnAuthWechat.setBackgroundResource(R.drawable.bg_auth_select);
                ivSelectWechat.setBackgroundResource(R.drawable.icon_auth_select);
                switch (storeType) {
                    case 1:
                        btnAuthNet.setBackgroundResource(R.drawable.bg_auth_unselect);
                        ivSelectNet.setBackgroundResource(R.drawable.icon_auth_unselect);
                        break;
                    case 2:
                        btnAuthEntity.setBackgroundResource(R.drawable.bg_auth_unselect);
                        ivSelectEntity.setBackgroundResource(R.drawable.icon_auth_unselect);
                        break;
                }
                storeType = 3;
                break;
            case R.id.btn_set:
                //重新认证
                emptyView.setVisibility(View.GONE);
                relNeedAuth.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_go_auth:
                //去认证
                switch (storeType) {
                    case 1:
                        startActivity(new Intent(context, RealAuthenticationNetActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(context, RealAuthenticationEntityActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(context, RealAuthenticationWeChatActivity.class));
                        break;
                }
                break;
        }
    }
}
