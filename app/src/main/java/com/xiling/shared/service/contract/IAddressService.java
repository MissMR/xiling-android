package com.xiling.shared.service.contract;

import com.xiling.shared.bean.Address;
import com.xiling.shared.bean.ServiceCity;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.body.SaveIdentityCardBody;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-06-11
 */
public interface IAddressService {

    @GET("address/list")
    Observable<RequestResult<PaginationEntity<Address, Object>>> getAddressList(@Query("pageOffset") int page);

    @GET("address/get/{id}")
    Observable<RequestResult<Address>> getAddressDetail(@Path("id") String addressId);

    @GET("address/getdefault")
    Observable<RequestResult<Address>> getDefaultAddress();

    @DELETE("address/del/{id}")
    Observable<RequestResult<Object>> deleteAddress(@Path("id") String addressId);

    @FormUrlEncoded
    @POST("address/edit/{id}")
    Observable<RequestResult<Object>> editAddress(@Path("id") String addressId, @FieldMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("address/add")
    Observable<RequestResult<Object>> createAddress(@FieldMap HashMap<String, Object> params);

    @POST("address/saveIdentityCard")
    Observable<RequestResult<Object>> saveIdentityCard(
            @Body SaveIdentityCardBody body
    );

    @GET("mapUtil/regeo")
    Observable<RequestResult<ServiceCity>> getCity(
            @Query("lag") double longitude,
            @Query("lat") double latitude
    );
}
