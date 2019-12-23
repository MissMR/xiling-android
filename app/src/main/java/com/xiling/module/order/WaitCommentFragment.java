package com.xiling.module.order;

import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.fragment.DDBaseListFragment;
import com.xiling.module.order.adapter.OrderWaitCommentListAdapter;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.OrderComment;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IOrderService;

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
