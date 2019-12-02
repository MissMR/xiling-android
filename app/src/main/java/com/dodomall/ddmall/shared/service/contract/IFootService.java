package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Chan on 2017/6/15.
 */

public interface IFootService {
    @GET("viewHistory/getViewHistory")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getFootList(@Query("pageOffset") int page);

    @FormUrlEncoded
    @POST("viewHistory/delViewHistory")
    Observable<RequestResult<Object>> delViewRecord(
            @Field("productId") String productId
    );
}
