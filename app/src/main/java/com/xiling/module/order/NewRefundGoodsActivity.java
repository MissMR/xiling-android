package com.xiling.module.order;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xiling.R;
import com.xiling.module.auth.Config;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.util.ConvertUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2018/10/23
 * 申请退款列表页
 */
public class NewRefundGoodsActivity extends BaseActivity {

    @BindView(R.id.magicIndicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private String mId;
    private List<BaseFragment> mFragments = new ArrayList<>();
    private String[] mNames = {"申请退货", "申请记录"};
    private PageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_tab_page);
        ButterKnife.bind(this);
        getIntentData();
        initData();
        initView();
    }

    private void getIntentData() {
        mId = getIntent().getStringExtra(Config.INTENT_KEY_ID);
    }

    private void initView() {
        setTitle("申请退货");
        setLeftBlack();

        mAdapter = new PageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new TabBarAdapter());
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    private void initData() {
        mFragments.add(SelectRefundsGoodsFragment.newInstance(mId));
        mFragments.add(NewRefundsOrderListFragment.newInstance(mId));
    }

    private IPagerTitleView getTabBarView(final int index, String name) {
        SimplePagerTitleView titleView = new SimplePagerTitleView(this);
        titleView.setText(name);
        titleView.setNormalColor(getResources().getColor(R.color.text_black));
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

    class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    class TabBarAdapter extends CommonNavigatorAdapter {

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, int index) {
            return getTabBarView(index, mNames[index]);
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setColors(getResources().getColor(R.color.red));
            indicator.setLineHeight(ConvertUtil.dip2px(2));
            return indicator;
        }
    }
}
