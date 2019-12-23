package com.xiling.shared.service.contract;

import com.xiling.shared.bean.Point;
import com.xiling.shared.bean.PointListExtra;
import com.xiling.shared.bean.ScoreModel;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-08-03
 */
public interface IPointService {

    @GET("score/scorelist")
    Observable<RequestResult<PaginationEntity<Point, PointListExtra>>> getPointList(@Query("pageOffset")int page);

    @GET("score/getscore")
    Observable<RequestResult<ScoreModel>> getScore();
}
