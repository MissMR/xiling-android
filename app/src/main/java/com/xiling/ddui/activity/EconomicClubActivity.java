package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiling.R;
import com.xiling.ddui.adapter.EconomicCourseAdapter;
import com.xiling.ddui.bean.EconomicArticleBean;
import com.xiling.ddui.bean.EconomicClubHomeBean;
import com.xiling.ddui.bean.EconomicCourseBean;
import com.xiling.ddui.bean.EconomicCourseCategoryBean;
import com.xiling.ddui.bean.EconomicHomeBanner;
import com.xiling.ddui.service.IEconomicClubService;
import com.xiling.ddui.tools.DLog;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/10/9
 * 商学院
 */
public class EconomicClubActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.nsv_container)
    NestedScrollView mNsvContainer;
    @BindView(R.id.v_toolbar_background)
    View mVToolbarBackground;
    @BindView(R.id.ll_header)
    LinearLayout mLlHeader;
    @BindView(R.id.rg_course)
    RadioGroup mRgCourse;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.vf_top_news)
    ViewFlipper mVfTopNews;
    @BindView(R.id.ll_story_container)
    LinearLayout mLlStoryContainer;

    @BindColor(R.color.ddm_black_dark)
    int mTitleBlackColor;
    @BindColor(R.color.white)
    int mTitleWhiteColor;

    private EconomicCourseAdapter mBaseQuickAdapter;
    private IEconomicClubService mEconomicClubService;
    private EconomicClubHomeBean mEconomicClubHomeBean;
    private List<EconomicCourseBean> mCourseList = new ArrayList<>();

    // 当前点击的 课程 item 索引  用于刷新已读数
    private int mCurrentCourseItemIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economic_club);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 点击课程item后，返回当前页面 刷新已读数目
        if (mCurrentCourseItemIndex > -1) {
            EconomicCourseBean courseBean = mBaseQuickAdapter.getData().get(mCurrentCourseItemIndex);
            courseBean.setReadCount(courseBean.getReadCount() + 1);
            mBaseQuickAdapter.notifyItemChanged(mCurrentCourseItemIndex);
            mCurrentCourseItemIndex = -1;
        }
    }

    private void initData() {
        mEconomicClubService = ServiceManager.getInstance().createService(IEconomicClubService.class);
        getEconomicData();
    }

    private void getEconomicData() {
        APIManager.startRequest(mEconomicClubService.getEconomicHomeInfo(), new BaseRequestListener<EconomicClubHomeBean>(this) {
            @Override
            public void onSuccess(EconomicClubHomeBean result) {
                super.onSuccess(result);
                mEconomicClubHomeBean = result;
                updateView();
            }
        });
    }


    private void initView() {

        ToastUtil.showLoading(this);

        initHeaderListener();

        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(EconomicClubActivity.this);

        mViewPager.setPageMargin(ConvertUtil.dip2px(12));
        mViewPager.setPageTransformer(true, new ScaleInTransformer());

        mVfTopNews.removeAllViews();
        mLlStoryContainer.removeAllViews();

        mRgCourse.removeAllViews();

        initRecyclerView();

    }

    private void updateView() {
        if (null == mEconomicClubHomeBean) {
            return;
        }

        // banner
        mViewPager.setAdapter(new BannerAdapter(mEconomicClubHomeBean.getHomeBannerList()));
        mViewPager.setOffscreenPageLimit(mEconomicClubHomeBean.getHomeBannerList().size());

        // 新闻头条
        mVfTopNews.removeAllViews();
        for (EconomicArticleBean article : mEconomicClubHomeBean.getNewsDetail()) {
            mVfTopNews.addView(getTopNewsItemView(article));
        }

        // 店主故事
        mLlStoryContainer.removeAllViews();
        for (EconomicArticleBean article : mEconomicClubHomeBean.getStoryDetail()) {
            mLlStoryContainer.addView(getMasterStoryItemView(article));
        }

        initCourseTab();
        if (mRgCourse.getChildCount() > 0) {
            updateRecyclerView(0);
        }

    }

    private void initCourseTab() {
        if (mEconomicClubHomeBean.getCourseCategoryDetail() == null) {
            return;
        }
        mRgCourse.removeAllViews();
        for (int i = 0; i < mEconomicClubHomeBean.getCourseCategoryDetail().size(); i++) {
            View view = getCourseTabItemView(mEconomicClubHomeBean.getCourseCategoryDetail().get(i));
            view.setId(i);
            mRgCourse.addView(view);
        }

        mRgCourse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateRecyclerView(checkedId);
            }
        });

        if (mRgCourse.getChildCount() > 0) {
            mRgCourse.check(0);
        }

    }

    private void updateRecyclerView(int courseTabIndex) {
        mBaseQuickAdapter.getData().clear();
        mBaseQuickAdapter.addData(mEconomicClubHomeBean.getCourseCategoryDetail().get(courseTabIndex).getCourseDetail());
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);

        mBaseQuickAdapter = new EconomicCourseAdapter(R.layout.item_economic_course_newer, mCourseList);
        mRecyclerView.setAdapter(mBaseQuickAdapter);
        mBaseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemCourseClick(mBaseQuickAdapter.getItem(position));
                mCurrentCourseItemIndex = position;
            }
        });
    }


    private void initHeaderListener() {
        mNsvContainer.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                DLog.i("onScrollChange: y = " + scrollY + ",  height = " + mLlHeader.getHeight());
                if (scrollY >= mLlHeader.getHeight()) {
                    mVToolbarBackground.setAlpha(1);
                    mIvBack.setImageResource(R.mipmap.ic_back_black);
                    mTvToolbarTitle.setTextColor(mTitleBlackColor);
                } else {
                    mVToolbarBackground.setAlpha(scrollY * 1.0f / mLlHeader.getHeight());
                    mIvBack.setImageResource(R.mipmap.ic_back_white);
                    mTvToolbarTitle.setTextColor(mTitleWhiteColor);
                }
            }
        });
    }

    @OnClick({R.id.iv_right, R.id.tv_more_story, R.id.tv_all_course})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                // 新闻头条 列表
                startActivity(new Intent(this, EconomicTopNewsActivity.class));
                break;
            case R.id.tv_more_story:
                // 店主故事 更多
                startActivity(new Intent(this, EconomicMasterStoryActivity.class));
                break;
            case R.id.tv_all_course:
                // 精品课堂 全部
                startActivity(new Intent(this, EconomicCourseActivity.class));
                break;
        }
    }

    private void onItemTopNewsClick(EconomicArticleBean articleBean) {
        InfoDetailActivity.jumpDetail(this, articleBean.getTitle(),
                articleBean.getArticleId(), InfoDetailActivity.InfoType.News, articleBean);
    }

    private void onItemStoryClick(EconomicArticleBean articleBean) {
        InfoDetailActivity.jumpDetail(this, articleBean.getTitle(),
                articleBean.getArticleId(), InfoDetailActivity.InfoType.Story, articleBean);
    }

    private void onItemCourseClick(EconomicCourseBean courseBean) {
        InfoDetailActivity.jumpDetail(this, courseBean.getTitle(),
                courseBean.getCourseId(), InfoDetailActivity.InfoType.Learn, courseBean);
    }

    private View getCourseTabItemView(EconomicCourseCategoryBean category) {
        RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.item_course_tab, mRgCourse, false);
        radioButton.setText(category.getTitle());
        return radioButton;
    }

    private View getTopNewsItemView(final EconomicArticleBean articleBean) {
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.item_view_flipper_news, null);
        textView.setText(articleBean.getTitle());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemTopNewsClick(articleBean);
            }
        });
        return textView;
    }


    private View getMasterStoryItemView(final EconomicArticleBean articleBean) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) LayoutInflater.from(this)
                .inflate(R.layout.item_economic_home_story, mLlStoryContainer, false);
        simpleDraweeView.setImageURI(articleBean.getHomeImageUrl());

        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemStoryClick(articleBean);
            }
        });

        return simpleDraweeView;
    }

    @OnClick(R.id.iv_back)
    void onBackClick() {
        finish();
    }

    private static class BannerAdapter extends PagerAdapter {
        private List<EconomicHomeBanner> mEconomicHomeBanners;
        private Context mContext;

        public BannerAdapter(List<EconomicHomeBanner> economicHomeBanners) {
            mEconomicHomeBanners = economicHomeBanners;
        }

        @Override
        public int getCount() {
            return mEconomicHomeBanners.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            if (null == mContext) {
                mContext = container.getContext();
            }

            View item = LayoutInflater.from(container.getContext()).inflate(R.layout.item_economic_club_banner, null);
            SimpleDraweeView simpleDraweeView = item.findViewById(R.id.sdv_banner);
            String imgUrl = mEconomicHomeBanners.get(position).getBanner();
            simpleDraweeView.setImageURI(imgUrl);

            container.addView(item);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(mEconomicHomeBanners.get(position));
                }
            });

            return item;
        }

        private void onItemClick(EconomicHomeBanner economicHomeBanner) {
            if (economicHomeBanner.getActionType() == 1) {
                // 启用原生界面
                // TOxDO:Jigsaw 2018/10/13 对接原生页面
            } else if (economicHomeBanner.getActionType() == 2) {
                // 打开网页
                WebViewActivity.jumpUrl(mContext, economicHomeBanner.getLinkUrl());
            }

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

}
