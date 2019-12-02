package com.dodomall.ddmall.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.UserAuthBean;
import com.dodomall.ddmall.ddui.fragment.IncomeRecordFragment;
import com.dodomall.ddmall.module.community.SmartTabLayout;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.AchievementBean;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IMasterCenterService;
import com.dodomall.ddmall.shared.util.CashWithdrawManager;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2018/9/12
 * 店主中心 收入明细页面
 */
public class IncomeRecordActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    SmartTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private TextView mTvIncomeAccept;
    private TextView mTvIncomeSale;
    private TextView mTvIncomeTrain;
    private TextView mTvIncomeTotal;

    private IncomeRecordFragment[] mFragmentList = new IncomeRecordFragment[3];

    private IMasterCenterService mMasterCenterService;

    private CashWithdrawManager mCashWithdrawManager;
    private int mIntentTabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_record);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mIntentTabIndex = getIntent().getIntExtra(Constants.Extras.TAB_INDEX, 0);
        mMasterCenterService = ServiceManager.getInstance().createService(IMasterCenterService.class);
        getAchievement();
    }

    private void initView() {
        showHeader("收益记录");
        initHeaderView();

        //标题字体颜色
        mTabLayout.setTitleTextColor(ContextCompat.getColor(this, R.color.ddm_red),
                ContextCompat.getColor(this, R.color.ddm_gray_dark));
        //滑动条宽度
        mTabLayout.setTabTitleTextSize(16);
        mTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.ddm_red));
        //均匀平铺选项卡
        mTabLayout.setPadding(10);
        mTabLayout.setDistributeEvenly(true);
        mTabLayout.setTabStripWidth(50);

        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(3);
        mTabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(mIntentTabIndex);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initHeaderView() {
        mTvIncomeAccept = findViewById(R.id.tv_income_accept);
        mTvIncomeSale = findViewById(R.id.tv_income_sale);
        mTvIncomeTrain = findViewById(R.id.tv_income_train);
        mTvIncomeTotal = findViewById(R.id.tv_income_total);
        findViewById(R.id.tv_btn_withdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 提现
                getCashWithdrawManager().checkCanWithdraw(new CashWithdrawManager.OnCheckListener() {
                    @Override
                    public void onCheckSuccess(UserAuthBean userAuthBean) {
                        startActivity(new Intent(IncomeRecordActivity.this, CashWithdrawActivity.class));
                    }

                    @Override
                    public void onCheckFailed(UserAuthBean userAuthBean) {

                    }
                });
            }
        });
    }

    private void getAchievement() {
        APIManager.startRequest(mMasterCenterService.getMemberAchievementDetail(SessionUtil.getInstance().getLoginUser().id),
                new BaseRequestListener<AchievementBean>() {
                    @Override
                    public void onSuccess(AchievementBean result) {
                        super.onSuccess(result);
                        renderHeaderView(result);
                    }
                });
    }

    private void renderHeaderView(AchievementBean result) {
        AchievementBean.ApiMyStatByRoleBeanBean bean = result.getApiMyStatByRoleBean();

        mTvIncomeAccept.setText(ConvertUtil.cent2yuan2(bean.getBalance()));
        mTvIncomeTotal.setText(ConvertUtil.cent2yuan2(bean.getIncomeTotal()));
        mTvIncomeSale.setText(ConvertUtil.cent2yuan2(bean.getSalePrizeTotal()));
        mTvIncomeTrain.setText(ConvertUtil.cent2yuan2(bean.getTrainingTotal()));
    }

    private CashWithdrawManager getCashWithdrawManager() {
        if (mCashWithdrawManager == null) {
            mCashWithdrawManager = new CashWithdrawManager(this);
        }
        return mCashWithdrawManager;
    }

    private FragmentPagerAdapter getViewPagerAdapter() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                IncomeRecordFragment fragment = mFragmentList[position];
                if (fragment == null) {
                    fragment = mFragmentList[position] = IncomeRecordFragment.newInstance(getIncomeRecordType(position));
                }
                return fragment;
            }

            private int getIncomeRecordType(int position) {
                int type;
                switch (position) {
                    case 1:
                        type = IMasterCenterService.INCOME_SALE;
                        break;
                    case 2:
                        type = IMasterCenterService.INCOME_TRAIN;
                        break;
                    default:
                        type = IMasterCenterService.INCOME_ALL;
                }
                return type;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String title = "";
                switch (position) {
                    case 0:
                        title = "全部记录";
                        break;
                    case 1:
                        title = "销售佣金";
                        break;
                    case 2:
                        title = "培训津贴";
                        break;
                }
                return title;
            }


        };
        return adapter;
    }


}
