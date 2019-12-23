package com.xiling.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.service.HtmlService;
import com.xiling.module.search.SearchActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.bean.Page;
import com.xiling.shared.constant.Key;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.page.CustomPageFragment;
import com.xiling.shared.service.contract.IPageService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.viewPager)
    protected ViewPager mViewPager;
    @BindView(R.id.magicIndicator)
    protected MagicIndicator mMagicIndicator;

    protected ArrayList<Page> pages = new ArrayList<>();
    protected HashMap<String, CustomPageFragment> fragments = new HashMap<>();
    @BindView(R.id.linkToBeShopkeeper)
    LinearLayout mLinkToBeShopkeeper;
    @BindView(R.id.ivNoData)
    ImageView mIvNoData;
    @BindView(R.id.tvNoData)
    TextView mTvNoData;
    @BindView(R.id.tvGoMain)
    TextView mTvNoDataBtn;
    @BindView(R.id.layoutNodata)
    LinearLayout mLayoutNodata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        initNoData();
        initPages();
        initData();
        return rootView;
    }

    private void initData() {
        IPageService pageService = ServiceManager.getInstance().createService(IPageService.class);
        APIManager.startRequest(pageService.getPageList(), new RequestListener<List<Page>>() {
            @Override
            public void onStart() {
                ToastUtil.showLoading(getActivity());
            }

            @Override
            public void onSuccess(List<Page> list) {
                mLayoutNodata.setVisibility(View.GONE);
                pages.addAll(list);
                initViewPager();
                initIndicator();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.hideLoading();
                mLayoutNodata.setVisibility(View.VISIBLE);
            }

            @Override
            public void onComplete() {
                ToastUtil.hideLoading();
            }
        });
    }

    private void initNoData() {
        mIvNoData.setImageResource(R.mipmap.no_data_normal);
        mTvNoData.setText("这个页面去火星了");
        mTvNoDataBtn.setText("刷新看看");
        mTvNoDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void initPages() {
        Page homePage = new Page();
        homePage.id = Key.PAGE_HOME;
        homePage.name = "首页";
        fragments.put(Key.PAGE_HOME, CustomPageFragment.newInstance(Key.PAGE_HOME));
        pages.add(homePage);
    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Page page = pages.get(position);
                CustomPageFragment fragment = fragments.get(page.id);
                if (fragment == null) {
                    fragment = CustomPageFragment.newInstance(page.id);
                    fragments.put(page.id, fragment);
                }
//                fragment.setPageId(page.id);
                return fragment;
            }

            @Override
            public int getCount() {
                return pages.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return pages.get(position).name;
            }
        });
        mViewPager.setOffscreenPageLimit(pages.size());
        mViewPager.setCurrentItem(0);
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setLeftPadding(ConvertUtil.dip2px(20));
        commonNavigator.setRightPadding(ConvertUtil.dip2px(20));
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return pages.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setText(pages.get(index).name);
                titleView.setNormalColor(getResources().getColor(R.color.default_text_color));
                titleView.setSelectedColor(getResources().getColor(R.color.red));
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                titleView.setPadding(0, 0, 0, 0);
                titleView.setTextSize(14);
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.red));
                indicator.setLineHeight(ConvertUtil.dip2px(2));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @OnClick(R.id.searchLayout)
    protected void clickSearchLayout() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    @OnClick(R.id.linkToBeShopkeeper)
    protected void toBeShopKeeper() {
        HtmlService.startBecomeStoreMasterActivity(getActivity());
    }
}
