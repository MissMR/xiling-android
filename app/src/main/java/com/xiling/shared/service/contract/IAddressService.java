package com.xiling.shared.service.contract;

import com.xiling.ddui.bean.AddressListBean;
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
    /**
     * 收货列表
     * @param page
     * @param pageSize
     * @return
     */
    @GET("address/list")
    Observable<RequestResult<AddressListBean>> getAddressList(@Query("pageOffset") int page, @Query("pageSize") int pageSize);

    /**
     * 编辑收货地址
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("address/edit")
    Observable<RequestResult<Object>> editAddress(@FieldMap HashMap<String, Object> params);

    /**
     * 添加收货地址
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("address/add")
    Observable<RequestResult<Object>> createAddress(@FieldMap HashMap<String, Object> params);

    /**
     * 地址详情
     * @param addressId
     * @return
     */
    @GET("address/get")
    Observable<RequestResult<AddressListBean.DatasBean>> getAddressDetail(@Query("addressId") String addressId);

    /**
     * 获取默认地址
     * @return
     */
    @GET("address/getdefault")
    Observable<RequestResult<AddressListBean.DatasBean>> getDefaultAddress();



    @GET("address/delete")
    Observable<RequestResult<Object>> deleteAddress(@Query("addressId") String addressId);



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
