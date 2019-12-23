package com.xiling.shared.service.contract;

import com.xiling.ddui.bean.WithdrawStatusBean;
import com.xiling.shared.bean.Balance;
import com.xiling.shared.bean.CommonExtra;
import com.xiling.shared.bean.DealDetail;
import com.xiling.shared.bean.PayDetail;
import com.xiling.shared.bean.Profit;
import com.xiling.shared.bean.ProfitData;
import com.xiling.shared.bean.Transfer;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.api.RequestResult;

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
