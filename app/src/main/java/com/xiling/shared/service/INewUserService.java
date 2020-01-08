package com.xiling.shared.service;

import com.xiling.ddui.bean.AccountInfo;
import com.xiling.ddui.bean.UserCostomBean;
import com.xiling.ddui.bean.UserInComeBean;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.api.RequestResult;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface INewUserService {
    /**
     * 微信注册--登录
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/login-or-registry/we-chat-code?")
    Observable<RequestResult<NewUserBean>> wxLogin(@Query("code") String code, @Query("device") String device );


    /**
     * 微信登录绑定手机号
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("user/bind-phone")
    Observable<RequestResult<Boolean>> bindPhone(@Body RequestBody body);

    /**
     * 微信登录，绑定邀请码
     * @param inviteCode 邀请码
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/bind-invite-code")
    Observable<RequestResult<NewUserBean>> bindInviteCode(@Query("inviteCode") String inviteCode);



    /**
     * 发送登录验证码
     *
     * @param sendType 验证码类型
     * @param phone    手机号
     */
    @GET("user/sms-code")
    Observable<RequestResult<Object>> getLoginCode(
            @Query("type") int sendType,
            @Query("phone") String phone);


    /**
     * 手机号登录，校验验证码
     *
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//添加header表明参数是json格式的
    @POST("user/login-or-registry/phone-code")
    Observable<RequestResult<NewUserBean>> checkCaptcha(@Body RequestBody body);

    /**
     * 手机号登录，绑定微信
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/bind-we-chat")
    Observable<RequestResult<Boolean>> bindWXCode(@Query("code") String code, @Query("device") String device );


    /**
     * 获取用户信息
     */
    @GET("user/getRealUserInfo")
    Observable<RequestResult<NewUserBean>> getUserInfo();

    /**
     * 个人中心我的资产
     */
    @GET("user/getUserIncome")
    Observable<RequestResult<UserInComeBean>> getUserIncome();

    /**
     * 我的客户信息
     */
    @GET("client/my-client")
    Observable<RequestResult<UserCostomBean>> getMyCostom();

    /**
     * 获取账户信息
     */
    @GET("account/getInfo")
    Observable<RequestResult<AccountInfo>> getAccountInfo();

}
