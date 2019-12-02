package com.dodomall.ddmall.module.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.user.LoginActivity;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.util.SessionUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * @author Stone
 * @time 2018/3/11  17:23
 * @desc 通用的tabLayout+ViewPager的fragment,adapter的设置是抽象的方法
 */

public class TabViewPagerFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    SmartTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.ic_publish_new)
    ImageView mIcPublishNew;
    @BindView(R.id.tab_bottom_line)
    View tabBottonLine;
    private TabViewPagerAdapter mTabAdapter;

    public static TabViewPagerFragment newInstance(TabViewPageAdapterTag adapterTag) {
        TabViewPagerFragment tabViewPagerFragment = new TabViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_EXTROS, adapterTag);
        tabViewPagerFragment.setArguments(bundle);
        return tabViewPagerFragment;
    }

    @Override
    protected int getFragmentResId() {
        return R.layout.fragment_tab_viewpager;
    }

    @Override
    protected void initViewConfig() {
        super.initViewConfig();

        //标题字体颜色
        mTabLayout.setTitleTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary),
                ContextCompat.getColor(mActivity, R.color.color_33));
        //滑动条宽度
        mTabLayout.setTabTitleTextSize(16);
        mTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        //均匀平铺选项卡
        mTabAdapter = getTabAdapter();
        mTabLayout.setDistributeEvenly(mTabAdapter.isDistribute());
        mTabLayout.setPadding(mTabAdapter.getTextPadding());
        mTabLayout.setTabStripWidth(mTabAdapter.getBottomLineWidth());
        mViewpager.setAdapter(mTabAdapter);
        mViewpager.setOffscreenPageLimit(mTabAdapter.getCount());
        mTabLayout.setViewPager(mViewpager);
        // 社区tab隐藏
        mTabLayout.setVisibility(View.GONE);
//        tabBottonLine.setVisibility(mTabAdapter.showLine() ? View.VISIBLE : View.GONE);
        tabBottonLine.setVisibility(View.GONE);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIcPublishNew.setVisibility(mTabAdapter.getVisiableAdd(position) ? View.VISIBLE : View.GONE);
                if (mTabAdapter.getNeedLogin(position) && !SessionUtil.getInstance().isLogin()) {
                    mViewpager.setCurrentItem(0, false);
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mTabAdapter.getSkipPage(position) != null) {
                    Class clazz = mTabAdapter.getSkipPage(position);
                    mViewpager.setCurrentItem(0, false);
                    startActivity(new Intent(mActivity, clazz));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public TabViewPagerAdapter getTabAdapter() {
        return new TabViewPagerAdapter(mActivity, getChildFragmentManager(),
                (TabViewPageAdapterTag) getArguments().getSerializable(Constants.KEY_EXTROS));

    }

    @Override
    protected void initListener() {
        super.initListener();
        mIcPublishNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventMessage(Event.PUBLISH_NEW, null));
            }
        });
    }

    public enum TabViewPageAdapterTag {
        /**
         * 社区和会员的
         */
        COMMUNITY, MEMBERS, SCHOOL, STATISTICS, INTEGRAL_DETAIL, INTEGRAL_LIST,
    }
}
