package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.ddui.bean.WithdrawStatusBean;
import com.dodomall.ddmall.shared.bean.Balance;
import com.dodomall.ddmall.shared.bean.CommonExtra;
import com.dodomall.ddmall.shared.bean.DealDetail;
import com.dodomall.ddmall.shared.bean.PayDetail;
import com.dodomall.ddmall.shared.bean.Profit;
import com.dodomall.ddmall.shared.bean.ProfitData;
import com.dodomall.ddmall.shared.bean.Transfer;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IBalanceService {

    @GET("withdraw/toWithdraw")
    Observable<RequestResult<WithdrawStatusBean>> checkWithdrawStatus();

    @GET("profit/get")
    Observable<RequestResult<ProfitData>> get();

    @GET("profit/getProfitList")
    Observable<RequestResult<PaginationEntity<Profit, CommonExtra>>> getProfitList(@Query("pageOffset") int page);

    @GET("profit/getBalanceList")
    Observable<RequestResult<PaginationEntity<Balance, CommonExtra>>> getBalanceList(@Query("pageOffset") int page);

    @GET("transfer/getTransferDetail")
    Observable<RequestResult<Transfer>> getTransferDetail(@Query("transferId") String transferId);

    @GET("transfer/getPayDetail")
    Observable<RequestResult<PayDetail>> getPayDetail(@Query("payId") String payId);

    @GET("deal/get/{id}")
    Observable<RequestResult<DealDetail>> getDealDetail(@Path("id") String dealId);

    @POST("deal/add")
    @FormUrlEncoded
    Observable<RequestResult<Object>> addDeal(
            @Field("applyMoney") long applyMoney,
            @Field("applyAccountId") String applyAccountId
    );
}
