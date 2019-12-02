package com.dodomall.ddmall.ddui.manager;

import android.os.Handler;
import android.os.Message;

import com.blankj.utilcode.utils.TimeUtils;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.PopupOrderList;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IProductService;

import java.util.ArrayList;
import java.util.List;

public class OrderToastManager {

    public interface OrderToastListener {
        //展示订单信息
        void onToastOrder(PopupOrderList.DatasEntity item);

        void onToastHide();
    }

    private OrderToastListener listener = null;

    public void setListener(OrderToastListener listener) {
        this.listener = listener;
    }

    private static OrderToastManager self = null;

    public static OrderToastManager share() {
        if (self == null) {
            self = new OrderToastManager();
        }
        return self;
    }

    private OrderToastManager() {

    }

    public void start() {
        getOrderToastList();
    }

    public void stop() {
        mToastHandler.removeMessages(GET_TOAST_DATA);
        mToastHandler.removeMessages(SHOW_TOAST);
//        mToastHandler.removeMessages(HIDE_TOAST);
    }

    private final int HIDE_TOAST = 0x000;
    private final int SHOW_TOAST = 0x111;
    private final int GET_TOAST_DATA = 0x222;

    private List<PopupOrderList.DatasEntity> mToastDatas = new ArrayList<>();

    private Handler mToastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SHOW_TOAST) {
                showOrderToast();
            } else if (msg.what == GET_TOAST_DATA) {
                getOrderToastList();
            } else if (msg.what == HIDE_TOAST) {
                if (listener != null) {
                    listener.onToastHide();
                } else {
                    DLog.w("no listener register for onToastHide()");
                }
            }
        }
    };

    private void getOrderToastList() {
        IProductService service = ServiceManager.getInstance().createService(IProductService.class);
        APIManager.startRequest(service.getPopupOrderList(1, Constants.PAGE_SIZE), new BaseRequestListener<PopupOrderList>() {
            @Override
            public void onSuccess(final PopupOrderList result) {
                if (result.datas.size() > 0) {
                    mToastDatas.clear();
                    mToastDatas.addAll(result.datas);
                    mToastHandler.sendEmptyMessageDelayed(SHOW_TOAST, TimeUtils.getNowTimeMills() % 10 * 1000);
                }
            }
        });
    }

    private void showOrderToast() {
        if (mToastDatas.size() > 0) {
            PopupOrderList.DatasEntity datasEntity = mToastDatas.get(0);
            if (listener != null) {
                listener.onToastOrder(datasEntity);
            } else {
                DLog.w("no listener register for onToastOrder.");
            }
            mToastDatas.remove(datasEntity);

            mToastHandler.removeMessages(HIDE_TOAST);
            mToastHandler.sendEmptyMessageDelayed(HIDE_TOAST, 5000);

            mToastHandler.removeMessages(SHOW_TOAST);
            mToastHandler.sendEmptyMessageDelayed(SHOW_TOAST, 8 * 1000 + TimeUtils.getNowTimeMills() % 5 * 1000);

        } else {
            mToastHandler.removeMessages(GET_TOAST_DATA);
            mToastHandler.sendEmptyMessageDelayed(GET_TOAST_DATA, 10 * 1000);
        }
    }


}
