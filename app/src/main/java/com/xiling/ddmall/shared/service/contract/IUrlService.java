package com.xiling.ddmall.shared.service.contract;

import com.xiling.ddmall.ddui.bean.DDNUrlBean;
import com.xiling.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IUrlService {

    @FormUrlEncoded
    @POST("shortCreate")
    Observable<RequestResult<DDNUrlBean>> shortUrl(@Field("url") String url);

    @FormUrlEncoded
    @POST("shortUrlToUrl")
    Observable<RequestResult<DDNUrlBean>> restore(@Field("sUrl") String url);
}
