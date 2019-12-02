package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.ddui.bean.DDUpgradeBean;
import com.dodomall.ddmall.module.community.Comment;
import com.dodomall.ddmall.module.community.Course;
import com.dodomall.ddmall.module.community.CourseModule;
import com.dodomall.ddmall.module.community.GroupCategoryModel;
import com.dodomall.ddmall.module.community.Like;
import com.dodomall.ddmall.module.community.MaterialVideoModule;
import com.dodomall.ddmall.module.publish.HistoryExtra;
import com.dodomall.ddmall.module.publish.PublishHisModule;
import com.dodomall.ddmall.module.publish.PublishInfoBody;
import com.dodomall.ddmall.shared.bean.Image;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author Stone
 * @time 2018/6/11  14:17
 * @desc ${TODD}
 */
public interface ICommunityService {

    @FormUrlEncoded
    @POST("group/addGroupTopicComment")
    @Deprecated
    Observable<RequestResult<MaterialVideoModule.CommentModule>> addGroupTopicComment(
            @Field("topicId") String topicId,
            @Field("content") String content
    );


    @FormUrlEncoded
    @POST("group/cancelGroupTopicLike")
    @Deprecated
    Observable<RequestResult<Object>> cancelGroupTopicLike(
            @Field("topicId") String topicId,
            @Field("likeId") String likeId
    );

    @FormUrlEncoded
    @POST("group/addGroupTopicLike")
    @Deprecated
    Observable<RequestResult<Like>> addGroupTopicLike(
            @Field("topicId") String topicId
    );

    @GET("materialLibrary/getMaterialLibraryCategoryList")
    @Deprecated
    Observable<RequestResult<List<GroupCategoryModel>>> getMaterialLibraryCategoryList(@Query("type") String type);


    @GET("group/getGroupTopicList")
    @Deprecated
    Observable<RequestResult<PaginationEntity<MaterialVideoModule, Object>>> getGroupTopicListNew(
            @Query("pageSize") int authorId,
            @Query("pageOffset") int page
    );

    @GET("materialLibrary/getMaterialLibraryList")
    Observable<RequestResult<PaginationEntity<MaterialVideoModule, Object>>> getMaterialLibraryList(@Query("type") int type, @Query("categoryId") String categoryId, @Query("keys") String keys, @Query("pageOffset") int pageOffset, @Query("pageSize") int pageSize);


    @GET("course/getCourseList")
    Observable<RequestResult<PaginationEntity<CourseModule, Object>>> getCourseList(@Query("courseType") int type, @Query("pageOffset") int pageOffset, @Query("pageSize)") int pageSize);

    @GET
    Observable<RequestResult<PaginationEntity<Course, Object>>> getCourseListByCategoryId(@Url String url, @Query("keys") String keys, @Query("categoryId") String categoryId, @Query("courseType") int type, @Query("pageOffset") int pageOffset, @Query("pageSize)") int pageSize);


    @GET("group/getGroupTopicCommentList")
    @Deprecated
    Observable<RequestResult<PaginationEntity<MaterialVideoModule.CommentModule, Object>>> getGroupTopicCommentList(@Query("pageOffset") int pageOffset, @Query("pageSize") int pageSize, @Query("topicId") String topicId);


    @GET("course/getCourseBannerList")
    Observable<RequestResult<ArrayList<Course>>> getCourseBannerList();

    @GET("course/getCourseCommentList")
    Observable<RequestResult<PaginationEntity<Comment, Object>>> getCourseCommentList(
            @Query("courseId") String courseId,
            @Query("pageOffset") int page
    );

    @FormUrlEncoded
    @POST("course/addCourseComment")
    Observable<RequestResult<Comment>> addCourseComment(
            @Field("courseId") String courseId,
            @Field("content") String content
    );

    @GET("course/getCourse")
    Observable<RequestResult<Course>> courseDetail(
            @Query("courseId") String courseId
    );


    @GET("materialLibrary/getMyMaterialLibraryList")
    Observable<RequestResult<PaginationEntity<PublishHisModule, HistoryExtra>>> getPublishHistory(@Query("pageOffset") int pageOffset, @Query("pageSize") int i);


    @GET("materialLibrary/getMyMaterialLibraryDetail")
    Observable<RequestResult<PublishHisModule>> getOldPublishData(@Query("libraryId") String libraryId);


    @Multipart
    @POST("upload/uploadImage")
    Observable<RequestResult<Image>> uploadImageNew(
            @Part MultipartBody.Part body
    );


    String OSS_CIRCLE = "materialLibrary";

    /**
     * 上传OSS图片,附带OSS的模块名字
     */
    @Multipart
    @POST("upload/uploadImage")
    Observable<RequestResult<Image>> uploadOSSImage(
            @Part("ossModuleCode") RequestBody ossModule,
            @Part MultipartBody.Part body
    );

    String TYPE_JSON = "Content-Type:application/json";

    @Headers(TYPE_JSON)
    @POST
    Observable<RequestResult<Object>> publishInfo(@Url String url, @Body PublishInfoBody body);

    /**
     * 新增协议接口
     */

    /*设备类型 0:iOS;1:Android*/
    int DEVICE_TYPE = 1;

    /**
     * App升级接口
     */
    @GET("version/getVersion")
    Observable<RequestResult<DDUpgradeBean>> upgradeApp(@Query("versionCode") String version, @Query("deviceType") int deviceType);


    /**
     * 取消喜欢发圈数据
     *
     * @param roundId 发圈素材Id
     */
    @POST("materials/delLikeRound")
    Observable<RequestResult<Object>> unLikeCommunityData(@Field("roundId") String roundId);

    /**
     * 发圈素材数据提交
     *
     * @param roundId 发圈素材Id
     * @param type    1=下载 2=喜欢 3=分享 4=取消喜欢
     */
    @FormUrlEncoded
    @POST("materials/pressMaterialsCount")
    Observable<RequestResult<Object>> submitCommunityCount(@Field("roundId") String roundId, @Field("type") int type);

}

