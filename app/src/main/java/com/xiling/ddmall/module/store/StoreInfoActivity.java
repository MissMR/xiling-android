package com.xiling.ddmall.module.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.bean.MemberStore;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.SessionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StoreInfoActivity extends BaseActivity {

    @BindView(R.id.ivAvatar)
    SimpleDraweeView mIvAvatar;
    @BindView(R.id.avatarLayout)
    LinearLayout mAvatarLayout;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvAddress)
    TextView mTvAddress;
    @BindView(R.id.tvContacts)
    TextView mTvContacts;
    @BindView(R.id.tvContactsPhone)
    TextView mTvContactsPhone;
    @BindView(R.id.tvExpress)
    TextView mTvExpress;
    @BindView(R.id.tvShipAddress)
    TextView mTvShipAddress;
    @BindView(R.id.tvExpressScoreSetting)
    TextView mTvExpressScoreSetting;
    @BindView(R.id.ivQrCode)
    SimpleDraweeView mIvQrCode;
    @BindView(R.id.tvShipType)
    TextView mTvShipType;
    @BindView(R.id.layoutExpressSetting)
    LinearLayout mLayoutExpressSetting;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    @BindView(R.id.layoutShipType)
    LinearLayout mLayoutShipType;
    private MemberStore mMemberStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        ButterKnife.bind(this);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        setTitle("店铺配置");
        setLeftBlack();
        getHeaderLayout().setRightText("修改");
        getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    private void setData() {
        FrescoUtil.setImageSmall(mIvAvatar, mMemberStore.thumbUrl);
        mTvName.setText(mMemberStore.storeName);
        mTvAddress.setText(mMemberStore.province + mMemberStore.city + mMemberStore.district + mMemberStore.address);
        mTvContacts.setText(mMemberStore.contact);
        mTvContactsPhone.setText(mMemberStore.phone);
        mTvExpress.setText(mMemberStore.expressName);
        mTvShipAddress.setText(mMemberStore.shipAddress);
        FrescoUtil.setImageSmall(mIvQrCode, mMemberStore.wxQrCode);
        User loginUser = SessionUtil.getInstance().getLoginUser();
        if (loginUser.storeType == 3 || loginUser.storeType == 4) {
            mTvShipType.setText(mMemberStore.shipType != 2 ? "自己发货" : "上级代发");
            mLayoutShipType.setVisibility(View.VISIBLE);
        } else {
            mLayoutShipType.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tvSubmit)
    public void onSubmit() {
        finish();
        startActivity(new Intent(this, StoreSettingActivity.class));
    }

    @OnClick(R.id.layoutExpressSetting)
    public void onExpressSetting() {
        startActivity(new Intent(this, StoreExpressSettingActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMsg(MsgStore msgStore) {
        switch (msgStore.getAction()) {
            case MsgStore.ACTION_SEND_STORE_OBJ:
                mMemberStore = msgStore.getMemberStore();
                setData();
                break;
            default:
        }
    }


}
