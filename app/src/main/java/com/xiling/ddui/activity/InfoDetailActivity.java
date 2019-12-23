package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.bean.CourseBean;
import com.xiling.ddui.bean.EconomicArticleBean;
import com.xiling.ddui.bean.EconomicCourseBean;
import com.xiling.ddui.bean.NetCourseInfoBean;
import com.xiling.ddui.bean.TitleBarValueBean;
import com.xiling.ddui.bean.UIEvent;
import com.xiling.ddui.config.AppConfig;
import com.xiling.ddui.custom.DDJavaScriptBridge;
import com.xiling.ddui.custom.DDShareWXDialog;
import com.xiling.ddui.tools.DLog;

import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICourseService;

import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.ddui.activity.InfoDetailActivity.InfoType.Learn;
import static com.xiling.ddui.activity.InfoDetailActivity.InfoType.News;
import static com.xiling.ddui.activity.InfoDetailActivity.InfoType.Story;

public class InfoDetailActivity extends BaseActivity {

    private static final String kTitle = "title";
    private static final String kInfoId = "infoId";
    private static final String kInfoType = "infoType";
    private static final String kInfoItem = "infoItem";

    ICourseService iCourse;

    String title = "";
    String infoId = "";
    InfoType infoType = News;

    NetCourseInfoBean data = null;

    Object item = null;

    public enum InfoType {
        News,/*新闻头条*/
        Story,/*店主故事*/
        Learn,/*课程*/
    }

    /**
     * 跳转到信息详情
     */
    public static void jumpDetail(Context context, String title, String infoId, InfoType type, Serializable item) {
        Intent intent = new Intent(context, InfoDetailActivity.class);
        intent.putExtra(kTitle, title);
        intent.putExtra(kInfoId, infoId);
        intent.putExtra(kInfoType, type.ordinal());
        intent.putExtra(kInfoItem, item);
        context.startActivity(intent);
    }

    //获取数据
    public void getData() {
        //获取数据
        if (infoType == News || infoType == Story) {
            //新闻头条或者店主故事
            getInfoAppData();
        } else {
            //课程
            getLearnData();
        }
    }

    //状态栏障眼法
    @BindView(R.id.statusView)
    View statusView;

    //标题栏透明度遮罩
    @BindView(R.id.vTitleMask)
    View vTitleMask;

    //标题栏返回按钮
    @BindView(R.id.btnTitleBack)
    ImageView btnTitleBack;

    //标题栏分享按钮
    @BindView(R.id.btnTitleShare)
    ImageView btnTitleShare;

    //标题栏文本控件
    @BindView(R.id.tvTitleText)
    TextView tvTitleText;

    @BindView(R.id.wvContent)
    WebView wvContent;

    //喜欢按钮
    @BindView(R.id.btnLike)
    ImageView btnLike;

    /**
     * 切换标题栏透明度
     */
    void switchTitleMaskAlpha(float alpha) {
        //设置透明度
        statusView.setAlpha(alpha);
        vTitleMask.setAlpha(alpha);
        //切换资源
        switchTitleBarStyle(alpha == 1);
    }

    /**
     * 切换标题栏样式
     */
    void switchTitleBarStyle(boolean isDark) {
        //控制标题栏文本显示
        tvTitleText.setVisibility(isDark ? View.VISIBLE : View.INVISIBLE);
        //返回按钮样式
        btnTitleBack.setImageResource(isDark ? R.mipmap.icon_title_back_black : R.mipmap.icon_title_back_write);
        //分享按钮样式
        btnTitleShare.setImageResource(isDark ? R.mipmap.icon_title_share_black : R.mipmap.icon_title_share_write);
        if (isDark) {
            darkStatusBar();
        } else {
            writeStatusBar();
        }
    }

    /**
     * 设置喜欢状态
     */
    void setLikeStatus(boolean isLike) {
        btnLike.setImageResource(isLike ? R.mipmap.btn_liked : R.mipmap.btn_unlike);
    }

    //标题栏按钮 - 返回
    @OnClick(R.id.btnTitleBack)
    void onTitleBackPressed() {
        onBackPressed();
    }

    //标题栏按钮 - 分享
    @OnClick(R.id.btnTitleShare)
    void onTitleSharePressed() {
        if (data != null && !TextUtils.isEmpty(data.getH5Url())) {
            DDShareWXDialog dialog = new DDShareWXDialog(this);
            dialog.setMode(DDShareWXDialog.Mode.Link);
            if (item != null) {
                if (item instanceof CourseBean) {
                    CourseBean courseBean = (CourseBean) item;
                    dialog.setThumbUrl(courseBean.getImageUrl());
                    dialog.setTitle(courseBean.getTitle());
                    dialog.setDesc(courseBean.getIntro());
                } else if (item instanceof EconomicArticleBean) {
                    EconomicArticleBean articleBean = (EconomicArticleBean) item;
                    dialog.setThumbUrl(articleBean.getImageUrl());
                    dialog.setTitle(articleBean.getTitle());
                    dialog.setDesc("");
                } else if (item instanceof EconomicCourseBean) {
                    EconomicCourseBean courseBean = (EconomicCourseBean) item;
                    dialog.setThumbUrl(courseBean.getImageUrl());
                    dialog.setTitle(courseBean.getTitle());
                    dialog.setDesc(courseBean.getIntro());
                } else {
                    //忽略其他情况进来的数据
                    dialog.setThumbUrl("");
                    dialog.setTitle("店多多");
                    dialog.setDesc(" ");
                }
            } else {
                dialog.setThumbUrl("");
                dialog.setTitle("店多多");
                dialog.setDesc(" ");
            }
            String url = getInfoUrl(data.getH5Url());
            dialog.setLinkUrl(url);
            dialog.show();
        } else {
            ToastUtil.error("数据异常");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        //设置全屏样式
        makeStatusBarTranslucent();
        //绑定View
        ButterKnife.bind(this);

        iCourse = ServiceManager.getInstance().createService(ICourseService.class);

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        //初始化布局
        initView();

        //获取数据
        getData();

        //设置数据
        setData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    RequestListener dataListener = new RequestListener<NetCourseInfoBean>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(NetCourseInfoBean result) {
            super.onSuccess(result);
            if (result != null) {
                data = result;

                //设置分享状态
                if (data.getShare() == 1) {
                    btnTitleShare.setVisibility(View.VISIBLE);
                } else {
                    btnTitleShare.setVisibility(View.GONE);
                }

                //设置喜欢状态
                setLikeStatus(data.isBeLike());

                //开始加载H5地址
                String url = getInfoUrl(data.getH5Url());
                DLog.i("url:" + url);
                wvContent.loadUrl(url);
            }
        }

        @Override
        public void onError(Throwable e) {
            ToastUtil.error(e.getMessage());
            //失败的时候需要关闭一下
            finish();
        }

        @Override
        public void onComplete() {

        }
    };


    /**
     * 根据服务器返回的相对路径拼接绝对路径
     */
    public String getInfoUrl(String path) {
        String url = BuildConfig.H5_URL + "" + path;
        Uri uri = Uri.parse(url);
        DLog.d("uri:" + uri.toString());
        String scheme = uri.getScheme() + "://";
        if (!TextUtils.isEmpty(scheme)) {
            //减掉协议头 + ://
            url = url.substring(scheme.length());
            //过滤//后填充协议头
            url = scheme + "" + url.replace("//", "/");
        } else {
            url = url.replace("//", "/");
        }
        DLog.i("info url is " + url);
        return url;
    }

    /**
     * 获取新闻头条或者店主故事详情
     */
    void getInfoAppData() {
        APIManager.startRequest(iCourse.getInfoAppData(infoId), dataListener);
    }

    /**
     * 获取课程详情
     */
    public void getLearnData() {
        APIManager.startRequest(iCourse.getLearnAppData(infoId), dataListener);
    }

    @OnClick(R.id.btnLike)
    public void onLikeButtonPressed() {
        DLog.i("onLikeButtonPressed");
        if (data != null) {
            if (data.isBeLike()) {
                APIManager.startRequest(iCourse.unLikeLearn(infoId), new RequestListener<Object>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Object result, String msg) {
                        super.onSuccess(result);
                        data.setBeLike(false);
                        setLikeStatus(false);
                        ToastUtil.success("" + msg);

                        UIEvent event = new UIEvent();
                        event.setType(UIEvent.Type.UnLikeLearn);
                        EventBus.getDefault().post(event);

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.error("" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            } else {
                APIManager.startRequest(iCourse.likeLearn(infoId), new RequestListener<Object>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Object result, String msg) {
                        super.onSuccess(result);
                        data.setBeLike(true);
                        setLikeStatus(true);
                        ToastUtil.success("" + msg);

                        UIEvent event = new UIEvent();
                        event.setType(UIEvent.Type.UnLikeLearn);
                        EventBus.getDefault().post(event);

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.error("" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }
    }

    void initView() {

        switchTitleBarStyle(true);

        //标题
        title = getIntent().getStringExtra(kTitle);
        //数据ID
        infoId = getIntent().getStringExtra(kInfoId);
        //类型
        int infoTypeValue = getIntent().getIntExtra(kInfoType, 0);
        if (InfoType.values().length > infoTypeValue) {
            infoType = InfoType.values()[infoTypeValue];
        }
        //附加数据
        item = getIntent().getSerializableExtra(kInfoItem);

        //设置喜欢状态
        if (infoType == Learn) {
            //只有课程显示喜欢按钮
            btnLike.setVisibility(View.VISIBLE);
        } else {
            //非课程隐藏喜欢按钮
            btnLike.setVisibility(View.INVISIBLE);
        }

        WebSettings settings = wvContent.getSettings();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setUseWideViewPort(true); // 关键点
        settings.setAllowFileAccess(true); // 允许访问文件

        wvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                DLog.i("onConsoleMessage:" + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

        });

        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                DLog.i("shouldOverrideUrlLoading:" + request.getUrl());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                switchTitleBarStyle(false);
                //发送增加阅读量本地更新消息
                UIEvent event = new UIEvent();
                event.setType(UIEvent.Type.AddReadCount);
                event.setInfoId(infoId);
                event.setInfoType(infoType);
                EventBus.getDefault().post(event);
            }
        });

        DDJavaScriptBridge bridge = new DDJavaScriptBridge(this);
        bridge.setWebView(wvContent);
        //增加DDJavaScriptBridge
        wvContent.addJavascriptInterface(bridge, DDJavaScriptBridge.NAME);

        //在Chrome中输入 chrome://inspect 可以进行远程调试
        WebView.setWebContentsDebuggingEnabled(AppConfig.DEBUG);
    }

    void setData() {
        //设置标题
        tvTitleText.setText("" + title);

//        wvContent.loadUrl("http://h5.dianduoduo.store/app_web/commercial-college/detail?type=3&id=eda980e86a534a4a88a15c0ad4c75845");

//        StringBuffer content = new StringBuffer();
//        content.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">");
//        content.append("<img src='https://t11.baidu.com/it/u=3710568955,693430037&fm=76' />");
//        content.append("<h3>米兔智能故事机</h3>");
//        content.append("<p>米兔智能故事机，这是一款由小米生态企业，南京机器岛智能科技有限公司专为儿童量身定做的早教产品，与传统故事机不同，米兔智能故事机突出了“智能”功能，让故事机不仅是孩子的早教的工具，更是实现宝宝和父母语音沟通的一款产品，内置了涵盖童话故事、童谣童诗、古典音乐、亲子英语、国学经典等多样化内容，还可以针对孩子的性格特点和用语习惯，实现与孩子的智能语音交互，并且可以通过相应软件实现家长与孩子的实时沟通。</p>");
//        content.append("<hr />");
//        content.append("<video width=\"100%\" height=\"auto\" src=\"http://staticfile-cdn.sightp.com/sightp/webar/webardemo-final.mp4\" id=\"WebAR-video\" preload=\"auto\"></video>");
//        content.append("<div>");
//        content.append("<p>米兔智能故事机还在人机交互方面进行了创新，采用基于人工智能和自然语言解析技术，以及儿童智能语音引擎，针对孩子的性格特点和用语习惯，建立相关知识图谱和海量语料库，实现与孩子的智能语音交互，把故事机打造成为一个无话不谈的机器人，帮助家长轻松早教。可以与孩子直接对话，问答互动，故事机还内置了“算数，考考我吧，问答模式”等环节，对孩子智力开发有极大帮助。</p>");
//        content.append("<hr />");
//        content.append("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">");
//        content.append("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
//        content.append("</div>");
//        content.append("<span><i>aaa</i></span>");
//        content.append("<h1>栅格系统用于通过一系列的行（row）与列（column）的组合来创建页面布局，你的内容就可以放入这些创建好的布局中。下面就介绍一下 Bootstrap 栅格系统的工作原理：“行（row）”必须包含在 .container （固定宽度）或 .container-fluid （100% 宽度）中，以便为其赋予合适的排列（aligment）和内补（padding）。通过“行（row）”在水平方向创建一组“列（column）”。                你的内容应当放置于“列（column）”内，并且，只有“列（column）”可以作为行（row）”的直接子元素。类似 .row 和 .col-xs-4 这种预定义的类，可以用来快速创建栅格布局。Bootstrap 源码中定义的 mixin 也可以用来创建语义化的布局。                通过为“列（column）”设置 padding 属性，从而创建列与列之间的间隔（gutter）。通过为 .row 元素设置负值 margin 从而抵消掉为 .container 元素设置的 padding，也就间接为“行（row）”所包含的“列（column）”抵消掉了padding。        负值的 margin就是下面的示例为什么是向外突出的原因。在栅格列中的内容排成一行。栅格系统中的列是通过指定1到12的值来表示其跨越的范围。例如，三个等宽的列可以使用三个 .col-xs-4 来创建。如果一“行（row）”中包含了的“列（column）”大于 12，多余的“列（column）”所在的元素将被作为一个整体另起一行排列栅格类适用于与屏幕宽度大于或等于分界点大小的设备 ， 并且针对小屏幕设备覆盖栅格类。 因此，在元素上应用任何 .col-md-* 栅格类适用于与屏幕宽度大于或等于分界点大小的设备 ， 并且针对小屏幕设备覆盖栅格类。 因此，在元素上应用任何 .col-lg-* 不存在， 也影响大屏幕设备。通过研究后面的实例，可以将这些原理应用到你的代码中。</h1>");
//
//        wvContent.loadDataWithBaseURL("about:blank", "" + content, "text/html", "UTF-8", "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTitleValueChanged(TitleBarValueBean value) {
        if (value != null && value instanceof TitleBarValueBean) {
            DLog.d("onTitleValueChanged:" + value.getType());
            if (value.getType() == TitleBarValueBean.ValueType.BarAlpha) {
                float alpha = value.getAlpha();
                switchTitleMaskAlpha(alpha);
            } else if (value.getType() == TitleBarValueBean.ValueType.BarRes) {
                boolean isDark = value.isDark();
                switchTitleBarStyle(isDark);
            }
        }
    }

}
