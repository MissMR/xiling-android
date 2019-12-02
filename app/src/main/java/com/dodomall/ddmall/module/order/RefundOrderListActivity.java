package com.dodomall.ddmall.module.order;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.bean.Page;
import com.dodomall.ddmall.shared.constant.AppTypes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

@Deprecated
public class RefundOrderListActivity extends BaseActivity {

    private OrderListFragment mOrderListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_order_list);
        ButterKnife.bind(this);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        setTitle("退款退货订单");
        setLeftBlack();
        mOrderListFragment = OrderListFragment.newInstance(
                new Page(AppTypes.ORDER.SELLER_REFUND, "待发货")
        );
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layoutContent, mOrderListFragment);
        fragmentTransaction.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(MsgStatus msgStatus) {
        switch (msgStatus.getAction()) {
            case MsgStatus.ACTION_REFUND_CHANGE:
                mOrderListFragment.refresh();
                break;
        }
    }
}
