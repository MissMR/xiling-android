package com.dodomall.ddmall.shared.util;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dodomall.ddmall.shared.component.NoData;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/19.
 */
public class RvUtils {

    private static LinearLayoutManager layout;

    public static void configRecycleView(Context context, final RecyclerView recyclerView) {
        layout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public static void configRecycleView(Context context, final RecyclerView recyclerView, BaseQuickAdapter adapter) {
        configRecycleView(context, recyclerView);
        adapter.setEmptyView(new NoData(context));
        recyclerView.setAdapter(adapter);
    }

    public static LinearLayoutManager getLayoutManager() {
        return layout;
    }


    public static void clearItemAnimation(RecyclerView recyclerView) {
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
}
