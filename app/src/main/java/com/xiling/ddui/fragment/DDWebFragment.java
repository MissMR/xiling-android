package com.xiling.ddui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xiling.R;
import com.xiling.ddui.custom.DDMPtrUIHandler;
import com.xiling.ddui.custom.NoScrollWebView;
import com.xiling.ddui.tools.DLog;
import com.xiling.module.community.BetterPtrClassicFrameLayout;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class DDWebFragment extends BaseFragment {

    private String cateName = "";
    private String url = "";

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @BindView(R.id.refreshLayout)
    BetterPtrClassicFrameLayout mPullRefresh;

    @BindView(R.id.sWebView)
    NoScrollWebView sWebView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DLog.i(cateName + ".onCreateView:" + url);
        View rootView = inflater.inflate(R.layout.fragment_dd_web, container, false);
        ButterKnife.bind(this, rootView);

        initView();
        initWebView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (!TextUtils.isEmpty(url)) {
            sWebView.loadUrl(url);
        } else {
            DLog.i("url is empty");
        }

        String fantasy = sWebView.getSettings().getFantasyFontFamily();
        String cursive = sWebView.getSettings().getCursiveFontFamily();
        String serif = sWebView.getSettings().getSerifFontFamily();
        String sansSerif = sWebView.getSettings().getSansSerifFontFamily();
        String standard = sWebView.getSettings().getStandardFontFamily();

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DLog.i(cateName + ".onCreate:" + url);
    }

    @Override
    public void onResume() {
        super.onResume();
//        DLog.i(cateName + ".onResume:" + url);
    }

    void onRefresh() {
        sWebView.loadUrl(url);
    }

    void initView() {

        DDMPtrUIHandler uiHandler = new DDMPtrUIHandler(getContext());
        int color = Color.parseColor("#FF4647");
        uiHandler.setBackground(color);

        mPullRefresh.setHeaderView(uiHandler);
        mPullRefresh.addPtrUIHandler(uiHandler);

        //设置下拉刷新控件的事件处理
        mPullRefresh.setPtrHandler(new PtrHandler() {
            //需要加载数据时触发
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onRefresh();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    void initWebView() {

        WebSettings settings = sWebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        sWebView.setWebViewClient(client);
        sWebView.setWebChromeClient(chromeClient);

        //禁用浏览器长按
        sWebView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    WebViewClient client = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            DLog.d(cateName + ".shouldOverrideUrlLoading:" + request.getUrl());
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            DLog.d(cateName + ".onPageStarted:" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            DLog.d(cateName + ".onPageFinished:" + url);
        }
    };

    WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
//            DLog.d(cateName + ".onProgressChanged:" + newProgress);
            if (newProgress == 100) {
                mPullRefresh.refreshComplete();
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
//            DLog.d(cateName + ".onReceivedTitle:" + title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
//            DLog.d(cateName + ".onReceivedIcon");
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            ToastUtil.success(message);
//            return super.onJsAlert(view, url, message, result);
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//            DLog.d("[Javascript]" + consoleMessage.messageLevel() + "," + consoleMessage.message());
            return true;
        }
    };

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(EventMessage message) {
        if (message.getEvent().equals(Event.loginSuccess) || message.getEvent().equals(Event.logout)
                || message.getEvent().equals(Event.becomeStoreMaster)) {
            onRefresh();
        }
    }
}
