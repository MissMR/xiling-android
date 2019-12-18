package com.xiling.ddmall.shared.util;

import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.module.pay.PayMsg;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.bean.PayResult;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * 一个非常不规范的支付宝支付工具
 * 在本地调用处理这些乱七八糟的事，希望别被人黑了
 * Created by zjm on 2018/3/28.
 */
public class AliPayUtils {

    private static final String NOTIFY_URL = Constants.URL_API_PREFIX + BuildConfig.ALIPAY_NOTIFY_URL;
    private static final int SDK_PAY_FLAG = 1;

    /**
     * 一个非常不规范的支付宝支付函数
     * 调用之后请在需要的页面接受 EventBus ，支付结果将以 EventBus 的方式回调出来
     *
     * @param context
     * @param money   支付的总金额（单位元，如 0.01）
     * @param orderNo 订单号
     */
    public static void pay(final Activity context, String money, String orderNo) {
        Map<String, String> params = buildOrderParamMap(BuildConfig.ALIPAY_APP_ID, true, money, orderNo, NOTIFY_URL);
        String orderParam = buildOrderParam(params);
        String sign = getSign(params, BuildConfig.ALIPAY_RSA_KEY, true);
        final String orderInfo = orderParam + "&" + sign;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;

                checkPayResult(result);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 检查支付结果，用 eventBus 将支付结果发送出去
     *
     * @param result
     */
    private static void checkPayResult(Map<String, String> result) {
        PayResult payResult = new PayResult(result);
        /**
         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
         */
        // 同步返回需要验证的信息
        String resultInfo = payResult.getResult();
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_ALIPAY_SUCCEED));
        } else {
            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
            EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_ALIPAY_FAIL));
        }
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map 待签名授权信息
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 构造支付订单参数列表
     *
     * @param pid
     * @param app_id
     * @param target_id
     * @return
     */
    public static Map<String, String> buildOrderParamMap(String app_id, boolean rsa2, String totalAmount, String orderNo, String notifyUrl) {
        Map<String, String> keyValues = new HashMap<>();
        keyValues.put("app_id", app_id);
        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"notify_url\":\"" + notifyUrl + "\",\"total_amount\":\"" + totalAmount + "\",\"subject\":\"订单支付\",\"body\":\"订单支付\",\"out_trade_no\":\"" + orderNo + "\"}");
        keyValues.put("charset", "utf-8");
        keyValues.put("method", "alipay.trade.app.pay");
        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");
        keyValues.put("timestamp", TimeUtils.getNowTimeString());
        keyValues.put("version", "1.0");
        LogUtils.e(keyValues.toString());
        return keyValues;
    }

    /**
     * 构造授权参数列表
     *
     * @param pid
     * @param app_id
     * @param target_id
     * @return
     */
    public static Map<String, String> buildAuthInfoMap(String pid, String app_id, String target_id, boolean rsa2) {
        Map<String, String> keyValues = new HashMap<String, String>();
        // 商户签约拿到的app_id，如：2013081700024223
        keyValues.put("app_id", app_id);
        // 商户签约拿到的pid，如：2088102123816631
        keyValues.put("pid", pid);
        // 服务接口名称， 固定值
        keyValues.put("apiname", "com.alipay.account.auth");
        // 商户类型标识， 固定值
        keyValues.put("app_name", "mc");
        // 业务类型， 固定值
        keyValues.put("biz_type", "openservice");
        // 产品码， 固定值
        keyValues.put("product_id", "APP_FAST_LOGIN");
        // 授权范围， 固定值
        keyValues.put("scope", "kuaijie");
        // 商户唯一标识，如：kkkkk091125
        keyValues.put("target_id", target_id);
        // 授权类型， 固定值
        keyValues.put("auth_type", "AUTHACCOUNT");
        // 签名类型
        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");
        return keyValues;
    }

    public static class SignUtils {

        private static final String ALGORITHM = "RSA";

        private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

        private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

        private static final String DEFAULT_CHARSET = "UTF-8";

        private static String getAlgorithms(boolean rsa2) {
            return rsa2 ? SIGN_SHA256RSA_ALGORITHMS : SIGN_ALGORITHMS;
        }

        public static String sign(String content, String privateKey, boolean rsa2) {
            try {
                PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                        Base64.decode(privateKey));
                KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
                PrivateKey priKey = keyf.generatePrivate(priPKCS8);

                java.security.Signature signature = java.security.Signature
                        .getInstance(getAlgorithms(rsa2));

                signature.initSign(priKey);
                signature.update(content.getBytes(DEFAULT_CHARSET));

                byte[] signed = signature.sign();

                return Base64.encode(signed);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}
