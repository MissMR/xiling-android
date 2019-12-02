package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.ddui.bean.DDNUrlBean;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

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
