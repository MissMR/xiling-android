package com.xiling.shared.service.contract;

import com.xiling.ddui.bean.OrderDetailBean;
import com.xiling.ddui.bean.WXPayBean;
import com.xiling.shared.bean.GetHiCardPay;
import com.xiling.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 支付接口
 */
public interface IPayService {
    /**
     * 账单类型 TYPE
     */
    //订单
    public static final String PAY_TYPE_ORDER = "ORDER";
    //周卡
    public static final String PAY_TYPE_WEEK_CARD = "WEEK_CARD";
    //充值
    public static final String PAY_TYPE_CHARGE_MONEY = "CHARGE_MONEY";

    /**
     * 支付渠道
     */
    //支付宝支付
    public static final String CHANNEL_A_LI_PAY = "A_LI_PAY";
    //微信支付
    public static final String CHANNEL_WE_CHAT_PAY = "WE_CHAT_PAY";
    //银联支付
    public static final String CHANNEL_UNION_PAY = "UNION_PAY";
    //离线支付
    public static final String CHANNEL_OFFLINE_PAY = "OFFLINE_PAY";

    /**
     * 聚合支付-创建流水单
     *
     * @param body
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("pay/add")
    Observable<RequestResult<String>> addPay(@Body RequestBody body);


    /**
     * 聚合支付-线下支付-上传凭证
     */
    @GET("pay/callback/{id}/notify")
    Observable<RequestResult<Boolean>> payOffline(@Path("id") String flowCode,@Query("name") String name,@Query("account") String account,@Query("payDate") String payDate,@Query("image") String image,@Query("mark") String mark);

    /**
     * 微信/支付宝支付
     */
    @GET("pay/app-pay")
    Observable<RequestResult<String>> pay(@Query("id") String orderCode);


    @POST("https://api.mch.weixin.qq.com/pay/unifiedorder")
    Observable<ResponseBody> unifiedOrder(@Body RequestBody body);

    @GET("hicardpay/getHiCardPay")
    Observable<RequestResult<GetHiCardPay>> getHiCardPay(
            @Query("orderCode") String orderCode,
            @Query("payType") String payType,
            @Query("openId") String openId,
            @Query("token") String token
    );


}
