package com.xiling.shared.service.contract;

import com.xiling.shared.bean.InvitationCode;
import com.xiling.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service.contract
 * @since 2017-08-03
 */
public interface IInviteService {

    @GET("code/getInvitationCode")
    Observable<RequestResult<InvitationCode>> getInvitationCode();
}
