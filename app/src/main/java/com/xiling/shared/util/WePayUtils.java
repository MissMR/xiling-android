package com.xiling.shared.util;

import android.app.Activity;
import android.os.SystemClock;

import com.blankj.utilcode.utils.ToastUtils;
import com.xiling.MyApplication;
import com.xiling.ddui.bean.WXPayBean;
import com.xiling.shared.Constants;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiling.BuildConfig;
import com.xiling.module.pay.PayMsg;
import com.xiling.shared.bean.WxPayModel;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IPayService;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * 一个十分不规范的微信支付工具类
 * 在本地预下单，然后调用唤起微信支付，最后结果将回调在WXPayEntryActivity页面，请监听 onResp 方法
 * <p>
 * Created by zjm on 2018/3/30.
 */
public class WePayUtils {


    private static final String WX_NOTIFY_URL = Constants.URL_API_PREFIX + BuildConfig.WX_PAY_NOTIFY_URL;
    private static IWXAPI mWxapi;

    @Deprecated
    public static IWXAPI initWePay(final Activity context) {
        return initWePay();
    }

    public static IWXAPI initWePay() {
        mWxapi = WXAPIFactory.createWXAPI(MyApplication.getInstance(), BuildConfig.WX_APP_ID, true);
        mWxapi.registerApp(BuildConfig.WX_APP_ID);
        return mWxapi;
    }

    /**
     * 一个十分不规范的微信支付方法,使用前"必须"需要先调 initWePay
     *
     * @param context
     * @param money   金额 单位：分
     * @param orderNo
     */
    public static void wePay(final Activity context, long money, String orderNo) {
        HashMap<String, String> params = buildParamsToPay(money, orderNo, WX_NOTIFY_URL);
        IPayService payService = ServiceManager.getInstance().createService(IPayService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml;ChartSet=utf-8"), StringUtil.hashMap2Xml(params));
        APIManager.startRequest(payService.unifiedOrder(requestBody), new Observer<ResponseBody>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (context == null || context.isFinishing() || context.isDestroyed()) {
                            return;
                        }
                        ToastUtil.showLoading(context);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            startPay(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.hideLoading();
                        EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_WXPAY_FAIL, "微信支付预下单接口异常"));
                    }

                    @Override
                    public void onComplete() {
                        ToastUtil.hideLoading();
                    }
                }
        );
    }


    public static byte[] callMapToXML(Map map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><bizdata>");
        mapToXMLTest2(map, sb);
        sb.append("</bizdata>");
        try {
            return sb.toString().getBytes("UTF-8");
        } catch (Exception e) {
        }
        return null;
    }

    private static void mapToXMLTest2(Map map, StringBuffer sb) {
        Set set = map.keySet();
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value) {
                value = "";
            }
            if (value.getClass().getName().equals("java.util.ArrayList")) {
                ArrayList list = (ArrayList) map.get(key);
                sb.append("<" + key + ">");
                for (int i = 0; i < list.size(); i++) {
                    HashMap hm = (HashMap) list.get(i);
                    mapToXMLTest2(hm, sb);
                }
                sb.append("</" + key + ">");

            } else {
                if (value instanceof HashMap) {
                    sb.append("<" + key + ">");
                    mapToXMLTest2((HashMap) value, sb);
                    sb.append("</" + key + ">");
                } else {
                    sb.append("<" + key + ">" + value + "</" + key + ">");
                }

            }

        }
    }


    /**
     * 调起微信支付
     */
    public static void startPay(WXPayBean wxPayBean) {
        PayReq request = new PayReq();
        request.appId = wxPayBean.getAppId();
        request.partnerId = wxPayBean.getPartnerId();
        String prepayId = wxPayBean.getPrepayId();
        request.prepayId = prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = wxPayBean.getNonceStr();
        request.timeStamp = wxPayBean.getTimeStamp();
        request.sign = wxPayBean.getSign();
        mWxapi.sendReq(request);
    }


    /**
     * 调起微信支付
     *
     * @param xmlStr 微信服务器返回的 xml
     */
    private static void startPay(String xmlStr) {
        try {
            HashMap<String, String> hashMap = StringUtil.xml2HashMap(xmlStr);
            Gson gson = new Gson();
            String json = gson.toJson(hashMap);
            Logger.e("预下单返回：" + json);
            WxPayModel prePayResponse = gson.fromJson(json, WxPayModel.class);
            if (!prePayResponse.isSuccess()) {
                throw new Exception(prePayResponse.returnMsg);
            }

            PayReq request = new PayReq();
            request.appId = prePayResponse.appid;
            request.partnerId = prePayResponse.mchId;
            request.prepayId = prePayResponse.prepayId;
            request.packageValue = "Sign=WXPay";
            request.nonceStr = prePayResponse.nonceStr;
            request.timeStamp = String.valueOf(SystemClock.currentThreadTimeMillis() / 1000);
            request.sign = buildSign(request);
            mWxapi.sendReq(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            ToastUtils.showShortToast(e.getMessage());
        }
    }

    private static HashMap<String, String> buildParamsToPay(long money, String orderNo, String notifyUrl) {
        HashMap<String, String> params = new HashMap<>(11);
        params.put("appid", BuildConfig.WX_APP_ID);
        params.put("attach", "支付订单：" + orderNo);
        params.put("body", "店多多订单：" + orderNo);
        params.put("mch_id", BuildConfig.WX_MCH_ID);
        params.put("nonce_str", StringUtil.randomString());
        params.put("notify_url", notifyUrl);
        params.put("out_trade_no", String.format("%s%s", orderNo, System.currentTimeMillis() / 1000));
        params.put("spbill_create_ip", "192.168.0.1");
        params.put("total_fee", String.valueOf(money));
        params.put("trade_type", "APP");
        params.put("sign", buildSign(params));
        return params;
    }

    private static String buildSign(PayReq request) {
        HashMap<String, String> hashMap = new HashMap<>(6);
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

    public final static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

}
