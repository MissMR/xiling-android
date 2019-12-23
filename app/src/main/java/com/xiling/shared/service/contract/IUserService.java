package com.xiling.shared.service.contract;

import com.xiling.ddui.bean.CashWithdrawRecordBean;
import com.xiling.ddui.bean.DQuestionBean;
import com.xiling.ddui.bean.FreeDataBean;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.bean.UserInfoBean;
import com.xiling.ddui.bean.RoleBean;
import com.xiling.ddui.bean.UserAuthBean;
import com.xiling.ddui.bean.WithdrawBean;
import com.xiling.module.NearStore.model.NearStoreModel;
import com.xiling.module.auth.model.AuthModel;
import com.xiling.module.auth.model.CardDetailModel;
import com.xiling.module.auth.model.CardItemModel;
import com.xiling.module.qrcode.model.GetSubscribe;
import com.xiling.module.user.model.SignModel;
import com.xiling.module.user.model.UpMemberModel;
import com.xiling.shared.basic.BaseBean;
import com.xiling.shared.bean.CheckNumber;
import com.xiling.shared.bean.Family;
import com.xiling.shared.bean.FamilyOrder;
import com.xiling.shared.bean.HasPasswordModel;
import com.xiling.shared.bean.LoginSwitchBean;
import com.xiling.shared.bean.MemberAchievement;
import com.xiling.shared.bean.MemberRatio;
import com.xiling.shared.bean.MemberStore;
import com.xiling.shared.bean.MyStatus;
import com.xiling.shared.bean.PayTypeModel;
import com.xiling.shared.bean.RuleIntro;
import com.xiling.shared.bean.ScoreStat;
import com.xiling.shared.bean.StoreFreight;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.VipTypeInfo;
import com.xiling.shared.bean.WeChatLoginModel;
import com.xiling.shared.bean.api.RequestResult;

import org.androidannotations.annotations.rest.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 用户接口
 * Created by JayChan on 2016/12/13.
 */
public interface IUserService {
    int CODE_REGISTER = 0;
    int CODE_LOGIN = 1;

    @FormUrlEncoded
    @POST("login")
    Observable<RequestResult<User>> login(@Field("username") String username, @Field("password") String password);

    @GET("logout")
    Observable<RequestResult<Object>> logout();

    @FormUrlEncoded
    @POST("user/add")
    Observable<RequestResult<Object>> register(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @POST("user/addWithNoPass")
    Observable<RequestResult<CheckNumber>> registerNoPassword(@FieldMap HashMap<String, String> params);

    @GET("user/getUserInfo")
    Observable<RequestResult<User>> getUserInfo();

    //更具邀请码获取用户基本信息
    @GET("user/invite-code")
    Observable<RequestResult<BaseBean<User>>> getUserInfoByCode(@Query("inviteCode") String invitationCode);

    @GET("user/getMemberInfoByPhone")
    Observable<RequestResult<User>> getUserInfoByPhone(@Query("phone") String phone);

    @FormUrlEncoded
    @POST("user/edit")
    Observable<RequestResult<Object>> editNickname(@Field("nickName") String nickname);

    @FormUrlEncoded
    @POST("user/putPassword")
    Observable<RequestResult<Object>> putPassword(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("user/changBindPhone")
    Observable<RequestResult<Object>> editPhone(@Field("phone") String newPhone, @Field("checkNumber") String captcha, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/bingPhone")
    Observable<RequestResult<Object>> bindPhone(@Field("phone") String newPhone, @Field("checkNumber") String captcha, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/chagesUserPassowrd")
    Observable<RequestResult<Object>> editPassword(@Field("password") String password, @Field("newPass") String newPassword);

    @FormUrlEncoded
    @POST("user/changeHead")
    Observable<RequestResult<Object>> updateAvatar(@Field("headImage") String imageUrl);

    @FormUrlEncoded
    @POST("user/checkUserInfo")
    Observable<RequestResult<Object>> checkUserInfo(
            @Field("phone") String phone,
            @Field("checkNumber") String string,
            @Field("password") String token
    );

    @GET("auth/get")
    Observable<RequestResult<AuthModel>> getAuth();

    @GET("account/get")
    Observable<RequestResult<CardDetailModel>> getCard();


    @GET("bank/list")
    Observable<RequestResult<List<CardItemModel>>> getBankList();

    @POST
    @FormUrlEncoded
    Observable<RequestResult<Object>> addCard(
            @Url String url,
            @FieldMap Map<String, String> body
    );

    @POST
    @FormUrlEncoded
    Observable<RequestResult<Object>> submitAuth(
            @Url String url,
            @FieldMap Map<String, String> body
    );

    @GET("family/getFamilyOrderList")
    Observable<RequestResult<FamilyOrder>> getFamilyOrderList(
            @Query("pageOffset") int page,
            @Query("pageSize") int size,
            @Query("level") int level
    );

    @GET("family/getFamilyList")
    Observable<RequestResult<Family>> getFamilyList(
            @Query("pageOffset") int page,
            @Query("pageSize") int size,
            @Query("level") int level,
            @Query("memberType") int memberType
    );

    //at 20190114 by hanQ 未读消息使用MessageService.getUnReadCount
    @Deprecated
    @GET("stat/myStat")
    Observable<RequestResult<MyStatus>> getMyStatus();

    @POST("transfer/doTransfer")
    @FormUrlEncoded
    Observable<RequestResult<Object>> doTransfer(
            @Field("payeePhone") String payeePhone,
            @Field("transferMoney") long transferMoney,
            @Field("trsMemo") String trsMemo,
            @Field("password") String password,
            @Field("checkNumber") String checkNumber
    );

    @GET("user/getUpMember")
    Observable<RequestResult<UpMemberModel>> getUpMember();

    @POST("score/signin")
    Observable<RequestResult<SignModel>> sign();

    @GET("user/getSubscribe")
    Observable<RequestResult<GetSubscribe>> getSubscribe();

    /**
     * @return 获取会员打折率，会员拥有积分，积分使用规则
     */
    @GET("sysConfig/getScoreStat")
    Observable<RequestResult<ScoreStat>> getScoreStat();

    /**
     * @return 获取会员打折率
     */
    @GET("sysConfig/getMemberRatio")
    Observable<RequestResult<List<MemberRatio>>> getMemberRatio();

    /**
     * @return 获取支付类型
     */
    @GET("payConfig/getPayTypeList?appType=1")
    Observable<RequestResult<List<PayTypeModel>>> getPayTypes();

    @POST("score/transferScore")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Observable<RequestResult<Object>> transferScore(
            @Body RequestBody body
    );

    @Post("user/bind-invite-code")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Observable<RequestResult<BaseBean<User>>> bindInviteCode(@Body RequestBody body);

    /**
     * @return 获取店铺配置
     */
    @GET("store/getMemberStore")
    Observable<RequestResult<MemberStore>> getMemberStore();

    /**
     * @return 获取店铺配置
     */
    @GET("store/getEditMemberStore")
    Observable<RequestResult<MemberStore>> getEditMemberStore();

    /**
     * @return 获取会员打折率
     */
    @GET("store/getStoreFreight")
    Observable<RequestResult<List<StoreFreight>>> getStoreFreight();

    @POST("store/addOrUpdateMemberStore")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Observable<RequestResult<Object>> addOrUpdateMemberStore(
            @Body RequestBody body
    );

    /**
     * @return 获取销售额 月销售额
     */
    @GET("achievement/getMemberAchievement")
    Observable<RequestResult<MemberAchievement>> getMemberAchievement();

    @POST("store/saveStoreFreight")
    Observable<RequestResult<Object>> saveStoreFreight(
            @Body List<StoreFreight> body
    );

    @GET("store/getNearByStoreList")
    Observable<RequestResult<NearStoreModel>> getNearStoreModelList(
            @Query("lag") double longitude,
            @Query("lat") double latitude,
            @Query("province") String province,
            @Query("city") String city,
            @Query("pageOffset") int page,
            @Query("pageSize") int size
    );

    @GET("store/getNearByStoreDetail")
    Observable<RequestResult<NearStoreModel>> getNearByStoreDetail(
            @Query("memberId") String memberId
    );

    @GET("memberRuleIntro/getRuleIntro")
    Observable<RequestResult<List<RuleIntro>>> getRuleIntro();

    @GET("user/hasPassword")
    Observable<RequestResult<HasPasswordModel>> hasPassowrd();

    @GET("user/getVipTypeInfo")
    Observable<RequestResult<List<VipTypeInfo>>> getVipTypeInfo();

    @GET("user/getDefaultUpMember")
    Observable<RequestResult<User>> getDefaultUpMember();

    @GET("weChatOpen/getAccessToken")
    Observable<RequestResult<WeChatLoginModel>> getAccessToken(
            @Query("code") String unionid
    );

    @Headers("Content-Type: application/json;charset=UTF-8")
    @Post("weChatOpen/getAccessToken")
    Observable<RequestResult<BaseBean<User>>> getAccessToken(
            @Body RequestBody body
    );

    /**
     * 微信注册--登录
     *
     * @param body
     * @return
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @Post("user/login-or-registry/we-chat-code")
    Observable<RequestResult<BaseBean<User>>> wxLogin(@Body RequestBody body);

    //微信登录---绑定手机号
    @Headers("Content-Type: application/json;charset=UTF-8")
    @Post("user/bind-phone")
    Observable<RequestResult<BaseBean<User>>> bindPhone(@Body RequestBody body);

    //?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Observable<RequestResult<Object>> getWXAccessToken(
            @Query("appid") String appid, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grant_type
    );

    // token:校验位md5(phone+salt+code+codeType)
    @FormUrlEncoded
    @POST("user/checkPhoneExist")
    Observable<RequestResult<HashMap<String, Integer>>> checkPhoneExist(@Field("phone") String phone,
                                                                        @Field("token") String token);

    // 获取各种状态
    @GET("auth/getDrawCashInfo")
    Observable<RequestResult<Object>> getDrawCashInfo();

    // 获取账户与安全各种状态
    @GET("auth/getDrawCashInfo")
    Observable<RequestResult<UserAuthBean>> getUserAuth();


    @GET("captcha/newPhoneBinding")
    Observable<RequestResult<Object>> updatePhoneNumber(@Query("phone") String phone, @Query("checkNumber") String captcha);

    @FormUrlEncoded
    @POST("auth/add")
    Observable<RequestResult<Object>> updateAuthInfo(@Field("idcardFrontImg") String idcardFrontImg, @Field("idcardBackImg") String idcardBackImg,
                                                     @Field("userName") String userName, @Field("identityCard") String identityCard);

    /**
     * 获取安全问题列表
     */
    @GET("auth/getSafeQuestion")
    Observable<RequestResult<DQuestionBean>> getSafeQuestion();

    /**
     * 获取安全问题列表
     */
    @FormUrlEncoded
    @POST("auth/addSafeKey")
    Observable<RequestResult<Object>> setSafeQuestion(@Field("safeType") int type, @Field("safeKey") String value);

    /**
     * 重置交易密码
     */
    @FormUrlEncoded
    @POST("auth/addCashPassword")
    Observable<RequestResult<Object>> resetTradePassword(@Field("cashPassword") String password);

    @GET("captcha/getAuthentication")
    Observable<RequestResult<Object>> checkUserAuth(@Query("idCard") String idCard, @Query("token") String token);

    @FormUrlEncoded
    @POST("auth/addBankAccount")
    Observable<RequestResult<Object>> bindBankCard(@Field("bankId") String bankId, @Field("bankAccount") String bankAccount,
                                                   @Field("bankcardAddress") String bankcardAddress);

    @GET("withdraw/toWithdraw")
    Observable<RequestResult<WithdrawBean>> getWithdrawInfo();

    // 提现申请
    @FormUrlEncoded
    @POST("withdraw/addWithdraw")
    Observable<RequestResult<String>> addWithdraw(@Field("amount") long money, @Field("tradePassword") String password, @Field("safeAnswer") String answer);


    @GET("withdraw/withdrawList")
    Observable<RequestResult<ListResultBean<CashWithdrawRecordBean>>> getWithdraws(@Query("pageOffset") int page, @Query("pageSize") int pageSize);

    @GET("withdraw/withdrawInfo")
    Observable<RequestResult<CashWithdrawRecordBean>> getWithdrawInfo(@Query("withdrawId") String withdrawId);

    @GET("user/getRoleInfoByMemberId")
    Observable<RequestResult<RoleBean>> getRoleInfo(@Query("memberId") String userId);

    @FormUrlEncoded
    @POST("version/getSwitchWithVersion")
    Observable<RequestResult<LoginSwitchBean>> getLoginFlag(@Field("androidVersion") int version, @Field("channelName") String channelName,
                                                            @Field("channelId") String channelId);

    /**
     * 获取0元购的用户状态
     */
    @GET("zeroBuy/checkUserFreeStatus")
    Observable<RequestResult<FreeDataBean>> getFreeBuyData();

    /**
     * 生成0元购活动的活动链接
     */
    @GET("zeroBuy/makeShareUrl")
    Observable<RequestResult<String>> getFreeBuyLink(@Query("type") String type);

    @GET("user/activityAdd")
    Observable<RequestResult<HashMap<String, String>>> getMemberInviteCode(@Query("phone") String phone);

    // 检查用户可提现次数
    @GET("withdraw/checkWithdrawNum")
    Observable<RequestResult<Object>> checkWidthdrawNum();

    @GET("achievement/addWechat")
    Observable<RequestResult<Object>> addWechat(@Query("wechat") String wechat);

    @GET("achievement/addWechatCode")
    Observable<RequestResult<Object>> addWechatCode(@Query("wechatCode") String wechatCode);

    @GET("achievement/getReferrerDetail")
    Observable<RequestResult<UserInfoBean>> getReferrerInfo();

    @GET("achievement/getWechatByIncId")
    Observable<RequestResult<UserInfoBean>> getUserInfo(@Query("incId") String incId);


}
