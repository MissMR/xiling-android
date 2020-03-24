package com.xiling.ddui.manager;

import android.content.Context;
import android.content.Intent;

import com.xiling.ddui.activity.ConfirmationOrderActivity;
import com.xiling.ddui.bean.CardExpandableBean;
import com.xiling.ddui.bean.SkuListBean;
import com.xiling.ddui.bean.XLCardListBean;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICartService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import static com.xiling.ddui.activity.ConfirmationOrderActivity.ORDER_SOURCE;
import static com.xiling.ddui.activity.ConfirmationOrderActivity.SKULIST;
import static com.xiling.shared.constant.Event.cartAmountUpdate;
import static com.xiling.shared.constant.Event.cartListUpdate;

/**
 * 购物管理
 */
public class ShopCardManager {

    public static ShopCardManager self;
    ICartService mCartService;

    private ShopCardManager() {
        mCartService = ServiceManager.getInstance().createService(ICartService.class);
    }

    public static ShopCardManager getInstance() {
        if (self == null) {
            synchronized (ShopCardManager.class) {
                self = new ShopCardManager();
            }
        }
        return self;
    }


    /**
     * 添加购物车
     */
    public void requestAddCart(Context context, String skuId, final int quantity, boolean cover, final boolean isToast) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("skuId", skuId);
        params.put("quantity", quantity);
        params.put("cover", cover);
        APIManager.startRequest(mCartService.addShopCart(APIManager.buildJsonBody(params)), new BaseRequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                if (result) {
                    if (isToast) {
                        ToastUtil.show("成功加入购物车", 1000);
                    }
                    requestUpDataShopCardCount(!isToast);
                }
            }
        });
    }

    /**
     * 添加购物车
     */
    public void requestAddCart(Context context, String skuId, final int quantity, boolean cover, final BaseRequestListener baseRequestListener) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("skuId", skuId);
        params.put("quantity", quantity);
        params.put("cover", cover);
        APIManager.startRequest(mCartService.addShopCart(APIManager.buildJsonBody(params)), new BaseRequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                if (baseRequestListener != null) {
                    baseRequestListener.onSuccess(result);
                }
                ToastUtil.show("成功加入购物车", 100);
                requestUpDataShopCardCount();

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (baseRequestListener != null) {
                    baseRequestListener.onError(e);
                }
            }
        });


    }


    /**
     * 查看购物车数量
     */
    public void requestUpDataShopCardCount() {
        if (UserManager.getInstance().isLogin()) {
            APIManager.startRequest(mCartService.getCardCount(), new BaseRequestListener<Integer>() {
                @Override
                public void onSuccess(Integer result) {
                    super.onSuccess(result);
                    EventBus.getDefault().post(new EventMessage(cartAmountUpdate, result));
                    EventBus.getDefault().post(new EventMessage(cartListUpdate, result));
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    ToastUtil.error(e.getMessage());
                }
            });
        }
    }

    /**
     * 查看购物车数量
     */
    public void requestUpDataShopCardCount(final boolean isCart) {
        if (UserManager.getInstance().isLogin()) {
            APIManager.startRequest(mCartService.getCardCount(), new BaseRequestListener<Integer>() {
                @Override
                public void onSuccess(Integer result) {
                    super.onSuccess(result);
                    EventBus.getDefault().post(new EventMessage(cartAmountUpdate, result));
                    if (!isCart) {
                        EventBus.getDefault().post(new EventMessage(cartListUpdate, result));
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    ToastUtil.error(e.getMessage());
                }
            });
        }
    }


    /**
     * 结算 - 校验箱规
     */
    public void preCheck(final Context mContext, final ArrayList<SkuListBean> skuList, final BaseRequestListener<Boolean> booleanBaseRequestListener) {
        APIManager.startRequest(mCartService.preCheck(APIManager.buildJsonBody(skuList)), new BaseRequestListener<Boolean>(mContext) {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                if (booleanBaseRequestListener != null) {
                    booleanBaseRequestListener.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }


}
