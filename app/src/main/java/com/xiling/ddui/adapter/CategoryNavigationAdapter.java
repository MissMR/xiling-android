package com.xiling.ddui.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.CategoryBean;
import com.xiling.ddui.bean.TopCategoryBean;

/**
 * created by Jigsaw at 2018/12/10
 * 分类左侧 分类导航
 */
public class CategoryNavigationAdapter extends BaseQuickAdapter<TopCategoryBean, BaseViewHolder> {

    public void setmActiveIndex(int mActiveIndex) {
        this.mActiveIndex = mActiveIndex;
        notifyDataSetChanged();
    }

    private int mActiveIndex = 0;
    public CategoryNavigationAdapter() {
        super(R.layout.item_category_navigation);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopCategoryBean item) {

        if (helper.getAdapterPosition() == mActiveIndex){
            helper.getView(R.id.rl_category_item_root).setSelected(true);
            helper.setTextColor(R.id.tv_category,mContext.getResources().getColor(R.color.login_button_wx_start));
        }else{
            helper.getView(R.id.rl_category_item_root).setSelected(false);
            helper.setTextColor(R.id.tv_category,mContext.getResources().getColor(R.color.title_color));
        }

        if (!TextUtils.isEmpty(item.getCategoryNameShort())){
            helper.setText(R.id.tv_category, item.getCategoryNameShort());
        }
        if (!TextUtils.isEmpty(item.getRemark())){
            helper.setVisible(R.id.tv_remark,true);
            helper.setText(R.id.tv_remark, item.getRemark());
        }else{
            helper.setVisible(R.id.tv_remark,false);
        }

        helper.getView(R.id.rl_category_item_root).setSelected(item.getCategoryId().equals(getData().get(mActiveIndex).getCategoryId()));
    }


}
