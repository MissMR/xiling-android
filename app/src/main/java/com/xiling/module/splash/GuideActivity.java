package com.xiling.module.splash;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseFragment;

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

    int[] guideList = {R.mipmap.guide_img_1,R.mipmap.guide_img_2,R.mipmap.guide_img_3,R.mipmap.guide_img_4,R.mipmap.guide_img_5};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        final ArrayList<BaseFragment> fragments = new ArrayList<>();
        for (int i = 0;i<guideList.length;i++){
            fragments.add(GuideFragment.newInstance(guideList[i],i==guideList.length -1));
        }

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
