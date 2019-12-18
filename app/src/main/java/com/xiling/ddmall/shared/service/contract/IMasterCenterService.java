package com.xiling.ddmall.shared.service.contract;

import com.xiling.ddmall.ddui.bean.AchievementRecordBean;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.bean.TeamOrderBean;
import com.xiling.ddmall.ddui.bean.TeamOrderDetailBean;
import com.xiling.ddmall.ddui.bean.UserAchievementBean;
import com.xiling.ddmall.shared.bean.AchievementBean;
import com.xiling.ddmall.shared.bean.FansBean;
import com.xiling.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by Jigsaw at 2018/9/15
 * 店主中心
 */
public interface IMasterCenterService {


    // 获取店主中心首页数据
    @GET("achievement/getMemberAchievementDetail")
    Observable<RequestResult<AchievementBean>> getMemberAchievementDetail(@Query("memberId") String memberId);


    int ROLE_ALL = -1;
    int ROLE_VIP = 1;
    int ROLE_MASTER = 2;

    /**
     * @param page
     * @param size
     * @param type     1:全部;2:直属粉丝;3:团队粉丝;
     * @param roleType 1=筛选会员 2=筛选店主
     * @return
     */
    @GET("achievement/getFans")
    Observable<RequestResult<ListResultBean<FansBean>>> getFans(@Query("page") int page, @Query("size") int size, @Query("type") int type,
                                                                @Query("roleType") int roleType);

    @GET("achievement/getFans?type=1")
    Observable<RequestResult<ListResultBean<FansBean>>> searchFans(@Query("page") int page, @Query("size") int size, @Query("nickName") String nickName);

    // 粉丝收益情况
    @GET("achievement/getFansInfo")
    Observable<RequestResult<UserAchievementBean>> getUserAchievement(@Query("fansIncId") String userId);

    // 【粉丝流量大放送活动】- 粉丝列表页
    @GET("activity1/getActivityFansDetails")
    Observable<RequestResult<ListResultBean<FansBean>>> getActivityFans(@Query("page") int page, @Query("size") int size);

    // 全部
    int INCOME_ALL = 0;
    // 销售佣金
    int INCOME_SALE = 1;
    // 培训津贴
    int INCOME_TRAIN = 2;

    // 获取收入列表
    @GET("achievement/getIncomeDetail")
    Observable<RequestResult<ListResultBean<AchievementRecordBean>>> getIncomeList(@Query("page") int page, @Query("size") int size, @Query("prizeType") int type);

    // 全部
    int SETTLEMENT_ALL = 0;
    // 未结算
    int SETTLEMENT_WAIT_PAY = 1;
    // 已结算
    int SETTLEMENT_PAIED = 3;
    // 退货退款
    int SETTLEMENT_REFUND = 4;

    // 团队订单列表
    @GET("teamOrder/getTeamProductOrderList")
    Observable<RequestResult<ListResultBean<TeamOrderBean>>> getTeamOrderList(@Query("pageOffset") int page, @Query("pageSize") int pageSize,
                                                                              @Query("settlementStatus") int settlementStatus);

    /**
     * 从订单列表页进入必传；收益记录页进入不需要传
     * SettlementNo(1, "未结算"),
     * SettlementWait(2, "未结算"),
     * SettlementYes(3, "已结算"),
     * RefundMoney(4, "已退款"),
     * RefundGoods(5, "已退货");
     */
    public final static class SettlementStatus {
        /*默认值*/
        public static final int DEFAULT = 3;
        /*未结算*/
        public static final int SettlementNo = 1;
        /*等待结算*/
        public static final int SettlementWait = 2;
        /*已结算*/
        public static final int SettlementYes = 3;
        /*已退款*/
        public static final int RefundMoney = 4;
        /*已退货*/
        public static final int RefundGoods = 5;
    }

    // 团队订单详情
    @GET("teamOrder/getOrderBalanceInfo")
    Observable<RequestResult<TeamOrderDetailBean>> getTeamOrderDetail(@Query("orderCode") String orderCode, @Query("settlementStatus") int settlementStatus);

    // 收益记录订单详情
    @GET("teamOrder/getOrderBalanceInfo")
    Observable<RequestResult<TeamOrderDetailBean>> getIncomeOrderDetail(@Query("orderCode") String orderCode, @Query("prizeType") int prizeType);


}
