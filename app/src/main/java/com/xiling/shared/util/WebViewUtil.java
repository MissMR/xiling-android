package com.xiling.shared.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.xiling.BuildConfig;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.basic.BaseWebViewClient;
import com.xiling.shared.service.JavascriptService;
import com.orhanobut.logger.Logger;

import static com.xiling.module.page.WebViewActivity.K_DDM;
import static com.xiling.module.page.WebViewActivity.V_DDM;

public class WebViewUtil {

    public static WebView createNewWebView(Context context) {
        return createNewWebView(context, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface"})
    public static WebView createNewWebView(Context context, ViewGroup.LayoutParams layoutParams) {
        WebView webView = new WebView(context);
        webView.setLayoutParams(layoutParams);
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
        webView.addJavascriptInterface(new JavascriptService(), "bridge");
        webView.setWebViewClient(new BaseWebViewClient());
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        return webView;
    }

    public static void configWebView(WebView webView) {
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
        webView.addJavascriptInterface(new JavascriptService(), "bridge");
        webView.setWebViewClient(new BaseWebViewClient());
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    public static void clearWebViewResource(@NonNull WebView webView) {
        Logger.i("清除 WebView 资源");
        ViewGroup parent = (ViewGroup) webView.getParent();
        if (parent != null) {
            parent.removeView(webView);
        }
        webView.getSettings().setJavaScriptEnabled(false);
        webView.removeAllViews();
        webView.setTag(null);
        webView.clearHistory();
        webView.destroy();
    }

    public static boolean compileUri(Uri uri) {
        Logger.e("Request Host:" + uri.getHost());
        return true;
    }

    public static void loadDataToWebView(WebView webView, String content) {
        if (!content.endsWith("<p><br/></p>")) {
            content += "<p><br/></p>";
        }
        content = String.format("<!DOCTYPE html><html lang=\"zh-CN\"><head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\"><style>body{padding:0;margin:0;}p{padding:0;margin:0;} p:last-child {line-height:0;} img{vertical-align:bottom;width:100%%;font-size:0;padding:0;margin:0;}</style></head><body>%s</body></html>", content);
        webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
    }

    public static String buildUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            try {
                Uri uri = Uri.parse(url);
                if (!uri.getQueryParameterNames().contains(K_DDM)) {
                    if (!url.contains("?")) {
                        url += "?" + K_DDM + "=" + V_DDM;
                    } else {
                        if (url.endsWith("&")) {
                            url += K_DDM + "=" + V_DDM;
                        } else {
                            url += "&" + K_DDM + "=" + V_DDM;
                        }
                    }
                } else if (!uri.getQueryParameterNames().contains("func")) {
                    url += "&func=" + BuildConfig.H5_FUNC;
                }

                if (SessionUtil.getInstance().isLogin() && !url.contains("inviteCode")) {
                    url += "&inviteCode=" + SessionUtil.getInstance().getLoginUser().invitationCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DLog.d("buildUrl:" + url);

        return url;
    }
}
