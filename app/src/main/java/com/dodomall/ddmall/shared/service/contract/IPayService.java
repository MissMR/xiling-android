package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.shared.bean.GetHiCardPay;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-07-06
 */
public interface IPayService {

    @POST("https://api.mch.weixin.qq.com/pay/unifiedorder")
    Observable<ResponseBody> unifiedOrder(@Body RequestBody body);

    @GET("hicardpay/getHiCardPay")
    Observable<RequestResult<GetHiCardPay>> getHiCardPay(
            @Query("orderCode")String orderCode,
            @Query("payType")String payType,
            @Query("openId")String openId,
            @Query("token")String token
    );

    @POST("https://api.mch.weixin.qq.com/pay/unifiedorder")
    Observable<String> unifiedOrder(
            @Body byte[] bytes
    );
}
