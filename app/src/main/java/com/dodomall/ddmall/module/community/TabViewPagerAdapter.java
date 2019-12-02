package com.dodomall.ddmall.module.community;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Stone
 * @time 2017/12/26  10:25
 * @desc 通用的TabViewPagerAdapter
 */

public class TabViewPagerAdapter extends FragmentPagerAdapter {
    private String[] titleArr;
    private final FragmentFactory mDataResource;

    public TabViewPagerAdapter(Context context, FragmentManager fm, TabViewPagerFragment.TabViewPageAdapterTag tag) {
        super(fm);
        mDataResource = createDataResource(tag, context);
        assert mDataResource != null;
        titleArr = mDataResource.getTabTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return mDataResource.createFragment(position);
    }

    @Override
    public int getCount() {
        return titleArr.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleArr[position];
    }

    private FragmentFactory createDataResource(TabViewPagerFragment.TabViewPageAdapterTag tag, Context context) {
        if (tag == TabViewPagerFragment.TabViewPageAdapterTag.COMMUNITY) {
            return new CommunityFragmentFactory(context);
        } else if (tag == TabViewPagerFragment.TabViewPageAdapterTag.SCHOOL) {
            return new SchoolFragmentFactory(context);
        }
        return null;
    }

    public int getBottomLineWidth() {
        return mDataResource.getTabBottomLineWidth();
    }

    public boolean getVisiableAdd(int position) {
        //发圈按钮一直存在的逻辑变更 at 20180904 by hanQ
        return true;
//        return mDataResource.getVisiableAdd(position);
    }


    public boolean getNeedLogin(int position) {
        return mDataResource.getNeedLogin(position);
    }

    public int getTextPadding() {
        return mDataResource.getTextPadding();
    }

    public Class getSkipPage(int position) {
        return mDataResource.getScrollSkipPage(position);
    }

    public boolean isDistribute() {
        return mDataResource.isDistribute();
    }

    public boolean showLine() {
        return mDataResource.showLine();
    }
}
