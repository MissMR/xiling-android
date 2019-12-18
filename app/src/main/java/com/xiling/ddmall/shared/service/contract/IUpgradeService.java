package com.xiling.ddmall.shared.service.contract;

import com.xiling.ddmall.shared.bean.UpgradeProgress;
import com.xiling.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-08-03
 */
public interface IUpgradeService {

    @GET("stat/getUpProgress")
    Observable<RequestResult<UpgradeProgress>> getUpgradeProgress();
}
