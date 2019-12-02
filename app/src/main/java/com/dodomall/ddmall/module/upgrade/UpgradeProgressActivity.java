package com.dodomall.ddmall.module.upgrade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.UpgradeProgress;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IUpgradeService;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.ConvertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.upgrade
 * @since 2017-08-03
 */
public class UpgradeProgressActivity extends BaseActivity {

    @BindView(R.id.totalRetailTv)
    protected TextView mTotalRetailTv;
    @BindView(R.id.firstLevelTv)
    protected TextView mFirstLevelTv;
    @BindView(R.id.secondLevelTv)
    protected TextView mSecondLevelTv;
    @BindView(R.id.thirdLevelTv)
    protected TextView mThirdLevelTv;
    @BindView(R.id.indicatorIv)
    protected ImageView mIndicatorIv;
    @BindView(R.id.memberNameTv)
    protected TextView mMemberNameTv;
    @BindView(R.id.myRetailTv)
    protected TextView mMyRetailTv;
    @BindView(R.id.teamRetailTv)
    protected TextView mTeamRetailTv;
    @BindView(R.id.progressBarLayout)
    protected FrameLayout mProgressBarLayout;
    @BindView(R.id.progressBarIv)
    protected ImageView mProgressBarIv;
    @BindView(R.id.levelContainer)
    protected LinearLayout mLevelContainer;

    private String[] mLevels = {"铂金店主", "董事", "一星股东", "二星股东", "三星股东"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        ButterKnife.bind(this);
        showHeader();
        setTitle("升级进度");
        setLeftBlack();
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_white);
        getHeaderLayout().makeHeaderRed();
        getUserInfo();
    }

    private void getUserInfo() {
        IUserService userService = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(userService.getUserInfo(), new BaseRequestListener<User>(this) {
            @Override
            public void onSuccess(User result) {
                mMemberNameTv.setText(result.nickname);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUpgradeProgress();
    }

    private void getUpgradeProgress() {

        IUpgradeService upgradeService = ServiceManager.getInstance().createService(IUpgradeService.class);
        APIManager.startRequest(upgradeService.getUpgradeProgress(), new BaseRequestListener<UpgradeProgress>(this) {
            @Override
            public void onSuccess(UpgradeProgress result) {
                mFirstLevelTv.setText(result.firLevelStr);
                mSecondLevelTv.setText(result.secLevelStr);
                mThirdLevelTv.setText(result.thrLevelStr);
                mTotalRetailTv.setText(ConvertUtil.cent2yuanNoZero(result.totalRetailMoney));
                mMyRetailTv.setText("：" + ConvertUtil.cent2yuanNoZero(result.meRetailMoney));
                mTeamRetailTv.setText(ConvertUtil.cent2yuanNoZero(result.groupRetailMoney));
                setProgressBarPosition(result.progress);
                setIndicatorPosition(result.progress);
                setIndicatorLabelPosition(result.progress);
                initLevelList(result);
            }
        });
    }

    private void setProgressBarPosition(int progress) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mProgressBarIv.getLayoutParams();
        if (progress == 100) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            int progressWidth = mProgressBarLayout.getMeasuredWidth();
            layoutParams.width = progressWidth * progress / 100;
        }
    }

    private void setIndicatorPosition(int progress) {
        int progressWidth = mProgressBarLayout.getMeasuredWidth();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mIndicatorIv.getLayoutParams();
        int x = progressWidth * progress / 100;
        x -= mIndicatorIv.getMeasuredHeight() / 2;
        x = Math.max(0, x);
        layoutParams.setMargins(x, 0, 0, 0);
    }


    private void setIndicatorLabelPosition(int progress) {
        int progressWidth = mProgressBarLayout.getMeasuredWidth();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mSecondLevelTv.getLayoutParams();
        int x = progressWidth * progress / 100;
        x -= mSecondLevelTv.getMeasuredHeight() / 2 + ConvertUtil.dip2px(12) / 2;
        x = Math.max(0, x);
        layoutParams.setMargins(x, 0, 0, 0);
    }

    private void initLevelList(UpgradeProgress result) {
        mLevelContainer.removeAllViews();
        for (int index = 0; index < mLevels.length; index++) {
            RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_upgrade, null);
            ImageView icon = (ImageView) view.findViewById(R.id.itemIconIv);
            icon.setImageResource(getResources().getIdentifier("icon_level_" + (index + 1), "mipmap", getPackageName()));
            TextView titleTv = (TextView) view.findViewById(R.id.itemTitleTv);
            titleTv.setText(mLevels[index]);
            TextView descTv = (TextView) view.findViewById(R.id.itemDescTv);
            ImageView tickIv = (ImageView) view.findViewById(R.id.itemTickIv);
            descTv.setText(String.format("恭喜！您已经升级为%s\n可以享受%s所有权益。", mLevels[index], mLevels[index]));
            if (result.level < index + 1) {
                // disabled
                icon.setAlpha(0.15f);
                titleTv.setAlpha(0.15f);
                descTv.setAlpha(0.15f);
                tickIv.setVisibility(View.GONE);
            }
            mLevelContainer.addView(view);
        }
    }
}
