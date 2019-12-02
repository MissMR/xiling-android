package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.ddui.bean.CategoryBean;
import com.dodomall.ddmall.ddui.bean.DDSkuListBean;
import com.dodomall.ddmall.ddui.bean.ListResultBean;
import com.dodomall.ddmall.dduis.bean.DDProductBean;
import com.dodomall.ddmall.module.groupBuy.GroupBuyModel;
import com.dodomall.ddmall.module.order.body.AddCommentBody;
import com.dodomall.ddmall.module.push.PushSkuDetailModel;
import com.dodomall.ddmall.shared.bean.Category;
import com.dodomall.ddmall.shared.bean.InstantData;
import com.dodomall.ddmall.shared.bean.Keyword;
import com.dodomall.ddmall.shared.bean.PopupOrderList;
import com.dodomall.ddmall.shared.bean.Product;
import com.dodomall.ddmall.shared.bean.ProductComment;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.ViewHistory;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IProductService {

    // 新的分类接口
    @GET("category/getCategoryHomeData")
    Observable<RequestResult<ArrayList<CategoryBean>>> getCategorys();

    @Deprecated
    @GET("category/list")
    Observable<RequestResult<ArrayList<Category>>> getCategoryList();

    @GET("product/list/{categoryId}")
    Observable<RequestResult<PaginationEntity<Product, Object>>> getProductList(@Path("categoryId") String categoryId, @Query("pageOffset") int page, @Query("pageSize") int pageSize);

    @GET("product/skulist")
    Observable<RequestResult<ArrayList<SkuInfo>>> getListBySkuIds(@Query("skuIds") String skuIds);

    @GET("product/skuDetail")
    Observable<RequestResult<SkuInfo>> getSkuById(@Query("skuId") String skuId);

    @GET("product/spuDetail")
    Observable<RequestResult<Product>> getDetailById(@Query("spuId") String productId);

    @GET("product/skuDetailByProperty")
    Observable<RequestResult<SkuInfo>> getSkuByPropertyValueIds(@Query("productId") String productId, @Query("propertyValueIds") String propertyValueIds);

    @GET("secondKill/getIndexSecondKill")
    Observable<RequestResult<InstantData>> getInstantComponentData();

    @GET("product/getSpuListByCategory")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getCategoryProductList(@Query("categoryId") String mCategoryId, @Query("pageOffset") int page);

    @GET("product/searchSkuList")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> search(@Query("skuName") String keyword, @Query("pageOffset") int page);

    @GET("productPush/searchSkuList")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> searchPush(@Query("skuName") String keyword, @Query("pageOffset") int page);

    @FormUrlEncoded
    @POST("viewHistory/addViewRecord")
    Observable<Object> addViewRecord(
            @Field("memberId") String memberId,
            @Field("productId") String productId
    );

    @GET("viewHistory/getViewHistory")
    Observable<RequestResult<ViewHistory>> getViewHistory();

    @GET("product/getKeyWordList")
    Observable<RequestResult<List<Keyword>>> getHotKeywords();

    @GET("popupOrder/getPopupOrderList")
    Observable<RequestResult<PopupOrderList>> getPopupOrderList(
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @POST("productComment/saveProductComment")
    Observable<RequestResult<Object>> saveProductComment(
            @Body AddCommentBody body
    );

    @GET("productComment/getProductComment")
    Observable<RequestResult<PaginationEntity<ProductComment, Object>>> getProductComment(
            @Query("productId") String productId,
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("product/getNewUpSkuList")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getNewUpSkuList(
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("product/getSaleUpSkuList")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getSaleSkuList(
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("groupOrder/getGroupCode")
    Observable<RequestResult<GroupBuyModel>> getGroupInfo(
            @Query("groupCode") String id
    );

    @GET
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getPushHotProductList(
            @Url String url,
            @Query("pageSize") int pageSize,
            @Query("pageOffset") int page,
            @Query("sort") int sort
    );

    @GET
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getPushCategoryProductList(
            @Url String url,
            @Query("pageSize") int pageSize,
            @Query("pageOffset") int page,
            @Query("sort") int sort,
            @Query("categoryId") String categoryId
    );

    @GET("productPush/skuPushDetail")
    Observable<RequestResult<PushSkuDetailModel>> getPushSkuDetail(
            @Query("skuId") String skuId
    );

    /**
     * 根据关键字搜索SKU列表
     */
    @GET("product/getSkuPagerLikeName")
    Observable<RequestResult<DDSkuListBean>> searchSkuByKeyword(
            @Query("skuName") String keyword,
            @Query("sortField") String sortType,
            @Query("pageSize") int size,
            @Query("pageOffset") int page
    );

    // 猜你喜欢
    @GET("product/thoughtULike")
    Observable<RequestResult<ListResultBean<DDProductBean>>> getSuggestProduct(@Query("pageOffset") int pageOffset,
                                                                               @Query("pageSize") int pageSize);


    /**
     * 开抢提醒/取消提醒
     *
     * @param productId   产品ID
     * @param flashSaleId 抢购场次ID
     */
    @GET("v20/activity/panic-buying/rush-push")
    Observable<RequestResult<Object>> noticeFlashSale(@Query("flashSpuId") String productId, @Query("flashSaleId") String flashSaleId);

}
