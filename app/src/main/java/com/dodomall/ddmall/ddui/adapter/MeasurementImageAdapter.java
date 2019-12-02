package com.dodomall.ddmall.ddui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * created by Jigsaw at 2019/3/19
 * 商品详情页测评图片适配器 单行4张
 */
public class MeasurementImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MeasurementImageAdapter() {
        super(R.layout.item_measurement_image);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() > 4 ? 4 : super.getItemCount();
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ((SimpleDraweeView) helper.getView(R.id.sdv_img)).setImageURI(item);
    }
}
