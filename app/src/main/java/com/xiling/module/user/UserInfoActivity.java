package com.xiling.module.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.activity.EditWechatQrCodeActivity;
import com.xiling.ddui.bean.UserInfoBean;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ClipboardUtil;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhihu.matisse.internal.ui.widget.SquareFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/12/8
 * 我的分享人  粉丝详情
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.sdv_avatar)
    SimpleDraweeView sdvAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_wechat_account)
    TextView tvWechatAccount;
    @BindView(R.id.iv_qr_code)
    SimpleDraweeView ivQrCode;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_btn_copy_wechat)
    TextView tvBtnCopyWechat;
    @BindView(R.id.fl_qr_code_container)
    SquareFrameLayout flQrCodeContainer;

    private String mQrCodeURL;
    private String mUserId;
    private String mWechatPlaceholder = "小主，您的%s还未填写微信号哦~";
    private String mWechatQrCodePlaceholder = "小主，您的%s还未上传微信二维码哦~";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        ButterKnife.bind(this);
        mUserId = getIntent().getStringExtra(Constants.Extras.USER_ID);
        showHeader(TextUtils.isEmpty(mUserId) ? "我的分享人" : "我的粉丝");
        initData();
    }

    private void initData() {
        IUserService service = ServiceManager.getInstance().createService(IUserService.class);
        Observable observable = TextUtils.isEmpty(mUserId) ? service.getReferrerInfo() : service.getUserInfo(mUserId);
        APIManager.startRequest(observable, new BaseRequestListener<UserInfoBean>(this) {
            @Override
            public void onSuccess(UserInfoBean result) {
                updateUserInfo(result);
                mQrCodeURL = result.getWechatCode();
            }
        });
    }

    private void updateUserInfo(UserInfoBean result) {
        sdvAvatar.setImageURI(result.getHeadImage());
        tvNickname.setText(result.getNickName());
        tvPhoneNumber.setText(result.getPhone());
        if (TextUtils.isEmpty(result.getWechat())) {
            tvBtnCopyWechat.setVisibility(View.GONE);
            tvWechatAccount.setText(String.format(mWechatPlaceholder, TextUtils.isEmpty(mUserId) ? "分享人" : "粉丝"));
        } else {
            tvWechatAccount.setText(result.getWechat());
        }
        if (TextUtils.isEmpty(result.getWechatCode())) {
            tvHint.setText(String.format(mWechatQrCodePlaceholder, TextUtils.isEmpty(mUserId) ? "分享人" : "粉丝"));
            flQrCodeContainer.setBackground(getDrawable(R.drawable.white15_radius_middle));
        } else {
            ivQrCode.setImageURI(result.getWechatCode());
            flQrCodeContainer.setBackground(getDrawable(R.drawable.white_radius_8));
        }
        tvHint.setVisibility(TextUtils.isEmpty(result.getWechatCode()) ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.tv_btn_call, R.id.tv_btn_copy_wechat, R.id.tv_btn_set_my_qr_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhoneNumber.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.tv_btn_copy_wechat:
                ClipboardUtil.setPrimaryClip(tvWechatAccount.getText().toString());
                ToastUtil.success("复制成功");
                break;
            case R.id.tv_btn_set_my_qr_code:
                // 上传我的二维码
                startActivity(new Intent(this, EditWechatQrCodeActivity.class));
                break;
        }
    }
}
