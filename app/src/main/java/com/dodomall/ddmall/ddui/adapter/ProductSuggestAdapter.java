package com.dodomall.ddmall.ddui.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.DDProductDetailActivity;
import com.dodomall.ddmall.shared.bean.Product;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.TextViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collection;

/**
 * created by Jigsaw at 2019/3/15
 */
public class ProductSuggestAdapter extends BaseQuickAdapter<Product.RelationProduct, BaseViewHolder> {

    private boolean isMaster;

    public ProductSuggestAdapter() {
        super(R.layout.item_product_suggest);
        isMaster = SessionUtil.getInstance().isMaster();
    }

    @Override
    public void replaceData(@NonNull Collection<? extends Product.RelationProduct> data) {
        super.replaceData(data);
        while (getData().size() % 3 != 0) {
            addData(new Product.RelationProduct());
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() > 9 ? 9 : super.getItemCount();
    }

    @Override
    protected void convert(BaseViewHolder helper, final Product.RelationProduct item) {
        ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
        layoutParams.width = (ScreenUtils.getScreenWidth() - ConvertUtil.dip2px(12)) / 3;
        helper.itemView.setLayoutParams(layoutParams);

        helper.itemView.setVisibility(TextUtils.isEmpty(item.getProductId()) ? View.INVISIBLE : View.VISIBLE);

        ((SimpleDraweeView) helper.getView(R.id.sdv_product)).setImageURI(item.getThumbUrlForShopNow());

        helper.setVisible(R.id.tv_reward_price, isMaster);
        helper.setVisible(R.id.tv_market_price, !isMaster);

        helper.setText(R.id.tv_title, item.getProductName());
        helper.setText(R.id.tv_retail_price, ConvertUtil.centToCurrencyNoZero(mContext, item.getMinPrice()));
        helper.setText(R.id.tv_reward_price, "赚" + ConvertUtil.cent2yuanNoZero(item.getMaxRewardPrice()));
        helper.setText(R.id.tv_market_price, ConvertUtil.centToCurrencyNoZero(mContext, item.getMaxMarketPrice()));
        TextViewUtil.addThroughLine((TextView) helper.getView(R.id.tv_market_price));

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDProductDetailActivity.start(v.getContext(), item.getProductId());
            }
        });

    }


}
