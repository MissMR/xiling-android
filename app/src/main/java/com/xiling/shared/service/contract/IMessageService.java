package com.xiling.shared.service.contract;

import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.bean.MessageGroupBean;
import com.xiling.ddui.bean.UnReadMessageCountBean;
import com.xiling.ddui.bean.XLMessageBean;
import com.xiling.shared.bean.Message;
import com.xiling.shared.bean.api.RequestResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-07-05
 */
public interface IMessageService {

    /**
     * 消息类型列表
     *
     * @return
     */
    @GET("message/typeList")
    Observable<RequestResult<ArrayList<MessageGroupBean>>> getMessageGroupList();

    /**
     * 消息未读数量
     *
     * @return
     */
    @GET("message/noReadNum")
    Observable<RequestResult<String>> getUnReadCount();


    /**
     * 分类下消息列表
     *
     * @param page
     * @param pageSize
     * @param messageGroupId
     * @return
     */
    @GET("message/msgList")
    Observable<RequestResult<ListResultBean<XLMessageBean>>> getMessageList(@Query("pageOffset") int page, @Query("pageSize") int pageSize,
                                                                            @Query("templateType") String messageGroupId);




    /**
     * 分类消息设置已读
     *
     * @return
     */
    @POST("message/readAll")
    Observable<RequestResult<String>> readAll(@Query("typeId") String typeId);




}
