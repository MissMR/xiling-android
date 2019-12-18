package com.xiling.ddmall.module.store.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.ProductComment;
import com.xiling.ddmall.shared.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/21.
 */
public class StoreCommentAdapter extends BaseQuickAdapter<ProductComment, BaseViewHolder> {

    public StoreCommentAdapter(@Nullable List<ProductComment> data) {
        super(R.layout.item_store_product_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ProductComment item) {
        helper.setText(R.id.tvName, item.nickName);
        helper.setText(R.id.tvDate, item.payDate);
        helper.setText(R.id.tvProperties, item.properties);
        helper.setText(R.id.tvContent, item.content);
        helper.setText(R.id.tvMyReply, item.reply);
        helper.addOnClickListener(R.id.tvReply);
        BGANinePhotoLayout layoutNineImages = helper.getView(R.id.layoutNineImages);
        layoutNineImages.setData((ArrayList<String>) item.images);
        layoutNineImages.setDelegate(new BGANinePhotoLayout.Delegate() {
            @Override
            public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
//                previewPhoto(ninePhotoLayout, position);
                ImageUtil.previewImage(ninePhotoLayout.getContext(), (ArrayList<String>) item.images,position);
            }
        });
    }

}
