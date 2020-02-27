package com.xiling.ddui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.image.GlideUtils;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class SkuSelectParentAdapter extends BaseQuickAdapter<ProductNewBean.PropertiesBean, BaseViewHolder> {
    private SkuSelectChildAdapter childAdapter;
    private List<ProductNewBean.PropertiesBean.PropertyValuesBean> childList = new ArrayList<>();

    public void setSelectChildId(String selectChildId) {
        this.selectChildId = selectChildId;
    }

    private String selectChildId = "";

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    private OnSelectListener onSelectListener;

    public SkuSelectParentAdapter(int layoutResId, @Nullable List<ProductNewBean.PropertiesBean> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(final BaseViewHolder helper, final ProductNewBean.PropertiesBean item) {
        helper.setText(R.id.tv_title, item.getPropertyName());
        RecyclerView mChildRecyclerView = helper.getView(R.id.recycler_sku_child);


        childList = item.getPropertyValues();
        if (mChildRecyclerView.getLayoutManager() == null) {
            GridLayoutManager childLayoutManager = new GridLayoutManager(mContext, 3);
            childLayoutManager.setAutoMeasureEnabled(true);
            mChildRecyclerView.setLayoutManager(childLayoutManager);
            mChildRecyclerView.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(mContext, 10), ScreenUtils.dip2px(mContext, 10)));
            childAdapter = new SkuSelectChildAdapter(R.layout.item_product_sku_tag_group_layout, childList);
            if (!TextUtils.isEmpty(selectChildId)) {
                childAdapter.setSelectId(selectChildId);
            }
            childAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (onSelectListener != null && item.getPropertyValues().get(position).isNeedSelect()) {
                        onSelectListener.selectItem(item, ((SkuSelectChildAdapter) adapter).getData().get(position), (SkuSelectChildAdapter) adapter);
                    }
                }
            });
            mChildRecyclerView.setAdapter(childAdapter);
        } else {
            ((SkuSelectChildAdapter) mChildRecyclerView.getAdapter()).setNewData(childList);
        }

    }


    public interface OnSelectListener {
        void selectItem(ProductNewBean.PropertiesBean parentBean, ProductNewBean.PropertiesBean.PropertyValuesBean childBean, SkuSelectChildAdapter childAdapter);
    }

}
