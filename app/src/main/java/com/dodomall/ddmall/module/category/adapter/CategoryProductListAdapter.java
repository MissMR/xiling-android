package com.dodomall.ddmall.module.category.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.MyApplication;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.TextViewUtil;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/6/7.
 */
public class CategoryProductListAdapter extends BaseQuickAdapter<SkuInfo,BaseViewHolder>{

    public CategoryProductListAdapter() {
        super(R.layout.layout_category_product,null);

    }

    @Override
    protected void convert(BaseViewHolder helper, SkuInfo item) {
        TextView tagTextView = helper.getView(R.id.itemTitleTv);
        TextViewUtil.setTagTitle(tagTextView,item.name,item.tags);
//        tagTextView.setText(item.name);
//        tagTextView.setTags(item.tags);
//        helper.setText(R.id.tvName,item.name);
        helper.setText(R.id.tvStock,"销量："+item.sales+"件");
        helper.setText(R.id.tvPrice, ConvertUtil.centToCurrency(MyApplication.getInstance().getApplicationContext(), item.retailPrice));
        FrescoUtil.loadRvItemImg(helper,R.id.ivProduct,item.thumb);

        TextView tvMarketPrice = helper.getView(R.id.itemMarkPriceTv);
        tvMarketPrice.setText(ConvertUtil.centToCurrency(tvMarketPrice.getContext(), item.marketPrice));
        TextViewUtil.addThroughLine(tvMarketPrice);
    }
}
