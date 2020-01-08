package com.xiling.shared.service.contract;

import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.bean.OrderDetailBean;
import com.xiling.ddui.bean.OrderPayStatusBean;
import com.xiling.shared.bean.GetOrderStatusCount;
import com.xiling.shared.bean.Order;
import com.xiling.shared.bean.OrderComment;
import com.xiling.shared.bean.OrderCount;
import com.xiling.shared.bean.OrderResponse;
import com.xiling.shared.bean.RefundBody;
import com.xiling.shared.bean.RefundsOrder;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.body.ReceiveRefundGoodsBody;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IOrderService {


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("order/confirm-order-show")
    Observable<RequestResult<OrderDetailBean>> getConfirmOrder(@Body RequestBody requestBody);



    @GET("order/getOrderStatusCount")
    Observable<RequestResult<OrderCount>> getOrderCount();

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST()
    Observable<RequestResult<OrderResponse>> create(
            @Url String url,
            @Body RequestBody requestBody
    );

    @GET("order/getOrderByOrderCode")
    Observable<RequestResult<Order>> getOrderByCode(@Query("orderCode") String orderCode);

    @GET("refundOrder/getOrderRefundList")
    Observable<RequestResult<List<RefundsOrder>>> getOrderRefundList(
            @Query("orderCode") String orderCode
    );

    @GET("refundOrder/getRefundList")
    Observable<RequestResult<PaginationEntity<RefundsOrder, Object>>> getOrderRefundList(
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize
    );

    @GET("storeOrder/getStoreRefundList")
    Observable<RequestResult<PaginationEntity<RefundsOrder, Object>>> getStoreOrderRefundList(
            @Query("pageOffset") int page,
            @Query("pageSize") int pageSize,
            @Query("refundType") int refundType,
            @Query("refundStatus") int refundStatus
    );

    @GET("order/getAllOrderList")
    Observable<RequestResult<ListResultBean<Order>>> getAllOrderList(
            @Query("pageOffset") int page
    );

    @GET("order/getOrderList")
    Observable<RequestResult<PaginationEntity<Order, Object>>> getOrderListByStatus(@Query("orderStatus") int status, @Query("pageOffset") int page);

    @GET("order/getOrderList")
    Observable<RequestResult<ListResultBean<Order>>> getOrderListByStatus2(@Query("orderStatus") int status, @Query("pageOffset") int page);

    @GET("order/getOrderWaitCommentList")
    Observable<RequestResult<ListResultBean<OrderComment>>> getOrderWaitCommentList(@Query("pageOffset") int page);

    @FormUrlEncoded
    @POST("order/paymentOrder")
    Observable<RequestResult<Object>> payBalance(@Field("orderCode") String orderCode, @Field("password") String password, @Field("checkNumber") String captcha);

    @FormUrlEncoded
    @POST("order/waitPayOrderCheck")
    Observable<RequestResult<Object>> checkOrderToPay(@Field("orderCode") String orderCode);

    @FormUrlEncoded
    @POST("order/cancel")
    Observable<RequestResult<Object>> cancelOrder(@Field("orderCode") String orderCode);

    @FormUrlEncoded
    @POST("order/received")
    Observable<RequestResult<Object>> receiveOrder(@Field("orderCode") String orderCode);

    @POST("storeOrder/receiveRefundGoodsExt")
    Observable<RequestResult<Object>> receiveOrder(
            @Body RefundBody body
    );


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("refundOrder/apply")
    Observable<RequestResult<Object>> refundOrder(@Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("refundOrder/applyExt")
    Observable<RequestResult<Object>> refundOrderExt(@Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("refundOrder/applyExtEdit")
    Observable<RequestResult<Object>> refundOrderExtEdit(
            @Body RequestBody body
    );

    @FormUrlEncoded
    @POST("refundOrder/saveRefundExpress")
    Observable<RequestResult<Object>> refundExpress(@Field("refundId") String refundId, @Field("expressName") String expressName, @Field("expressCode") String expressCode);

    @FormUrlEncoded
    @POST("refundOrder/cancel")
    Observable<RequestResult<Object>> refundCancel(
            @Field("orderCode") String orderCode,
            @Field("remark") String remark
    );

    /**
     * 取消退款退货申请单
     *
     * @param remark
     * @return
     */
    @FormUrlEncoded
    @POST("refundOrder/cancelExt")
    Observable<RequestResult<Object>> refundCancelExt(
            @Field("refundId") String refundId,
            @Field("remark") String remark
    );

    @GET("storeOrder/getOrderStatusCount")
    Observable<RequestResult<GetOrderStatusCount>> getOrderStatusCount();

    @GET("storeOrder/getOrderList")
    Observable<RequestResult<ListResultBean<Order>>> getStoreOrderList(
            @Query("orderStatus") int status,
            @Query("pageOffset") int page
    );

    @POST("storeOrder/shipOrder")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Observable<RequestResult<Object>> shipOrder(
            @Body RequestBody body
    );

    @POST("storeOrder/agreeRefundMoneyExt")
    Observable<RequestResult<Object>> refundMoney(
            @Body RefundBody body
    );

    @POST("storeOrder/agreeRefundGoodsExt")
    Observable<RequestResult<Object>> refundGoods(
            @Body RefundBody body
    );

    @POST("storeOrder/rejectRefundGoodsExt")
    Observable<RequestResult<Object>> refuseRefundGoods(
            @Body RefundBody body
    );

    @POST("storeOrder/rejectRefundMoneyExt")
    Observable<RequestResult<Object>> refuseRefundMoney(
            @Body RefundBody body
    );

    @POST("storeOrder/receiveRefundGoods")
    Observable<RequestResult<Object>> receiveRefundGoods(
            @Body ReceiveRefundGoodsBody body
    );

    @GET("refundOrder/getRefundDetail")
    Observable<RequestResult<RefundsOrder>> getRefundDetail(
            @Query("refundId") String refundId
    );

    @GET("storeOrder/getStoreRefundDetail")
    Observable<RequestResult<RefundsOrder>> getSellerRefundDetail(
            @Query("refundId") String refundId,
            @Query("memberId") String memberId
    );

    // 获取订单状态
    @GET("order/getOrderStatus")
    Observable<RequestResult<OrderPayStatusBean>> getOrderStatus(@Query("orderCode") String orderCode);

}
