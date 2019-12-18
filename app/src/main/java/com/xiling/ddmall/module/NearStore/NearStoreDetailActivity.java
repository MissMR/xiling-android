package com.xiling.ddmall.module.NearStore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.NearStore.model.NearStoreModel;
import com.xiling.ddmall.module.auth.event.MsgStatus;
import com.xiling.ddmall.module.user.NewRegisterActivity;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.RuleIntro;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.SessionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NearStoreDetailActivity extends BaseActivity {

    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.ivAvatar)
    SimpleDraweeView mIvAvatar;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.tvLocation)
    TextView mTvLocation;
    @BindView(R.id.btnSubmit)
    TextView mBtnSubmit;
    @BindView(R.id.layoutImages)
    LinearLayout mLayoutImages;
    @BindView(R.id.layoutAvatar)
    FrameLayout mLayoutAvatar;
    @BindView(R.id.layoutTop)
    RelativeLayout mLayoutTop;
    private NearStoreModel.DatasEntity mData;
    private boolean mIsSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_store_detail);
        QMUIStatusBarHelper.translucent(this);
//        setBarPadingHeight(QMUIStatusBarHelper.getStatusbarHeight(this));
//        setBarPadingColor(getResources().getColor(R.color.red));

        ButterKnife.bind(this);
        mLayoutTop.setPadding(0,QMUIStatusBarHelper.getStatusbarHeight(this),0,0);
        getIntentData();
        getData();
        setData();
        if (SessionUtil.getInstance().isLogin()) {
            mBtnSubmit.setVisibility(View.GONE);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getData() {
        IUserService service = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(
                service.getRuleIntro(),
                new BaseRequestListener<List<RuleIntro>>(this) {
                    @Override
                    public void onSuccess(List<RuleIntro> result) {
                        for (RuleIntro ruleIntro : result) {
                            mLayoutImages.addView(getImageView(ruleIntro));
                        }
                    }
                }
        );
    }

    private SimpleDraweeView getImageView(RuleIntro ruleIntro) {
        int width = ScreenUtils.getScreenWidth();
        int height =
                (int) (ruleIntro.height * 1.0f / ruleIntro.width * ScreenUtils.getScreenWidth());
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(NearStoreDetailActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        simpleDraweeView.setLayoutParams(layoutParams);
        FrescoUtil.setImage(simpleDraweeView, ruleIntro.imageUrl);
        return simpleDraweeView;
    }

    private void setData() {
        FrescoUtil.setImageSmall(mIvAvatar, mData.headImage);
        mTvName.setText(mData.nickName);
        mTvPhone.setText(ConvertUtil.maskPhone(mData.phone));
        mTvLocation.setText(mData.getLocationStr() + "    " + mData.getDistanceStr());
    }

    private void getIntentData() {
        mData = (NearStoreModel.DatasEntity) getIntent().getSerializableExtra("data");
        mIsSelect = getIntent().getBooleanExtra("isSelect", false);
    }

    @OnClick(R.id.btnSubmit)
    public void onMBtnSubmitClicked() {
        Intent intent = new Intent(this, NewRegisterActivity.class);
        User user = new User();
        user.phone = mData.phone;
        user.invitationCode = mData.inviteCode;
        user.nickname = mData.nickName;
        user.avatar = mData.headImage;
        intent.putExtra("user", user);
        startActivity(intent);
        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_SELECT_STORE));
        if (!mIsSelect) {
            this.overridePendingTransition(R.anim.activity_top_bottom_open, 0);
        }

    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(MsgStatus msgStatus) {
        if (msgStatus.getAction() == MsgStatus.ACTION_SELECT_STORE && mIsSelect) {
            finish();
        }
    }
}
