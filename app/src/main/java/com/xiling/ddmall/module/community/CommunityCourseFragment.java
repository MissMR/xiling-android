package com.xiling.ddmall.module.community;


import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiling.ddmall.R;
import com.xiling.ddmall.databinding.ViewImageBinding;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICommunityService;
import com.xiling.ddmall.shared.util.SessionUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author Stone
 * @time 2018/3/16  14:21
 * @desc ${TODD}
 */

public class CommunityCourseFragment extends BaseFragment {


    @BindView(R.id.bannerView)
    BannerView mBannerView;
    @BindView(R.id.tabLayout)
    SmartTabLayout mTabLayout;
    @BindView(R.id.viewpgager_content)
    ViewPager mViewpager;
    private CourseBannerAdapter adapter = new CourseBannerAdapter();
    private ICommunityService mPageService;

    @Override
    protected int getFragmentResId() {
        return R.layout.activity_course;
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
        mTabLayout.setPadding(10);
        mTabLayout.setDistributeEvenly(true);
    }

    public TabViewPagerAdapter getTabAdapter(){
        return new TabViewPagerAdapter(mActivity,getChildFragmentManager(),
                TabViewPagerFragment.TabViewPageAdapterTag.SCHOOL);
    }

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();
        mPageService = ServiceManager.getInstance().createService(ICommunityService.class);
        if (!SessionUtil.getInstance().isLogin()) {
            isDataInitiated = false;
        } else {
            TabViewPagerAdapter tabAdapter = getTabAdapter();
            mViewpager.setAdapter(tabAdapter);
            mTabLayout.setTabStripWidth(tabAdapter.getBottomLineWidth());
            mViewpager.setOffscreenPageLimit(tabAdapter.getCount());
            mTabLayout.setViewPager(mViewpager);
            requestBanner();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    private void requestBanner() {
        APIManager.startRequest(mPageService.getCourseBannerList(), new RequestListener<ArrayList<Course>>() {
            @Override
            public void onSuccess(ArrayList<Course> result) {
                super.onSuccess(result);
                adapter.getDataList().addAll(result);
                mBannerView.setAdapter(adapter);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onError(Throwable e) {
                showError(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private class CourseBannerAdapter extends SimplePagerAdapter<Course> {
        @Override
        protected int getLayoutId(int postion) {
            return R.layout.view_image;
        }

        @Override
        protected void onBindData(ViewDataBinding binding, final Course data, int position) {
            ViewImageBinding b = (ViewImageBinding) binding;
            b.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDetail(data);
                }
            });
            String url = data.banner;
           ImageView iv= b.ivImage;
            Glide.with(mActivity).load(url).into(iv);
            binding.executePendingBindings();
        }
    }

    private void toDetail(Course course) {
        Intent intent = new Intent();
/*        if (course.courseType == Course.MEDIA) {
            intent.setClass(mActivity, VoiceDetailActivity.class);
        }else if(course.courseType==Course.TEXT) {
            intent.setClass(mActivity, ArticleDetailActivity.class);
        }else {
            intent.setClass(mActivity, VideoDetailActivity.class);
        }
        intent.putExtra(Constants.Extras.COURSE, course);*/
        startActivity(intent);
    }

    @Override
    public boolean needLogin() {
        return true;
    }
}
