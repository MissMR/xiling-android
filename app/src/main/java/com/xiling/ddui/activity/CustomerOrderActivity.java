package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.fragment.CustomerOrderFragment;
import com.xiling.ddui.fragment.OrderFragment;
import com.xiling.shared.basic.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xiling.shared.Constants.ORDER_ALL;
import static com.xiling.shared.Constants.ORDER_IS_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.Constants.ORDER_WAIT_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;

/**
 * pt
 * 客户订单
 */
public class CustomerOrderActivity extends BaseActivity {

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_order)
    ViewPager viewpagerOrder;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> childNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        ButterKnife.bind(this);
        setTitle("客户订单");
        setLeftBlack();
        initData();
    }

    private void initData(){
        childNames.add("全部");
        childNames.add("未结算");
        childNames.add("已结算");

        fragments.add(CustomerOrderFragment.newInstance(""));
        fragments.add(CustomerOrderFragment.newInstance("1"));
        fragments.add(CustomerOrderFragment.newInstance("2"));
        slidingTab.setViewPager(viewpagerOrder, childNames.toArray(new String[childNames.size()]), this, fragments);
    }


}
