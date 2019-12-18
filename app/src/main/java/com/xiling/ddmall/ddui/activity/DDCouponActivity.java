package com.xiling.ddmall.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.DDCouponBean;
import com.xiling.ddmall.ddui.custom.DDMagicIndicator;
import com.xiling.ddmall.ddui.fragment.DDCouponListFragment;
import com.xiling.ddmall.shared.basic.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2019/6/12
 * 优惠券页面
 */
public class DDCouponActivity extends BaseActivity {

    @BindView(R.id.dd_magic_indicator)
    DDMagicIndicator ddMagicIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddcoupon);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        showHeader("我的优惠券");
        getHeaderLayout().setRightText("使用说明");
        getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DDCouponActivity.this, DDCouponIntroActivity.class));
            }
        });

        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(DDCouponListFragment.newInstance(DDCouponBean.STATUS_UNUSE));
        fragments.add(DDCouponListFragment.newInstance(DDCouponBean.STATUS_USED));
        fragments.add(DDCouponListFragment.newInstance(DDCouponBean.STATUS_TIME_OUT));

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            private String[] title = {"未使用", "已使用", "已过期"};

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return title.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        });
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        ddMagicIndicator.setupViewPager(viewPager);

    }
}
