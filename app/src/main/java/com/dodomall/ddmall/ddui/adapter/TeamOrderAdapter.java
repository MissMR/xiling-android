package com.dodomall.ddmall.ddui.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.TeamOrderBean;
import com.dodomall.ddmall.shared.service.contract.IMasterCenterService;
import com.dodomall.ddmall.shared.util.ConvertUtil;

/**
 * created by Jigsaw at 2018/12/21
 * 团队订单item
 */
public class TeamOrderAdapter extends BaseQuickAdapter<TeamOrderBean, BaseViewHolder> {

    public TeamOrderAdapter() {
        super(R.layout.item_team_order);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TeamOrderBean item) {

        helper.setText(R.id.tv_order_id, "订单号：" + item.getOrderCode());
        helper.setText(R.id.tv_reward, ConvertUtil.cent2yuan2(item.getEstimateDirectRebate()));

        helper.setText(R.id.tv_order_status, item.getSettlementStatusStr());
        helper.setTextColor(R.id.tv_order_status,
                item.getSettlementStatus() == IMasterCenterService.SettlementStatus.SettlementNo
                        ? ContextCompat.getColor(mContext, R.color.ddm_yellow) : ContextCompat.getColor(mContext, R.color.ddm_red));

        RecyclerView recyclerView = helper.getView(R.id.rv_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setFocusable(false);
        TeamOrderProductAdapter adapter = new TeamOrderProductAdapter();
        recyclerView.setAdapter(adapter);
        adapter.replaceData(item.getSkuDetail());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (getOnItemClickListener() != null) {
                    getOnItemClickListener().onItemClick(TeamOrderAdapter.this, null, getIndexOfItem(item));
                }
            }
        });
    }

    private int getIndexOfItem(TeamOrderBean target) {
        for (int i = 0; i < getItemCount(); i++) {
            if (target == getItem(i)) {
                return i;
            }
        }
        return -1;
    }

}
