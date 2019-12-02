package com.dodomall.ddmall.module.splash;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author zjm
 * @date 2018/8/1
 */
public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        final ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(GuideFragment.newInstance(1));
        fragments.add(GuideFragment.newInstance(2));
        fragments.add(GuideFragment.newInstance(3));
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(fragments.size());
    }
}
