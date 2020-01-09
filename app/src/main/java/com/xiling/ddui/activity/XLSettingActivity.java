package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xiling.R;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.bean.NewUserBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XLSettingActivity extends BaseActivity {

    NewUserBean userBean;
    @BindView(R.id.iv_head)
    ImageView ivHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlsetting);
        ButterKnife.bind(this);
        setTitle("设置");
        setLeftBlack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserManager.getInstance().isLogin()) {
            ivHead.setVisibility(View.VISIBLE);
            userBean = UserManager.getInstance().getUser();
            GlideUtils.loadHead(this, ivHead, userBean.getHeadImage());
        } else {
            ivHead.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.rel_personal_data, R.id.rel_real_name, R.id.rel_account, R.id.rel_password, R.id.rel_bank_card, R.id.rel_about_us, R.id.rel_clear_cache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_personal_data://个人资料
                break;
            case R.id.rel_real_name://实名认证
                break;
            case R.id.rel_account://账户管理
                break;
            case R.id.rel_password://交易密码
                startActivity(new Intent(context,TransactionPasswordActivity.class));
                break;
            case R.id.rel_bank_card://我的银行卡
                break;
            case R.id.rel_about_us: //关于我们
                break;
            case R.id.rel_clear_cache://清除缓存
                UserManager.getInstance().loginOut();
                finish();
                break;
        }
    }

    @Override
    protected boolean isNeedLogin() {
        return true;
    }
}
