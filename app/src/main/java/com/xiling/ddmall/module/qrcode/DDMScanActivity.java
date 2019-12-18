package com.xiling.ddmall.module.qrcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.xiling.ddmall.MyApplication;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.manager.ShortUrlMaker;
import com.xiling.ddmall.ddui.service.HtmlService;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.ddui.tools.URLFormatUtils;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.module.page.WebViewActivity;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import rx.functions.Action1;

/**
 * @author Jigsaw
 * @date 2018/8/27
 */
public class DDMScanActivity extends BaseActivity implements QRCodeView.Delegate {

    /**
     * 跳转到QR
     *
     * @param context
     * @param title
     * @param mode
     */
    public static void jumpQR(Context context, String title, int mode) {
        Intent intent = new Intent(context, DDMScanActivity.class);
        intent.putExtra(TAG_TITLE, title);
        intent.putExtra(TAG_SCAN_MODE, mode);
        context.startActivity(intent);
    }

    /**
     * 外部传入的标题
     */
    public static final String TAG_TITLE = Constants.Extras.TITLE;
    /**
     * 是否是邀请人扫描模式
     */
    public static final String TAG_SCAN_MODE = "";

    public static final class MODE {
        public static final int INVITE = 0;
        public static final int HOME = 1;
    }

    @BindView(R.id.zbarview)
    ZBarView mQRCodeView;

    /**
     * 标题
     */
    String title = "";
    /**
     * 扫描模式
     * <p>
     * 0：邀请人模式,只识别邀请码
     * 1：首页扫描,支持邀请码+产品二维码识别
     */
    int inviteMode = MODE.INVITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        //检查权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    ToastUtil.error("请允许获取相机权限，否则无法使用");
                } else {
                    initView();
                }
            }
        });
    }

    private void initView() {
        /*此标题以前没有使用，现在将此标题作为逻辑动态赋值*/
        title = getIntent().getStringExtra(TAG_TITLE);
        /*需要兼容以前的邀请人识别逻辑，所以默认不传入的扫描模式为邀请人模式*/
        inviteMode = getIntent().getIntExtra(TAG_SCAN_MODE, MODE.INVITE);

        /*如果传入的标题是空*/
        if (TextUtils.isEmpty(title)) {
            if (inviteMode == 0) {
                /*扫描模式是邀请二维码*/
                title = "扫描邀请人二维码";
            } else {
                /*扫描模式不是邀请人则设置扫一扫*/
                title = "扫一扫";
            }
        }

        //设置标题栏
        setTitle(title);
        //设置返回按钮
        setLeftBlack();

        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(final String result) {
        ToastUtil.success("扫描成功");
        if (URLFormatUtils.canMapLongURL(result)) {
            DLog.i("满足短链接转长连接的条件，URL:" + result);
            ShortUrlMaker.share().restore(result, new ShortUrlMaker.ShortUrlListener() {
                @Override
                public void onUrlCreate(String url) {
                    responseQR(url);
                }

                @Override
                public void onUrlCreateError(String error) {
                    responseQR(result);
                }
            });
        } else {
            responseQR(result);
        }
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtil.error("打开相机出错");
        /**出错的时候关闭自己*/
        finish();
    }

    /**
     * @param result QR结果
     */
    public void responseQR(String result) {
        if (inviteMode == MODE.INVITE) {
            //返回上层结果
            setResult(RESULT_OK, new Intent().putExtra(Constants.Extras.RESULT, result));
        } else {
            //跳转指定的Activity
            DLog.d("result:" + result);
            try {

//                分享商品二维码
//                https://store.dodomall.com/p/2de9e7078e4949f69b3e293a5e754137/20110632
//                邀请二维码
//                https://store.dodomall.com/app_web/beshopkepper?inviteCode=20110632

                //自动拼接Http协议头
                if (!result.startsWith("http://") && !result.startsWith("https://")) {
                    result = "http://" + result;
                }

                Uri target = Uri.parse(result);

                String host = target.getHost();
                if (!HtmlService.isAllowHost(host)) {
                    DLog.d("非法业务域名不解析数据");
                    return;
                }

                String path = target.getPath();
                //Path开头规则判断
                if (path.startsWith("/p/")) {
                    DLog.d("商品分享");
                    //URL Path最后一个分段
                    String lastPathSegment = target.getLastPathSegment();
                    //
                    if (lastPathSegment.length() <= 8) {
                        //列出所有的URL Path
                        List<String> segment = target.getPathSegments();
                        int size = segment.size();
                        //检查是否满足规则
                        if (size > 2) {
                            //获取到skuId
                            String skuId = segment.get(size - 2);
                            DLog.d("skuId:" + skuId + ",inviteCode:" + lastPathSegment);
                            //检查用户已登录
                            if (checkUser(lastPathSegment)) {
                                jumpSkuDetail(skuId);
                            }
                        }
                    } else {
                        //如果大于8则不是邀请码,直接当skuId处理
                        DLog.d("skuId:" + lastPathSegment);
                        jumpSkuDetail(lastPathSegment);
                    }

                    // 检查商品是否存在
                    //     1.商品不存在提示
                    //     2.商品存在跳转到详情

                } else if (path.startsWith("/app_web/beshopkepper")) {
                    DLog.d("邀请有奖");
                    String inviteCode = target.getQueryParameter("inviteCode");
                    DLog.i("inviteCode:" + inviteCode);
                    if (checkUser(inviteCode)) {
                        ToastUtil.success("您已成为店多多会员，无需注册");
                        finish();
                    }
                } else {
                    DLog.d("进入其他的WebView");
                    WebViewActivity.jumpUrl(context, result);
                }

                DLog.i("end");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查用户是否登录
     * 1.未登录跳转到登录 并传递邀请码
     * 2.已登录返回true
     *
     * @param inviteCode 邀请码
     */
    public boolean checkUser(String inviteCode) {
        MyApplication.getInstance().setInviteCode(inviteCode);
        if (!SessionUtil.getInstance().isLogin()) {
            ToastUtil.error(Config.NET_MESSAGE.NO_LOGIN);
            EventBus.getDefault().post(new EventMessage(Event.goToLogin, inviteCode));
            finish();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 跳转到SKU商品详情
     *
     * @param skuId 商品ID
     */
    public void jumpSkuDetail(String skuId) {
//        DDProductDetailActivity.start(context,skuId);
        finish();
    }

    /**
     * 跳转到WebView
     *
     * @param url 通用的URL跳转逻辑
     */
    public void jumpToWebView(String url) {
        WebViewActivity.jumpUrl(context, url);
        finish();
    }
}
