package com.xiling.ddmall.ddui.adapter;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.CategoryBean;

/**
 * created by Jigsaw at 2018/12/10
 * 分类左侧 分类导航
 */
public class CategoryNavigationAdapter extends BaseQuickAdapter<CategoryBean, BaseViewHolder> {
    private int mActiveIndex = 0;
    private Drawable mLeftCategoryTagDrawable;

    public CategoryNavigationAdapter() {
        super(R.layout.item_category_navigation);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryBean item) {
        helper.setText(R.id.tv_category, item.getCategoryNameShort());
        helper.getView(R.id.rl_category_item_root).setSelected(item.getCategoryId().equals(getData().get(mActiveIndex).getCategoryId()));
        setTextViewActive((TextView) helper.getView(R.id.tv_category));
    }

    private void setTextViewActive(TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(textView.isSelected() ? getLeftCategoryTagDrawable() : null,
                null, null, null);
    }

    private Drawable getLeftCategoryTagDrawable() {
        if (mLeftCategoryTagDrawable == null) {
            mLeftCategoryTagDrawable = mContext.getDrawable(R.mipmap.category_tag);
        }
        return mLeftCategoryTagDrawable;
    }

    public int getNavigationActiveIndex() {
        return mActiveIndex;
    }

    public void setCategoryActive(int index) {
        if (mActiveIndex == index) {
            return;
        }
        if (index >= 0 && index < getData().size()) {
            mActiveIndex = index;
            notifyDataSetChanged();
        }
    }
}
