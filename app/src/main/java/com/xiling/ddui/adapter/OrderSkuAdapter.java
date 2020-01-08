package com.xiling.ddui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.OrderDetailBean;
import com.xiling.ddui.tools.NumberHandler;

import java.util.List;

public class OrderSkuAdapter extends BaseQuickAdapter<OrderDetailBean.StoresBean, BaseViewHolder> {

    public OrderSkuAdapter() {
        super(R.layout.item_order_sku);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailBean.StoresBean item) {
        helper.setText(R.id.tv_title,item.getStoreName());
        item.getProducts();
        List<OrderDetailBean.StoresBean.ProductsBean> productsBeans = item.getProducts();

        RecyclerView skuRecyclerView = helper.getView(R.id.recycler_sku);
        skuRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        SkuAdapter skuAdapter = new SkuAdapter();
        skuRecyclerView.setAdapter(skuAdapter);
        skuAdapter.setNewData(productsBeans);


    }
}
