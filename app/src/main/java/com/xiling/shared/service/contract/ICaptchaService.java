package com.xiling.shared.service.contract;

import com.xiling.shared.basic.BaseBean;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.api.RequestResult;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-03-10
 */
public interface ICaptchaService {

    int TYPE_MESSAGE = 0;
    int TYPE_VOICE = 1;


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
     * 根据邀请码获取用户基本信息
     */
    @GET("user/getRealUserInfo")
    Observable<RequestResult<NewUserBean>> getUserInfo();


    @GET("captcha/getRegisterMsg")
    Observable<RequestResult<Object>> getCaptchaForRegister(@Query("token") String token, @Query("phone") String phone);

    //sendtype 0 短信验证码 1语音验证码
    @GET("captcha/getRegisterMsg")
    Observable<RequestResult<Object>> getCaptchaForRegister(@Query("token") String token, @Query("phone") String phone, @Query("sendType") int type);

    // 发送换绑手机号时旧手机短信验证码+带语音(保持以前的协议)
    @GET("captcha/getMemberInfoChangeMsg")
    Observable<RequestResult<Object>> getUserCaptcha(@Query("token") String token, @Query("phone") String phone, @Query("sendType") int type);

    //微信登录绑定手机号
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("user/bind-phone")
    Observable<RequestResult<BaseBean<User>>> bindPhone(@Body RequestBody body);

    // 需要登录
    @GET("captcha/getMemberAuthMsg")
    Observable<RequestResult<Object>> getCaptchaForCheck(@Query("token") String token, @Query("phone") String phone);

    // 发送重置交易密码验证短信
    @GET("captcha/getMemberInfoChangeMsg")
    Observable<RequestResult<Object>> getCaptchaForUpdate(@Query("token") String token, @Query("phone") String phone);

    // 发送换绑手机号的新手机短信验证码+带语音(保持以前的协议)
    @GET("captcha/getUpdatePhoneMsg")
    Observable<RequestResult<Object>> getCaptchaForUpdatePhone(@Query("token") String token, @Query("phone") String phone);

    @GET("captcha/getUpdatePhoneMsg")
    Observable<RequestResult<Object>> getCaptchaForUpdatePhone(@Query("token") String token, @Query("phone") String phone, @Query("sendType") int sendType);

    @GET("captcha/getMemberAuthMsgByReservedPhone")
    Observable<RequestResult<Object>> getMemberAuthMsgByReservedPhone(
            @Query("phone") String phone
    );

    @GET("user/sendLoginPhoneCode")
    Observable<RequestResult<Object>> getCaptchaForRegister(
            @Query("phone") String phone
    );

    /**
     * 下方是新增的协议实现
     * <p>
     * at 20180827 实现登录和注册验证码支持语音验证码的逻辑 by hanQ
     */

    /*发送短信验证码*/
    int SEND_TYPE_SMS = 0;
    /*发送语音验证码*/
    int SEND_TYPE_VOICE = 1;

    /*注册业务*/
    int CODE_TYPE_REGISTER = 0;
    /*登录业务*/
    int CODE_TYPE_LOGIN = 1;
    // 单纯的校验验证码
    int CODE_TYPE_DEFAULT = 3;


    @GET("user/sendLoginPhoneCode")
    Observable<RequestResult<Object>> getLoginCode(
            @Query("token") String token,
            @Query("sendType") int sendType,
            @Query("phone") String phone);

    // 普通的校验验证码 codeType=3
    //
    @GET("user/sendLoginPhoneCode")
    Observable<RequestResult<Object>> getDefaultCaptcha(@Query("sendType") int sendType,
                                                        @Query("phone") String phone,
                                                        @Query("codeType") int codeType);

    /**
     * 发送注册验证码
     *
     * @param sendType 验证码类型
     * @param token    校验位
     * @param phone    手机号码
     */
    @GET("captcha/getRegisterMsg")
    Observable<RequestResult<Object>> getRegisterCode(@Query("sendType") int sendType,
                                                      @Query("token") String token,
                                                      @Query("phone") String phone);

    /**
     * 验证验证码有效性
     *
     * @param codeType 验证码业务类型
     * @param phone    电话号码
     * @param code     验证码
     * @param token    校验位
     */
    @FormUrlEncoded
    @POST("user/validatePhoneCode")
    Observable<RequestResult<Object>> checkCaptcha(@Field("codeType") int codeType,
                                                   @Field("phone") String phone,
                                                   @Field("code") String code,
                                                   @Field("token") String token);

}
