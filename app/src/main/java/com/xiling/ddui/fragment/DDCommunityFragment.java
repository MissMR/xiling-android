package com.xiling.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiling.R;
import com.xiling.module.community.SmartTabLayout;
import com.xiling.shared.basic.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by Jigsaw at 2018/10/11
 * 社区
 */
public class DDCommunityFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    SmartTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private Unbinder unbinder;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dd_community, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        mFragmentList.clear();
        mFragmentList.add(MaterialDataFragment.newInstance(MaterialDataFragment.MATERIAL_PRODUCT));
        mFragmentList.add(MaterialDataFragment.newInstance(MaterialDataFragment.MATERIAL_MARKETING));
        mFragmentList.add(new MaterialHelloFragment());

        //标题字体颜色
        mTabLayout.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.ddm_red),
                ContextCompat.getColor(getContext(), R.color.ddm_gray_dark));
        //滑动条宽度
        mTabLayout.setTabTitleTextSize(16);
        mTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(getContext(), R.color.ddm_red));
        //均匀平铺选项卡
        mTabLayout.setPadding(10);
        mTabLayout.setDistributeEvenly(true);
        mTabLayout.setTabStripWidth(50);

        mViewPager.setAdapter(getViewPagerAdapter());
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setViewPager(mViewPager);

    }

    private FragmentPagerAdapter getViewPagerAdapter() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String title = "";
                switch (position) {
                    case 0:
                        title = "商品素材";
                        break;
                    case 1:
                        title = "营销素材";
                        break;
                    case 2:
                        title = "早晚安语";
                        break;

                }
                return title;
            }


        };
        return adapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
