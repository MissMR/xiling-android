package com.xiling.module.order.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.basic.BaseCallback;
import com.xiling.shared.bean.Order;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.decoration.ListDividerDecoration;
import com.xiling.shared.service.OrderService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.EventUtil;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.order.adapter
 * @since 2017-07-06
 */
public class OrderListAdapter extends BaseAdapter<Order, OrderListAdapter.ViewHolder> {

    private Activity mContext;
    private int mModel;

    public OrderListAdapter(Activity context) {
        super(context);
        mContext = context;
    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.ViewHolder holder, int position) {
        final Order order = items.get(position);
        holder.setOrder(order);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventUtil.viewOrderDetail(context, order.orderMain.orderCode);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private Order mOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        public void setOrder(Order order) {
            mOrder = order;

        }


    }
}
