package com.dodomall.ddmall.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.util.HomeWebManager;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.dodomall.ddmall.shared.util.WebViewUtil;
import com.facebook.stetho.common.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoActivity extends AppCompatActivity implements HomeWebManager.Callback {

    @BindView(R.id.web_view)
    WebView webView;
//    HomeWebManager mHomeWebManager;

    private String url = "http://192.168.1.27:8080/jigsaw/webzip.zip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        WebViewUtil.configWebView(webView);
//        mHomeWebManager = new HomeWebManager(this);
    }

    @OnClick(R.id.btn_unzip)
    public void onViewClicked() {
        ToastUtil.showLoading(this);
//        mHomeWebManager.execute(url);
    }

    @OnClick(R.id.btn_reload)
    public void onReload() {
        webView.clearCache(true);
        webView.reload();
    }

    @Override
    public void onSuccess(String webIndexPath) {
        LogUtil.i("load webview");
        webView.loadUrl("file://" + webIndexPath);
        ToastUtil.hideLoading();
    }

    @Override
    public void onFail(Throwable throwable) {

    }

}
