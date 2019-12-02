package com.dodomall.ddmall.ddui.service;

import com.dodomall.ddmall.ddui.bean.CommunityDataBean;
import com.dodomall.ddmall.ddui.bean.HelloBean;
import com.dodomall.ddmall.ddui.bean.ListResultBean;
import com.dodomall.ddmall.ddui.bean.TopicBean;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * created by Jigsaw at 2018/10/12
 */
public interface ICommunityService {

    int TYPE_HELLO_MORNING = 1;
    int TYPE_HELLO_EVENING = 2;

    int TYPE_HELLO_DOWNLOAD = 3;
    int TYPE_HELLO_SHARE = 4;

    int TYPE_TOPIC_FOLLOW = 1;
    int TYPE_TOPIC_UNFOLLOW = 0;

    int TYPE_MATERIAL_DOWNLOAD = 1;
    int TYPE_MATERIAL_LIKE = 2;
    int TYPE_MATERIAL_SHARE = 3;
    int TYPE_MATERIAL_UNLIKE = 4;

    int TYPE_TOPIC_LIST_HOT = 1;
    int TYPE_TOPIC_LIST_FOLLOW = 1;

    // 素材排序 1 精选 2 最新
    int TYPE_MATERIAL_ORDER_HOT = 1;
    int TYPE_MATERIAL_ORDER_NEW = 2;

    int TYPE_MATERIAL_PRODUCT = 0;
    int TYPE_MATERIAL_MARKETING = 1;

    // 获取早晚安语数据 接口  greetType：1=早安、2=晚安、不传代表所有数据
    @GET("greeting/getGreetingData")
    Observable<RequestResult<ListResultBean<HelloBean>>> getHelloList(@Query("pageOffset") int page,
                                                                      @Query("pageSize") int pageSize,
                                                                      @Query("greetType") int type);

    @GET("greeting/getGreetingData")
    Observable<RequestResult<ListResultBean<HelloBean>>> getHelloList(@Query("pageOffset") int page,
                                                                      @Query("pageSize") int pageSize);

    // 早晚安语下载或分享后数量+1 接口  type:3=下载 4=分享
    @POST("greeting/pressGreetingCount")
    @FormUrlEncoded
    Observable<RequestResult<Object>> increaseHelloCount(@Field("id") String id, @Field("type") int type);

    // 获取热门话题分页数据 接口
    @GET("communityTopic/topicList")
    Observable<RequestResult<ListResultBean<TopicBean>>> getHotTopicList(@Query("hotFlag") int type);

    // 获取话题列表
    @GET("communityTopic/topicList")
    Observable<RequestResult<ListResultBean<TopicBean>>> getTopicList(@Query("pageOffset") int page, @Query("pageSize") int pageSize);

    // 获取我关注的话题列表
    @GET("communityTopic/topicList")
    Observable<RequestResult<ListResultBean<TopicBean>>> getTopicFollowedList(@Query("pageOffset") int page, @Query("pageSize") int pageSize,
                                                                              @Query("myFlag") int type);

    // 获取热门话题详细信息 接口（目前只有话题信息）
    @GET("communityTopic/topicInfo")
    Observable<RequestResult<TopicBean>> getTopicDetail(@Query("topicId") String id);

    // 热门话题关注和取消关注 接口
    @GET("communityTopic/concernsTopic")
    Observable<RequestResult<Object>> updateTopicFollowState(@Query("topicId") String id, @Query("type") int type);

    // 获取发圈素材
    @GET("materials/getMaterialsData?type=2")
    Observable<RequestResult<ListResultBean<CommunityDataBean>>> getMaterialList(@Query("pageOffset") int page, @Query("pageSize") int pageSize,
                                                                                 @Query("materialType") int materialType);

    // 获取发圈素材 - 多商品关联版本
    @GET("materials/getMaterialsOnMultiPro?type=2")
    Observable<RequestResult<ListResultBean<CommunityDataBean>>> getMaterialsList(@Query("pageOffset") int page, @Query("pageSize") int pageSize,
                                                                                  @Query("materialType") int materialType);

    // 获取邀新专区素材
    @GET("materials/getMaterialsData?type=2&materialType=0&isInviteArea=1")
    Observable<RequestResult<ListResultBean<CommunityDataBean>>> getInvitationMaterialList(@Query("pageOffset") int page, @Query("pageSize") int pageSize);

    @GET("materials/getMaterialsData")
    Observable<RequestResult<ListResultBean<CommunityDataBean>>> getMaterialListByTopicId(@Query("pageOffset") int page, @Query("pageSize") int pageSize,
                                                                                          @Query("topicId") String topicId, @Query("type") int orderType);

    @GET("materials/pressMaterialsCount")
    Observable<RequestResult<Object>> increaseMaterialStateCount(@Query("id") String id, @Query("type") int type);

    // 根据spuId获取 商品素材
    @GET("materials/getMaterialsByProductId")
    Observable<RequestResult<ListResultBean<CommunityDataBean>>> getMaterialListBySpuId(@Query("productId") String spuId,
                                                                                        @Query("pageOffset") int page, @Query("pageSize") int pageSize);

}
