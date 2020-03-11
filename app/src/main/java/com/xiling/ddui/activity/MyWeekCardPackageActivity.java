package com.xiling.ddui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.bean.WeekCardInfo;
import com.xiling.ddui.fragment.MyWeekCardPackageFragment;
import com.xiling.ddui.fragment.OrderFragment;
import com.xiling.shared.basic.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xiling.shared.Constants.ORDER_ALL;
import static com.xiling.shared.Constants.ORDER_IS_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.Constants.ORDER_WAIT_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;

/**
 * pt
 * 我的周卡包
 */
public class MyWeekCardPackageActivity extends BaseActivity {

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_order)
    ViewPager viewpagerOrder;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> childNames = new ArrayList<>();
    WeekCardInfo weekCardInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_week_card_package);
        ButterKnife.bind(this);
        setTitle("我的周卡");
        setBarPadingColor(Color.parseColor("#2C252D"));
        setLeftBlack();

        if (getIntent() != null){
            weekCardInfo = getIntent().getParcelableExtra("weekCardInfo");
        }

        initData();
    }

    private void initData() {
        childNames.add("未使用");
        childNames.add("已使用");
        childNames.add("已失效");

        fragments.add(MyWeekCardPackageFragment.newInstance(1,weekCardInfo));
        fragments.add(MyWeekCardPackageFragment.newInstance(2,weekCardInfo));
        fragments.add(MyWeekCardPackageFragment.newInstance(3,weekCardInfo));

        slidingTab.setViewPager(viewpagerOrder, childNames.toArray(new String[childNames.size()]), this, fragments);
    }

}
