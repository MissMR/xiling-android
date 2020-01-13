package com.xiling.ddui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.BankListBean;
import com.xiling.image.GlideUtils;

import java.util.List;

/**
 *
 */
public class XLBankPayAdapter extends BaseQuickAdapter<BankListBean, BaseViewHolder> {

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    int selectPosition = 0;

    public XLBankPayAdapter(@Nullable List<BankListBean> data) {
        super(R.layout.item_bank_pay, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BankListBean item) {
        if (item.getBankLogoGround() > 0){
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon),item.getBankLogoGround());
        }else{
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon),item.getBankLogo());
        }

        helper.setText(R.id.tv_name,item.getBankName());
        helper.setBackgroundRes(R.id.iv_select,helper.getAdapterPosition() == selectPosition?R.drawable.icon_selected:R.drawable.icon_unselect);
    }
}
