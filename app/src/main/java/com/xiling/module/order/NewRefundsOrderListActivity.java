package com.xiling.module.order;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;

public class NewRefundsOrderListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_refunds_order);

        setTitle("售后订单");
        setLeftBlack();

        NewRefundsOrderListFragment fragment = NewRefundsOrderListFragment.newInstance(null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
