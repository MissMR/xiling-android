package com.dodomall.ddmall.module.order;

import com.dodomall.ddmall.ddui.bean.ListResultBean;
import com.dodomall.ddmall.ddui.fragment.DDBaseListFragment;
import com.dodomall.ddmall.module.order.adapter.OrderWaitCommentListAdapter;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.bean.OrderComment;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IOrderService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/12/6
 */
public class WaitCommentFragment extends DDBaseListFragment<OrderComment> {

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected Observable<RequestResult<ListResultBean<OrderComment>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IOrderService.class).getOrderWaitCommentList(mPage);
    }

    @Override
    protected BaseAdapter getBaseAdapter() {
        return new OrderWaitCommentListAdapter(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listHandler(EventMessage message) {
        if (message.getEvent().equals(Event.commentFinish)) {
            if (mAdapter == null || mAdapter.getItems().isEmpty()) {
                return;
            }
            refresh();
        }
    }
}
