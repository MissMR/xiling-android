package com.xiling.ddui.service;

import com.xiling.ddui.bean.MemberCenterInfo;
import com.xiling.ddui.bean.StoreBean;
import com.xiling.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 会员接口
 */
public interface IMemberService {
    // 获取会员中心信息
    @GET("center/index")
    Observable<RequestResult<MemberCenterInfo>> getCenterInfo();
}
