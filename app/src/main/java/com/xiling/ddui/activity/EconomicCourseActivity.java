package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.xiling.R;
import com.xiling.ddui.bean.EconomicCourseCategoryBean;
import com.xiling.ddui.fragment.DDCourseFragment;
import com.xiling.ddui.service.IEconomicClubService;
import com.xiling.module.community.SmartTabLayout;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2018/10/9
 * 精品课堂
 */
public class EconomicCourseActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    SmartTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private IEconomicClubService mEconomicClubService;
    private List<EconomicCourseCategoryBean> mCourseCategoryBeans;
    private int mTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economic_course);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        mTabIndex = getIntent().getIntExtra(Constants.Extras.TAB_INDEX, 0);
        mEconomicClubService = ServiceManager.getInstance().createService(IEconomicClubService.class);
        getCourseCategory();
    }

    private void getCourseCategory() {
        APIManager.startRequest(mEconomicClubService.getCourseCategory(), new BaseRequestListener<List<EconomicCourseCategoryBean>>(this) {
            @Override
            public void onSuccess(List<EconomicCourseCategoryBean> result) {
                super.onSuccess(result);
                mCourseCategoryBeans = result;
                updateView();
            }
        });
    }


    private void initView() {
        showHeader("精品课堂");

        ToastUtil.showLoading(this);

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


    }

    private void updateView() {
        if (mCourseCategoryBeans == null || mCourseCategoryBeans.isEmpty()) {
            return;
        }
        mViewPager.setAdapter(getViewPagerAdapter());
        mViewPager.setOffscreenPageLimit(mCourseCategoryBeans.size());
        mTabLayout.setViewPager(mViewPager);

        if (mTabIndex > 0 && mTabIndex < mViewPager.getChildCount()) {
            mViewPager.setCurrentItem(mTabIndex);
        }

    }

    private FragmentPagerAdapter getViewPagerAdapter() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                // type 1 小图  新手学堂大图
                return DDCourseFragment.newInstance(mCourseCategoryBeans.get(position).getCategoryId(),
                        mCourseCategoryBeans.get(position).getType());
            }

            @Override
            public int getCount() {
                return mCourseCategoryBeans.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mCourseCategoryBeans.get(position).getTitle();
            }

        };
        return adapter;
    }
}
