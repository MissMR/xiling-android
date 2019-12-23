package com.xiling.ddui.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.utils.TimeUtils;
import com.xiling.R;
import com.xiling.ddui.bean.DDHomeBanner;
import com.xiling.ddui.bean.DHomeBean;
import com.xiling.ddui.config.AppConfig;
import com.xiling.ddui.custom.DDFooterView;
import com.xiling.ddui.custom.DDJavaScriptBridge;
import com.xiling.ddui.custom.NestScrollView;
import com.xiling.ddui.custom.NoScrollWebView;
import com.xiling.ddui.manager.BannerManager;
import com.xiling.ddui.manager.OrderToastManager;
import com.xiling.ddui.service.HtmlService;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.UITools;
import com.xiling.ddui.tools.ViewAnimationUtils;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.PopupOrderList;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IPageService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.HomeWebManager;
import com.xiling.shared.util.ToastUtil;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.nineoldandroids.view.ViewHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.magicviewpager.transformer.AlphaPageTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;

/**
 * 今日特价Fragment
 */
public class DDTodayFragment extends BaseFragment implements NestScrollView.OnScrollChangedCallback, OnLoadMoreListener, OnRefreshListener {

    /**
     * 第一个分类的特效回调事件
     * <p>
     * 用来改变顶部区域的表现方式
     */
    public interface DDHomeTitleBarListener {
        /**
         * 头部标题栏变化
         *
         * @param alpha 透明度
         */
        void onTitleBarAlphaChanged(float alpha);

        /**
         * 是否显示头部快捷入口
         */
        void onTitleShortcutChanged(boolean visibility, float dy);
    }

    DDHomeTitleBarListener titleBarAlphaListener = null;

    public void setTitleBarListener(DDHomeTitleBarListener titleBarAlphaListener) {
        this.titleBarAlphaListener = titleBarAlphaListener;
    }

    private DDJavaScriptBridge.DDHomeShortcutListener homeShortcutListener = null;

    public void setHomeShortcutListener(DDJavaScriptBridge.DDHomeShortcutListener homeShortcutListener) {
        this.homeShortcutListener = homeShortcutListener;
    }

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mPullRefresh;

    @BindView(R.id.mainScrollView)
    NestScrollView mainScrollView = null;

    @BindView(R.id.leftMaskImageView)
    ImageView leftMaskImageView;

    @BindView(R.id.rightMaskImageView)
    ImageView rightMaskImageView;

    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner = null;

    @BindView(R.id.nsWebView)
    NoScrollWebView nsWebView = null;

    @BindView(R.id.toastPanel)
    RelativeLayout toastPanel = null;

    @BindView(R.id.avatarImageView)
    SimpleDraweeView avatarImageView;

    @BindView(R.id.nameTextView)
    TextView nameTextView;

    @BindView(R.id.timeTextView)
    TextView timeTextView;

    //Banner数据源
    ArrayList<DDHomeBanner> banners = new ArrayList<>();
    //Banner图片数据源
    ArrayList<String> bannerImages = new ArrayList<>();

    float titleBarHeight = 0;

    private HomeWebManager mHomeWebManager;
    private OrderToastManager mOrderToastManager;

    public static DDTodayFragment newInstance() {
        Bundle args = new Bundle();
        DDTodayFragment fragment = new DDTodayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_dd_today, container, false);
        ButterKnife.bind(this, rootView);
        mHomeWebManager = new HomeWebManager(getActivity());
        mOrderToastManager = OrderToastManager.share();
        EventBus.getDefault().register(this);

        mOrderToastManager.setListener(new OrderToastManager.OrderToastListener() {
            @Override
            public void onToastOrder(PopupOrderList.DatasEntity item) {
                String time = TimeUtils.getFitTimeSpan(TimeUtils.string2Millis(item.createDate), TimeUtils.getNowTimeMills(), 4);
                if (item != null && !TextUtils.isEmpty(time)) {

                    //名字为空的时候不显示订单信息
                    if (TextUtils.isEmpty(item.nickName)) {
                        ViewAnimationUtils.showAndHiddenAnimation(toastPanel, ViewAnimationUtils.AnimationState.STATE_HIDDEN, 0);
                        return;
                    }

                    //设置头像
                    FrescoUtil.setImageSmall(avatarImageView, item.headImage);
                    //设置昵称
                    nameTextView.setText("" + item.nickName);
                    //设置时间
                    timeTextView.setText("来自" + time + "之前的订单");

                    ViewAnimationUtils.showAndHiddenAnimation(toastPanel, ViewAnimationUtils.AnimationState.STATE_SHOW, 1000);
                } else {
                    ViewAnimationUtils.showAndHiddenAnimation(toastPanel, ViewAnimationUtils.AnimationState.STATE_HIDDEN, 1000);
                }
            }

            @Override
            public void onToastHide() {
                ViewAnimationUtils.showAndHiddenAnimation(toastPanel, ViewAnimationUtils.AnimationState.STATE_HIDDEN, 200);
            }
        });

        int statusBarHeight = UITools.getStatusBarHeight(getContext());
        float topHeight = getContext().getResources().getDimension(R.dimen.dd_home_top_height);
        titleBarHeight = topHeight + statusBarHeight;

        initView();
        loadData();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mOrderToastManager.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mOrderToastManager.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(EventMessage message) {
        if (message.getEvent().equals(Event.loginSuccess) || message.getEvent().equals(Event.logout)
                || message.getEvent().equals(Event.becomeStoreMaster)) {
            onRefresh();
        }
    }

    void loadLocalBanner() {
        ArrayList<DDHomeBanner> data = BannerManager.share().getData();
        if (data != null) {
            banners = data;
            setBannerData();
        }
        loadNetData();
    }

    private void loadHtmlData() {
        //加载H5首页资源
        nsWebView.loadUrl(HtmlService.HOME_WEB);
    }

    ViewPager.OnPageChangeListener bannerChangeListener = new ViewPager.OnPageChangeListener() {

        int lastPosition = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
//            DLog.d("onPageScrolled:position=" + position + ",positionOffset=" + positionOffset + ",positionOffsetPixels=" + positionOffsetPixels);
            this.lastPosition = position;
            if (positionOffset == 0) {
                return;
            }
            setBannerBackgroundBlur(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
//            DLog.d("onPageSelected:position=" + position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            DLog.d("onPageScrollStateChanged:state=" + state);
            if (state != 1) {
                setBannerBackgroundBlur(lastPosition, 0);
            }
        }
    };


    //刷新数据
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        DLog.i("刷新数据...");
        onRefresh();
        mPullRefresh.setEnableLoadMore(true);
    }

    //加载更多
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        DLog.i("加载更多...");
        nsWebView.webLoadMore();
//        mSmartRefreshLayout.finishLoadMore(100);
    }


    void initView() {

        //初始化WebView
        initWebView();

        //设置拉动事件监听
        mainScrollView.setOnScrollChangedCallback(this);

        //设置头部布局
        ClassicsHeader header = new ClassicsHeader(getContext());
        //改变提示颜色
        header.setAccentColor(Color.WHITE);
        mPullRefresh.setRefreshHeader(header);
        //设置脚部布局
        DDFooterView footer = new DDFooterView(getContext());
        footer.setBackgroundColor(Color.parseColor("#f2f2f2"));
        mPullRefresh.setRefreshFooter(footer);

        //设置刷新的事件
        mPullRefresh.setOnRefreshListener(this);

        //开启上拉加载更多
        mPullRefresh.setEnableLoadMore(true);
        //设置加载更多的事件
        mPullRefresh.setOnLoadMoreListener(this);

    }

    private void setBannerBackgroundBlur(int position, float positionOffset) {
        int size = bannerImages.size();
        if (size > 0) {
            int realIndex = position % size;
            int preIndex = (position + 1) % size;
//                DLog.i("realIndex:" + realIndex + "=>" + preIndex);
            if (preIndex == 0) {
                Bitmap leftBitmap = maskBitmaps.get(size - 1);
                Bitmap rightBitmap = maskBitmaps.get(0);
                leftMaskImageView.setImageBitmap(leftBitmap);
                rightMaskImageView.setImageBitmap(rightBitmap);
            } else {
                if (realIndex < size) {
                    Bitmap leftBitmap = maskBitmaps.get(realIndex);
                    Bitmap rightBitmap = maskBitmaps.get(realIndex + 1);
                    leftMaskImageView.setImageBitmap(leftBitmap);
                    rightMaskImageView.setImageBitmap(rightBitmap);
                } else {
                    Bitmap leftBitmap = maskBitmaps.get(size - 1);
                    Bitmap rightBitmap = maskBitmaps.get(size - 1);
                    leftMaskImageView.setImageBitmap(leftBitmap);
                    rightMaskImageView.setImageBitmap(rightBitmap);
                }
            }
            animateAlpha(leftMaskImageView, rightMaskImageView, realIndex, positionOffset);
        }
    }

    private void animateAlpha(View left, View right, int position, float positionOffset) {
        if (left != null) {
            ViewHelper.setAlpha(left, 1 - positionOffset);
        }
        if (right != null) {
            ViewHelper.setAlpha(right, positionOffset);
        }
    }

    private void initWebView() {
        WebSettings settings = nsWebView.getSettings();

        settings.setJavaScriptEnabled(true);

        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);

        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);

        //        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);

        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //在Chrome中输入 chrome://inspect 可以进行远程调试
        WebView.setWebContentsDebuggingEnabled(AppConfig.DEBUG);

        //设置分类数据源
        nsWebView.setHomeShortcutDataListener(homeShortcutListener);
        nsWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//                DLog.i("js:" + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        nsWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                DLog.d("shouldOverrideUrlLoading:" + request.getUrl().toString());
                view.loadUrl(request.getUrl().toString());
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                DLog.d("onPageFinished:" + url);
//                nsWebView.resize();
//                nsWebView.webReload();
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                DLog.d("onReceivedError:" + failingUrl + ",errorCode:" + errorCode);
                if (!nsWebView.checkError(errorCode)) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }
            }

            @TargetApi(23)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (!nsWebView.checkError(error.getErrorCode())) {
                    super.onReceivedError(view, request, error);
                }
            }
        });

        //禁用浏览器长按
        nsWebView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        //对接浏览器的完成加载更多事件
        nsWebView.setWebViewLoadMoreListener(new DDJavaScriptBridge.DDBLoadMoreListener() {
            @Override
            public void onFinishLoadMore(boolean hasMore) {
                DLog.d("onFinishLoadMore:" + hasMore);
                //设置是否结束上拉动画
                mPullRefresh.finishLoadMore(0);
                //设置是否还有更多数据
//                mSmartRefreshLayout.setEnableLoadMore(hasMore);
                //没有更多数据
                mPullRefresh.setNoMoreData(!hasMore);
            }
        });

        //配置轮播图
        convenientBanner.setCanLoop(true);
        convenientBanner.startTurning(5000);
        convenientBanner.setOnPageChangeListener(bannerChangeListener);
        convenientBanner.setPageTransformer(new AlphaPageTransformer());
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position < banners.size()) {
                    DDHomeBanner banner = banners.get(position);
                    String event = banner.getEvent();
                    String target = banner.getTarget();
                    DDHomeBanner.process(getContext(), event, target);
                }
            }
        });
    }

    public void loadData() {
        loadHtmlData();
        loadLocalBanner();
        loadNetData();
    }

    SparseArray<Bitmap> maskBitmaps = new SparseArray<>();

    void addMaskBitmap(int position, Bitmap bitmap) {
        maskBitmaps.append(position, bitmap);
    }

    void makeMaskBitmap(final int position, String imageUrl, final ImageView maskImageView) {
        //生成图片
        getFrescoCacheBitmap(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    try {
                        Bitmap bitmap = (Bitmap) msg.obj;
                        Bitmap sourceMap = Bitmap.createBitmap(bitmap);
                        if (sourceMap != null && !sourceMap.isRecycled()) {
                            int scaleFactor = 12;
                            Bitmap overlay = Bitmap.createScaledBitmap(sourceMap, sourceMap.getWidth() / scaleFactor, sourceMap.getHeight() / scaleFactor, false);
                            if (overlay != null && !overlay.isRecycled()) {
                                try {
                                    Blurry.with(getContext()).async(new Blurry.ImageComposer.ImageComposerListener() {
                                        @Override
                                        public void onImageReady(BitmapDrawable drawable) {
                                            addMaskBitmap(position, drawable.getBitmap());
                                        }
                                    }).from(overlay).into(maskImageView);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                DLog.e("缩放之后的图片是null");
                            }
                        } else {
                            DLog.e("高斯模糊之前的图片是null");
                        }
                    } catch (Exception e) {
                        //fix bugly Id 6902
                        e.printStackTrace();
                    }
                }
            }
        }, imageUrl, getContext());
    }

    public class ImageViewHolder implements Holder<String> {

        SimpleDraweeView bannerImageView = null;
        ImageView maskImageView = null;

        public ImageViewHolder() {

        }

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_home_banner, null);
            bannerImageView = view.findViewById(R.id.bannerImageView);
            maskImageView = view.findViewById(R.id.maskImageView);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
//            FrescoUtil.setImageSmall(bannerImageView, data);
//            FrescoUtil.setImage(bannerImageView, data);
            if (!TextUtils.isEmpty(data)) {
                bannerImageView.setImageURI(data);
                makeMaskBitmap(position, data, maskImageView);
            }
        }

    }

    public static void getFrescoCacheBitmap(final Handler handler, String
            imageUrl, Context context) {
        Uri uri = Uri.parse(imageUrl);
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(Bitmap bitmap) {
                if (bitmap == null) {
                    handler.sendEmptyMessage(0);
                    return;
                }
                Bitmap source = Bitmap.createBitmap(bitmap);
                Message message = new Message();
                message.what = 1;
                message.obj = source;
                handler.sendMessage(message);
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                handler.sendEmptyMessage(0);
            }
        }, CallerThreadExecutor.getInstance());
    }

    /**
     * 加载网络数据
     */
    void loadNetData() {
        IPageService mPageService = ServiceManager.getInstance().createService(IPageService.class);
        APIManager.startRequest(mPageService.getDHomeData(), new BaseRequestListener<DHomeBean>() {
            @Override
            public void onStart() {
                ToastUtil.showLoading(getContext());
            }

            @Override
            public void onSuccess(DHomeBean result) {
                super.onSuccess(result);
                banners = result.getBanner();
                BannerManager.share().setData(banners);
                setBannerData();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.hideLoading();
                setDefaultBanner();
                mPullRefresh.finishRefresh();
            }

            @Override
            public void onComplete() {
                ToastUtil.hideLoading();
                delayHandler.sendEmptyMessageDelayed(0, 1000);
            }
        });
    }

    Handler delayHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mPullRefresh.finishRefresh();
            }
        }
    };

    public void onRefresh() {
        nsWebView.reload();
        loadLocalBanner();
    }

    public void setDefaultBanner() {
//        bannerImages.clear();
//        bannerImages.add("http://tools.ldmf.net/res/banner1.png");
//        setBanner();
    }

    /**
     * 设置Banner数据
     */
    public void setBannerData() {
        bannerImages.clear();
        for (DDHomeBanner banner : banners) {
            bannerImages.add(banner.getImgUrl());
        }
        setBanner();
    }


    /**
     * 设置Banner数据到界面
     */
    public void setBanner() {
        convenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public ImageViewHolder createHolder() {
                return new ImageViewHolder();
            }
        }, bannerImages);
//        convenientBanner.stopTurning();
        convenientBanner.setcurrentitem(0);
        firstImageHandler.sendEmptyMessageDelayed(0, 100);

        setBannerBackgroundBlur(0, 0);
    }

    Handler firstImageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //生成第一张图片
            if (bannerImages.size() > 0) {
                String imageUrl = bannerImages.get(0);
                getFrescoCacheBitmap(new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            Bitmap bitmap = (Bitmap) msg.obj;
                            Bitmap source = Bitmap.createBitmap(bitmap);
                            int scaleFactor = 12;
                            Bitmap overlay = Bitmap.createScaledBitmap(source, source.getWidth() / scaleFactor, source.getHeight() / scaleFactor, false);
                            Blurry.with(getContext()).async(new Blurry.ImageComposer.ImageComposerListener() {
                                @Override
                                public void onImageReady(BitmapDrawable drawable) {
                                    addMaskBitmap(0, drawable.getBitmap());
                                    leftMaskImageView.setImageBitmap(drawable.getBitmap());
                                }
                            }).from(overlay).into(leftMaskImageView);
                        }
                    }
                }, imageUrl, getContext());
            }
        }
    };

    @Override
    public void onScroll(int dx, int dy) {
//        DLog.w("onScroll:" + dx + "," + dy);

//        if (dx > dy) {
//            //横向滚动大于竖向滚动的时候不要下拉刷新
//            mSmartRefreshLayout.finishRefresh();
//            mSmartRefreshLayout.finishLoadMore();
//            mSmartRefreshLayout.setEnabled(false);
//        } else {
//            mSmartRefreshLayout.setEnabled(true);
//        }

        //改变标题栏颜色
        changeTitle(dy);
        //快捷入口
        changeShortcut(dy);
    }

    public void changeTitle(int nowY) {
        float alpha = 0;
        if (nowY > titleBarHeight) {
            //标题栏完全显示
            alpha = 1;
        } else {
            if (nowY == 0) {
                //隐藏标题栏颜色
                alpha = 0;
            } else {
                //修改状态栏颜色透明度
                alpha = (float) ((nowY * 1.0) / titleBarHeight);
            }
        }
        if (titleBarAlphaListener != null) {
            titleBarAlphaListener.onTitleBarAlphaChanged(alpha);
        }
    }

    public void changeShortcut(int nowY) {
//        DLog.i("changeShortcut：" + nowY);
        int menuItem = ConvertUtil.dip2px(80);
        float shortCutHeight = titleBarHeight + menuItem;
        boolean isShowShortcut;
        if (nowY > shortCutHeight) {
            isShowShortcut = true;
        } else {
            isShowShortcut = false;
        }
        if (titleBarAlphaListener != null) {
            titleBarAlphaListener.onTitleShortcutChanged(isShowShortcut, nowY - shortCutHeight);
        }
    }


}
