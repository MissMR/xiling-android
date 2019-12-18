package com.xiling.ddmall.shared.service;

import android.content.Context;

import com.xiling.ddmall.shared.basic.BaseCallback;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICouponService;
import com.xiling.ddmall.shared.util.ToastUtil;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service
 * @since 2017-07-05
 */
public class CouponService {

    public static void getCoupon(Context context, String couponId) {
        ICouponService service = ServiceManager.getInstance().createService(ICouponService.class);
        APIManager.startRequest(service.receiveCouponById(couponId), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("优惠券领取成功");
            }
        });
    }

    public static void getCoupon(Context context, String couponId, final BaseCallback<Object> callback) {
        ICouponService service = ServiceManager.getInstance().createService(ICouponService.class);
        APIManager.startRequest(service.receiveCouponById(couponId), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("优惠券领取成功");
                callback.callback(result);
            }
        });
    }

}
