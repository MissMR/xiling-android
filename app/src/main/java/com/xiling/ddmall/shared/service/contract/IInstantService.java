package com.xiling.ddmall.shared.service.contract;

import com.xiling.ddmall.shared.bean.GetSecondKillProductListModel;
import com.xiling.ddmall.shared.bean.InstantData;
import com.xiling.ddmall.shared.bean.api.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-08-01
 */
public interface IInstantService {

    @GET("secondKill/getSecondKillList")
    Observable<RequestResult<List<InstantData.SecondKill>>> getInstantList();

    @GET("secondKill/getSecondKillProductList")
    Observable<RequestResult<GetSecondKillProductListModel>> getSecondKillProductList(
            @Query("pageOffset")  int page,
            @Query("pageSize")  int size,
            @Query("secondKillId")  String id
    );
}
