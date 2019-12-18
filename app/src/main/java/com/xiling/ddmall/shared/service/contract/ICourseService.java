package com.xiling.ddmall.shared.service.contract;

import com.xiling.ddmall.ddui.bean.NetCourseInfoBean;
import com.xiling.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ICourseService {

    //获取头条新闻或者店主故事的详情
    @GET("course/getNewsOrStoryByIdForApp")
    Observable<RequestResult<NetCourseInfoBean>> getInfoAppData(@Query("id") String infoId);

    //获取课程详情
    @GET("course/getCourseForApp")
    Observable<RequestResult<NetCourseInfoBean>> getLearnAppData(@Query("courseId") String infoId);

    //喜欢课程
    @GET("course/addCourseLike")
    Observable<RequestResult<Object>> likeLearn(@Query("courseId") String infoId);

    //取消喜欢的课程
    @POST("course/delCourseLiker")
    Observable<RequestResult<Object>> unLikeLearn(@Query("courseId") String infoId);

}
