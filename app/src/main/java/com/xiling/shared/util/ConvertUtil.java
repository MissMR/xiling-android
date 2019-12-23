package com.xiling.shared.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.xiling.R;
import com.xiling.shared.bean.CartItem;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.User;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.page.bean.BasicData;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConvertUtil {

    public static long add1Count(String count) {
        if (TextUtils.isDigitsOnly(count)) {
            long value = Long.parseLong(count);
            return value + 1;
        } else {
            return -1;
        }
    }

    /**
     * 过万展现格式为【1.0万】，最多显示【99.9万+】
     * <p>
     * 新的格式化逻辑
     */
    public static String formatWan(long count) {
        if (count >= 10000 && count <= 999000) {
            return String.format("%.1fw", count / 10000.00);
        } else if (count > 999000) {
            return "99.9w+";
        } else {
            return "" + count + "";
        }
    }

    public static long reduce1Count(String count) {
        if (TextUtils.isDigitsOnly(count)) {
            long value = Long.parseLong(count);
            if (value > 1) {
                return value - 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    /**
     * 兼容服务器数据
     */
    public static String toShowCount(String count) {
        if (TextUtils.isEmpty(count)) {
            return "";
        }
        if (TextUtils.isDigitsOnly(count)) {
            long value = Long.parseLong(count);
            return toShowCount(value);
        } else {
            return count;
        }
    }

    public static String formatSaleCount(long saleCount) {
        if (saleCount / (100 * 10000) > 0) {
            String formatSaleCount = String.format("%.2f", saleCount / 10000.00);
            return formatSaleCount.contains(".00") ?
                    formatSaleCount.replace(".00", "") + "万"
                    : formatSaleCount + "万";
        }
        return String.valueOf(saleCount);
    }

    /**
     * 换成显示的统计数量
     */
    public static String toShowCount(long count) {
        String result = "";
        if (count > 999) {
            result = "999+";
        } else if (count < 0) {
            result = "0";
        } else {
            result = "" + count;
        }
        return result;
    }

    public static int dip2px(float dip) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static int dip2px(int dip) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static int px2dip(int pixel) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pixel / scale + 0.5f);
    }

    public static int convertHeight(Context context, int width, int height) {
        return (int) (CommonUtil.getScreenWidth(context) * 1.0f / width * height);
    }

    public static double cent2yuan(long cent) {
        BigDecimal bigDecimal = BigDecimal.valueOf(cent);
        return bigDecimal.divide(BigDecimal.valueOf(100)).doubleValue();
    }

    /**
     * 保留小数点后两位小数的金钱转换逻辑 0.00
     */
    public static String cent2yuan2(long cent) {
        return String.format("%.2f", cent2yuan(cent));
    }

    public static String cent2yuan2(String cent) {
        if (TextUtils.isDigitsOnly(cent)) {
            try {
                long value = Long.parseLong(cent);
                return cent2yuan2(value);
            } catch (Exception e) {
                e.printStackTrace();
                return cent2yuan2(0);
            }
        } else {
            return cent;
        }
    }

    /**
     * 智能显示价格 将分转成元
     *
     * @param skuInfo
     * @return 22.2
     */
    public static double cent2yuan(SkuInfo skuInfo) {
        long price = skuInfo.retailPrice;
        if (SessionUtil.getInstance().isLogin()) {
            User loginUser = SessionUtil.getInstance().getLoginUser();
            if (loginUser.isShopkeeper()) {
                price = skuInfo.retailPrice;
            } else {
                price = skuInfo.marketPrice;
            }
        }
        return cent2yuan(price);
    }

    /**
     * @param skuInfo
     * @return
     */
    public static long cent2CurrentCent(CartItem skuInfo) {
        long price = skuInfo.retailPrice;
//        long price = skuInfo.currentVipTypePrice;
        if (SessionUtil.getInstance().isLogin()) {
            User loginUser = SessionUtil.getInstance().getLoginUser();
            if (loginUser.isShopkeeper()) {
//                price = skuInfo.currentVipTypePrice;
                price = skuInfo.retailPrice;
            } else {
                price = skuInfo.marketPrice;
            }
        }
        return price;
    }

    /**
     * 将分转换成元，结尾删掉 .00
     *
     * @param cent 230  300
     * @return 2.3 3
     */
    public static String cent2yuanNoZero(long cent) {
//        return String.format(Locale.getDefault(), "%.2f", cent2yuan(cent)).replace(".00", "");
        if (cent == 0) {
            return "0";
        } else {
            String centStr = String.valueOf(cent);
            if (centStr.endsWith("00")) {
                return String.valueOf(cent / 100);
            } else if (centStr.endsWith("0")) {
                //TIPS 补充上只有一位小数的时候 即:0.1
                return String.format("%.1f", cent2yuan(cent));
            } else {
                return String.format("%.2f", cent2yuan(cent));
            }
        }
    }

    /**
     * 根据店主或者普通成员，智能显示价格
     *
     * @param context
     * @param skuInfo
     * @return ¥12.2
     */
    public static String centToCurrency(Context context, SkuInfo skuInfo) {
        if (skuInfo == null) {
            return "";
        }
        long price = skuInfo.retailPrice;
        if (SessionUtil.getInstance().isLogin()) {
            User loginUser = SessionUtil.getInstance().getLoginUser();
            if (loginUser.isShopkeeper()) {
                price = skuInfo.retailPrice;
            } else {
                price = skuInfo.marketPrice;
            }
        }
        return centToCurrency(context, price);
    }

    /**
     * 根据店主或者普通成员，智能显示价格
     *
     * @param context
     * @param skuInfo
     * @return
     */
    public static String centToCurrency(Context context, CartItem skuInfo) {
        long price = skuInfo.retailPrice;
        if (skuInfo.isRush()) {
            price = skuInfo.flashSaleSkuDTO.getActivityPrice();
        } else {
            if (SessionUtil.getInstance().isLogin()) {
                User loginUser = SessionUtil.getInstance().getLoginUser();
                if (loginUser.isShopkeeper()) {
                    price = skuInfo.retailPrice;
                } else {
                    price = skuInfo.marketPrice;
                }
            }
        }
        return centToCurrency(context, price);
    }

    /**
     * 将分转化成元，加上¥符号
     *
     * @param context
     * @param cent    2212
     * @return ¥22.12
     */
    public static String centToCurrency(Context context, long cent) {
        if (cent < 0) {
            return "-" + centToCurrency(context, -cent);
        }
        if (context == null) {
            return String.format("¥%.2f", ConvertUtil.cent2yuan(cent));
        }
        return context.getResources().getString(R.string.currency) + String.format(context.getResources().getString(R.string.format_money), ConvertUtil.cent2yuan(cent));
    }

    /**
     * @param context
     * @param cent
     * @return
     */
    public static String centToCurrencyNoZero(Context context, long cent) {
        return "￥" + cent2yuanNoZero(cent);
    }

    /**
     * 将分转换成元长度对应位数的 ?
     *
     * @param cent 12345
     * @return ???
     */
    public static String cent2QM(long cent) {
        String yuan = String.valueOf(cent / 100);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < yuan.length(); i++) {
            sb.append("?");
        }
        return sb.toString();
    }

    /**
     * 将分转换成 店主价：？？？
     *
     * @param cent
     * @return
     */
    public static String cent2ShopkeeperPrice(long cent) {
        return "店主价：" + cent2QM(cent);
    }

    public static ArrayList<BasicData> json2DataList(JsonElement json) {
        Type type = new TypeToken<ArrayList<BasicData>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static BasicData json2object(JsonElement json) {
        Type type = new TypeToken<BasicData>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static ArrayList<String> json2StringList(JsonElement json) {
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static Map<String, String> objToMap(Object object) throws JSONException {
        Map<String, String> data = new HashMap<>();
        JSONObject jsonObject = new JSONObject(new Gson().toJson(object));
        Iterator ite = jsonObject.keys();
        while (ite.hasNext()) {
            String key = ite.next().toString();
            String value = jsonObject.get(key).toString();
            data.put(key, value);
        }
        return data;
    }

    public static String maskPhone(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    public static String convertWeight(long weight) {
        if (weight == 0) {
            return "无";
        }
        if (weight < 1000) {
            return weight + "克";
        }
        return String.format("%.2f克", weight / 1000f);
    }

    /**
     * 输入元单位的字符串转换成对应的 long 分
     *
     * @param moneyStr 12.34 元
     * @return 分
     */
    public static long stringMoney2Long(String moneyStr) {
        if (StringUtils.isEmpty(moneyStr)) {
            return 0;
        }
        long money1 = (long) (Double.parseDouble(moneyStr) * 100);
        double moneyDouble = Double.parseDouble(moneyStr);
        moneyDouble = moneyDouble > 0 ? moneyDouble + 0.005 : 0;
        long money2 = new Double(moneyDouble * 100).longValue();
        LogUtils.e("输入" + moneyStr + " 输出：" + money2 + "  元函数：" + money1 + "     double" + moneyDouble);
        return money2;
    }

    public static long yuan2cent(String money) {
        if (TextUtils.isEmpty(money)) {
            return -1;
        } else if ("0".equals(money)) {
            return 0;
        }
        try {
            return 100L * Long.parseLong(money);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String memberType2TypeStr(int memberType) {
        String memberTypeStr = "";
        switch (memberType) {
            case AppTypes.FAMILY.MEMBER_NORMAL:
                memberTypeStr = "普通会员";
                break;
            case AppTypes.FAMILY.MEMBER_ZUNXIANG:
                memberTypeStr = "尊享会员";
                break;
            case AppTypes.FAMILY.MEMBER_JINKA:
                memberTypeStr = "金卡会员";
                break;
            case AppTypes.FAMILY.MEMBER_TIYAN:
                memberTypeStr = "铂金会员";
                break;
            case AppTypes.FAMILY.MEMBER_ZHUANYING:
                memberTypeStr = "钻石会员";
                break;
        }
        return memberTypeStr;
    }

    /**
     * @param idCard 将身份证号中间改成*
     * @return
     */
    public static String idCard2xing(String idCard) {
        return new StringBuffer().append(idCard.substring(0, 4)).append("**********").append(idCard.substring(14)).toString();
    }
}
