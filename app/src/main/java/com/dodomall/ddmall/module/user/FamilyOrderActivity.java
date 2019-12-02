package com.dodomall.ddmall.module.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.constant.AppTypes;
import com.dodomall.ddmall.shared.util.ConvertUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FamilyOrderActivity extends BaseActivity {

    @BindView(R.id.magicIndicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private String[] mTitles = {"大众家人", "中众家人", "小众家人"};
    private BaseFragment[] mFragmens = {
        FamlyOrderFragment.newInstance(AppTypes.FAMILY.LARGE),
//        FamlyOrderFragment.newInstance(AppTypes.FAMILY.MIDDLE),
//        FamlyOrderFragment.newInstance(AppTypes.FAMILY.SMALL),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_order);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setTitle("积分订单");
        setLeftBlack();

        mMagicIndicator.setVisibility(View.GONE);
//        initIndicator();
        initViewPager();
    }

    public void initViewPager() {
        mViewPager.setOffscreenPageLimit(mTitles.length);
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragmens.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmens[position];
            }
        };
        mViewPager.setAdapter(pagerAdapter);
    }

    public void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setLeftPadding(ConvertUtil.dip2px(15));
        commonNavigator.setRightPadding(ConvertUtil.dip2px(15));
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setText(mTitles[index]);
                titleView.setNormalColor(getResources().getColor(R.color.default_text_color));
                titleView.setSelectedColor(getResources().getColor(R.color.red));
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                titleView.setPadding(0, 0, 0, 0);
                titleView.setTextSize(16);
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.red));
                indicator.setLineHeight(ConvertUtil.dip2px(2));
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }
}
