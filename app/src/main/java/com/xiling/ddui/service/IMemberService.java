package com.xiling.ddui.service;

import com.xiling.ddui.bean.MemberCenterInfo;
import com.xiling.ddui.bean.StoreBean;
import com.xiling.ddui.bean.WeekCardConfigBean;
import com.xiling.ddui.bean.WeekCardInfo;
import com.xiling.shared.bean.api.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 会员接口
 */
public interface IMemberService {
    // 获取会员中心信息
    @GET("center/index")
    Observable<RequestResult<MemberCenterInfo>> getCenterInfo();


    // 获取周卡状态
    @GET("user/getWeekCardInfo")
    Observable<RequestResult<WeekCardInfo>> getWeekCardInfo();


    // 获取周卡配置
    @GET("weekCard/getWeekCardConfigList")
    Observable<RequestResult<List<WeekCardConfigBean>>> getWeekCardConfigList();
}
