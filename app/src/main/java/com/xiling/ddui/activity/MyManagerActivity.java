package com.xiling.ddui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.MyManagerBean;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ClipboardUtil;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * pt
 * 我的经理人
 */
public class MyManagerActivity extends BaseActivity {

    INewUserService iNewUserService;

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_manager_head)
    ImageView ivManagerHead;
    @BindView(R.id.tv_manager_name)
    TextView tvManagerName;
    @BindView(R.id.tv_manager_phone)
    TextView tvManagerPhone;
    @BindView(R.id.tv_manager_weixin)
    TextView tvManagerWeixin;
    @BindView(R.id.tv_remarks)
    TextView tvRemarks;
    @BindView(R.id.tv_work_time1)
    TextView tvWorkTime1;
    @BindView(R.id.tv_work_time2)
    TextView tvWorkTime2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_manager);
        ButterKnife.bind(this);
        setTitle("我的经理");
        setLeftBlack();
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        getMyManager();
    }

    private void getMyManager() {
        APIManager.startRequest(iNewUserService.getMyManager(), new BaseRequestListener<MyManagerBean>() {

            @Override
            public void onSuccess(MyManagerBean result) {
                super.onSuccess(result);
                if (result != null) {
                    tvName.setText("经理：" + result.getSuperNickName());
                    tvPhone.setText(PhoneNumberUtil.getSecretPhoneNumber(result.getSuperPhone()));
                    GlideUtils.loadHead(context, ivHead, result.getSuperHeadImage());


                    switch (result.getAuthStatus()) {
                        case 0:
                        case 4:
                            tvRemarks.setText("请先在“我的-设置-账户认证”，认证实名后，才能免费开启专属管家服务");
                            tvWorkTime1.setVisibility(View.GONE);
                            tvWorkTime2.setVisibility(View.GONE);
                            break;
                        case 1:
                            tvRemarks.setText("您的账户认证正在加急审核中，通过后免费开启专属管家服务");
                            tvWorkTime1.setVisibility(View.GONE);
                            tvWorkTime2.setVisibility(View.GONE);
                            break;
                        case 2:
                            tvRemarks.setText("一站式解决各类基础问题、货品经销问题，精准匹配专业课程，帮助经销商快速成长");
                            tvWorkTime1.setVisibility(View.VISIBLE);
                            tvWorkTime2.setVisibility(View.VISIBLE);

                            GlideUtils.loadHead(context, ivManagerHead, result.getHeadImage());
                            if (!TextUtils.isEmpty(result.getName())) {
                                tvManagerName.setText(result.getName());
                            }

                            if (!TextUtils.isEmpty(result.getPhone())) {
                                tvManagerPhone.setText(result.getPhone());
                            }

                            if (!TextUtils.isEmpty(result.getWechatCode())) {
                                tvManagerWeixin.setText(result.getWechatCode());
                            }


                            break;

                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }

        });
    }


    @OnClick({R.id.tv_btn_call, R.id.tv_btn_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_call:
                if (!TextUtils.isEmpty(tvManagerPhone.getText().toString())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvManagerPhone.getText().toString()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    ToastUtil.error("暂无电话");
                }

                break;
            case R.id.tv_btn_copy:
                if (!TextUtils.isEmpty(tvManagerWeixin.getText().toString())) {
                    ClipboardUtil.setPrimaryClip(tvManagerWeixin.getText().toString());
                    ToastUtil.success("复制成功");
                } else {
                    ToastUtil.error("暂无微信");
                }
                break;
        }
    }
}
