package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.ddui.bean.ListResultBean;
import com.dodomall.ddmall.ddui.bean.MessageGroupBean;
import com.dodomall.ddmall.ddui.bean.UnReadMessageCountBean;
import com.dodomall.ddmall.shared.bean.Message;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-07-05
 */
public interface IMessageService {

    @GET("msg/noReadNum")
    Observable<RequestResult<UnReadMessageCountBean>> getUnReadCount();

    @Deprecated
    @GET("message/list")
    Observable<RequestResult<ListResultBean<Message>>> getMessageList(@Query("pageOffset") int page);

    @GET("msg/typeList")
    Observable<RequestResult<ArrayList<MessageGroupBean>>> getMessageGroupList();

    @GET("msg/msgList")
    Observable<RequestResult<ListResultBean<Message>>> getMessageList(@Query("pageOffset") int page, @Query("pageSize") int pageSize,
                                                                      @Query("templateType") String messageGroupId);


}
