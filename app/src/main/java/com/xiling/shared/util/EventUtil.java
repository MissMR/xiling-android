package com.xiling.shared.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.service.HtmlService;
import com.xiling.module.MainActivity;
import com.xiling.module.auth.Config;
import com.xiling.module.cart.CartActivity;
import com.xiling.module.category.CategoryActivity;
import com.xiling.module.coupon.CouponCenterActivity;
import com.xiling.module.coupon.CouponListActivity;
import com.xiling.module.instant.InstantActivity;
import com.xiling.module.lottery.LotteryActivity;
import com.xiling.module.message.MessageListActivity;
import com.xiling.module.order.OrderDetailActivity;
import com.xiling.module.order.OrderListActivity;
import com.xiling.module.order.RefundDetailActivity;
import com.xiling.module.page.CustomPageActivity;
import com.xiling.module.page.WebViewActivity;
import com.xiling.module.pay.PayActivity;
import com.xiling.module.pay.PayDialogActivity;
import com.xiling.module.product.NewUpPriductListActivity;
import com.xiling.module.product.SalePriductListActivity;
import com.xiling.module.user.AboutUsActivity;
import com.xiling.module.user.ProfileActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseCallback;
import com.xiling.shared.bean.Coupon;
import com.xiling.shared.bean.Product;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.SkuPvIds;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.CartManager;
import com.xiling.shared.service.CouponService;
import com.xiling.shared.service.ProductService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

public class EventUtil {

    public static void compileEvent(Context context, String event, String target) {
        Logger.e("Event: %s\n Target: %s", event, target);
        if ("none".equals(event) || StringUtils.isEmpty(event)) {
            return;
        }
        if ("link".equals(event)) {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", target);
            context.startActivity(intent);
            return;
        }
        if ("native".equals(event)) {
            compileNative(context, target);
        }
    }

    private static void compileNative(final Context context, String target) {
        final Uri parse = Uri.parse("app://" + target);
        switch (parse.getHost()) {
            case "product":
            case "product-instant":
                viewProductDetail(context, parse.getQueryParameter("skuId"));
                break;
            case "product-list":
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("categoryId", parse.getQueryParameter("categoryId"));
                context.startActivity(intent);
//                EventBus.getDefault().postSticky(new EventMessage(Event.changeCategory, parse.getQueryParameter("categoryId")));
                break;
            case "order-detail":
                viewOrderDetail(context, parse.getQueryParameter("orderCode"));
                break;
            case "order-list":
                viewOrderList(context, parse.getQueryParameter("type"));
            case "message":
                context.startActivity(new Intent(context, MessageListActivity.class));
                break;
            case "about-us":
                context.startActivity(new Intent(context, AboutUsActivity.class));
                break;
            case "instant":
                viewInstant(context, parse.getQueryParameter("id"));
                break;
            case "coupon":
                context.startActivity(new Intent(context, CouponListActivity.class));
                break;
            case "coupon-center":
                context.startActivity(new Intent(context, CouponCenterActivity.class));
                break;
            case "add-to-cart":
                addProductToCart(context, parse.getQueryParameter("skuId"), parse.getQueryParameter("amount"));
                break;
            case "category":
                viewCategory(context);
                break;
            case "profile":
                context.startActivity(new Intent(context, ProfileActivity.class));
                return;
            case "contact-us":
                CSUtils.start(context);
                return;
            case "get-coupon":
                getCoupon(context, parse.getQueryParameter("couponId"));
                break;
            case "user-center":
                viewUserCenter(context);
                break;
            case "to-be-shopkeeper":
                HtmlService.startBecomeStoreMasterActivity((Activity) context);
                break;
            case "custom":
                viewCustomPage(context, parse.getQueryParameter("pageId"));
                break;
            case "new-products":
                context.startActivity(new Intent(context, NewUpPriductListActivity.class));
                break;
            case "cart":
                context.startActivity(new Intent(context, CartActivity.class));
                break;
            case "lottery":
                if (UiUtils.checkUserLogin(context)) {
                    context.startActivity(new Intent(context, LotteryActivity.class));
                }
                break;
            case "sale-product":
                context.startActivity(new Intent(context, SalePriductListActivity.class));
                break;
            case "invite-code":
                HtmlService.startInviteActivity(context);
                break;
            default:
                break;
        }
    }

    private static void viewCustomPage(Context context, String pageId) {
        if (pageId == null || pageId.isEmpty()) {
            return;
        }
        Intent intent = new Intent(context, CustomPageActivity.class);
        intent.putExtra("pageId", pageId);
        context.startActivity(intent);
    }

    public static void viewUserCenter(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("tab", "user-center");
        context.startActivity(intent);
    }

    public static void viewNearStoreList(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("tab", "near");
        context.startActivity(intent);
        EventBus.getDefault().post(new EventMessage(Event.viewNearStore));
    }

    private static void getCoupon(Context context, String couponId) {
        if (couponId == null || couponId.isEmpty()) {
            ToastUtil.error("优惠券错误");
            return;
        }
        CouponService.getCoupon(context, couponId);
    }

    public static void viewOrderList(Context context, String type) {
        Intent intent = new Intent(context, OrderListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void viewOrderDetail(Context context, String orderCode) {
        if (orderCode == null || orderCode.isEmpty()) {
            return;
        }

        Intent intent = new Intent(context, OrderDetailActivity.class);
        if (context instanceof PayActivity || context instanceof PayDialogActivity) {
            intent.putExtra(Constants.Extras.FROM_ACTION, "buy");
        }
        intent.putExtra("orderCode", orderCode);
        context.startActivity(intent);
    }

    public static void viewRefundOrderDetail(Context context, String orderCode) {
        if (orderCode == null || orderCode.isEmpty()) {
            return;
        }
        Intent intent = new Intent(context, RefundDetailActivity.class);
        intent.putExtra(Config.INTENT_KEY_ID, orderCode);
        context.startActivity(intent);
    }

    public static void viewOrderDetailBySeller(Context context, String orderCode) {
        if (orderCode == null || orderCode.isEmpty()) {
            return;
        }
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("orderCode", orderCode);
        context.startActivity(intent);
    }

    public static void viewInstant(Context context, String id) {
        Intent intent = new Intent(context, InstantActivity.class);
        if (!(id == null || id.isEmpty())) {
            intent.putExtra("id", id);
        }
        context.startActivity(intent);
    }

    private static void viewCategory(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("tab", "category");
        context.startActivity(intent);
        EventBus.getDefault().post(new EventMessage(Event.viewCategory));
    }

    public static void viewHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("tab", "home");
        context.startActivity(intent);
        EventBus.getDefault().post(new EventMessage(Event.viewHome));
    }


    private static void addProductToCart(final Context context, String skuId, final String amountString) {
        if (skuId == null || skuId.isEmpty()) {
            return;
        }
        ProductService.getSkuInfoById(skuId, new BaseCallback<SkuInfo>() {
            @Override
            public void callback(SkuInfo skuInfo) {
                int amount = 0;
                if (!(amountString == null || amountString.isEmpty())) {
                    amount = Math.max(1, Integer.valueOf(amountString));
                }
                CartManager.addToCart(context, skuInfo, amount, false);
            }
        });
    }

    public static void viewProductDetail(Context context, String spuId) {
        if (spuId == null || spuId.isEmpty()) {
            return;
        }
        DDProductDetailActivity.start(context, spuId);
    }

    public static void viewCouponDetail(final Activity context, final Coupon coupon) {
        Date now = new Date();
        if (coupon == null || coupon.limitStartDate == null) {
            // 优惠券数据异常
        } else if (now.before(coupon.limitStartDate)) {
            ToastUtils.showShortToast("该优惠券还未到使用时间");
        } else if (StringUtils.isEmpty(coupon.productId)) {
            //全场优惠券或者店铺优惠券 跳大首页
            viewHome(context);
            context.finish();
        } else {
            ProductService.getProductInfoById(coupon.productId, new BaseCallback<Product>() {
                @Override
                public void callback(Product product) {
                    if (product.skus.isEmpty()) {
                        compileNative(context, "custom?pageId=" + coupon.storeId);
                    } else {
                        SkuPvIds skuPvIds = product.skus.get(0);
//                        viewProductDetail(context, skuPvIds.skuId);
                    }
                }
            });
        }
    }

}
