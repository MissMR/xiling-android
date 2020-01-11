package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.AuthListAdapter;
import com.xiling.ddui.bean.RealAuthBean;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Array;
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
     * 校验有没有密码
     */
    private void getAuth() {
        APIManager.startRequest(mUserService.getAuth(), new BaseRequestListener<RealAuthBean>() {
            @Override
            public void onSuccess(RealAuthBean result) {
                super.onSuccess(result);
                //认证状态（0，未认证，1，认证申请，2，认证通过，4，认证拒绝）
                switch (result.getAuthStatus()) {
                    case 0:
                        emptyView.setVisibility(View.VISIBLE);
                        btnSet.setVisibility(View.VISIBLE);
                        tvTitle.setText("您还没有添加实名认证信息");
                        btnSet.setText("添加实名认证");
                        break;
                    case 1:
                        emptyView.setVisibility(View.VISIBLE);
                        btnSet.setVisibility(View.GONE);
                        tvTitle.setText("周一至周五9:00-17:00（工作时 间）提交将在2小时内审核完毕， 非工作时间将顺延至工作时间 处理，请耐心等待，喜领客服稍 微将致电与您验证商户信息注意 接听 ");
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
                        tvTitle.setText("您的实名认证信息审核失败 填写的身份证号与身份证的证件号不匹配 ");
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

    @OnClick(R.id.btn_set)
    public void onViewClicked() {
        startActivity(new Intent(this, IdentificationUploadActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.REAL_AUTH_SUCCESS)) {
            getAuth();
        }
    }

}
