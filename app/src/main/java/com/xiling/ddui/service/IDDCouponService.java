package com.xiling.ddui.service;

import com.xiling.ddui.bean.DDCouponBean;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.shared.bean.api.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * created by Jigsaw at 2019/6/18
 */
public interface IDDCouponService {

    /**
     * 获取优惠券列表
     *
     * @param couponStatus 优惠券状态 DDCouponBean.STATUS_XXX
     */
    @GET("coupon/getCouponList")
    Observable<RequestResult<ListResultBean<DDCouponBean>>> getCouponList(@Query("status") int couponStatus,
                                                                          @Query("pageOffset") int page,
                                                                          @Query("pageSize") int pageSize);

    /**
     * 下单时获取可用优惠券
     *
     * @param requestBody
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("coupon/getAvailableUseCouponList")
    Observable<RequestResult<List<DDCouponBean>>> getAvailableCouponList(@Body RequestBody requestBody);

}
