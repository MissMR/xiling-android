package com.xiling.ddui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.xiling.R;
import com.xiling.ddui.activity.MessageGroupActivity;
import com.xiling.ddui.adapter.DDHomeShortcutAdapter;
import com.xiling.ddui.bean.DHomeBean;
import com.xiling.ddui.bean.DHomeCategoryBean;
import com.xiling.ddui.bean.HomeShortcutBean;
import com.xiling.ddui.config.UIConfig;
import com.xiling.ddui.custom.DDJavaScriptBridge;
import com.xiling.ddui.manager.BannerManager;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.UITools;
import com.xiling.module.qrcode.DDMScanActivity;
import com.xiling.module.search.SearchActivity;
import com.xiling.module.user.LoginActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.bean.MyStatus;
import com.xiling.shared.bean.Page;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IPageService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_MATCH_EDGE;

/**
 * @author 逄涛
 * 首页Fragment
 */
public class DDHomeFragment extends BaseFragment implements DDTodayFragment.DDHomeTitleBarListener {

    ArrayList<Page> pages = new ArrayList<>();
    HashMap<String, BaseFragment> fragments = new HashMap<>();

    @BindView(R.id.titleBarScrollView)
    ScrollView titleBarScrollView;

    //默认的标题栏背景
    @BindView(R.id.topTitleMaskArea)
    View topTitleMaskArea = null;

    @BindView(R.id.rawTitleBar)
    LinearLayout rawTitleBar;

    @BindView(R.id.homeTitleBar)
    RelativeLayout homeTitleBar;//标题栏

    @BindView(R.id.homeShortcutView)
    RecyclerView homeShortcutView;//首页快捷入口

    @BindView(R.id.titleMsgDotImageView)
    ImageView titleMsgDotImageView;

    //分类显示指示器
    @BindView(R.id.magicIndicator)
    MagicIndicator mMagicIndicator;

    //分类实际显示的ViewPager
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    ArrayList<DHomeCategoryBean> categorys = new ArrayList<>();

    @OnClick(R.id.titleQRButton)
    void onQRPressed() {
        DDMScanActivity.jumpQR(getContext(), "扫一扫", DDMScanActivity.MODE.HOME);
    }

    @OnClick(R.id.titleMsgButton)
    void onMessagePressed() {
        if (SessionUtil.getInstance().isLogin()) {
            startActivity(new Intent(getContext(), MessageGroupActivity.class));
        } else {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }

    @OnClick(R.id.titleSearchButton)
    void onSearchButtonPressed() {
        //此按钮需求调整为跳转到第二个TAB-分类
        // 又要跳搜索商品页了 Jigsaw
        startActivity(new Intent(getContext(), SearchActivity.class));
//        MsgStatus msgStatus = new MsgStatus(MsgStatus.JUMP_TO_CATEGORY);
//        EventBus.getDefault().post(msgStatus);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_dd_home, container, false);
        ButterKnife.bind(this, rootView);

        initHomeData();

        initTitleValue();
        //初始化快捷入口
        initShortcut();

        //初始化第一个的数据
//        initData();

        //请求网络数据 需求变动，不需要加载其他分类数据到首页头部 by hanQ at 20181107
//        loadNetData();

        return rootView;
    }

    DDHomeShortcutAdapter homemShortCutAdapter = null;

    public void initShortcut() {
        homemShortCutAdapter = new DDHomeShortcutAdapter(getContext());
        homeShortcutView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        homeShortcutView.setAdapter(homemShortCutAdapter);
    }

    public void setShortcut(ArrayList<HomeShortcutBean> data) {
        homemShortCutAdapter.setItems(data);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    void initHomeData() {
        DDTodayFragment fragment = DDTodayFragment.newInstance();
        fragment.setTitleBarListener(this);
        fragment.setHomeShortcutListener(new DDJavaScriptBridge.DDHomeShortcutListener() {
            @Override
            public void onGetHomeShortcutData(ArrayList<HomeShortcutBean> homeShortcuts) {
                DLog.i("Today get shortcut data:" + homeShortcuts.size());
                setShortcut(homeShortcuts);
            }
        });

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment).commit();
    }

    void initData() {
        Page homePage = new Page();
        homePage.id = UIConfig.HOME.FRAGMENT_TODAY;
        homePage.name = "首页";
        DDTodayFragment fragment = DDTodayFragment.newInstance();
        fragment.setTitleBarListener(this);
        fragments.put(homePage.name, fragment);
        pages.add(homePage);
        initViewPager();
        initIndicator();
    }

    public void loadNetData() {
        IPageService pageService = ServiceManager.getInstance().createService(IPageService.class);
        APIManager.startRequest(pageService.getDHomeData(), new RequestListener<DHomeBean>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(DHomeBean result) {
                super.onSuccess(result);
                categorys = result.getCategory();
                //设置轮播图数据
                BannerManager.share().setData(result.getBanner());
                //获取完数据后设置
                initPages();
                initViewPager();
                initIndicator();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void initPages() {
        if (categorys.size() > 0) {
            String title = categorys.get(0).getTitle();
            categorys.remove(0);
            if (!TextUtils.isEmpty(title)) {
                pages.get(0).name = title;
            }
            for (DHomeCategoryBean category : categorys) {
                Page webPage = new Page();
                webPage.id = UIConfig.HOME.FRAGMENT_WEB;
                webPage.name = category.getTitle();
                DDWebFragment webFragment = new DDWebFragment();
                webFragment.setCateName(webPage.name);
                webFragment.setUrl(category.getUrl());
//                webFragment.setUrl("https://ldmf.net");
                fragments.put(webPage.name, webFragment);
                pages.add(webPage);
            }
        }
    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Page page = pages.get(position);
                BaseFragment fragment = fragments.get(page.name);
                if (fragment == null) {
                    if (UIConfig.HOME.FRAGMENT_TODAY.equals(page.id)) {
                        fragments.put(page.name, new DDTodayFragment());
                    } else {
                        fragments.put(page.name, new DDTodayFragment());
                    }
                    pages.add(page);
                }
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
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initIndicator();
                lastSelectItem = position;
                if (position != 0) {
                    onTitleBarAlphaChanged(1);
                } else {
                    onTitleBarAlphaChanged(lastAlpha);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
                titleView.setNormalColor(Color.WHITE);
                titleView.setSelectedColor(Color.WHITE);
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                titleView.setPadding(0, 0, 0, 0);
                if (mViewPager.getCurrentItem() == index) {
                    titleView.setTextSize(16);
                } else {
                    titleView.setTextSize(14);
                }
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.WHITE);
                //设置的大小
//                indicator.setMode(MODE_EXACTLY);
//                indicator.setLineWidth(ConvertUtil.dip2px(10));
                //控件宽度
                indicator.setMode(MODE_MATCH_EDGE);
                indicator.setLineHeight(ConvertUtil.dip2px(2));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }


    @Override
    public void onTitleBarAlphaChanged(float alpha) {
        if (lastSelectItem == 0) {
            this.lastAlpha = alpha;
        }
        topTitleMaskArea.setAlpha(alpha);
    }

    int vTitleBarTop = 0;
    int vTitleBarRight = 0;
    int vTitleBarHeight = 0;
    int vShortcutTop = 0;
    int vShortcutBottom = 0;

    /**
     * 初始化标题栏特效
     */
    void initTitleValue() {
        vTitleBarTop = ConvertUtil.dip2px(22);
        vTitleBarRight = UITools.getScreenWidth(getContext());
        vTitleBarHeight = ConvertUtil.dip2px(44);

        vShortcutTop = vTitleBarTop + vTitleBarHeight;
        vShortcutBottom = vShortcutTop + vTitleBarHeight;
    }

    @Override
    public void onTitleShortcutChanged(boolean visibility, float dy) {
        DLog.i("onTitleShortcutChanged:" + visibility + "," + dy);
        int offset = (int) (dy);
        if (visibility) {
            //显示快捷入口
            homeShortcutView.setVisibility(View.VISIBLE);
            if (offset < vTitleBarTop && offset > 0) {
                titleBarScrollView.layout(0, -offset, vTitleBarRight, vShortcutBottom + vTitleBarHeight);
                homeTitleBar.setVisibility(View.VISIBLE);
            } else {
                homeTitleBar.setVisibility(View.GONE);
            }
        } else {
            if (dy > 0) {
                titleBarScrollView.layout(0, -offset, vTitleBarRight, vShortcutBottom + vTitleBarHeight);
                homeTitleBar.setVisibility(View.VISIBLE);
            } else {
                homeTitleBar.setVisibility(View.VISIBLE);
                homeShortcutView.setVisibility(View.GONE);
//                titleBarScrollView.layout(0, vTitleBarTop, vTitleBarRight, vShortcutTop);
            }
        }
    }

    int lastSelectItem = 0;
    float lastAlpha = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyStatus status) {
        if (status != null) {
            DLog.i("onEvent+MessageCount:" + status.messageCount);
            if (status.messageCount > 0) {
                titleMsgDotImageView.setVisibility(View.VISIBLE);
            } else {
                titleMsgDotImageView.setVisibility(View.INVISIBLE);
            }
        }
    }
}