package com.xiling.shared.constant;

/**
 * 事件枚举，用于 EventBus
 * Created by JayChan on 2016/12/14.
 */
public enum Event {
    wxLoginSuccess,
    loginSuccess,
    goToLogin,
    UPDATE_CARD_COUNT,

    hideLoading,
    toastErrorMessage,
    networkDisconnected,
    networkConnected,
    showAlert,
    inviterUpdate,
    showLoading,
    cartAmountUpdate,
    updateAvatar,
    updateNickname,
    selectAddress,
    changeCategory,
    viewCategory,
    viewNearStore,
    viewCart,
    viewCenter,
    viewCommunity,

    /*购物车选中状态变更*/
    selectCartItem,

    /*新的商品加入购物车*/
    AddToCart,

    alipayResponse,
    regionSelect,
    deleteAddress, saveAddress, cancelOrder, finishOrder, selectImage2Upload, refundOrder,
    paySuccess, refundExpressSubmit, commentFinish, transferSuccess, viewHome, logout,
    createOrderSuccess, selectLotteryAddress, acceptPrizeSuccess, sendSelectDialog, PUBLISH_NEW,
    addCommond, addSupport, cancelSupport, PUBLISH_EDIT_FINISH, wxLoginCancel,
    becomeStoreMaster, followStateChange, orderChange, materialUnLike

    ,LOGIN_SUCCESS
    ,LOGIN_OUT
    ,UPDE_PASSWORD
    ,REAL_AUTH_SUCCESS
    ,FINISH_ORDER
    ,CANCEL_ORDER
    ,ORDER_OVERTIME
    ,ORDER_RECEIVED_GOODS
    ,UPDATE_AVATAR
    ,UPDATE_NICK
    ,UPDATEE_PHONE
    ,RECHARGE_SUCCESS
    ,WEEK_CARD_OPEN
    ,WEEK_CARD_PAY
}
