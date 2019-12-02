package com.dodomall.ddmall.shared.basic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.dodomall.ddmall.shared.util.UiUtils;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/24.
 */
public abstract class BaseListActivity extends BaseActivity implements OnRefreshListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setRefreshLayout(SmartRefreshLayout refreshLayout){
        refreshLayout.setOnRefreshListener(this);
    }

    public void setRecyclerView(RecyclerView recyclerView){
        UiUtils.configRecycleView(recyclerView,new LinearLayoutManager(this),true);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }
}
