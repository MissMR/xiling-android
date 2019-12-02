package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.shared.bean.Coupon;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.bean.body.CalcOrderCouponListBody;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-06-20
 */
public interface ICouponService {

    @GET("coupon/getMemberCouponList")
    Observable<RequestResult<PaginationEntity<Coupon, Object>>> getMyCouponList(@Query("pageOffset") int page);

    @GET("coupon/getPlatformCouponList")
    Observable<RequestResult<PaginationEntity<Coupon, Object>>> getPlatformCouponList(@Query("pageOffset") int page);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("coupon/calcOrderCouponList")
    Observable<RequestResult<List<Coupon>>> getCouponListForOrder(@Body RequestBody body);

    @POST("coupon/receiveCoupon")
    Observable<RequestResult<Object>> receiveCouponById(@Query("couponId") String couponId);

    @GET("coupon/getCoupon")
    Observable<RequestResult<Coupon>> getCouponDetailById(@Query("couponId") String couponId);

    @GET("coupon/getProductCoupon")
    Observable<RequestResult<Coupon>> getProductCoupon(@Query("productId") String productId);

    @GET("coupon/getProductCouponByIds")
    Observable<RequestResult<List<Coupon>>> getProductCouponByIds(@Query("productIds") String productIds);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("coupon/calcOrderCouponList")
    Observable<RequestResult<List<Coupon>>> calcOrderCouponList(
            @Body CalcOrderCouponListBody body
    );
}
