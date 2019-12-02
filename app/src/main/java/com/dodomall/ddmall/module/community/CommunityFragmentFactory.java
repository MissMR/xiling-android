package com.dodomall.ddmall.module.community;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.dodomall.ddmall.R;


/**
 * @author Stone
 * @time 2018/3/11  18:12
 * @desc 社区的创建fragment的factory
 */

public class CommunityFragmentFactory extends FragmentFactory {
    private Boolean[] visiable = new Boolean[]{false, true, true, false};
    private Boolean[] needLogin = new Boolean[]{false, false, false, true};

    public CommunityFragmentFactory(Context context) {
        super(context);
    }

    @Override
    public String[] getTabTitle() {
        return mContext.getResources().getStringArray(R.array.Message_Title_Arr);
    }

    @Override
    public Fragment getFragment(int position, Fragment fragment) {
        switch (position) {
            case 0:
//                fragment = MessageFragment.instance();
//                fragment = GroupFragment.instance(GroupFragment.CommunityType.TYPE_GROUP, "",true);
                // tab 四个只留一个素材库
                fragment = GroupFragment.instance(GroupFragment.CommunityType.TYPE_MATERIAL, "", true);
                break;
            case 1:
                fragment = GroupFragment.instance(GroupFragment.CommunityType.TYPE_MATERIAL, "", true);
                break;
            case 2:
                fragment = GroupFragment.instance(GroupFragment.CommunityType.TYPE_VIDEO, "", false);
                break;
            case 3:
                fragment = new CommunityCourseFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public boolean getVisiableAdd(int position) {
        return visiable[position];
    }

    @Override
    public boolean getNeedLogin(int position) {
        return needLogin[position];
    }

}
