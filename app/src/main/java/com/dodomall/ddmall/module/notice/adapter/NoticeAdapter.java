package com.dodomall.ddmall.module.notice.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.NoticeListModel;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/19.
 */
public class NoticeAdapter extends BaseQuickAdapter<NoticeListModel.DatasEntity,BaseViewHolder> {

    public NoticeAdapter(@Nullable List<NoticeListModel.DatasEntity> data) {
        super(R.layout.item_notice,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeListModel.DatasEntity item) {
        helper.setText(R.id.tvTitle,item.title);
    }
}
