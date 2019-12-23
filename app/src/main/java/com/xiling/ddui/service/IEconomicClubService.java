package com.xiling.ddui.service;

import com.xiling.ddui.bean.EconomicArticleBean;
import com.xiling.ddui.bean.EconomicClubHomeBean;
import com.xiling.ddui.bean.EconomicCourseBean;
import com.xiling.ddui.bean.EconomicCourseCategoryBean;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.shared.bean.api.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by Jigsaw at 2018/10/12
 * 商学院相关协议
 */
public interface IEconomicClubService {

    int TYPE_ARTICLE_NEWS = 1;
    int TYPE_ARTICLE_STORY = 2;

    // 商学院首页
    @GET("course/home")
    Observable<RequestResult<EconomicClubHomeBean>> getEconomicHomeInfo();

    // 获取店多多头条/店主故事列表
    @GET("course/getNewsOrStoryDetails")
    Observable<RequestResult<ListResultBean<EconomicArticleBean>>> getArticleList(@Query("pageOffset") int page,
                                                                                  @Query("pageSize") int pageSize,
                                                                                  @Query("type") int type);

    // 获取店多多头条/店主故事详情App
    @GET("course/getNewsOrStoryByIdForApp")
    Observable<RequestResult<ListResultBean<EconomicArticleBean>>> getArticleDetail(@Query("id") String id);


    // 获取课程分类
    @GET("course/getCourseCategory")
    Observable<RequestResult<List<EconomicCourseCategoryBean>>> getCourseCategory();

    // 获取课程列表
    @GET("course/getCourseDetails")
    Observable<RequestResult<ListResultBean<EconomicCourseBean>>> getCourseList(@Query("categoryId") String categoryId,
                                                                                @Query("pageOffset") int page,
                                                                                @Query("pageSize") int pageSize);

    // 课程添加喜欢
    @GET("course/addCourseLike")
    Observable<RequestResult<Object>> addCourseLike(@Query("courseId") String courseId);


    // 获取课程详情
    @GET("course/getCourseForApp")
    Observable<RequestResult<EconomicCourseBean>> getCourseDetail(@Query("courseId") String courseId);

}
