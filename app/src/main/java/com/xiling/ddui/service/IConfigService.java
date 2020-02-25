package com.xiling.ddui.service;

import com.xiling.ddui.bean.HelloBean;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.bean.SystemConfigBean;
import com.xiling.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IConfigService {
    /**
     * 获取系统参数
     */
    @GET("config/sys-param")
    Observable<RequestResult<SystemConfigBean>> getSystemConfig();
}
