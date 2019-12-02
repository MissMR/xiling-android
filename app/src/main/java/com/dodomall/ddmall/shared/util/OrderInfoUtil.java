package com.dodomall.ddmall.shared.util;

import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.bean.Order;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInfoUtil {

    /**
     * 构造支付订单参数列表
     *
     * @param order 订单信息
     * @return Map
     */
    public static Map<String, String> buildOrderParamMap(Order order) {

        Map<String, String> params = new HashMap<>();

        params.put("app_id", BuildConfig.ALIPAY_APP_ID);

        params.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" + ConvertUtil.cent2yuan(order.orderMain.totalMoney) + "\",\"subject\":\"支付订单：" + order.orderMain.orderCode + "\",\"body\":\"支付订单：" + order.orderMain.orderCode + "\",\"out_trade_no\":\"" + order.orderMain.orderCode + "\"}");

        params.put("charset", "UTF-8");

        params.put("method", "alipay.trade.app.pay");

        params.put("sign_type", "RSA");

        params.put("timestamp", Constants.FORMAT_DATE_FULL.format(new Date()));

        params.put("version", "1.0");

        params.put("notify_url", BuildConfig.ALIPAY_NOTIFY_URL);

        return params;
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return String
     */
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());

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
     * @param key      Key
     * @param value    Value
     * @param isEncode isEncode
     * @return String
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
     * 对支付参数信息进行签名
     *
     * @param map 待签名授权信息
     * @return 签名
     */
    public static String getSign(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());
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

        String oriSign = SignUtil.sign(authInfo.toString(), BuildConfig.ALIPAY_RSA_KEY, false);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

}
