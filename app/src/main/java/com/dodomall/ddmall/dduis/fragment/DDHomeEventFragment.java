package com.dodomall.ddmall.dduis.fragment;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.DDHomeBanner;
import com.dodomall.ddmall.ddui.config.AppConfig;
import com.dodomall.ddmall.ddui.custom.DDJavaScriptBridge;
import com.dodomall.ddmall.ddui.custom.NoScrollWebView;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.util.WebViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.magicviewpager.transformer.AlphaPageTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DDHomeEventFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {

    private String url = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @BindView(R.id.layout_refresh)
    SmartRefreshLayout mPullRefresh;

    @BindView(R.id.swv_web_view)
    NoScrollWebView nsWebView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_event, container, false);
        ButterKnife.bind(this, view);

        mPullRefresh.setEnableRefresh(true);
        mPullRefresh.setEnableLoadMore(false);

        mPullRefresh.setOnRefreshListener(this);
        mPullRefresh.setOnLoadMoreListener(this);

        initWebView();
        loadData();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void loadData() {
        nsWebView.loadUrl(WebViewUtil.buildUrl(url));
    }

    //刷新数据
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        DLog.i("刷新数据...");
        loadData();
    }

    //加载更多
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        DLog.i("加载更多...");
        nsWebView.webLoadMore();
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
                mPullRefresh.finishRefresh();
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
                //没有更多数据
                mPullRefresh.setNoMoreData(!hasMore);
            }
        });
    }


}
