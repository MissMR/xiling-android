package com.dodomall.ddmall.shared.constant;

/**
 * 事件枚举，用于 EventBus
 * Created by JayChan on 2016/12/14.
 */
public enum Event {
    wxLoginSuccess,
    loginSuccess,
    goToLogin,

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

}
