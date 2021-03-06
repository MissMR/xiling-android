package com.xiling.shared.service.contract;

import com.xiling.shared.bean.UploadResponse;
import com.xiling.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-06-07
 */
public interface IUploadService {

    @Multipart
    @POST("upload/uploadImage")
    Observable<RequestResult<UploadResponse>> uploadImage(@Part MultipartBody.Part body, @Part("ossModuleCode") RequestBody ossModuleCode);

    /*身份证正面*/
    int IDCard_Front = 1;
    /*身份证反面*/
    int IDCard_Back = 0;


    /**
     * 上传身份证照片
     * @param body
     * @return
     */
    @Multipart
    @POST("auth/uploadImageForAuth")
    Observable<RequestResult<UploadResponse>> uploadIdCard(@Part MultipartBody.Part body);


    /**
     * 上传身份证照片数据（废弃）
     *
     * @param body  文件流
     * @param type  身份证类型
     * @param token 校验位
     */
    @Multipart
    @POST("upload/uploadImage")
    Observable<RequestResult<UploadResponse>> uploadPersonalIdCard(@Part MultipartBody.Part body,
                                                                   @Part("type") RequestBody type,
                                                                   @Part("token ") RequestBody token);

}
