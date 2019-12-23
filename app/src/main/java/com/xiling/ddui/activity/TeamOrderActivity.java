package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.xiling.R;
import com.xiling.ddui.fragment.TeamOrderListFragment;
import com.xiling.module.community.SmartTabLayout;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.service.contract.IMasterCenterService;
import com.xiling.shared.util.SpanUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2018/12/21
 * 客户订单
 */
public class TeamOrderActivity extends BaseActivity implements TeamOrderListFragment.OnOrderCountReceiver {

    private final String[] tabNameList = {"全部", "未结算", "已结算", "退款/退货"};

    @BindView(R.id.smart_tab_layout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_order);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        showHeader("我的客户订单");

        //标题字体颜色
        smartTabLayout.setTitleTextColor(ContextCompat.getColor(this, R.color.ddm_red),
                ContextCompat.getColor(this, R.color.ddm_gray_dark));
        //滑动条宽度
        smartTabLayout.setTabTitleTextSize(16);
        smartTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.ddm_red));
        //均匀平铺选项卡
        smartTabLayout.setPadding(10);
        smartTabLayout.setDistributeEvenly(true);
        smartTabLayout.setTabStripWidth(50);

        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(4);
        smartTabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(0);
    }

    private FragmentPagerAdapter getViewPagerAdapter() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                int status;
                switch (position) {
                    case 1:
                        status = IMasterCenterService.SETTLEMENT_WAIT_PAY;
                        break;
                    case 2:
                        status = IMasterCenterService.SETTLEMENT_PAIED;
                        break;
                    case 3:
                        status = IMasterCenterService.SETTLEMENT_REFUND;
                        break;
                    default:
                        status = IMasterCenterService.SETTLEMENT_ALL;
                }
                return TeamOrderListFragment.newInstance(status);
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabNameList[position];
            }


        };
        return adapter;
    }

    @Override
    public void onOrderCountReceive(int count) {
        showHeader(new SpanUtils().append("我的客户订单")
                .append(String.format("(%s)", String.valueOf(getFormatOrderCount(count))))
                .setForegroundColor(ContextCompat.getColor(this, R.color.ddm_red))
                .create());
    }

    /**
     * 若订单数量超过1W，则以万为单位计数，保留有效小数后两位；eg:1.03W
     *
     * @param count
     * @return
     */
    private String getFormatOrderCount(int count) {
        String formatCount = String.valueOf(count);
        if (count / 10000 > 0) {
            formatCount = String.format("%.2f万", count / 10000.00);
        }
        return formatCount;
    }
}
