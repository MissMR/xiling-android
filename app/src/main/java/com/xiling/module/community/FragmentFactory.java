package com.xiling.module.community;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.xiling.shared.util.ConvertUtil;

/**
 * @author Stone
 * @time 2018/3/11  18:07
 * @desc ${TODD}
 */

public abstract class FragmentFactory {

    public final Context mContext;

    private boolean isVisiable=false;
    public FragmentFactory(Context context) {
        this.mContext=context;
    }

    public SparseArray<Fragment> mFragmentSparseArray = new SparseArray<>();

    public Fragment createFragment(int position) {
        Fragment fragment = mFragmentSparseArray.get(position);
        if (fragment == null) {
            fragment = getFragment(position, fragment);
            mFragmentSparseArray.append(position, fragment);
        }
        return fragment;
    }

    public abstract String[] getTabTitle();

    public abstract Fragment getFragment(int position, Fragment fragment);


    public int getTabBottomLineWidth() {
        return ConvertUtil.dip2px(50);
    }

    public  boolean getVisiableAdd(int position){
        return false;
    }

    public boolean getNeedLogin(int position) {
        return false;
    }

    public int getTextPadding() {
        return 16;
    }

    public boolean getCanScroll() {
        return true;
    }

    public Class getScrollSkipPage(int position) {
        return null;
    }
    public boolean isDistribute() {
        return true;
    }

    public boolean showLine() {
        return true;
    }
}
