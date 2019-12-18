package com.xiling.ddmall.shared.service.contract;

import com.xiling.ddmall.ddui.bean.DDSuggestListBean;
import com.xiling.ddmall.shared.bean.CartAmount;
import com.xiling.ddmall.shared.bean.CartStore;
import com.xiling.ddmall.shared.bean.api.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-06-09
 */
public interface ICartService {

    @GET("cart/list")
    Observable<RequestResult<List<CartStore>>> getAllList();

    @GET("cart/getAccountList")
    Observable<RequestResult<List<CartStore>>> getListBySkuIds(@Query("skuIds") String skuIds);

    @FormUrlEncoded
    @POST("cart/operateCart")
    Observable<RequestResult<CartAmount>> updateCartItem(@Field("skuId") String skuId, @Field("quantity") int amount);

    @FormUrlEncoded
    @POST("cart/operateCartAdd")
    Observable<RequestResult<CartAmount>> addToCart(@Field("skuId") String skuId, @Field("quantity") int amount);

    @FormUrlEncoded
    @POST("cart/removeSkuId")
    Observable<RequestResult<Object>> removeItem(@Field("skuIds") String skuIds);

    @GET("cart/getCartQuantity")
    Observable<RequestResult<CartAmount>> getTotal();

    /**
     * 获取猜你喜欢的数据
     *
     * @param pageOffset 页码
     * @param pageSize   单页数据数量
     */
    @GET("product/thoughtULike")
    Observable<RequestResult<DDSuggestListBean>> getSuggestProduct(@Query("pageOffset") int pageOffset,
                                                                   @Query("pageSize") int pageSize);
}
