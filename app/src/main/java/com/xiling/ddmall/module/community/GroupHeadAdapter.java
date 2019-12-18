package com.xiling.ddmall.module.community;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xiling.ddmall.R;

import java.util.List;

/**
 * @author Stone
 * @time 2017/12/29  17:45
 * @desc group head recycleView adapter
 */

public class GroupHeadAdapter extends BaseQuickAdapter<GroupCategoryModel, BaseViewHolder> {
    public GroupHeadAdapter(@Nullable List<GroupCategoryModel> data) {
        super(R.layout.item_group_head_view, data);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, GroupCategoryModel groupCategoryModel) {
        //为了让4个控件可以完全显示
        View view = baseViewHolder.getView(R.id.head_root_View);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if(getData().size()>4) {
            params.width= (int) (ScreenUtils.getScreenWidth()/4.5);
        }else {
            params.width = (int) (ScreenUtils.getScreenWidth() / 4);
        }
        params.height= ConvertUtils.dp2px(100);
        view.setLayoutParams(params);
        baseViewHolder.setText(R.id.item_title_tv, groupCategoryModel.getName());
        String iconUrl = groupCategoryModel.getIconUrl();
        ImageView imageView = baseViewHolder.getView(R.id.item_gallery_iv);
        if (!TextUtils.isEmpty(iconUrl)) {
            Picasso.with(mContext).load(iconUrl).fit().placeholder(R.drawable.img_default).error(R.drawable.img_default).into(imageView);
        }else {
            imageView.setImageResource(R.drawable.img_default);
        }
    }

}
