package com.dodomall.ddmall.shared.service.contract;

import com.dodomall.ddmall.shared.bean.MainAdModel;
import com.dodomall.ddmall.shared.bean.Splash;
import com.dodomall.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-08-03
 */
public interface IAdService {

    @GET("splashScreen/getSplashScreen")
    Observable<RequestResult<Splash>> getSplashAd();

    @GET("popupWindows/get")
    Observable<RequestResult<MainAdModel>> getMainAd();
}
