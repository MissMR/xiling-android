package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.xiling.R;
import com.xiling.ddui.config.H5UrlConfig;
import com.xiling.ddui.tools.GlideEngine;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 设置页面
 */
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
                startActivity(new Intent(context, PersonalDataActivity.class));
                break;
            case R.id.rel_real_name://实名认证
                startActivity(new Intent(context, RealAuthActivity.class));
                break;
            case R.id.rel_account://账户管理
                startActivity(new Intent(context, XLAccountManagerActivity.class));
                break;
            case R.id.rel_password://交易密码
                startActivity(new Intent(context, TransactionPasswordActivity.class));
                break;
            case R.id.rel_bank_card://我的银行卡
                startActivity(new Intent(context, MyBankCardActivity.class));
                break;
            case R.id.rel_about_us: //关于我们
                WebViewActivity.jumpUrl(context, "关于我们", H5UrlConfig.ABOUT_US);
                break;
            case R.id.rel_clear_cache://清除缓存
               GlideUtils.clearImageCache(context);
                ToastUtil.showSuccessToast(context,"清除成功");

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdata(EventMessage message) {
        switch (message.getEvent()) {
            case LOGIN_OUT:
                finish();
                break;
            case UPDATE_AVATAR:
                GlideUtils.loadHead(this, ivHead, (String) message.getData());
                break;
        }
    }


    @Override
    protected boolean isNeedLogin() {
        return true;
    }
}
