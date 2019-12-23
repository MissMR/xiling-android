package com.xiling.shared.service.contract;

import com.xiling.ddui.bean.CategoryBean;
import com.xiling.ddui.bean.DDSkuListBean;
import com.xiling.shared.bean.Category;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.api.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ICategoryService {
    @GET("category/getParentCategory")
    Observable<RequestResult<PaginationEntity<Category, Object>>> getTopCategory(@Query("pageOffset") int page);

    @GET("category/getParentCategory")
    Observable<RequestResult<PaginationEntity<Category, Object>>> getTopCategory(
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("category/getChildCategory")
    Observable<RequestResult<PaginationEntity<Category, Object>>> getCategory(
            @Query("parentCategoryId") String categoryId,
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("category/getParentCategory")
    Observable<RequestResult<PaginationEntity<Category, Object>>> getPushTopCategory(
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("category/getChildCategory")
    Observable<RequestResult<PaginationEntity<Category, Object>>> getPushChildCategory(
            @Query("parentCategoryId") String categoryId,
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("product/getSpuListByCategory")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getSkuListByCategory(
            @Query("categoryId") String categoryId,
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    /*获取二级分类列表*/
    @FormUrlEncoded
    @POST("category/getSecondCategory")
    Observable<RequestResult<List<CategoryBean>>> getSecondCategory(@Field("nodeId") String nodeId);

    @GET("product/getSkuPagerByCategory")
    Observable<RequestResult<DDSkuListBean>> getSkuByCategory(@Query("categoryId") String categoryId, @Query("sortField") int sortField, @Query("pageOffset") int pageOffset, @Query("pageSize") int pageSize);


}
