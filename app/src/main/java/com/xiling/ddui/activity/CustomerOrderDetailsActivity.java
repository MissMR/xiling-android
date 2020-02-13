package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.CustomterOrderAdapter;
import com.xiling.ddui.adapter.SkuOrderAdapter;
import com.xiling.ddui.bean.CustomerOrderBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 客户订单详情
 */
public class CustomerOrderDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_shouyi)
    TextView tvShouyi;
    @BindView(R.id.tv_warehouse_name)
    TextView tvWarehouseName;
    @BindView(R.id.recycler_sku)
    RecyclerView recyclerSku;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_order_create_time)
    TextView tvOrderCreateTime;
    @BindView(R.id.tv_order_pay_time)
    TextView tvOrderPayTime;
    @BindView(R.id.tv_order_complete_time)
    TextView tvOrderCompleteTime;

    CustomerOrderBean.OrderDetailsBean recordsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_details);
        ButterKnife.bind(this);
        setTitle("订单详情");
        setLeftBlack();
        if (getIntent() != null) {
            recordsBean = getIntent().getParcelableExtra("recordsBean");
        }
        initView();
    }

    private void initView() {
        GlideUtils.loadHead(context, ivHead, recordsBean.getHeadImage());
        tvName.setText("经销商：" + recordsBean.getNickName());
        tvShouyi.setText("收益指数 " + recordsBean.getReceiptsIndices());
        SkuOrderAdapter skuAdapter = new SkuOrderAdapter();
        recyclerSku.setAdapter(skuAdapter);
        recyclerSku.setLayoutManager(new LinearLayoutManager(context));
        skuAdapter.setNewData(recordsBean.getClientOrderDetailList());

        if (!TextUtils.isEmpty(recordsBean.getOrderCode())) {
            tvOrderId.setText("订单编号：" + recordsBean.getOrderCode());
        }


        tvOrderCreateTime.setText("创建时间：" + (TextUtils.isEmpty(recordsBean.getCreateDate()) ? "" : recordsBean.getCreateDate()));
        tvOrderPayTime.setText("付款时间：" + (TextUtils.isEmpty(recordsBean.getPayDate()) ? "" : recordsBean.getPayDate()));
        tvOrderCompleteTime.setText("完成时间：" + (TextUtils.isEmpty(recordsBean.getReceivedDate()) ? "" : recordsBean.getReceivedDate()));


    }
}
