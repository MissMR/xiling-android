package com.xiling.ddui.service;

import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.bean.StoreBean;
import com.xiling.ddui.bean.StoreProductBean;
import com.xiling.shared.bean.Image;
import com.xiling.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * created by Jigsaw at 2019/3/26
 * 小店相关协议
 */
public interface IStoreService {

    // 获取小店信息
    @GET("v20/shop/user/get/shop-info")
    Observable<RequestResult<StoreBean>> getStoreInfo();

    // 修改小店公告
    @FormUrlEncoded
    @POST("v20/shop/user/edit/announcement")
    Observable<RequestResult<Object>> editStoreDesc(@Field("announcement") String storeDesc);

    // 修改小店昵称
    @FormUrlEncoded
    @POST("v20/shop/user/edit/nickname")
    Observable<RequestResult<Object>> editStoreName(@Field("nickname") String storeName);

    // 更新小店头像
    @Multipart
    @POST("v20/shop/user/post/upload-head-image")
    Observable<RequestResult<Image>> uploadStoreImage(@Part MultipartBody.Part body);

    // 戳一下店主
    @FormUrlEncoded
    @POST("v20/shop/user/post/poke-shop-member")
    Observable<RequestResult<Object>> notifyStoreMaster(@Field("shopMemberIncId") String storeMasterIncId);

    // 小店商品上架
    @GET("v20/shop/product/get/add-shop-product")
    Observable<RequestResult<Object>> addProduct(@Query("productId") String spuId);

    // 小店商品列表
    @GET("v20/shop/product/get/product-list")
    Observable<RequestResult<ListResultBean<StoreProductBean>>> getStoreProductList(@Query("pageOffset") int page, @Query("pageSize") int pageSize);

    // 小店商品下架
    @GET("v20/shop/product/get/remove-shop-product")
    Observable<RequestResult<Object>> removeProduct(@Query("productId") String spuId);

    // 小店商品置顶
    @GET("v20/shop/product/get/top-shop-product")
    Observable<RequestResult<Object>> topProduct(@Query("productId") String spuId);

    // 更新小店访问量
    @FormUrlEncoded
    @POST("v20/shop/user/post/update-shop-visit-number")
    Observable<RequestResult<Object>> addStoreBrowserRecord(@Field("shopMemberIncId") String storeMasterIncId);

}
