package com.xiling.ddmall.shared.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.xiling.ddmall.ddui.manager.CartAmountManager;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.module.pay.PayActivity;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseCallback;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.Address;
import com.xiling.ddmall.shared.bean.CartAmount;
import com.xiling.ddmall.shared.bean.CartItem;
import com.xiling.ddmall.shared.bean.CartStore;
import com.xiling.ddmall.shared.bean.MemberRatio;
import com.xiling.ddmall.shared.bean.SkuAmount;
import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.bean.SkuPvIds;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.constant.AppTypes;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.service.contract.ICartService;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.xiling.ddmall.shared.util.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.manager
 * @since 2017-06-09
 */
public class CartManager {

    public static void addToCart(final Context context, final SkuInfo skuInfo, final int amount, boolean checkOnSale) {
        if (!UiUtils.checkUserLogin(context)) {
            return;
        }

        if (checkOnSale && skuInfo.status == 0) {
            ToastUtil.error("商品已下架");
            return;
        }
        getQuantity(skuInfo, new BaseCallback<Integer>() {
            @Override
            public void callback(Integer data) {
                if (skuInfo.stock < amount + data) {
                    ToastUtil.error("库存不足");
                    return;
                }
                ICartService service = ServiceManager.getInstance().createService(ICartService.class);
                APIManager.startRequest(service.addToCart(skuInfo.skuId, amount), new BaseRequestListener<CartAmount>() {

                    @Override
                    public void onSuccess(CartAmount result) {
                        ToastUtil.success("添加购物车成功");
                        CartAmountManager.share().setAmount(result.amount);
                        EventBus.getDefault().post(new EventMessage(Event.AddToCart));
                    }
                });
            }
        });
    }

    public static void addToCart(final Context context, final SkuPvIds skuInfo, final int amount) {
        if (!UiUtils.checkUserLogin(context)) {
            return;
        }

        getQuantity(skuInfo, new BaseCallback<Integer>() {
            @Override
            public void callback(Integer data) {
                if (skuInfo.stock < amount + data) {
                    ToastUtil.error("库存不足");
                    return;
                }
                ICartService service = ServiceManager.getInstance().createService(ICartService.class);
                APIManager.startRequest(service.addToCart(skuInfo.skuId, amount), new BaseRequestListener<CartAmount>() {

                    @Override
                    public void onSuccess(CartAmount result) {
                        ToastUtil.success("添加购物车成功");
                        CartAmountManager.share().setAmount(result.amount);
                        EventBus.getDefault().post(new EventMessage(Event.AddToCart));
                    }
                });
            }
        });
    }

    private static void getQuantity(final SkuInfo skuInfo, final BaseCallback<Integer> callback) {
        ICartService cartService = ServiceManager.getInstance().createService(ICartService.class);
        APIManager.startRequest(cartService.getAllList(), new BaseRequestListener<List<CartStore>>() {
            @Override
            public void onSuccess(List<CartStore> result) {
                for (CartStore cartStore : result) {
                    for (CartItem product : cartStore.products) {
                        if (product.skuId.equals(skuInfo.skuId)) {
                            int amount = Math.min(product.amount, product.stock);
                            callback.callback(amount);
                            return;
                        }
                    }
                }
                callback.callback(0);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        });

    }

    private static void getQuantity(final SkuPvIds skuInfo, final BaseCallback<Integer> callback) {
        ICartService cartService = ServiceManager.getInstance().createService(ICartService.class);
        APIManager.startRequest(cartService.getAllList(), new BaseRequestListener<List<CartStore>>() {
            @Override
            public void onSuccess(List<CartStore> result) {
                for (CartStore cartStore : result) {
                    for (CartItem product : cartStore.products) {
                        if (product.skuId.equals(skuInfo.skuId)) {
                            int amount = Math.min(product.amount, product.stock);
                            callback.callback(amount);
                            return;
                        }
                    }
                }
                callback.callback(0);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        });

    }

    public static void buyNow(final Context context, final SkuInfo skuInfo, final int amount) {
        buyNow(context, skuInfo, amount, null);
    }

    public static void buyNow(final Context context, final SkuInfo skuInfo, final int amount, final Address address) {
        if (!UiUtils.checkUserLogin(context)) {
            return;
        }
        // 2578 AndroidAPP测试环境，当把一个商品的所有库存加入购物车后，在从商品详情页进行购买，会提示库存不足
//        getQuantity(skuInfo, new BaseCallback<Integer>() {
//            @Override
//            public void callback(Integer data) {
//                if (skuInfo.stock < amount + data) {
//                    ToastUtil.error("库存不足");
//                    return;
//                }
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("from", "buy");
        intent.putExtra("skuAmount", new SkuAmount(skuInfo.skuId, amount));
        intent.putExtra(Constants.Extras.ADDRESS, address);
        context.startActivity(intent);
//            }
//        });

    }

    public static void buyNow(final Context context, final SkuPvIds skuInfo, final int amount, final String flashSaleId) {
        if (!UiUtils.checkUserLogin(context)) {
            return;
        }
//        getQuantity(skuInfo, new BaseCallback<Integer>() {
//            @Override
//            public void callback(Integer data) {
//                if (skuInfo.stock < amount + data) {
//                    ToastUtil.error("库存不足");
//                    return;
//                }
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("from", "buy");
        SkuAmount skuAmount = new SkuAmount(skuInfo.skuId, amount);
        if (!TextUtils.isEmpty(flashSaleId)) {
            skuAmount.setActivityType(2);
            skuAmount.setFlashSaleId(flashSaleId);
        }
        intent.putExtra("skuAmount", skuAmount);

        context.startActivity(intent);
//            }
//        });

    }

    /**
     * 获取购物车总数量
     */
    public static void getAmount() {
        CartAmountManager.share().reload();
    }

    /**
     * @param list
     * @return 购物车所有商品的价格和
     */
    public static long getTotalMoney(List<CartStore> list) {
        long totalPrice = 0L;
        if (list == null) {
            return totalPrice;
        }
        for (CartStore item : list) {
            if (item.products == null) {
                continue;
            }
            for (CartItem product : item.products) {
                if (product.isSelected) {
                    totalPrice += (ConvertUtil.cent2CurrentCent(product) * product.amount);
                }
            }
        }
        return totalPrice;
    }

    /**
     * @param list
     * @return 购物车所有商品的价格和
     */
    public static long getSelectTotalMoney(List<CartItem> list) {
        long totalPrice = 0L;
        if (list == null) {
            return totalPrice;
        }
        for (CartItem product : list) {
            if (product.isSelected) {
                totalPrice += (ConvertUtil.cent2CurrentCent(product) * product.amount);
            }
        }
        return totalPrice;
    }

    public static long getOrderRadioMoney(List<CartStore> cartStores, List<MemberRatio> memberRatios) {
        return getOrderRadioMoney(cartStores, memberRatios, false, false);
    }

    /**
     * 计算选中商品一共打折了多少钱
     *
     * @param cartStores   商品列表
     * @param memberRatios 折扣列表
     * @param isAllSelect  是否计算所有商品（如果为 false，只机选选中商品）
     * @param isGroupBuy   是否是团购（团购不参与打折）
     * @return
     */
    public static long getOrderRadioMoney(List<CartStore> cartStores, List<MemberRatio> memberRatios, boolean isAllSelect, boolean isGroupBuy) {
        long radioMoney = 0;
        if (isGroupBuy || !Config.IS_DISCOUNT) {
            return radioMoney;
        }
        for (CartStore cartStore : cartStores) {
            for (CartItem product : cartStore.products) {
                if ((isAllSelect || product.isSelected) && product.productType != 3) {
                    radioMoney += (product.retailPrice - product.currentVipTypePrice) * product.amount;
                }
            }
        }
        return radioMoney;
    }


    /**
     * 计算所有参与积分折扣的金额的和
     *
     * @param cartStores         需要计算的商品
     * @param updateMemberRatios
     * @return
     */
    public static long getScoreTotalMoney(List<CartStore> cartStores, ArrayList<MemberRatio> updateMemberRatios) {
        long orderRadioMoney = getOrderRadioMoney(cartStores, updateMemberRatios, true, false);
        long totalBasePrice = getTotalBasePrice(cartStores);
        long price = totalBasePrice - orderRadioMoney - getNoScorePrice(cartStores, orderRadioMoney > 0);
        return price;
    }

    /**
     * 获取不参与积分抵扣的商品价格和
     *
     * @param cartStores   商品列表
     * @param isRadioPrice 商品是否打折了
     * @return 不参与积分抵扣的商品价的和
     */
    private static long getNoScorePrice(List<CartStore> cartStores, boolean isRadioPrice) {
        int memberType = isRadioPrice ? AppTypes.FAMILY.MEMBER_ZUNXIANG : AppTypes.FAMILY.MEMBER_NORMAL;
        long price = 0;
        for (CartStore cartStore : cartStores) {
            for (CartItem product : cartStore.products) {
                if (product.buyScore != 1) {
                    price += product.getTypePrice(memberType) * product.amount;
                }
            }
        }
        return price;
    }

    private static long getTotalBasePrice(List<CartStore> cartStores) {
        long price = 0;
        for (CartStore cartStore : cartStores) {
            price += getTotalBasePrice((ArrayList<CartItem>) cartStore.products);
        }
        return price;
    }


    /**
     * 获得所有商品的普通会员价的和
     *
     * @param cartItems
     * @return
     */
    public static long getTotalBasePrice(ArrayList<CartItem> cartItems) {
        long price = 0;
        for (CartItem cartItem : cartItems) {
            price += cartItem.retailPrice * cartItem.amount;
        }
        return price;
    }


    /**
     * 获取选中的商品 id list
     *
     * @param list 所有商品
     * @return 选中商品列表
     */
    public static ArrayList<String> getSelectedIds(List<CartStore> list) {
        ArrayList<String> ids = new ArrayList<>();
        if (list == null) {
            return ids;
        }

        for (CartStore item : list) {
            if (item.products == null) {
                continue;
            }
            for (CartItem product : item.products) {
                if (product.isSelected) {
                    ids.add(product.skuId);
                }
            }
        }
        return ids;
    }

    public static ArrayList<String> getSelectedItemIds(List<CartItem> list) {
        ArrayList<String> ids = new ArrayList<>();
        if (list == null) {
            return ids;
        }
        for (CartItem product : list) {
            if (product.isSelected) {
                ids.add(product.skuId);
            }
        }

        return ids;
    }

    public static int getSelectedQuantity(List<CartStore> list) {
        int quantity = 0;
        for (CartStore item : list) {
            if (item.products == null) {
                continue;
            }
            for (CartItem product : item.products) {
                if (product.isSelected) {
                    quantity += product.amount;
                }
            }
        }
        return quantity;
    }

    public static int getSelectedItemQuantity(List<CartItem> list) {
        int quantity = 0;
        for (CartItem product : list) {
            if (product.isSelected) {
                quantity += product.amount;
            }
        }
        return quantity;
    }


    /**
     * 删除所有选中的商品
     *
     * @param list 所有商品
     */
    public static void removeSelected(List<CartStore> list) {
        ArrayList<CartStore> teamCard = new ArrayList<>();
        teamCard.addAll(list);
        for (CartStore cartStore : teamCard) {
            if (cartStore.products != null) {
                for (CartItem product : cartStore.products) {
                    if (product.isSelected) {
                        list.remove(product);
                    }
                }
            }
        }
    }

    /**
     * 删除所有选中的商品
     *
     * @param list 所有商品
     */
    public static void removeSelectedItem(List<CartItem> list) {
        ArrayList<CartItem> teamCard = new ArrayList<>();
        teamCard.addAll(list);
        for (CartItem item : teamCard) {
            if (item.isSelected) {
                list.remove(item);
            }
        }
    }

    public static void groupBuy(final Context context, final SkuInfo skuInfo, final int amount) {
        if (!UiUtils.checkUserLogin(context)) {
            return;
        }
        getQuantity(skuInfo, new BaseCallback<Integer>() {
            @Override
            public void callback(Integer data) {
                if (skuInfo.stock < amount + data) {
                    ToastUtil.error("库存不足");
                    return;
                }
                Intent intent = new Intent(context, PayActivity.class);
                intent.putExtra("from", "groupBuy");
                intent.putExtra("skuAmount", new SkuAmount(skuInfo.skuId, amount));
                context.startActivity(intent);
            }
        });
    }

    public static void updateCartItem(Context context, String skuId, int amount) {

        ICartService service = ServiceManager.getInstance().createService(ICartService.class);
        APIManager.startRequest(service.updateCartItem(skuId, amount), new BaseRequestListener<CartAmount>() {

            @Override
            public void onSuccess(CartAmount result) {
                EventBus.getDefault().post(new EventMessage(Event.cartAmountUpdate, result.amount));
            }
        });
    }
}
