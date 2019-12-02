package com.dodomall.ddmall.module;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import com.blankj.utilcode.utils.StringUtils;
import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.MyApplication;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.api.MApiActivity;
import com.dodomall.ddmall.ddui.bean.UnReadMessageCountBean;
import com.dodomall.ddmall.ddui.config.UIConfig;
import com.dodomall.ddmall.ddui.custom.DDPermissionDialog;
import com.dodomall.ddmall.ddui.fragment.DDCartFragment;
import com.dodomall.ddmall.ddui.fragment.DDMineFragment;
import com.dodomall.ddmall.ddui.fragment.DDStoreMasterFragment;
import com.dodomall.ddmall.ddui.fragment.DDWebViewFragment;
import com.dodomall.ddmall.ddui.manager.AppUpgradeManager;
import com.dodomall.ddmall.ddui.manager.CartAmountManager;
import com.dodomall.ddmall.ddui.service.HtmlService;
import com.dodomall.ddmall.ddui.tools.AppTools;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.dduis.fragment.DDHomeMainFragment;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.module.page.WebViewActivity;
import com.dodomall.ddmall.module.publish.PublishDialog;
import com.dodomall.ddmall.module.publish.PublishHisActivity;
import com.dodomall.ddmall.module.publish.PublishPicActivity;
import com.dodomall.ddmall.module.user.LoginActivity;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.MainAdModel;
import com.dodomall.ddmall.shared.bean.MyStatus;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.component.MainAdView;
import com.dodomall.ddmall.shared.component.NoScrollViewPager;
import com.dodomall.ddmall.shared.component.dialog.WJDialog;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IAdService;
import com.dodomall.ddmall.shared.service.contract.IMessageService;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.SharedPreferenceUtil;
import com.dodomall.ddmall.shared.util.StringUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
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

import static com.dodomall.ddmall.ddui.config.UIConfig.isUseNewUI;

/**
 * 待优化 根据 tab 索引来跳转对应的tab页
 */
public class MainActivity extends BaseActivity {

    private static final int REQUEST_PIC_CHOOSE = 1000;
    private static final int REQUEST_VIDEO_CHOOSE = 1001;
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
    private List<String> mTabNames = Lists.newArrayList("home", "store-master", "cart", "user-center");


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

        if (!AppTools.isEnableNotification(this)) {
            DLog.d("MSG-PERMISSION", "未授权消息推送");
            //显示消息的权限提示
            showMsgPermissionTip();
        } else {
            DLog.d("MSG-PERMISSION", "已经授权推送");
        }

        initSplash();

        ButterKnife.bind(this);
        MyApplication.startLocation(this);

        darkStatusBar();
        clearViewHeight(statusBarBackgroundView);

        DDHomeMainFragment homeFragment = new DDHomeMainFragment();
        mFragments.add(homeFragment);
//        mFragments.add(new DDStoreFragment());
        mFragments.add(new DDStoreMasterFragment());
        mFragments.add(DDCartFragment.newInstance(DDCartFragment.TYPE_FRAGMENT));
        mFragments.add(new DDMineFragment());
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
                    //分类||店主中心 - 透明
                    clearViewHeight(statusBarBackgroundView);
                    window.setStatusBarColor(Color.TRANSPARENT);
                    writeStatusBar();
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
        loadUserStatus();

        //加载本地缓存的购物车数量
        CartAmountManager.share().reload();

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

    private void initSplash() {
        initAdDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.hideLoading();
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

    @Override
    protected void onPause() {
        super.onPause();

    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一下退出店多多",
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

    private void initAdDialog() {
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
    }

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
        DLog.d("onClick:" + index);

        if ((view.getId() == R.id.tabStoreMaster && (!SessionUtil.getInstance().isLogin() || !SessionUtil.getInstance().getLoginUser().isStoreMaster()))) {
            // 店主中心  若未登录 或 不是店主
            showMasterGuidePager(index);
            return;
        }

        //如果是小店 / 购物车或者我的界面则需要执行登录逻辑
        if ((index >= 2 || index == 1) && !SessionUtil.getInstance().isLogin()) {
            EventBus.getDefault().post(new EventMessage(Event.goToLogin));
            return;
        }

        //设置TAB选中状态
        if (index == mViewPager.getCurrentItem()) {
            view.setSelected(true);
            return;
        } else {
            mViewPager.setCurrentItem(index, false);
        }

        setTabActive(index);

        //获取当前界面的tabName
        mCurrentTab = mTabNames.get(index);
        mCurrentIndex = index;
        loadUserStatus();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PIC_CHOOSE && data != null) {
            ArrayList<Uri> uris = (ArrayList<Uri>) Matisse.obtainResult(data);
          /*  LogUtils.e("拿到图片" + uris.get(0).getPath());
            updateImage(requestCode, uris.get(0));*/
            if (StringUtil.isNullOrEmpty(uris)) {
                return;
            }
            Intent intent = new Intent(this, PublishPicActivity.class);
            intent.putParcelableArrayListExtra(Constants.KEY_EXTROS, uris);
            startActivity(intent);
        } else if (requestCode == REQUEST_VIDEO_CHOOSE && data != null) {
            ArrayList<Uri> uris = (ArrayList<Uri>) Matisse.obtainResult(data);
            Intent intent = new Intent(this, PublishPicActivity.class);
            intent.putParcelableArrayListExtra(Constants.KEY_EXTROS, uris);
            intent.putExtra(Constants.KEY_IS_VIDEO, true);
            startActivity(intent);
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
            case viewNearStore:
                onClickTabItems(mTabs.get(2));
                break;
            case viewCart:
                if (SessionUtil.getInstance().isLogin()) {
                    onClickTabItems(mTabs.get(3));
                } else {
                    EventBus.getDefault().post(new EventMessage(Event.goToLogin));
                }
                break;
            case cartAmountUpdate:
                int total = (int) message.getData();
                mCartBadgeTv.setText(total > 99 ? "99+" : String.valueOf(total));
                mCartBadgeTv.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
                break;
            case logout:
                mCartBadgeTv.setText("");
                mCartBadgeTv.setVisibility(View.GONE);
                break;
            case goToLogin:
                Logger.e("跳登录");
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
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
                loadUserStatus();
                break;
            default:
        }
    }

    /**
     * 用来读取未读消息条数
     */
    public void loadUserStatus() {
        if (!SessionUtil.getInstance().isLogin()) {
            return;
        }

        IMessageService messageService = ServiceManager.getInstance().createService(IMessageService.class);
        APIManager.startRequest(messageService.getUnReadCount(), new BaseRequestListener<UnReadMessageCountBean>() {
            @Override
            public void onSuccess(UnReadMessageCountBean result) {
                super.onSuccess(result);
                if (result != null) {

                    DLog.i("MainActivity.getNum:" + result.getNum());

                    MyStatus status = new MyStatus();
                    status.messageCount = result.getNum();
                    EventBus.getDefault().post(status);
                }
            }
        });

        //at 20190114 废弃以前的用户状态接口标记的未读消息数量，改为新的专用未读消息接口 by hanQ
//        IUserService mUserService = ServiceManager.getInstance().createService(IUserService.class);
//        APIManager.startRequest(mUserService.getMyStatus(), new BaseRequestListener<MyStatus>() {
//            @Override
//            public void onSuccess(MyStatus myStatus) {
//                if (myStatus != null) {
//                    EventBus.getDefault().post(myStatus);
//                }
//            }
//        });

    }

    private void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.KEY_EXTROS, true);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void publishInfo(EventMessage eventMessage) {
        if (eventMessage.getEvent() == Event.PUBLISH_NEW) {
            if (!SessionUtil.getInstance().isLogin()) {
                toLogin();
                return;
            }

            if (mDialog == null) {
                mDialog = new PublishDialog(this);
                mDialog.setOnClickCallBack(new PublishCallBackImpl());
            }
            mDialog.show();
        }
    }

    public class PublishCallBackImpl implements PublishDialog.onClickCallBack {

        @Override
        public void onTakePic() {
            gotoSelectPic(true);
        }

        @Override
        public void onTakeVideo() {
            gotoSelectPic(false);
        }

        @Override
        public void onHistoryClick() {
            Intent intent = new Intent(MainActivity.this, PublishHisActivity.class);
            startActivity(intent);
        }
    }

    private void gotoSelectPic(final boolean isPic) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    gotoMatisseActivity(isPic);
                } else {
                    Toast.makeText(MainActivity.this, "权限拒绝，无法使用，请打开权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gotoMatisseActivity(boolean isPic) {
        Matisse.from(this)
                .choose(isPic ? MimeType.ofImage() : MimeType.ofVideo())
                .captureStrategy(
                        new CaptureStrategy(true, "com.dodomall.ddmall.fileprovider"))
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(isPic ? 9 : 1)
                .imageEngine(new PicassoEngine())
                .forResult(isPic ? REQUEST_PIC_CHOOSE : REQUEST_VIDEO_CHOOSE);
    }

    public static void goBack(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
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
