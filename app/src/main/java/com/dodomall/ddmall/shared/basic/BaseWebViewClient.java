package com.dodomall.ddmall.shared.basic;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.facebook.stetho.common.LogUtil;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.orhanobut.logger.Logger;
import com.dodomall.ddmall.MyApplication;
import com.dodomall.ddmall.shared.util.WebViewUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;

public class BaseWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Uri uri = Uri.parse(url);
        Logger.e("Request Url: " + url);
        if ("app".equals(uri.getScheme())) {
            return WebViewUtil.compileUri(uri);
        }
        return super.shouldOverrideUrlLoading(view, url);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return shouldInterceptRequest(view, request.getUrl().toString());
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse response = null;
        if (url.endsWith(".png")) {
            response = getWebResourceResponse(url, "image/png", ".png");
        } else if (url.endsWith(".gif")) {
            response = getWebResourceResponse(url, "image/gif", ".gif");
        } else if (url.endsWith(".jpg")) {
            response = getWebResourceResponse(url, "image/jpeg", ".jpg");
        } else if (url.endsWith(".jpeg")) {
            response = getWebResourceResponse(url, "image/jpeg", ".jpeg");
        } else if (url.endsWith(".js")) {
            response = getWebResourceResponse("text/javascript", "UTF-8", ".js");
        } else if (url.endsWith(".css")) {
            response = getWebResourceResponse("text/css", "UTF-8", ".css");
        } else if (url.endsWith(".html")) {
            response = getWebResourceResponse("text/html", "UTF-8", ".html");
        }
        return response;
    }

    private WebResourceResponse getWebResourceResponse(String url, String mime, String suffix) {
        WebResourceResponse response = null;
        try {
            File file = new File(getFilePath(mime) + Hashing.md5().hashString(url, Charset.defaultCharset()) + suffix);
            if (!file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                response = new WebResourceResponse(mime, "UTF-8", fileInputStream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getFilePath(String mime) {
        String dirName = mime.startsWith("image") ? File.separator + "images" : "";
        String splashTargetPath = MyApplication.getInstance().getCacheDir().getPath() + dirName + File.separator;
        Logger.e("File Path: " + splashTargetPath);
        File file = new File(splashTargetPath);
        if (!file.exists()) {
            if (mkdirs(file)) {
                return splashTargetPath;
            }
        }
        File tempDir = Files.createTempDir();
        if (mkdirs(tempDir)) {
            return tempDir.getAbsolutePath();
        }
        return MyApplication.getInstance().getCacheDir().getAbsolutePath();
    }

    private boolean mkdirs(File file) {
        if (!file.exists()) {
            try {
                return file.mkdirs();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        LogUtil.i("webview onPageStarted");
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        LogUtil.i("webview onPageFinished");
    }
}
