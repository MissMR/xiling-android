package com.xiling.ddui.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.xiling.BuildConfig;
import com.xiling.ddui.activity.BrandActivity;
import com.xiling.ddui.activity.CategorySecondActivity;
import com.xiling.ddui.activity.DDCategoryActivity;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.activity.MyFollowersActivity;
import com.xiling.ddui.activity.RealAuthActivity;
import com.xiling.ddui.activity.XLMemberCenterActivity;
import com.xiling.ddui.bean.HomeShortcutBean;
import com.xiling.ddui.bean.TitleBarValueBean;
import com.xiling.ddui.bean.WXShareInfo;
import com.xiling.ddui.manager.FreeEventManager;
import com.xiling.ddui.manager.PosterMaker;
import com.xiling.ddui.manager.ProductShareManager;
import com.xiling.ddui.tools.AppTools;
import com.xiling.ddui.tools.DBase64;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ImageTools;
import com.xiling.dduis.dialog.DDWebViewShareImageDialog;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.page.WebViewActivity;
import com.xiling.module.user.LoginActivity;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.User;
import com.xiling.shared.util.ClipboardUtil;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.xiling.ddui.custom.DDJavaScriptBridge.DDJavaScriptType.FINISH_LOAD_MORE;
import static com.xiling.ddui.custom.DDJavaScriptBridge.DDJavaScriptType.JUMP_ACTIVITY;
import static com.xiling.ddui.custom.DDJavaScriptBridge.DDJavaScriptType.SCREENSHOT;
import static com.xiling.ddui.custom.DDJavaScriptBridge.DDJavaScriptType.SET_HOME_SHORTCUT;
import static com.xiling.ddui.custom.DDJavaScriptBridge.DDJavaScriptType.SET_TITLE_BAR_SHARE_ACTION;
import static com.xiling.ddui.custom.DDJavaScriptBridge.DDJavaScriptType.SHARE_TO_WX;
import static com.xiling.ddui.custom.DDJavaScriptBridge.DDJavaScriptType.SHOW_SHARE_IMAGE_PANEL;

public class DDJavaScriptBridge {

    public interface DDBActivityListener {
        void onActivityShareToWX(WXShareInfo shareInfo);
    }

    public interface DDBBackFinishListener {
        void onRegisterBackFunction(String backFuncName);

        void onBackFinish();
    }

    public interface DDBTitleBarListener {
        void onTitleShareTypeChanged(WXShareInfo shareInfo);

        void onTitleShareAction(String funcName);
    }

    public interface DDBLoadMoreListener {
        void onFinishLoadMore(boolean hasMore);
    }

    public interface DDBOAuthListener {
        void onOAuthRequest(String userJson);
    }

    public interface DDHomeShortcutListener {
        void onGetHomeShortcutData(ArrayList<HomeShortcutBean> homeShortcuts);
    }

    public static final String NAME = "DDMallBridge";
    private Activity mActivity;
    private WebView webView = null;

    /*默认memberID*/
    public static final String DEFAULT_MEMBER_ID = "anonymous";

    public DDJavaScriptBridge(Activity activity) {
        this.mActivity = activity;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    //活动分享监听
    private DDBActivityListener mActivityListener = null;
    //标题栏分享按钮设置监听
    private DDBTitleBarListener mTitleBarListener = null;
    //设置加载更多事件监听
    private DDBLoadMoreListener mLoadMoreListener = null;
    //授权协议回调
    private DDBOAuthListener mOAuthListener = null;
    //首页快捷资源事件监听
    private DDHomeShortcutListener mHomeShortcutListener = null;
    // 界面返回事件监听
    private DDBBackFinishListener mBackFinishListener = null;

    public void setActivityListener(DDBActivityListener activityListener) {
        this.mActivityListener = activityListener;
    }

    public void setTitleBarListener(DDBTitleBarListener mTitleBarListener) {
        this.mTitleBarListener = mTitleBarListener;
    }

    public void setLoadMoreListener(DDBLoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    public void setOAuthListener(DDBOAuthListener mOAuthListener) {
        this.mOAuthListener = mOAuthListener;
    }

    public void setHomeShortcutListener(DDHomeShortcutListener mHomeShortcutListener) {
        this.mHomeShortcutListener = mHomeShortcutListener;
    }

    public void setBackFinishListener(DDBBackFinishListener mBackFinishListener) {
        this.mBackFinishListener = mBackFinishListener;
    }

    /**
     * 协议类型
     */
    public enum DDJavaScriptType {

        OPEN_PRODUCT,/*商品详情*/
        SHARE_PRODUCT_WITH_PROFIT,/*发起分享赚分享*/
        SHARE_SPU_WITH_PROFIT,/*发起分享SPU功能*/

        OPEN_HTML,/*打开网页*/

        ON_HEIGHT_CHANGED,/*网页高度改变*/

        GET_OAUTH,/*获取用户授权信息*/
        GET_HOST,/*获取API_HOST地址主机头*/

        TOAST,/*弹出提示泡*/

        SET_TITLE_BAR_ALPHA,/*设置标题栏透明度*/
        SET_TITLE_BAR_DARK_RES,/*设置标题栏黑白样式*/
        SET_TITLE_BAR_SHARE_TYPE,/*设置标题栏分享类型*/

        JUMP_ACTIVITY,/*跳转到指定的活动*/

        SHARE_TO_WX,/*分享到微信*/

        FINISH_LOAD_MORE,/*结束加载更多*/

        SET_HOME_SHORTCUT,/*设置首页快捷入口*/

        SCREENSHOT,/*网页截图*/

        SHOW_SHARE_IMAGE_PANEL,/*显示分享图片框 - 带预览*/

        SET_TITLE_BAR_SHARE_ACTION,/*设置标题栏右上角分享回调*/
    }

    public void callback(final String js) {
        if (!TextUtils.isEmpty(js)) {
            if (webView != null) {
                webView.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        DLog.d("exec js(" + js + ") return :" + value);
                    }
                });
            } else {
                DLog.e("callBack check webView is null.");
            }
        } else {
            DLog.e("callback js is empty.");
        }
    }

    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            DDJavaScriptType[] values = DDJavaScriptType.values();
            if (msg.what > values.length) {
                DLog.w("DDJavaScript指令超出范围");
                return;
            }

            DDJavaScriptType command = values[msg.what];
            switch (command) {
                case ON_HEIGHT_CHANGED:
                    int height = msg.arg1;
                    WebView webView = getWebView();
                    if (webView != null) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtil.dip2px(height));
                        webView.setLayoutParams(layoutParams);
                    } else {
                        DLog.w("webView is null.");
                    }
                    break;
                case SHARE_PRODUCT_WITH_PROFIT:
                    //分享赚
//                    String skuId = (String) msg.obj;
//                    ProductShareManager shareManager = new ProductShareManager(mActivity, skuId);
//                    shareManager.show();
//                    ToastUtil.error("错误：使用了已废弃的协议");
                    break;
                case SHARE_SPU_WITH_PROFIT:
                    String spuId = (String) msg.obj;
                    ProductShareManager shareManager = new ProductShareManager(mActivity, spuId);
                    shareManager.show();
                    break;
                case GET_OAUTH://OAuth回调
                    NewUserBean userBean = UserManager.getInstance().getUser();
                    String userJson = null;
                    if (userBean != null) {
                        userJson = new Gson().toJson(userBean);
                    }
                    WebView webView2 = getWebView();
                    if (webView2 != null) {
                        if (userJson == null){
                            webView2.evaluateJavascript("vue.onOAuthResponse("+ userJson + ")", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    DLog.i("getOAuth.onReceiveValue:" + s);
                                }
                            });
                        }else{
                            webView2.evaluateJavascript("vue.onOAuthResponse(" + "'" + userJson + "'" + ")", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    DLog.i("getOAuth.onReceiveValue:" + s);
                                }
                            });
                        }


                    } else {
                        if (mOAuthListener != null) {
                            mOAuthListener.onOAuthRequest(userJson);
                        } else {
                            DLog.w("no oauth response register!");
                        }
                    }

                    break;
                case GET_HOST://Host回调
                    String host = BuildConfig.BASE_URL;
                    DLog.i("getHost:" + host);
                    WebView webView3 = getWebView();
                    if (webView3 != null) {
                        DLog.d("vue.onHostResponse('" + host + "')");
                        webView3.evaluateJavascript("vue.onHostResponse('" + host + "')", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                DLog.i("getHost.onReceiveValue:" + s);
                            }
                        });
                    }
                    break;
                case SET_TITLE_BAR_ALPHA://设置标题栏透明度
                case SET_TITLE_BAR_DARK_RES://设置标题栏资源颜色
                    if (msg.obj != null && msg.obj instanceof TitleBarValueBean) {
                        EventBus.getDefault().post(msg.obj);
                    }
                    break;
                case JUMP_ACTIVITY:
                    if (msg.arg1 == 1) {
                        //进入0元购的业务判断流程
                        FreeEventManager.share().checkStatus(mActivity);
                    } else {
                        ToastUtil.error("不支持的活动");
                    }
                    break;
                case SET_TITLE_BAR_SHARE_TYPE://设置标题栏右侧分享按钮
                    if (mTitleBarListener != null && msg.obj != null) {
                        WXShareInfo shareInfo = (WXShareInfo) msg.obj;
                        mTitleBarListener.onTitleShareTypeChanged(shareInfo);
                    } else {
                        DLog.w("no ddjsb titleBar listener for DD_JSB!");
                    }
                    break;
                case SHARE_TO_WX://分享到微信
                    if (mActivityListener != null && msg.obj != null) {
                        WXShareInfo shareInfo = (WXShareInfo) msg.obj;
                        mActivityListener.onActivityShareToWX(shareInfo);
                    } else {
                        DLog.w("no activity listener for DD-JSB!");
                    }
                    break;
                case FINISH_LOAD_MORE:
                    if (mLoadMoreListener != null) {
                        mLoadMoreListener.onFinishLoadMore(msg.arg1 == 1);
                    } else {
                        DLog.w("no load more listener for DD-JSB");
                    }
                    break;
                case SET_HOME_SHORTCUT:
                    if (msg.obj != null && msg.obj instanceof String) {
                        if (mHomeShortcutListener != null) {
                            ArrayList<HomeShortcutBean> data = new ArrayList<>();
                            try {
                                Type listType = new TypeToken<ArrayList<HomeShortcutBean>>() {
                                }.getType();
                                String json = (String) msg.obj;
                                ArrayList<HomeShortcutBean> jsonArray = new Gson().fromJson(json, listType);
                                data.addAll(jsonArray);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mHomeShortcutListener.onGetHomeShortcutData(data);
                        } else {
                            DLog.w("no home shortcut listener for DD-JSB");
                        }
                    } else {
                        DLog.e("setHomeShortCut jsonArray is null.");
                    }
                    break;
                case SCREENSHOT:
                    uiThreadScreenshot(msg.arg1);
                    break;

                case SHOW_SHARE_IMAGE_PANEL:
                    // 带 预览的 图片分享框

                    DDWebViewShareImageDialog.DDWebViewShareImageData data = (DDWebViewShareImageDialog.DDWebViewShareImageData) msg.obj;

                    DDWebViewShareImageDialog dialog = new DDWebViewShareImageDialog(mActivity);
                    dialog.setShareData(data);
                    dialog.show();

                    break;
                case SET_TITLE_BAR_SHARE_ACTION:
                    String action = (String) msg.obj;
                    if (mTitleBarListener != null) {
                        mTitleBarListener.onTitleShareAction(action);
                    } else {
                        DLog.i("SET_TITLE_BAR_SHARE_ACTION:" + action);
                    }
                    break;

            }
        }
    };

    @JavascriptInterface
    public void openAppWithPackage(String appName, String packageName, String className) {
        if (AppTools.isAppInstall(mActivity, packageName)) {
            AppTools.openApp(mActivity, packageName, className);
        } else {
            ToastUtil.error("请先安装" + appName + "客户端");
        }
    }

    /**
     * 拨打电话
     *
     * @param phoneNum
     */
    @JavascriptInterface
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        mActivity.startActivity(intent);
    }


    @JavascriptInterface
    public void openApp(String appName, String schema) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(schema));
            mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.error("无法启动" + appName + "客户端,请确认是否已安装");
        }
    }

    @JavascriptInterface
    public void copyToClipboard(String text) {
        ClipboardUtil.setPrimaryClip(text);
    }

    @JavascriptInterface
    public void showShareImagePanel(String thumbUrl, String title, String desc, String url, String base64Data) {
        DLog.i("showShareImagePanel:" + thumbUrl + "," + title + "," + desc + "," + url);
        DLog.i("base64Data:" + base64Data);

        DDWebViewShareImageDialog.DDWebViewShareImageData data = new DDWebViewShareImageDialog.DDWebViewShareImageData();
        data.setThumbUrl(thumbUrl);
        data.setTitle(title);
        data.setDesc(desc);
        data.setUrl(url);
        data.setBase64Data(base64Data);

        uiHandler.obtainMessage(SHOW_SHARE_IMAGE_PANEL.ordinal(), data).sendToTarget();
    }

    /**
     * 设置关闭返回按钮拦截
     */
    @JavascriptInterface
    public void setTitleBarBackListener(String backFunc) {
        if (mBackFinishListener != null) {
            mBackFinishListener.onRegisterBackFunction(backFunc);
        } else {
            DLog.i("setTitleBarBackListener listener is null");
        }
    }

    @JavascriptInterface
    public void setTitleBarShareAction(String funcName) {
        uiHandler.obtainMessage(SET_TITLE_BAR_SHARE_ACTION.ordinal(), funcName).sendToTarget();
    }

    /**
     * 关闭当前浏览器
     */
    @JavascriptInterface
    public void closeWebView() {
        if (mBackFinishListener != null) {
            mBackFinishListener.onBackFinish();
        } else {
            DLog.i("closeWebView listener is null");
        }
    }


    /**
     * 2.0版本的跳转到SPU详情
     *
     * @param spuId 商品的SPU ID
     */
    @JavascriptInterface
    public void openSpuDetail(String spuId) {
        DDProductDetailActivity.start(mActivity, spuId);
    }

    /**
     * 分享赚
     * <p>
     * 2.0版本废弃此功能
     *
     * @param skuId 商品skuID
     */
    @JavascriptInterface
    @Deprecated
    public void shareProductWithProfit(String skuId) {
        uiHandler.obtainMessage(DDJavaScriptType.SHARE_PRODUCT_WITH_PROFIT.ordinal(), skuId).sendToTarget();
    }

    /**
     * 分享SPU商品
     */
    @JavascriptInterface
    public void shareSPUWithProfit(String spuId) {
        uiHandler.obtainMessage(DDJavaScriptType.SHARE_SPU_WITH_PROFIT.ordinal(), spuId).sendToTarget();
    }

    /**
     * 跳转到指定的网页
     *
     * @param url 跳转的地址
     */
    @JavascriptInterface
    public void openHtml(String url) {
        DLog.i("openHtml:" + url);
        WebViewActivity.jumpUrl(mActivity, url);
//        mActivity.startActivity(new Intent(mActivity, WebViewActivity.class).putExtra(Constants.Extras.WEB_URL, url));
    }

    @JavascriptInterface
    /**
     * 跳转到指定顶部颜色的网页
     */
    public void openColorHtml(int whiteStyle, String titleBarColor, String url) {
        DLog.i("openColorHtml:" + whiteStyle + "," + titleBarColor + "," + url);
        WebViewActivity.jumpUrl(mActivity, whiteStyle, titleBarColor, url);
    }

    @JavascriptInterface
    public void openColorHtml(int whiteStyle, String title, String titleBarColor, String url) {
        DLog.i("openColorHtml:" + whiteStyle + "," + title + "," + titleBarColor + "," + url);
        WebViewActivity.jumpUrl(mActivity, whiteStyle, title, titleBarColor, url);
    }

    /**
     * 跳转到登录
     */
    @JavascriptInterface
    public void login() {
        DLog.i("login");
        if (getWebView() == null) {
            return;
        }
        Context context = getWebView().getContext();
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @JavascriptInterface
    public void onHeightChanged(int height) {
        DLog.d("onHeightChanged:" + height);
        //让UI线程执行高度重设
        uiHandler.obtainMessage(DDJavaScriptType.ON_HEIGHT_CHANGED.ordinal(), 0, height).sendToTarget();
    }

    /**
     * 登录注册
     */
    @JavascriptInterface
    public void showLogin() {
        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));

    }

    /**
     * 会员中心
     */
    @JavascriptInterface
    public void showMemberCenter() {
        mActivity.startActivity(new Intent(mActivity, XLMemberCenterActivity.class));
    }

    /**
     * 实名页面
     */
    @JavascriptInterface
    public void showRealName() {
        mActivity.startActivity(new Intent(mActivity, RealAuthActivity.class));
    }

    @JavascriptInterface
    public void getOAuth() {
        uiHandler.obtainMessage(DDJavaScriptType.GET_OAUTH.ordinal()).sendToTarget();
    }

    /**
     * 获取当前App请求主机信息
     * <p>
     * Javascript需要实现onHostResponse(String host)来接收返回值
     */
    @JavascriptInterface
    public void getHost() {
        uiHandler.obtainMessage(DDJavaScriptType.GET_HOST.ordinal()).sendToTarget();
    }

    /**
     * 设置标题栏透明度 - 商学院 详情H5专用
     *
     * @param alpha 透明度
     */
    @JavascriptInterface
    public void setTitleBarAlpha(float alpha) {
        DLog.i("setTitleBarAlpha:" + alpha);
        TitleBarValueBean valueBean = new TitleBarValueBean();
        valueBean.setType(TitleBarValueBean.ValueType.BarAlpha);
        valueBean.setAlpha(alpha);
        //让UI线程执行
        uiHandler.obtainMessage(DDJavaScriptType.SET_TITLE_BAR_ALPHA.ordinal(), valueBean).sendToTarget();
    }

    /**
     * 设置标题栏资源样式 - 商学院 详情H5专用
     *
     * @param isDark 是否是黑色资源
     */
    @JavascriptInterface
    public void setTitleBarDarkRes(boolean isDark) {
        DLog.i("setTitleBarDarkRes:" + isDark);
        TitleBarValueBean valueBean = new TitleBarValueBean();
        valueBean.setType(TitleBarValueBean.ValueType.BarRes);
        valueBean.setDark(isDark);
        //让UI线程执行
        uiHandler.obtainMessage(DDJavaScriptType.SET_TITLE_BAR_DARK_RES.ordinal(), valueBean).sendToTarget();
    }

    @JavascriptInterface
    public void toast(int status, String message) {
        DLog.d("toast status:" + status + ",message:" + message);
        if (status == 0) {
            ToastUtil.success("" + message);
        } else {
            ToastUtil.error("" + message);
        }
    }

    @JavascriptInterface
    /**
     * 设置标题栏右侧分享按钮
     *
     * @param shareType 类型 0：不显示；1：默认分享；2：每日上新
     * @param imgUrl 分享的图片地址
     * @param title 分享标题
     * @param desc 分享描述
     * */
    public void setTitleBarShareType(int shareType, String imgUrl, String title, String desc) {
        DLog.d("setTitleBarShareType:" + shareType);
        uiHandler.obtainMessage(DDJavaScriptType.SET_TITLE_BAR_SHARE_TYPE.ordinal(), new WXShareInfo(shareType, imgUrl, title, desc)).sendToTarget();
    }

    @JavascriptInterface
    /**
     * 设置标题栏右侧分享按钮
     *
     * @param shareType 类型 0：不显示；1：默认分享；2：每日上新
     * @param imgUrl 分享的图片地址
     * @param title 分享标题
     * @param desc 分享描述
     * */
    public void setTitleBarShareType(int shareType, String imgUrl, String title, String desc, String shareUrl) {
        DLog.d("setTitleBarShareType:" + shareType);
        uiHandler.obtainMessage(DDJavaScriptType.SET_TITLE_BAR_SHARE_TYPE.ordinal(), new WXShareInfo(shareType, imgUrl, title, desc, shareUrl)).sendToTarget();
    }

    @JavascriptInterface
    /**
     *  跳转到指定活动
     *
     * @param type 活动类型
     */
    public void jumpActivity(int type) {
        DLog.d("jumpActivity:" + type);
        uiHandler.obtainMessage(JUMP_ACTIVITY.ordinal(), type, 0).sendToTarget();
    }

    @JavascriptInterface
    /**
     * 分享到微信
     *
     * @param shareType 类型 1：默认分享；2:0元领面膜
     * @param imgUrl 分享的图片地址
     * @param title 分享标题
     * @param desc 分享描述
     */
    public void shareToWX(String shareType, String imgUrl, String title, String desc) {
        DLog.d("shareToWX:" + shareType);
        int shareValue = 0;
        try {
            shareValue = Integer.parseInt(shareType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        uiHandler.obtainMessage(SHARE_TO_WX.ordinal(), new WXShareInfo(shareValue, imgUrl, title, desc)).sendToTarget();
    }

    @JavascriptInterface
    /**
     * 分享到微信
     *
     * @param shareType 类型 1：默认分享；2:0元领面膜
     * @param imgUrl 分享的图片地址
     * @param title 分享标题
     * @param desc 分享描述
     * @param shareUrl 分享的URL
     */
    public void shareToWX(String shareType, String imgUrl, String title, String desc, String shareUrl) {
        DLog.d("shareToWX:" + shareType);
        int shareValue = 0;
        try {
            shareValue = Integer.parseInt(shareType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        uiHandler.obtainMessage(SHARE_TO_WX.ordinal(), new WXShareInfo(shareValue, imgUrl, title, desc, shareUrl)).sendToTarget();
    }

    @JavascriptInterface
    public void finishLoadMore(int hasMore) {
        DLog.d("finishLoadMore:" + hasMore);
        uiHandler.obtainMessage(FINISH_LOAD_MORE.ordinal(), hasMore, 0).sendToTarget();
    }

    @JavascriptInterface
    public void setHomeShortcut(String jsonArray) {
        DLog.d("setHomeShortcut:" + jsonArray);
        uiHandler.obtainMessage(SET_HOME_SHORTCUT.ordinal(), jsonArray).sendToTarget();
    }

    @JavascriptInterface
    public void jumpMyFans(int mode) {
        DLog.d("jumpMyFans:" + mode);
        MyFollowersActivity.start(mActivity, mode);
    }


    private String screenshotFile = PosterMaker.DataRootPath + "screenshot_webview.jpg";

    @JavascriptInterface
    /**
     * type ==0 本地相册
     * type == 1 微信分享
     * type == 2 base64回传js
     * */
    public void screenshot(int type) {
        uiHandler.obtainMessage(SCREENSHOT.ordinal(), type, 0).sendToTarget();
    }

    public void uiThreadScreenshot(int type) {
        callback("vue.onScreenshotStart()");
        if (webView != null) {
            captureHandler.removeMessages(type);
            captureHandler.sendEmptyMessageDelayed(type, 500);
            ToastUtil.showLoading(mActivity);
        } else {
            callback("vue.onScreenshotEnd(1,\"\")");
            DLog.e("WebView is null.");
        }
    }

    Handler captureHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            execCapture(msg.what);
        }
    };

    private void execCapture(int type) {
        Bitmap bitmap = captureWebView(webView);
//            Bitmap bitmap = getFullWebViewSnapshot(webView);
        if (bitmap != null && !bitmap.isRecycled()) {
            if (type == 0) {
                //保存到相册
                ImageTools.saveBitmapToAlbum(mActivity, bitmap, "ddm_screenshot_" + System.currentTimeMillis());
                //提示语
                ToastUtil.success("图片已保存至相册");
                //截图成功回调
                callback("vue.onScreenshotEnd(0,\"\")");
            } else if (type == 1) {
                File file = new File(screenshotFile);
                File parent = file.getParentFile();
                //检查父级是否存在，不存在则创建
                if (!parent.exists()) parent.mkdirs();
                //检查目标文件是否存在，不存在则创建
                if (file != null && file.exists()) {
                    file.delete();
                    try {
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //截图成功回调
                callback("vue.onScreenshotEnd(0,\"\")");
                //微信分享
                ImageTools.saveBitmapToSD(bitmap, screenshotFile, 95, false);
                DDShareWXDialog dialog = new DDShareWXDialog(mActivity);
                dialog.setImgPath(screenshotFile);
                dialog.setMode(DDShareWXDialog.Mode.SingleImage);
                dialog.show();
            } else if (type == 2) {
                //回传给js
                String base64 = DBase64.bitmapToBase64(bitmap);
                if (TextUtils.isEmpty(base64)) {
                    //base64失败
                    callback("vue.onScreenshotEnd(1,\"\")");
                } else {
                    //base64成功
                    callback("vue.onScreenshotEnd(0," + base64 + ")");
                }
            }
        } else {
            //截图失败回调
            callback("vue.onScreenshotEnd(1,\"\")");
        }
        ToastUtil.hideLoading();
        bitmap.recycle();
    }


    /**
     * 截取浏览器图片
     *
     * @param webView 浏览器对象
     * @return Bitmap 图片对象
     */
    private Bitmap captureWebView(WebView webView) {
        //取样Html宽高
        int width = webView.getWidth();
        float scale = webView.getScale();
        int height = (int) (webView.getContentHeight() * scale);
        //生成画布图片
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //将webView绘制到画布
        webView.draw(canvas);
        return bitmap;
    }

    public static Bitmap getFullWebViewSnapshot(WebView webView) {
        //重新调用WebView的measure方法测量实际View的大小（将测量模式设置为UNSPECIFIED模式也就是需要多大就可以获得多大的空间）
        webView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //调用layout方法设置布局（使用新测量的大小）
        webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
        //开启WebView的缓存(当开启这个开关后下次调用getDrawingCache()方法的时候会把view绘制到一个bitmap上)
        webView.setDrawingCacheEnabled(true);
        //强制绘制缓存（必须在setDrawingCacheEnabled(true)之后才能调用，否者需要手动调用destroyDrawingCache()清楚缓存）
        webView.buildDrawingCache();
        //根据测量结果创建一个大小一样的bitmap
        Bitmap picture = Bitmap.createBitmap(webView.getMeasuredWidth(),
                webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //已picture为背景创建一个画布
        Canvas canvas = new Canvas(picture); // 画布的宽高和 WebView 的网页保持一致
        Paint paint = new Paint();
        //设置画笔的定点位置，也就是左上角
        canvas.drawBitmap(picture, 0, webView.getMeasuredHeight(), paint);
        //将webview绘制在刚才创建的画板上
        webView.draw(canvas);
        webView.setDrawingCacheEnabled(false);
        return picture;
    }

    /**
     * 跳转到商品详情
     */
    @JavascriptInterface
    public void openProduct(String spuId) {
        DDProductDetailActivity.start(mActivity, spuId);
    }

    /**
     * 跳转到品牌馆
     */
    @JavascriptInterface
    public void jumpToBrand(String brandId, String categoryId) {
        BrandActivity.jumpBrandActivity(mActivity, categoryId, brandId);
    }

    /**
     * 跳转到二级分类
     */
    @JavascriptInterface
    public void jumpToCategory(String parentId, String categoryId) {
        CategorySecondActivity.jumpCategorySecondActivity(mActivity, parentId, categoryId);
    }


}