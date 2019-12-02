package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.ddui.bean.DDHomeBean;
import com.dodomall.ddmall.ddui.bean.DHomeBean;
import com.dodomall.ddmall.ddui.bean.DPosterBean;
import com.dodomall.ddmall.ddui.bean.PosterBean;
import com.dodomall.ddmall.shared.bean.Page;
import com.dodomall.ddmall.shared.bean.PageConfig;
import com.dodomall.ddmall.shared.page.bean.Element;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IPageService {
    @GET("pageConfig/getIndexConfig")
    Observable<RequestResult<List<Element>>> getHomePageConfig();

//    @GET("pageConfig/getSecondKill")
//    Observable<RequestResult<List<Element>>> getInstantPageConfig();

    @GET("pageConfig/getToBeStoreConfig")
    Observable<RequestResult<List<Element>>> getToBeShopkeeperPageConfig();

    @GET("pageConfig/getGeneralConfig")
    Observable<RequestResult<PageConfig>> getPageConfig(@Query("pageId") String pageId);

    @GET("pageConfig/getGeneralLable")
    Observable<RequestResult<List<Page>>> getPageList();

    /**
     * 以下是新增协议
     * <p>
     * at 20180828 by hanQ
     */

    /**
     * 获取业务控制开关
     */
    @POST("version/getSwitchWithVersion")
    Observable<RequestResult<Object>> getDDFlag();

    /**
     * 获取第一个首页业务数据
     * <p>
     * 从1.0.2开始首页数据改为getDHomeData,这个接口获取的是首页第一个业务数据
     */
//    @GET("pageConfig/getIndexConfigNew")
    @GET("getIndexConfigNew")
    Observable<RequestResult<DDHomeBean>> getDDHomeData();

    /**
     * 获取首页数据
     */
//    @GET("pageConfig/index")
    @GET("index")
    Observable<RequestResult<DHomeBean>> getDHomeData();

    /**
     * 获取专属海报数据
     */
    @POST("splashScreen/getPosterList")
    Observable<RequestResult<DPosterBean>> getPosterData();


}
