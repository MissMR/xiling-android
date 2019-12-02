package com.dodomall.ddmall.module.community;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.util.ConvertUtil;


/**
 * @author Stone
 * @time 2018/4/17  17:23
 * @desc ${TODD}
 */

public class SchoolFragmentFactory extends  FragmentFactory{
    public SchoolFragmentFactory(Context context) {
        super(context);
    }

    @Override
    public String[] getTabTitle() {
        return mContext.getResources().getStringArray(R.array.arr_school_str);
    }

    @Override
    public Fragment getFragment(int position, Fragment fragment) {
        switch (position) {
            case 0:
                fragment = CourseFragment.newInstance(2, true, "", false);
                break;
            case 1:
                fragment = CourseFragment.newInstance(1, true, "", false);
                break;
            case 2:
                fragment = CourseFragment.newInstance(3, true, "", false);
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getTabBottomLineWidth() {
        return ConvertUtil.dip2px(70);
    }

}
