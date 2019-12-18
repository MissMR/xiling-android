package com.xiling.ddmall.ddui.service;

import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.bean.ProductEvaluateBean;
import com.xiling.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by Jigsaw at 2019/4/3
 * 商品测评相关
 */
public interface IProductMeasurementService {

    @GET("v20/engineer/queryProductEvaluateByEngineerId")
    Observable<RequestResult<ListResultBean<ProductEvaluateBean>>>
    getProductMeasurementByEngineerId(@Query("engineerId") String engineerId,
                                      @Query("pageOffset") int page,
                                      @Query("pageSize") int pageSize);

    @GET("product/getSpuProductEvaluateByProductId")
    Observable<RequestResult<ProductEvaluateBean>> getMeasurementDetail(@Query("productId") String spuId);

}
