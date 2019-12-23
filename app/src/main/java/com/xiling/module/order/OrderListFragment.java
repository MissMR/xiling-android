package com.xiling.module.order;

import android.os.Bundle;

import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.fragment.DDBaseListFragment;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.module.order.adapter.OrderListAdapter;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.Order;
import com.xiling.shared.bean.Page;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.constant.OrderStatus;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IOrderService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/12/6
 * 重构 继承 DDBaseListFragment
 */
public class OrderListFragment extends DDBaseListFragment<Order> implements DDBaseListFragment.OnRequestSuccessListener {

    private Page mPageParam;

    public static OrderListFragment newInstance(Page page) {
        Bundle args = new Bundle();
        args.putSerializable("page", page);
        OrderListFragment fragment = new OrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        getIntentData();
        setOnRequestSuccessListener(this);
    }

    @Override
    protected Observable<RequestResult<ListResultBean<Order>>> getApiObservable() {
        IOrderService service = ServiceManager.getInstance().createService(IOrderService.class);
        Observable<RequestResult<ListResultBean<Order>>>
                requestResultObservable = null;
        if ("all".equalsIgnoreCase(mPageParam.id)) {
            requestResultObservable = service.getAllOrderList(mPage);
        } else {
            requestResultObservable = service.getOrderListByStatus2(OrderStatus.getCodeByKey(mPageParam.id), mPage);
        }
        return requestResultObservable;
    }

    @Override
    protected BaseAdapter getBaseAdapter() {
        return new OrderListAdapter(getActivity());
    }

    private void getIntentData() {
        mPageParam = (Page) getArguments().get("page");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected boolean isNeedLogin() {
        return true;
    }

    @Override
    public void onRequestSuccess() {
    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listHandler(EventMessage message) {
        if (message.getEvent().equals(Event.cancelOrder) || message.getEvent().equals(Event.refundOrder) || message.getEvent().equals(Event.paySuccess) || message.getEvent().equals(Event.refundOrder) || message.getEvent().equals(Event.finishOrder)) {
            if (mAdapter == null) {
                return;
            }
            refresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listHandler(MsgStatus message) {
        if (message.getAction() == MsgStatus.ACTION_STORE_SHIT_SUCCEED) {
            refresh();
        }
    }
}
