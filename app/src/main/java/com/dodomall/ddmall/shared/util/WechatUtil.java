package com.dodomall.ddmall.shared.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.shared.basic.BaseCallback;
import com.dodomall.ddmall.shared.bean.Order;
import com.dodomall.ddmall.shared.bean.PrePayResponse;
import com.dodomall.ddmall.shared.bean.ShareObject;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IPayService;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.util
 * @since 2017-07-06
 */
public class WechatUtil {


    public static IWXAPI newWxApi(Context context) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, BuildConfig.WX_APP_ID, false);
        wxApi.registerApp(BuildConfig.WX_APP_ID);
        return wxApi;
    }

    public static boolean isWeChatAppInstalled(Context context) {
        IWXAPI api = newWxApi(context);
        if (api.isWXAppInstalled()) {
            return true;
        } else {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equalsIgnoreCase("com.tencent.mm")) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static void shareToWeChat(final IWXAPI wxApi, final ShareObject shareObject, final boolean isShareFriend) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WXWebpageObject webPageObject = new WXWebpageObject();
                webPageObject.webpageUrl = shareObject.url;
                WXMediaMessage wxMediaMessage = new WXMediaMessage(webPageObject);
                wxMediaMessage.description = shareObject.desc;
                wxMediaMessage.title = shareObject.title;
                wxMediaMessage.thumbData = BitmapUtil.bitmapToBytes(BitmapUtil.scaleBitmapForShare(shareObject.thumb));
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("img");
                req.message = wxMediaMessage;
                req.scene = isShareFriend ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                wxApi.sendReq(req);
            }
        }).start();
    }

    public static void pay(final Context context, final IWXAPI wxApi, Order order) {
        HashMap<String, String> params = buildParamsToPay(order);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml;ChartSet=utf-8"), StringUtil.hashMap2Xml(params));
        IPayService payService = ServiceManager.getInstance().createService(IPayService.class);
        APIManager.startRequest(payService.unifiedOrder(requestBody), new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
//                ToastUtil.showLoading(context);
            }

            @Override
            public void onNext(@NonNull ResponseBody body) {
                try {
                    HashMap<String, String> hashMap = StringUtil.xml2HashMap(body.string());
                    Gson gson = new Gson();
                    String json = gson.toJson(hashMap);
                    Logger.e("预下单返回：" + json);
                    PrePayResponse prePayResponse = gson.fromJson(json, PrePayResponse.class);
                    if (!prePayResponse.isSuccess()) {
                        throw new Exception(prePayResponse.message);
                    }
                    PayReq request = new PayReq();
                    request.appId = prePayResponse.appId;
                    request.partnerId = prePayResponse.mchId;
                    request.prepayId = prePayResponse.prePayId;
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = prePayResponse.nonceStr;
                    request.timeStamp = String.valueOf(SystemClock.currentThreadTimeMillis() / 1000);
                    request.sign = buildSign(request);
                    wxApi.sendReq(request);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    ToastUtil.error(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                ToastUtil.error(e.getMessage());
                ToastUtil.hideLoading();
            }

            @Override
            public void onComplete() {
                ToastUtil.hideLoading();
            }
        });
    }

    private static HashMap<String, String> buildParamsToPay(Order order) {
        HashMap<String, String> params = new HashMap<>();
        params.put("appid", BuildConfig.WX_APP_ID);
        params.put("attach", "支付订单：" + order.orderMain.orderCode);
        params.put("body", "支付订单：" + order.orderMain.orderCode);
        params.put("mch_id", BuildConfig.WX_MCH_ID);
        params.put("nonce_str", StringUtil.randomString());
        params.put("notify_url", BuildConfig.WX_PAY_NOTIFY_URL);
        params.put("out_trade_no", String.format("%s%s", order.orderMain.orderCode, System.currentTimeMillis() / 1000));
        params.put("spbill_create_ip", "192.168.0.1");
        params.put("total_fee", String.valueOf(order.orderMain.totalMoney));
        params.put("trade_type", "APP");
        params.put("sign", buildSign(params));
        return params;
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private static String buildSign(PayReq request) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appid", request.appId);
        hashMap.put("partnerid", request.partnerId);
        hashMap.put("prepayid", request.prepayId);
        hashMap.put("package", request.packageValue);
        hashMap.put("noncestr", request.nonceStr);
        hashMap.put("timestamp", request.timeStamp);
        return buildSign(hashMap);
    }

    private static String buildSign(HashMap<String, String> params) {
        Object[] keys = params.keySet().toArray();
        Arrays.sort(keys);
        List<String> pieces = new ArrayList<>();
        for (Object key : keys) {
            pieces.add(key + "=" + params.get(String.valueOf(key)));
        }
        pieces.add("key=" + BuildConfig.WX_PAY_KEY);
        String str = Joiner.on("&").join(pieces);
        return StringUtil.md5(str).toUpperCase();
    }

    public static void compileResponse(BaseResp baseResp) {
        compileResponse(baseResp, null);
    }

    public static void compileResponse(BaseResp baseResp, @Nullable BaseCallback<BaseResp> successCallback) {
        compileResponse(baseResp, successCallback, null);
    }

    public static void compileResponse(BaseResp baseResp, @Nullable BaseCallback<BaseResp> successCallback, @Nullable BaseCallback<BaseResp> failureCallback) {
        if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
            if (successCallback != null) {
                successCallback.callback(baseResp);
            }
        } else {
            if (failureCallback != null) {
                failureCallback.callback(baseResp);
            }
        }
    }
}
