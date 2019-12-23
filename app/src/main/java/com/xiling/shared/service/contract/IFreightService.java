package com.xiling.shared.service.contract;

import com.xiling.shared.bean.Freight;
import com.xiling.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-06-20
 */
public interface IFreightService {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("expressPrice/calc")
    Observable<RequestResult<Freight>> calculate(@Body RequestBody body);
}
