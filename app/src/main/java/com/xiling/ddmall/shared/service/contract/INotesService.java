package com.xiling.ddmall.shared.service.contract;

import com.xiling.ddmall.shared.bean.NoticeDetailsModel;
import com.xiling.ddmall.shared.bean.NoticeListModel;
import com.xiling.ddmall.shared.bean.api.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/17.
 */
public interface INotesService {

    @GET("notes/getNotes")
    Observable<RequestResult<NoticeDetailsModel>> getNotes(
            @Query("notesId")String id
    );

    @GET("notes/getList")
    Observable<RequestResult<NoticeListModel>> getList(
            @Query("pageSize") int size,
            @Query("pageOffset") int page
    );
}
