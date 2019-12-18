package com.xiling.ddmall.ddui.custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.xiling.ddmall.ddui.config.AppConfig;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.shared.util.ToastUtil;

/**
 * 不滚动的WebView
 * <p>
 * 用来支持ScrollView的嵌套
 *
 * @author hanQ
 */
public class NoScrollWebView extends WebView {

    public interface NoScrollWebViewListener {

        void onFinishLoadMore();

    }

    /**
     * 让H5进行加载更多的操作
     */
    public static final String jsWebLoadMore = "vue.infiniteHandler()";

    @SuppressLint("NewApi")
    public NoScrollWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public NoScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NoScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoScrollWebView(Context context) {
        super(context);
        init();
    }

    public boolean checkError(int errorCode) {
        if (errorCode == -2
                || errorCode == -5
                || errorCode == -8 /*net::ERR_CONNECTION_TIMED_OUT*/) {
            showEmptyPage();
            ToastUtil.error("似乎已断开与互联网的链接。");
            return true;
        } else {
            return false;
        }
    }

    public void showEmptyPage() {
        super.loadUrl("about:_blank");
    }

    String rawUrl = "";

    @Override
    public void loadUrl(String url) {
        this.rawUrl = url;
        super.loadUrl(url);
    }

    @Override
    public void reload() {
        if (this.getUrl().equals(rawUrl)) {
            super.reload();
        } else {
            super.loadUrl(rawUrl);
        }
    }

    DDJavaScriptBridge bridge = null;

    void init() {

        bridge = new DDJavaScriptBridge((Activity) getContext());
        bridge.setWebView(this);
        //增加DDJavaScriptBridge
        this.addJavascriptInterface(bridge, DDJavaScriptBridge.NAME);

        //在Chrome中输入 chrome://inspect 可以进行远程调试
        WebView.setWebContentsDebuggingEnabled(AppConfig.DEBUG);
    }

    public void setHomeShortcutDataListener(DDJavaScriptBridge.DDHomeShortcutListener listener) {
        if (bridge != null) {
            bridge.setHomeShortcutListener(listener);
        } else {
            DLog.w("bridge is null.");
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float dx = Math.abs(ev.getX());
        float dy = Math.abs(ev.getY());

        DLog.d("WebView dispatchTouchEvent:" + dx + "," + dy);

        if (dx > dy) {
            //当滑动为横向滑动的时候申请父控件高抬贵手放过在下
            getParent().requestDisallowInterceptTouchEvent(true);
        } else {
            //尝试修正H5无响应
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

    public void resize() {
        //这个是一定要加上那个的,配合scrollView和WebView的height=wrap_content属性使用
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        //重新测量
        measure(w, h);
    }

    public void setWebViewLoadMoreListener(DDJavaScriptBridge.DDBLoadMoreListener loadMoreListener) {
        if (bridge != null) {
            bridge.setLoadMoreListener(loadMoreListener);
        } else {
            DLog.w("setJsWebLoadMoreListener bridge is null.");
        }
    }

    /**
     * 让H5进行加载更多的操作
     */
    public void webLoadMore() {
        highJsCall(jsWebLoadMore);
    }

    /**
     * 让H5执行刷新操作
     */
    public void webReload() {
        highJsCall(jsWebLoadMore);
    }

    /**
     * 登录状态改变
     *
     * @param isLogin 是否登录
     */
    public void onLoginChanged(boolean isLogin) {
        highJsCall("loginChanged(" + isLogin + ")");
    }

    /**
     * 触发JS协议
     *
     * @param script 需要触发的Javascript
     */
    void highJsCall(final String script) {
        evaluateJavascript("" + script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                DLog.d("highJsCall:" + script + " => " + s);
            }
        });
    }
}
