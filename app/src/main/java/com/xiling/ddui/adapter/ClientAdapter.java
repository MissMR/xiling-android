package com.xiling.ddui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.MyClientListBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.image.GlideUtils;

import java.util.List;

/**
 * 我的客户列表
 */
public class ClientAdapter extends BaseQuickAdapter<MyClientListBean.DataBean, BaseViewHolder> {

    public ClientAdapter() {
        super(R.layout.item_client);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyClientListBean.DataBean item) {
        GlideUtils.loadHead(mContext, (ImageView) helper.getView(R.id.iv_head),item.getHeadImage());
        helper.setText(R.id.tv_name,item.getMemberName());
        helper.setText(R.id.tv_index,"本月消费指数¥"+ NumberHandler.reservedDecimalFor2(item.getMonthlyConsumption()));

    }
}
