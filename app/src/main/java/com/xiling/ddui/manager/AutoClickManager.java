package com.xiling.ddui.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.xiling.ddui.activity.BrandActivity;
import com.xiling.ddui.bean.AutoClickBean;
import com.xiling.ddui.service.HtmlService;
import com.xiling.module.cart.CartActivity;
import com.xiling.module.category.CategoryActivity;
import com.xiling.module.coupon.CouponCenterActivity;
import com.xiling.module.coupon.CouponListActivity;
import com.xiling.module.lottery.LotteryActivity;
import com.xiling.module.message.MessageListActivity;
import com.xiling.module.page.WebViewActivity;
import com.xiling.module.product.NewUpPriductListActivity;
import com.xiling.module.product.SalePriductListActivity;
import com.xiling.module.user.AboutUsActivity;
import com.xiling.module.user.ProfileActivity;
import com.xiling.shared.util.CSUtils;
import com.xiling.shared.util.UiUtils;

public class AutoClickManager {

    /**
     * 解析
     */
    public static void pars(Context context, AutoClickBean autoClickBean) {
        switch (autoClickBean.getEvent()) {
            case "link":
                WebViewActivity.jumpUrl(context, autoClickBean.getTarget());
                break;
            case "native":
                compileNative(context, autoClickBean.getTarget());

                break;
        }
    }


    private static void compileNative(final Context context, String target) {
        final Uri parse = Uri.parse("app://" + target);
        switch (parse.getHost()) {
            case "brand":
                BrandActivity.jumpBrandActivity(context, "", parse.getQueryParameter("brandid"));
                break;
            default:
                break;
        }
    }


}
