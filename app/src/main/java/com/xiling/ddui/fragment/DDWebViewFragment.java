package com.xiling.ddui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xiling.R;
import com.xiling.ddui.config.AppConfig;
import com.xiling.ddui.custom.DDJavaScriptBridge;
import com.xiling.ddui.service.HtmlService;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.Constants;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.WebViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by Jigsaw at 2018/11/20
 * 成为店主导航页 399
 */
public class DDWebViewFragment extends Fragment {

    @BindView(R.id.web_view)
    WebView webView;
    Unbinder unbinder;

    private String mURL = HtmlService.BESHOPKEPPER;

    public static DDWebViewFragment newInstance(String url) {
        DDWebViewFragment fragment = new DDWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Extras.WEB_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mURL = getArguments().getString(Constants.Extras.WEB_URL, HtmlService.BESHOPKEPPER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_guide, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {

        WebSettings settings = webView.getSettings();
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

        //在Chrome中输入 chrome://inspect 可以进行远程调试
        WebView.setWebContentsDebuggingEnabled(AppConfig.DEBUG);

        DDJavaScriptBridge bridge = new DDJavaScriptBridge((Activity) getContext());
        bridge.setWebView(webView);
        //增加DDJavaScriptBridge
        webView.addJavascriptInterface(bridge, DDJavaScriptBridge.NAME);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                DLog.d("onReceivedError:" + failingUrl + ",errorCode:" + errorCode);
                DLog.d("onReceivedError:" + failingUrl + ",errorCode:" + errorCode);
                if (!checkError(errorCode)) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }
            }

            @TargetApi(23)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (!checkError(error.getErrorCode())) {
                    super.onReceivedError(view, request, error);
                }

            }
        });

        //禁用浏览器长按
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webView.loadUrl(WebViewUtil.buildUrl(mURL));
    }

    private void showEmptyView() {
        webView.loadUrl("about:_blank");
    }

    public boolean checkError(int errorCode) {
        if (errorCode == -2 || errorCode == -5) {
            showEmptyView();
            ToastUtil.error("似乎已断开与互联网的链接。");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
