package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.shared.bean.LotteryActivityModel;
import com.dodomall.ddmall.shared.bean.LotteryWinner;
import com.dodomall.ddmall.shared.bean.LuckDraw;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/10/12.
 */
public interface ILotteryService {
    @GET("luckDraw/getActivity")
    Observable<RequestResult<LotteryActivityModel>> getActivity();

    @GET("luckDraw/getLuckDraw")
    Observable<RequestResult<LuckDraw>> getLuckDraw(
            @Query("activityId") String id
    );

    @GET("luckDraw/getLuckDrawHistory")
    Observable<RequestResult<PaginationEntity<LotteryWinner, Object>>> getWinnerList(
            @Query("pageSize")int pageSize,
            @Query("pageOffset")int page
    );

    @POST("luckDraw/acceptPrize")
    @FormUrlEncoded
    Observable<RequestResult<Object>> acceptPrize(
            @Field("drawId") String drawId,
            @Field("addressId") String addressId
    );
}
