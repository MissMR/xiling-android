package com.xiling.module.page;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.blankj.utilcode.utils.StringUtils;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.bean.WXShareInfo;
import com.xiling.ddui.config.UIConfig;
import com.xiling.ddui.custom.DDJavaScriptBridge;
import com.xiling.ddui.custom.DDShareWXDialog;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICommunityService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 逄涛
 * 嵌套H5页面
 */
public class WebViewActivity extends BaseActivity {

    private static final String kTitle = "WebViewActivity.kTitle";
    private static final String kColor = "WebViewActivity.kColor";
    private static final String kStyle = "WebViewActivity.kStyle";

    //客服模式
    private static final String kServiceMode = "WebViewActivity.kServiceMode";

    /**
     * 分享文案的常量数据
     */
    public class ShareText {
        /**
         * 默认分享数据
         */
        public static final int DEFAULT_SHARE_ICON = R.mipmap.ic_launcher;
        public static final String DEFAULT_SHARE_TITLE = "店多多全球精选平台";
        public static final String DEFAULT_SHARE_DESC = "听说只有长得好看的人才能看到这个分享~";
    }

    /**
     * 跳转到指定的URL
     *
     * @param context 上下文
     * @param url     URL地址
     */
    public static void jumpUrl(Context context, String url) {
        Intent lineIt = new Intent(context, WebViewActivity.class);
        lineIt.putExtra(Constants.Extras.WEB_URL, url);
        context.startActivity(lineIt);
    }

    /**
     * 跳转到指定的URL
     *
     * @param context 上下文
     * @param url     URL地址
     */
    public static void jumpUrl(Context context, String title, String url) {
        Intent lineIt = new Intent(context, WebViewActivity.class);
        lineIt.putExtra(Constants.Extras.WEB_URL, url);
        lineIt.putExtra(kTitle, title);
        context.startActivity(lineIt);
    }

    /**
     * 跳转到客服系统
     */
    public static void jumpService(Context context, String url) {
        Intent lineIt = new Intent(context, WebViewActivity.class);
        lineIt.putExtra(Constants.Extras.WEB_URL, url);
        lineIt.putExtra(kServiceMode, true);
        context.startActivity(lineIt);
    }

    /**
     * 跳转到指定的URL
     *
     * @param context 上下文
     * @param url     URL地址
     */
    public static void jumpUrl(Context context, int whiteStyle, String titleBarColor, String url) {
        Intent lineIt = new Intent(context, WebViewActivity.class);
        lineIt.putExtra(Constants.Extras.WEB_URL, url);
        lineIt.putExtra(kStyle, whiteStyle);
        lineIt.putExtra(kColor, titleBarColor);
        context.startActivity(lineIt);
    }

    public static void jumpUrl(Context context, int whiteStyle, String title, String titleBarColor, String url) {
        Intent lineIt = new Intent(context, WebViewActivity.class);
        lineIt.putExtra(Constants.Extras.WEB_URL, url);
        lineIt.putExtra(kTitle, title);
        lineIt.putExtra(kStyle, whiteStyle);
        lineIt.putExtra(kColor, titleBarColor);
        context.startActivity(lineIt);
    }

    @BindView(R.id.layoutWebview)
    FrameLayout mLayoutWebview;

    AgentWeb mAgentWeb = null;

    private String mTitle = "";
    private String mUrl = "";

    private String mColorValue = "";
    private int mColor = Color.WHITE;

    private int titleBarStyle = 0;//标题栏样式，0是黑色，1是白色

    private boolean serviceMode = false;

    //返回函数名 - 默认为空，直接返回
    private String backFinishFunc = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }

        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mUrl = getIntent().getStringExtra(Constants.Extras.WEB_URL);
        if (StringUtils.isEmpty(mUrl)) {
            ToastUtil.error("Web访问地址异常");
            finish();
            return;
        }

        //获取顶部导航栏颜色
        mColorValue = getIntent().getStringExtra(kColor);
        if (!TextUtils.isEmpty(mColorValue)) {
            try {
                mColor = Color.parseColor(mColorValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getHeaderLayout().setBackgroundColor(mColor);
        setStatusBarColor(mColor);

        titleBarStyle = getIntent().getIntExtra(kStyle, 0);
        if (titleBarStyle == 1) {
            setLeftWhite();
            getHeaderLayout().setTitleTextColor(Color.WHITE);
//            QMUIStatusBarHelper.setStatusBarLightMode(this);
        } else {
            setLeftBlack();
//            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        }
        //返回键拦截到onBackPressed做统一处理
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTitle = getIntent().getStringExtra(kTitle);
        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }

        serviceMode = getIntent().getBooleanExtra(kServiceMode, false);
        if (serviceMode) {
            initService();
        }

        initBridge();

        DLog.i("url : " + mUrl);
        DLog.i("buildUrl : " + mUrl);
        openWeb(buildUrl(mUrl));

//        getHeaderLayout().setRightDrawable(R.mipmap.icon_title_share_black);
//        getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ddJsb.uiThreadScreenshot(1);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.hideLoading();
    }

    public void initService() {
    }

    private ValueCallback mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    DDJavaScriptBridge ddJsb = null;

    void initBridge() {
        ddJsb = new DDJavaScriptBridge(this);
        //设置活动分享事件处理
        ddJsb.setActivityListener(new DDJavaScriptBridge.DDBActivityListener() {
            @Override
            public void onActivityShareToWX(WXShareInfo shareInfo) {
                if (shareInfo.getShareType() == WXShareInfo.EventShareValue.FREE_BUY) {
                    getFreeBuyShareLink(shareInfo);
                } else {
                    showDefaultShare(shareInfo);
                }
            }
        });
        //设置标题栏分享按钮状态的事件处理
        ddJsb.setTitleBarListener(new DDJavaScriptBridge.DDBTitleBarListener() {
            @Override
            public void onTitleShareTypeChanged(final WXShareInfo shareInfo) {
                if (shareInfo.getShareType() == WXShareInfo.ShareValue.NONE) {
                    //不显示
                    getHeaderLayout().hideRightItem();
                } else if (shareInfo.getShareType() == WXShareInfo.ShareValue.DALIY) {
                    //每日上新
                    getHeaderLayout().setRightDrawable(R.mipmap.icon_title_share_black);
                    getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDailyShare(shareInfo);
                        }
                    });
                } else {
                    //默认分享
                    getHeaderLayout().setRightDrawable(R.mipmap.icon_title_share_black);
                    getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDefaultShare(shareInfo);
                        }
                    });
                }
            }

            @Override
            public void onTitleShareAction(final String funcName) {
                getHeaderLayout().setRightDrawable(R.mipmap.icon_title_share_black);
                getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ddJsb.callback(funcName);
                    }
                });
            }
        });
        //返回事件监听
        ddJsb.setBackFinishListener(new DDJavaScriptBridge.DDBBackFinishListener() {
            @Override
            public void onRegisterBackFunction(String backFuncName) {
                backFinishFunc = backFuncName;
            }

            @Override
            public void onBackFinish() {
                finish();
            }
        });
        ddJsb.setOAuthListener(new DDJavaScriptBridge.DDBOAuthListener() {
            @Override
            public void onOAuthRequest(String oauth, String memberId) {
                DLog.d("onOAuthRequest('" + oauth + "','" + memberId + "')");
                if (mAgentWeb != null) {
                    String js = "vue.onOAuthResponse('" + oauth + "','" + memberId + "')";
                    mAgentWeb.getJsEntraceAccess().quickCallJs(js);
                } else {
                    DLog.e("onOAuthRequest agentWeb is null.");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mAgentWeb.back()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAgentWeb != null) {
            mAgentWeb.destroy();
        }
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    public static final String K_DDM = "xl_from";
    public static final String V_DDM = "" + ICommunityService.DEVICE_TYPE;

    public String buildUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            try {
                Uri uri = Uri.parse(url);

                //强制拼接平台类型
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
                }

                //强制链接拼接 FUNC
                if (!uri.getQueryParameterNames().contains("func")) {
                    url += "&func=" + BuildConfig.H5_FUNC;
                }

                //强制拼接邀请码
                if (SessionUtil.getInstance().isLogin() && !url.contains("inviteCode")) {
                    url += "&inviteCode=" + SessionUtil.getInstance().getLoginUser().invitationCode;
                }

                if (!url.startsWith("http")) {
                    url = "http://" + url;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //URL为空的时候进入空白页
            url = "about:blank";
        }

        DLog.d("buildUrl:" + url);

        return url;
    }

    private void openWeb(String url) {
        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(mLayoutWebview, new FrameLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()
                .setIndicatorColorWithHeight(Color.parseColor(UIConfig.kMainColor), 0)
                .setWebChromeClient(new WebChromeClient() {
                    @Override
                    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                        DLog.i("onConsoleMessage:" + consoleMessage.message());
                        return true;
                    }

                    @Override
                    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                        mUploadCallbackAboveL = filePathCallback;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        WebViewActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
                        return true;
                    }
                })
                .setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        mUrl = url;
                        return super.shouldOverrideUrlLoading(view, url);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        mUrl = request.getUrl().toString();
                        return super.shouldOverrideUrlLoading(view, request);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        view.loadUrl("javascript:sessionStorage.setItem('xl_from', '2')");
                        super.onPageFinished(view, url);
                    }
                })
                .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        title = title.replace(".html", "");
                        //2018/12/29 需求变更，如果上层传入的title有值则不关心title改变
                        if (TextUtils.isEmpty(mTitle)) {
                            setTitle(title);
                            mTitle = title;
                        }
                    }
                }) //设置 Web 页面的 title 回调
                .createAgentWeb()
                .ready()
                .go(url);
        //增加WebView
        ddJsb.setWebView(mAgentWeb.getWebCreator().get());
        //增加JSB
        mAgentWeb.getJsInterfaceHolder().addJavaObject(DDJavaScriptBridge.NAME, ddJsb);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {

            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }

    /**
     * 获取0元购的分享链接地址
     */
    public void getFreeBuyShareLink(final WXShareInfo shareInfo) {

        IUserService userService = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(userService.getFreeBuyLink("" + shareInfo.getShareType()), new RequestListener<String>() {
            @Override
            public void onStart() {
                ToastUtil.showLoading(context);
            }

            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                showFreeBuyShare(shareInfo, result);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.hideLoading();
            }

            @Override
            public void onComplete() {
                ToastUtil.hideLoading();
            }
        });
    }

    /**
     * 进入0元购的分享逻辑
     */
    public void showFreeBuyShare(WXShareInfo shareInfo, String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("type")) {
                if (!url.contains("?")) {
                    url += "?type=" + shareInfo.getShareType();
                } else {
                    url += "&type=" + shareInfo.getShareType();
                }
            }
        }
        showShareWX(shareInfo.getImgUrl(), shareInfo.getTitle(), shareInfo.getDesc(), url);
    }

    /**
     * 进入每日上新分享逻辑
     */
    public void showDailyShare(WXShareInfo shareInfo) {
        if (TextUtils.isEmpty(shareInfo.getShareUrl())) {
            shareInfo.setShareUrl(mUrl);
        }
        showShareWX(shareInfo.getImgUrl(), shareInfo.getTitle(), shareInfo.getDesc(), buildUrl(shareInfo.getShareUrl()));
    }

    /**
     * 进入默认分享逻辑
     */
    public void showDefaultShare(WXShareInfo shareInfo) {
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = ShareText.DEFAULT_SHARE_TITLE;
        }
        if (shareInfo == null) {
            showShareWX(ShareText.DEFAULT_SHARE_ICON, mTitle, ShareText.DEFAULT_SHARE_DESC, buildUrl(mUrl));
        } else {
            String title = shareInfo.getTitle();
            if (TextUtils.isEmpty(title)) {
                title = mTitle;
            }
            String desc = shareInfo.getDesc();
            if (TextUtils.isEmpty(desc)) {
                desc = ShareText.DEFAULT_SHARE_DESC;
            }
            String iconUrl = shareInfo.getImgUrl();

            String shareUrl = shareInfo.getShareUrl();
            if (TextUtils.isEmpty(shareUrl)) {
                shareUrl = mUrl;
            }

            if (TextUtils.isEmpty(iconUrl)) {
                showShareWX(ShareText.DEFAULT_SHARE_ICON, title, desc, buildUrl(shareUrl));
            } else {
                showShareWX(iconUrl, title, desc, buildUrl(shareUrl));
            }
        }
    }

    /**
     * 跳转到分享
     *
     * @param iconId
     * @param title
     * @param desc
     * @param url
     */
    public void showShareWX(int iconId, String title, String desc, String url) {
        DDShareWXDialog dialog = new DDShareWXDialog(this);
        dialog.setMode(DDShareWXDialog.Mode.Link);
        dialog.setThumbId(iconId);
        dialog.setTitle("" + title);
        dialog.setDesc("" + desc);
        dialog.setLinkUrl("" + url);
        dialog.show();
    }

    /**
     * 跳转到分享
     *
     * @param iconUrl
     * @param title
     * @param desc
     * @param url
     */
    public void showShareWX(String iconUrl, String title, String desc, String url) {
        DDShareWXDialog dialog = new DDShareWXDialog(this);
        dialog.setMode(DDShareWXDialog.Mode.Link);
        dialog.setThumbUrl("" + iconUrl);
        dialog.setTitle("" + title);
        dialog.setDesc("" + desc);
        dialog.setLinkUrl("" + url);
        dialog.show();
    }
}
