package com.xiling.ddui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.activity.BrandActivity;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.bean.IndexBrandBean;
import com.xiling.image.GlideUtils;
import com.xiling.shared.service.contract.IFootService;

import java.util.List;

/**
 * @author 逄涛
 * 首页-精选品牌
 */
public class IndexSelectedBrandAdapter extends BaseQuickAdapter<IndexBrandBean, BaseViewHolder> {
    IndexBrandShopAdapter indexBrandShopAdapter;

    public IndexSelectedBrandAdapter() {
        super(R.layout.item_index_brand);
    }

    @Override
    protected void convert(BaseViewHolder helper, final IndexBrandBean item) {
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_brand), item.getIndexUrl());
        RecyclerView recyclerView = helper.getView(R.id.recycler_shop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<IndexBrandBean.IndexBrandBeanListBean> brandList = item.getIndexBrandBeanList();
        brandList.add(null);
        indexBrandShopAdapter = new IndexBrandShopAdapter(brandList);
        recyclerView.setAdapter(indexBrandShopAdapter);

        indexBrandShopAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IndexBrandBean.IndexBrandBeanListBean bean = (IndexBrandBean.IndexBrandBeanListBean) adapter.getData().get(position);
                if (bean != null) {
                    DDProductDetailActivity.start(mContext, bean.getProductId());
                } else {
                    BrandActivity.jumpBrandActivity(mContext, "", item.getBrandId());
                }
            }
        });

        helper.setOnClickListener(R.id.iv_brand, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BrandActivity.jumpBrandActivity(mContext, "", item.getBrandId());
            }
        });


    }
}
