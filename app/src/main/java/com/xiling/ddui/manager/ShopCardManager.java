package com.xiling.ddui.manager;

import com.xiling.ddui.bean.CardExpandableBean;
import com.xiling.ddui.bean.XLCardListBean;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICartService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import static com.xiling.shared.constant.Event.cartAmountUpdate;

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
    public void requestAddCart(String skuId, final int quantity,boolean cover) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("skuId", skuId);
        params.put("quantity", quantity);
        params.put("cover", cover);
        APIManager.startRequest(mCartService.addShopCart(APIManager.buildJsonBody(params)), new BaseRequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                if (result) {
                    ToastUtil.success("成功加入购物车");
                    requestUpDataShopCardCount();
                }
            }
        });
    }

    /**
     * 查看购物车数量
     */
    public void requestUpDataShopCardCount() {
        APIManager.startRequest(mCartService.getCardCount(), new BaseRequestListener<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                super.onSuccess(result);
                EventBus.getDefault().post(new EventMessage(cartAmountUpdate, result));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

}