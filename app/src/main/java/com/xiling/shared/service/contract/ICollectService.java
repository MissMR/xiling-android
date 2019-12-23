package com.xiling.shared.service.contract;

import com.xiling.ddui.bean.CommunityDataBean;
import com.xiling.ddui.bean.CourseBean;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Chan on 2017/6/15.
 */

public interface ICollectService {

    String URL_COLLECT = "collect/addCollect";
    String URL_UNCOLLECT = "collect/delCollect";

    @GET("collect/getMemberCollectList")
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> getCollectList(@Query("pageOffset") int page);

    @FormUrlEncoded
    @POST()
    Observable<RequestResult<PaginationEntity<SkuInfo, Object>>> changeCollect(@Url String url, @Field("productId") String spuId);

    /**
     * 获取喜欢的发圈列表
     *
     * @param page 页码数
     */
    @GET("materials/getLikeRound")
    Observable<RequestResult<PaginationEntity<CommunityDataBean, Object>>> getLikeDataList(@Query("pageOffset") int page);

    /**
     * 获取喜欢的课程列表
     */
    @GET("course/likeCourseList")
    Observable<RequestResult<PaginationEntity<CourseBean, Object>>> getLikeCourseList(@Query("pageOffset") int page);

}
