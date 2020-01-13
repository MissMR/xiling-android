package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.fragment.OrderFragment;
import com.xiling.shared.basic.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zlc.season.rxdownload2.function.Constant;

import static com.xiling.ddui.fragment.OrderFragment.ORDER_STATUS;
import static com.xiling.shared.Constants.ORDER_ALL;
import static com.xiling.shared.Constants.ORDER_IS_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.Constants.ORDER_WAIT_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;

/**
 * 我的订单列表
 */
public class OrderListActivit extends BaseActivity {

    String status = ORDER_ALL;

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_order)
    ViewPager viewpagerOrder;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> childNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        setTitle("我的订单");
        setLeftBlack();

        if (getIntent() != null) {
            status = getIntent().getStringExtra(ORDER_STATUS);
        }
        initData();
    }

    /**
     * 刷新全部订单的数据
     */
    public void refreshAllData() {
        OrderFragment allFragment = (OrderFragment) fragments.get(0);
        allFragment.getOrderList();
    }


    private void initData() {
        childNames.add("全部");
        childNames.add("待付款");
        childNames.add("待发货");
        childNames.add("待收货");
        childNames.add("已完成");

        fragments.add(OrderFragment.newInstance(ORDER_ALL));
        fragments.add(OrderFragment.newInstance(ORDER_WAIT_PAY));
        fragments.add(OrderFragment.newInstance(ORDER_WAIT_SHIP));
        fragments.add(OrderFragment.newInstance(ORDER_WAIT_RECEIVED));
        fragments.add(OrderFragment.newInstance(ORDER_IS_RECEIVED));

        slidingTab.setViewPager(viewpagerOrder, childNames.toArray(new String[childNames.size()]), this, fragments);

        switch (status) {
            case ORDER_WAIT_PAY:
                viewpagerOrder.setCurrentItem(1);
                break;
            case ORDER_WAIT_SHIP:
                viewpagerOrder.setCurrentItem(2);
                break;
            case ORDER_WAIT_RECEIVED:
                viewpagerOrder.setCurrentItem(3);
                break;
            case ORDER_IS_RECEIVED:
                viewpagerOrder.setCurrentItem(4);
                break;
        }

    }

}
