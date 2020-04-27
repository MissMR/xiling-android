package com.xiling.module;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.SPUtils;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.api.MApiActivity;
import com.xiling.ddui.config.UIConfig;
import com.xiling.ddui.custom.DDPermissionDialog;
import com.xiling.ddui.fragment.DDCartFragment;
import com.xiling.ddui.fragment.DDCategoryFragment;
import com.xiling.ddui.fragment.DDWebViewFragment;
import com.xiling.ddui.fragment.XLMineFragment;
import com.xiling.ddui.manager.AppUpgradeManager;
import com.xiling.ddui.manager.ShopCardManager;
import com.xiling.ddui.manager.XLMessageManager;
import com.xiling.ddui.service.HtmlService;
import com.xiling.ddui.tools.AppTools;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.fragment.DDHomeMainFragment;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.module.publish.PublishDialog;
import com.xiling.module.splash.SplashActivity;
import com.xiling.module.user.LoginActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.NoScrollViewPager;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.PushManager;
import com.xiling.shared.util.SharedPreferenceUtil;
import com.xiling.shared.util.ToastUtil;
import com.google.common.collect.Lists;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.xiling.ddui.config.UIConfig.isUseNewUI;

/**
 * @author 逄涛
 * 主页Activity，viewpager+fragment
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    protected LinearLayout mTabLayout;
    @BindViews({R.id.tabHomeLayout, R.id.tabStoreMaster,
            R.id.tabCartLayout, R.id.tabMeLayout})
    protected List<View> mTabs;

    @BindView(R.id.viewPager)
    protected NoScrollViewPager mViewPager;

    @BindView(R.id.cartBadgeTv)
    protected TextView mCartBadgeTv;

    protected ArrayList<Fragment> mFragments = new ArrayList<>();
    private String mCurrentTab = "home";
    private List<String> mTabNames = Lists.newArrayList("home", "cate-gory", "cart", "user-center");


    private PublishDialog mDialog;
    //    private PublishDialog mDialog;

    @BindView(R.id.statusBarBackgroundView)
    View statusBarBackgroundView = null;

    private int mCurrentIndex;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 存储因系统回收内存导致tab错乱的bug
        outState.putInt(Constants.Extras.TAB_INDEX, mCurrentIndex);
        DLog.i(MainActivity.class.getSimpleName() + " onSaveInstanceState:" + mCurrentIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSavedInstanceState(savedInstanceState);

        SPUtils spUtils = new SPUtils(SplashActivity.class.getName() + "_" + BuildConfig.VERSION_NAME);
        if (!spUtils.getBoolean("oneStart")) {
            //检查权限
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (!aBoolean) {
                        ToastUtil.error("请允许 app 获取相关权限，否则将导致部分功能无法使用");
                    }
                }
            });
        }


        if (!AppTools.isEnableNotification(this)) {
            DLog.d("MSG-PERMISSION", "未授权消息推送");
            //显示消息的权限提示
            showMsgPermissionTip();
        } else {
            DLog.d("MSG-PERMISSION", "已经授权推送");
        }

        if (UserManager.getInstance().isLogin()) {
            PushManager.setJPushInfo(context, UserManager.getInstance().getUser());
        }


     //   initSplash();

        ButterKnife.bind(this);
        darkStatusBar();
        clearViewHeight(statusBarBackgroundView);

        DDHomeMainFragment homeFragment = new DDHomeMainFragment();
        mFragments.add(homeFragment);
//        mFragments.add(new DDStoreFragment());
        mFragments.add(new DDCategoryFragment());
        mFragments.add(new DDCartFragment());
        mFragments.add(new XLMineFragment());
        mFragments.add(DDWebViewFragment.newInstance(HtmlService.BESHOPKEPPER + "?func=" + BuildConfig.H5_FUNC));

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //modify by hanQ at 20180823 不需要响应两次点击
//                onClickTabItems(mTabs.get(position));
                Window window = MainActivity.this.getWindow();
                if (position == 3) {
                    //我的
                    if (isUseNewUI) {
                        //透明
                        clearViewHeight(statusBarBackgroundView);
                        window.setStatusBarColor(Color.TRANSPARENT);
                        writeStatusBar();
                    } else {
                        //不透明
                        setStatusBarView(statusBarBackgroundView);
                        darkStatusBar();
                    }
                } else if (position == 1 || position == mFragments.size()) {
                    //分类 - 透明
                    clearViewHeight(statusBarBackgroundView);
                    window.setStatusBarColor(Color.TRANSPARENT);
                    darkStatusBar();
                } else if (position == 0) {
                    //首页
                    clearViewHeight(statusBarBackgroundView);
                    darkStatusBar();
                } else {
                    // 不透明
                    setStatusBarView(statusBarBackgroundView);
                    darkStatusBar();
                    DLog.d("黑色状态栏");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置ViewPager的预加载数量
        mViewPager.setOffscreenPageLimit(mFragments.size());

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        getParam();

        //初始化
        initTab();

        //检查升级
        new AppUpgradeManager(context).check(false);
        //获取消息条数
        XLMessageManager.loadUserStatus();

        //加载本地缓存的购物车数量
        // CartAmountManager.share().reload();

        if (UIConfig.isProtocolTest) {
            startActivity(new Intent(context, MApiActivity.class));
        }
    }

    private void getSavedInstanceState(Bundle savedInstanceState) {
        DLog.i(MainActivity.class.getSimpleName() + " saveInstanceState : " + savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.Extras.TAB_INDEX)) {
            DLog.i("saveInstanceState : " + savedInstanceState.toString());
            mCurrentIndex = savedInstanceState.getInt(Constants.Extras.TAB_INDEX);
            if (mCurrentIndex > -1 && mCurrentIndex < mTabNames.size()) {
                mCurrentTab = mTabNames.get(mCurrentIndex);
            }
        }
    }

    public void setStatusTransparent() {
        clearViewHeight(statusBarBackgroundView);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        writeStatusBar();
    }

    void diyStatusBar() {
        makeStatusBarTranslucent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.hideLoading();

    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一下退出喜领",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            try {
                android.os.Process.killProcess(android.os.Process.myPid());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    void showMsgPermissionTip() {
        String SHOW_KEY = "isMsgPerShow";
        if (!SharedPreferenceUtil.getInstance().getBoolean(SHOW_KEY, false)) {
            DLog.d("MSG-PERMISSION", "显示消息权限提示");
            DDPermissionDialog dialog = new DDPermissionDialog(this);
            dialog.show();
        } else {
            DLog.d("MSG-PERMISSION", "不显示消息权限提示");
        }
        SharedPreferenceUtil.getInstance().putBoolean(SHOW_KEY, true);
    }

  /*  private void initAdDialog() {
        final WJDialog wjDialog = new WJDialog(this);
        final MainAdView mainAdView = new MainAdView(this);
        IAdService service = ServiceManager.getInstance().createService(IAdService.class);
        APIManager.startRequest(service.getMainAd(), new BaseRequestListener<MainAdModel>() {

            @Override
            public void onSuccess(MainAdModel result) {
                if (StringUtils.isEmpty(result.backUrl)) {
                    return;
                }
                wjDialog.show();
                mainAdView.setCloseClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wjDialog.dismiss();
                    }
                });
                mainAdView.setData(result);
                wjDialog.setContentView(mainAdView);
            }
        });
    }*/

    void initTab() {
        if (!mTabs.isEmpty()) {
            if (mTabNames.contains(mCurrentTab)) {
                onClickTabItems(mTabs.get(mTabNames.indexOf(mCurrentTab)));
            } else {
                onClickTabItems(mTabs.get(0));
            }
        }
    }

    void getParam() {
        if (!"home".equals(mCurrentTab)) {
            return;
        }
        Intent intent = getIntent();
        if (!(intent == null || intent.getExtras() == null)) {
            mCurrentTab = getIntent().getExtras().getString("tab");
            mCurrentTab = mCurrentTab == null ? "home" : mCurrentTab;
        }
    }

    @OnClick({R.id.tabHomeLayout, R.id.tabStoreMaster,
            R.id.tabCartLayout, R.id.tabMeLayout})
    protected void onClickTabItems(View view) {
        //获取当前界面的缓存坐标
        int index = mTabs.indexOf(view);

        if (index > 1 && !UserManager.getInstance().isLogin()) {
            EventBus.getDefault().post(new EventMessage(Event.goToLogin));
        } else {
            mViewPager.setCurrentItem(index, false);
            setTabActive(index);
            //获取当前界面的tabName
            mCurrentTab = mTabNames.get(index);
            mCurrentIndex = index;
            XLMessageManager.loadUserStatus();
        }

    }

    private void showMasterGuidePager(int index) {
        if (index < 0 || index >= mTabs.size()) {
            DLog.e("数组越界：index=" + index + ",mTabSize=" + mTabs.size());
            return;
        }
        setTabActive(index);
        if (index == mCurrentIndex) {
            return;
        } else {
            int currentItem = mViewPager.getChildCount() - 1;
            DLog.e("currentItem:--------" + currentItem);
            mViewPager.setCurrentItem(currentItem, false);
            setStatusTransparent();
        }
        mCurrentTab = mTabNames.get(index);
        mCurrentIndex = index;
    }

    private void setTabActive(int index) {
        for (int i = 0; i < mTabs.size(); i++) {
            mTabs.get(i).setSelected(i == index);
        }
    }

    private void setMasterNavActive(boolean isActive) {
        mTabs.get(2).setVisibility(isActive ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThreadEvent(EventMessage message) {
        switch (message.getEvent()) {
            case viewHome:
                onClickTabItems(mTabs.get(0));
                break;
            case viewCommunity:
                onClickTabItems(mTabs.get(1));
                break;
            case viewCart:
                if (UserManager.getInstance().isLogin()) {
                    onClickTabItems(mTabs.get(2));
                } else {
                    EventBus.getDefault().post(new EventMessage(Event.goToLogin));
                }
                break;
            case viewCenter:
                if (UserManager.getInstance().isLogin()) {
                    onClickTabItems(mTabs.get(3));
                } else {
                    EventBus.getDefault().post(new EventMessage(Event.goToLogin));
                }
                break;
            case cartAmountUpdate:
                int total = (int) message.getData();
                ViewUtil.setCartBadge(total,mCartBadgeTv);
                break;
            case LOGIN_OUT:
                mCartBadgeTv.setText("");
                mCartBadgeTv.setVisibility(View.GONE);
                onClickTabItems(mTabs.get(0));
                break;
            case LOGIN_SUCCESS:
                ShopCardManager.getInstance().requestUpDataShopCardCount(true);
                break;
            case goToLogin:
                Logger.e("跳登录");
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case FINISH_ORDER:
                ShopCardManager.getInstance().requestUpDataShopCardCount(false);
                break;
            default:
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBecomeMasterEvent(EventMessage eventMessage) {
        DLog.i("onBecomeMasterEvent");
        switch (eventMessage.getEvent()) {
            case loginSuccess:
            case becomeStoreMaster:
                checkUpdatePager();
                break;
        }
    }

    private void checkUpdatePager() {
        if (mCurrentIndex == 1 || mCurrentIndex == 2) {
            onClickTabItems(mTabs.get(mCurrentIndex));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatusMsg(MsgStatus message) {
        switch (message.getAction()) {
            case MsgStatus.ACTION_EDIT_PHONE:
                onClickTabItems(mTabs.get(0));
                break;
            case MsgStatus.JUMP_TO_CATEGORY:
                onClickTabItems(mTabs.get(1));
                break;
            case MsgStatus.ReloadMsgCount:
                XLMessageManager.loadUserStatus();
                break;
            default:
        }
    }


    public static void goBack(Context context, int tabIndex) {
        context.startActivity(new Intent(context, MainActivity.class).putExtra(Constants.Extras.TAB_INDEX, tabIndex));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        DLog.i("onNewIntent");
        if (intent != null && intent.hasExtra(Constants.Extras.TAB_INDEX)) {
            int index = intent.getIntExtra(Constants.Extras.TAB_INDEX, mCurrentIndex);
            DLog.i("onNewIntent:tabIndex = " + index);
            if (index >= 0 && index <= mTabs.size() - 1) {
                onClickTabItems(mTabs.get(index));
            }
        }
    }
}
