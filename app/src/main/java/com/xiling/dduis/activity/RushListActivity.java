package com.xiling.dduis.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.config.UIConfig;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.UITools;
import com.xiling.dduis.base.BackgroundMaker;
import com.xiling.dduis.bean.DDHomeRushDataBean;
import com.xiling.dduis.bean.DDRushHeaderBean;
import com.xiling.dduis.dialog.DDShareRushDialog;
import com.xiling.dduis.fragment.DDRushBuyFragment;
import com.xiling.module.user.LoginActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.DDHomeService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RushListActivity extends BaseActivity {

    private static final String kRushId = "RushListActivity.rushId";
    private String rushId = "";

    public static void jumpToFullList(Context context, String rushId) {
        Intent intent = new Intent(context, RushListActivity.class);
        intent.putExtra(kRushId, rushId);
        context.startActivity(intent);
    }

    @BindView(R.id.magicIndicator)
    MagicIndicator mIndicator;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    DDHomeService homeService = null;
    DDHomeRushDataBean rushData;
    CommonNavigator commonNavigator = null;
    ArrayList<DDRushBuyFragment> fragments = new ArrayList<>();
    private ArrayList<String> mTabTitles = new ArrayList<>();

    //服务器默认场次
    int currentIndex = -1;
    //当前用户选择场次
    int nowIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeService = ServiceManager.getInstance().createService(DDHomeService.class);

        setContentView(R.layout.activity_rush_list);
        ButterKnife.bind(this);

        rushId = getIntent().getStringExtra(kRushId);
        if (TextUtils.isEmpty(rushId)) {
            rushId = "";
        }

        setTitle("限时购");
        setLeftBlack();

        getHeaderLayout().setRightDrawable(R.drawable.ic_push_share);
        getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DLog.d("pressed share.");
                if (!SessionUtil.getInstance().isLogin()) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    return;
                }
                DDShareRushDialog shareRushDialog = new DDShareRushDialog(RushListActivity.this);
                shareRushDialog.setHeaderBean(rushData.getTimeList().get(nowIndex));
                shareRushDialog.show();
            }
        });
        getHeaderLayout().showRightItem();

        initView();

        loadRushData();
    }

    void initView() {

        initViewPager();

        commonNavigator = new CommonNavigator(context);
        commonNavigator.setFollowTouch(true);
        commonNavigator.setSkimOver(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                CommonPagerTitleView titleView = new CommonPagerTitleView(context);
                titleView.setContentView(R.layout.layout_rush_pager);

                final LinearLayout layoutRush = titleView.findViewById(R.id.layout_rush);
                layoutRush.setBackgroundColor(Color.BLACK);

                final TextView tvTime = titleView.findViewById(R.id.tv_time);
                final TextView tvStatus = titleView.findViewById(R.id.tv_status);
                final View vDown = titleView.findViewById(R.id.v_down);

                DDRushHeaderBean header = rushData.getTimeList().get(index);
                //设置抢购时间
                tvTime.setText("" + header.getFormatTimeText());

                layoutRush.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                titleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                    @Override
                    public void onSelected(int index, int totalCount) {
                        vDown.setVisibility(View.VISIBLE);
                        DDRushHeaderBean header = rushData.getTimeList().get(index);
                        if (header.isRushEnable()) {
                            layoutRush.setBackgroundColor(UIConfig.COLOR_RUSH_VIP);
                            BackgroundMaker.setTriangleDrawableColor(vDown, UIConfig.COLOR_RUSH_VIP);
                        } else {
                            layoutRush.setBackgroundColor(UIConfig.COLOR_RUSH_USER);
                            BackgroundMaker.setTriangleDrawableColor(vDown, UIConfig.COLOR_RUSH_USER);
                        }
                        tvTime.setTextColor(Color.WHITE);
                        tvStatus.setTextColor(Color.WHITE);

                        nowIndex = index;

                        //设置抢购状态
                        tvStatus.setText("" + header.getStatusText(index == currentIndex));
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        layoutRush.setBackgroundColor(Color.BLACK);
                        vDown.setVisibility(View.INVISIBLE);
                        tvTime.setTextColor(Color.GRAY);
                        tvStatus.setTextColor(Color.GRAY);
                        DDRushHeaderBean header = rushData.getTimeList().get(index);
                        //设置抢购状态
                        tvStatus.setText("" + header.getStatusText(index == currentIndex));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

                    }
                });
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        mIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);
    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                BaseFragment fragment = fragments.get(position);
                return fragment;
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragments.get(position).getTitle();
            }

        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void loadRushData() {
        DLog.i("加载首页秒杀数据");
        APIManager.startRequest(homeService.getHomeRushData(), new RequestListener<DDHomeRushDataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDHomeRushDataBean result) {
                super.onSuccess(result);
                DLog.d("onSuccess:" + result.getTime());
                rushData = result;

                for (DDRushHeaderBean bean : result.getTimeList()) {
                    mTabTitles.add(bean.getFormatTimeText());
                }

                render();
            }

            @Override
            public void onError(Throwable e) {
                DLog.e("onError:" + e.getLocalizedMessage());
                ToastUtil.error(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public boolean canAdjust() {
        int width = ConvertUtil.dip2px(100);
        int maxWidth = mTabTitles.size() * width;
        int screenWidth = UITools.getScreenWidth(context);
        DLog.i("screenWidth:" + screenWidth + ",itemCount:" + mTabTitles + ",maxWidth:" + maxWidth);
        return screenWidth >= maxWidth;
    }

    public void render() {
        fragments.clear();
        int index = 0;

        ArrayList<DDRushHeaderBean> headers = rushData.getTimeList();

        if (headers.size() <= 0) {
            ToastUtil.error("当前没有可用的抢购");
            return;
        }

        for (DDRushHeaderBean bean : headers) {
            //找出当前应该选中哪个坐标
            if (!TextUtils.isEmpty(rushId) && nowIndex < 0) {
                if (rushId.equals(bean.getFlashSaleId())) {
                    currentIndex = index;
                    nowIndex = index;
                }
            } else {
                if (bean.isSelected() && nowIndex < 0) {
                    currentIndex = index;
                    nowIndex = index;
                }
            }

            //填充二级界面数据
            DDRushBuyFragment rushBuyFragment = new DDRushBuyFragment();
            rushBuyFragment.setHeaderData(bean);
            fragments.add(rushBuyFragment);
            index++;
        }

        mViewPager.getAdapter().notifyDataSetChanged();
        //自动适配屏幕的逻辑
        commonNavigator.setAdjustMode(canAdjust());
        commonNavigator.notifyDataSetChanged();

        if (fragments.size() > 2) {
            mViewPager.setOffscreenPageLimit(2);
        }

        if (nowIndex > 0) {
            mViewPager.setCurrentItem(nowIndex);
        } else {
            mViewPager.setCurrentItem(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBecomeMasterEvent(EventMessage eventMessage) {
        DLog.i("onBecomeMasterEvent");
        switch (eventMessage.getEvent()) {
            case loginSuccess:
            case becomeStoreMaster:
                loadRushData();
                break;
        }
    }

}
