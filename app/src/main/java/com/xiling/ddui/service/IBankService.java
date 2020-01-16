package com.xiling.ddui.service;

import com.xiling.ddui.bean.BankListBean;
import com.xiling.shared.bean.api.RequestResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IBankService {

    /**
     * 查询绑定银行卡列表
     *
     * @return
     */
    @GET("unionPayCard/queryUoionPayCardList")
    Observable<RequestResult<List<BankListBean>>> getBankCardList();

    /**
     * 添加银行卡
     *
     * @return
     */
    @FormUrlEncoded
    @POST("unionPayCard/addUoionPayCard")
    Observable<RequestResult<Object>> addBank(@FieldMap HashMap<String, String> params);

    /**
     * 解绑银行卡
     *
     * @return
     */
    @GET("unionPayCard/delUoionPayCard")
    Observable<RequestResult<Object>> deleteBank(@Query("cardId") String cardId);


    /**
     * 银行列表
     *
     * @return
     */
    @GET("bank/list")
    Observable<RequestResult<List<BankListBean>>> getBankList();


}
