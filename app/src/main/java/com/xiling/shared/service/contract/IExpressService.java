package com.xiling.shared.service.contract;

import com.xiling.shared.bean.ExpressCompany;
import com.xiling.shared.bean.ExpressDetails;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/6.
 */
public interface IExpressService {

    /**
     * 查询快递详情
     *
     * @param companyCode 快递名称代码
     * @param expressCode 快递号
     * @param id          传1
     * @param time        当前时间
     * @return
     */
    @GET("https://m.kuaidi100.com/query")
    Observable<ExpressDetails> getExpressDetails(
            @Query("type") String companyCode,
            @Query("postid") String expressCode,
            @Query("id") String id,
            @Query("temp") String time
    );

    /**
     * 查询快递详情
     *
     * @param companyCode 快递名称代码
     * @param expressCode 快递号
     * @param id          传1
     * @param time        当前时间
     * @return
     */
    @GET("https://m.kuaidi100.com/query")
    Observable<List<ExpressCompany>> listExpressCompany(
            @Query("postid") String expressCode
    );
}
