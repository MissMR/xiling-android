package com.xiling.ddui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.CategoryBean;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.util.ConvertUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collection;

/**
 * created by Jigsaw at 2018/12/10
 * 右侧分类 适配器
 */
public class CategoryAdapter extends BaseQuickAdapter<CategoryBean, BaseViewHolder> {

    private OnCategoryGridItemClickListener mOnCategoryGridItemClickListener;
    // 最后一项 高度阀值 为了沾满一屏
    private int mHeightThreshold;

    private int mCategoryChildrenCountEnough = -1;

    public CategoryAdapter() {
        super(R.layout.item_category);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CategoryBean item) {

        helper.setText(R.id.tv_category, item.getCategoryName());
        ((SimpleDraweeView) helper.getView(R.id.sdv_category_banner)).setImageURI(item.getBannerUrl());

        RecyclerView recyclerView = helper.getView(R.id.rv_category_grid);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        final CategoryGridAdapter adapter = new CategoryGridAdapter(item.getCategoryBeans());
        recyclerView.setAdapter(adapter);

        if (getOnCategoryGridItemClickListener() != null) {
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    mOnCategoryGridItemClickListener.onCategoryGridItemClick(adapter, item, view, position);
                }
            });
            helper.getView(R.id.tv_all_product).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击全部产品 相当于 点击grid第一个分类
                    mOnCategoryGridItemClickListener.onCategoryGridItemClick(adapter, item, v, 0);
                }
            });
        }

    }

    @Override
    public void replaceData(@NonNull Collection<? extends CategoryBean> data) {
        super.replaceData(data);
        if (!checkLastSubCategoryEnough()) {
            addNullData();
        }
    }

    private void addNullData() {
        while (!checkLastSubCategoryEnough()) {
            getData().get(getItemCount() - 1).getCategoryBeans().add(null);
        }
    }

    /**
     * 根据设置的数量 粗略计算是否子分类足够多 可以充满一屏
     *
     * @return
     */
    private boolean checkLastSubCategoryEnough() {
        if (getData() == null || getData().isEmpty()) {
            return false;
        }
        // 160 +(x * 90)
        return getData().get(getItemCount() - 1).getCategoryBeans() == null ? false
                : getData().get(getItemCount() - 1).getCategoryBeans().size() > getCategoryChildrenCountEnough();
    }

    public OnCategoryGridItemClickListener getOnCategoryGridItemClickListener() {
        return mOnCategoryGridItemClickListener;
    }

    public int getCategoryChildrenCountEnough() {
        if (mCategoryChildrenCountEnough <= 0) {
            DLog.i("dp:" + ConvertUtil.px2dip(mHeightThreshold));
            setCategoryChildrenCountEnough((ConvertUtil.px2dip(mHeightThreshold) - 160) / 90 * 3);
            DLog.i("count:" + getCategoryChildrenCountEnough());
        }
        return mCategoryChildrenCountEnough;
    }

    public void setCategoryChildrenCountEnough(int categoryChildrenCountEnough) {
        mCategoryChildrenCountEnough = categoryChildrenCountEnough;
    }

    public void setHeightThreshold(int heightThreshold) {
        mHeightThreshold = heightThreshold;
    }

    public void setOnCategoryGridItemClickListener(OnCategoryGridItemClickListener onCategoryGridItemClickListener) {
        mOnCategoryGridItemClickListener = onCategoryGridItemClickListener;
    }


    public interface OnCategoryGridItemClickListener {
        void onCategoryGridItemClick(BaseQuickAdapter<CategoryBean, BaseViewHolder> adapter, CategoryBean parentCategory, View view, int position);
    }


}
