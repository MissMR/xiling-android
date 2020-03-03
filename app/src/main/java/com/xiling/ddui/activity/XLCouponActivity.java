package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.fragment.XLCouponFragment;
import com.xiling.shared.basic.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @auth 宋秉经
 * 优惠券列表
 */
public class XLCouponActivity extends BaseActivity {

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_coupon)
    ViewPager viewpagerCoupon;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> childNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlcoupon);
        ButterKnife.bind(this);
        setTitle("优惠券");
        setLeftBlack();
        initData();
    }

    private void initData(){
        childNames.add("未使用");
        childNames.add("已使用");
        childNames.add("已失效");

        fragments.add(XLCouponFragment.newInstance(2));
        fragments.add(XLCouponFragment.newInstance(1));
        fragments.add(XLCouponFragment.newInstance(3));
        slidingTab.setViewPager(viewpagerCoupon, childNames.toArray(new String[childNames.size()]), this, fragments);
    }

}
