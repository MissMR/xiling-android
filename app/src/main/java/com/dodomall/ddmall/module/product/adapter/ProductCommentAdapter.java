package com.dodomall.ddmall.module.product.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.ProductComment;
import com.dodomall.ddmall.shared.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/30.
 */
public class ProductCommentAdapter extends BaseQuickAdapter<ProductComment, BaseViewHolder> {

    public ProductCommentAdapter(@Nullable List<ProductComment> data) {
        super(R.layout.item_product_comment,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ProductComment item) {
        helper.setText(R.id.tvCommentName,item.nickName);
        helper.setText(R.id.tvCommentContent,item.content);
        helper.setText(R.id.tvCommentProperties,item.properties);

        BGANinePhotoLayout layoutNineImages = helper.getView(R.id.layoutCommentNineImages);
        layoutNineImages.setData((ArrayList<String>) item.images);
        layoutNineImages.setDelegate(new BGANinePhotoLayout.Delegate() {
            @Override
            public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                ImageUtil.previewImage(ninePhotoLayout.getContext(), (ArrayList<String>) item.images,position);
            }
        });
    }
}
