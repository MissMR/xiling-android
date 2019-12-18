package com.xiling.ddmall.shared.service.contract;

import com.xiling.ddmall.shared.bean.RegionCity;
import com.xiling.ddmall.shared.bean.RegionDistinct;
import com.xiling.ddmall.shared.bean.RegionProvince;
import com.xiling.ddmall.shared.bean.api.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-07-06
 */
public interface IRegionService {

    @GET("address/province")
    Observable<RequestResult<List<RegionProvince>>> getProvinceList();

    @GET("address/city")
    Observable<RequestResult<List<RegionCity>>> getCityList(@Query("provinceId") String provinceId);

    @GET("address/district")
    Observable<RequestResult<List<RegionDistinct>>> getDistinctList(@Query("cityId") String cityId);
}
