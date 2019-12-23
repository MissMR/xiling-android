package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.DDTopMenuItem;
import com.xiling.ddui.custom.DDTopMenuPopupWindow;
import com.xiling.ddui.fragment.MyFollowersFragment;
import com.xiling.module.community.SmartTabLayout;
import com.xiling.module.user.UserInfoActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.service.contract.IMasterCenterService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/13
 * 我的粉丝页
 */
public class MyFollowersActivity extends BaseActivity implements MyFollowersFragment.FollowerExtendReceiver {

    @BindView(R.id.tab_layout)
    SmartTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.rl_toolbar)
    RelativeLayout mRlToolbar;
    @BindView(R.id.tv_fans_count)
    TextView mTvFansCount;
    @BindView(R.id.tv_referrer)
    TextView mTvReferrer;

    private DDTopMenuPopupWindow mDDTopMenuPopupWindow;
    private MyFollowersFragment[] mFragmentList = new MyFollowersFragment[3];
    private int mCurrentPageIndex = 0;

    public static void start(Context context, int tabIndex) {
        context.startActivity(new Intent(context, MyFollowersActivity.class).putExtra(Constants.Extras.TAB_INDEX, tabIndex));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_followers);
        ButterKnife.bind(this);
        getIntentData();
        initView();
    }

    private void getIntentData() {
        mCurrentPageIndex = getIntent().getIntExtra(Constants.Extras.TAB_INDEX, 0);
        if (mCurrentPageIndex > 2 || mCurrentPageIndex < 0) {
            mCurrentPageIndex = 0;
        }
    }

    private void initView() {

        //标题字体颜色
        mTabLayout.setTitleTextColor(ContextCompat.getColor(this, R.color.ddm_red),
                ContextCompat.getColor(this, R.color.ddm_gray_dark));
        //滑动条宽度
        mTabLayout.setTabTitleTextSize(16);
        mTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.ddm_red));
        //均匀平铺选项卡
        mTabLayout.setPadding(10);
        mTabLayout.setDistributeEvenly(true);
        mTabLayout.setTabStripWidth(50);

        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(3);
        mTabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(mCurrentPageIndex);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPageIndex = position;
                MyFollowersFragment followersFragment = mFragmentList[mCurrentPageIndex];
                if (followersFragment != null) {
                    setFansCount(followersFragment.getFansCount());
                    setReferrerName(followersFragment.getReferrerName());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setFansCount(String count) {
        mTvFansCount.setText(count);
    }

    private void setReferrerName(String name) {
        if (TextUtils.isEmpty(mTvReferrer.getText())) {
            mTvReferrer.setText(name);
        }
    }

    @OnClick(R.id.tv_filter)
    public void onFilterClicked() {
        if (mDDTopMenuPopupWindow == null) {
            mDDTopMenuPopupWindow = new DDTopMenuPopupWindow(this, getPopupWindowItems());
            mDDTopMenuPopupWindow.setOnItemClickListener(new DDTopMenuPopupWindow.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Object item, int position) {
                    setFragmentFilter(position);
                    mFragmentList[mCurrentPageIndex].refresh();
                }
            });
        }
        mDDTopMenuPopupWindow.showAsDropDown(mRlToolbar);
    }

    private void setFragmentFilter(int filterIndex) {
        int filter = getFilter(filterIndex);
        for (int i = 0; i < mFragmentList.length; i++) {
            mFragmentList[i].setFilterRole(filter);
        }
    }

    private int getFilter(int filterIndex) {
        int filter;
        switch (filterIndex) {
            case 1:
                filter = IMasterCenterService.ROLE_MASTER;
                break;
            case 2:
                filter = IMasterCenterService.ROLE_VIP;
                break;
            default:
                filter = IMasterCenterService.ROLE_ALL;
        }
        return filter;
    }

    @OnClick(R.id.tv_referrer)
    public void onReferrerClick() {
        startActivity(new Intent(this, UserInfoActivity.class));
    }

    @OnClick(R.id.fl_search)
    public void onSearchClicked() {
        startActivity(new Intent(this, FollowerSearchActivity.class));
    }

    @OnClick(R.id.iv_back)
    public void onCloseClicked() {
        finish();
    }

    private FragmentPagerAdapter getViewPagerAdapter() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                MyFollowersFragment fragment = mFragmentList[position];
                if (fragment == null) {
                    fragment = mFragmentList[position] = MyFollowersFragment.newInstance(position + 1);
                    fragment.setFollowerExtendReceiver(MyFollowersActivity.this);
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String title = "";
                switch (position) {
                    case 0:
                        title = "全部粉丝";
                        break;
                    case 1:
                        title = "直属粉丝";
                        break;
                    case 2:
                        title = "团队粉丝";
                        break;
                }
                return title;
            }


        };
        return adapter;
    }

    @Override
    public void onFollowerExtendReceive(MyFollowersFragment followersFragment) {
        if (mFragmentList[mCurrentPageIndex] == followersFragment) {
            setFansCount(followersFragment.getFansCount());
            setReferrerName(followersFragment.getReferrerName());
        }
    }

    private List<DDTopMenuItem> getPopupWindowItems() {
        List<DDTopMenuItem> list = new ArrayList<>();
        list.add(new DDTopMenuItem("全部"));
        list.add(new DDTopMenuItem("仅显示店主"));
        list.add(new DDTopMenuItem("仅显示会员"));
        return list;
    }
}
