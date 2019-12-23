package com.xiling.ddui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.EconomicCourseBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * created by Jigsaw at 2018/10/9
 */
public class EconomicCourseAdapter extends BaseQuickAdapter<EconomicCourseBean, BaseViewHolder> {

    public EconomicCourseAdapter(int layoutResId, @Nullable List<EconomicCourseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EconomicCourseBean item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_sub_title, item.getIntro());
        helper.setText(R.id.tv_tip, item.getReadCount() + "已学");

        SimpleDraweeView simpleDraweeView = helper.getView(R.id.sdv_img);
        simpleDraweeView.setImageURI(item.getImageUrl());

    }
}
