package com.dodomall.ddmall.ddui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.TeamOrderSkuBean;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * created by Jigsaw at 2018/12/21
 * 团队订单 -- 商品item
 */
public class TeamOrderProductAdapter extends BaseQuickAdapter<TeamOrderSkuBean, BaseViewHolder> {
    public TeamOrderProductAdapter() {
        super(R.layout.item_team_order_product);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamOrderSkuBean item) {
        SimpleDraweeView productImageView = helper.getView(R.id.sdv_img);

        String productImageUrl = item.getProductImage();
        String skuName = item.getSkuName();
        String skuDesc = item.getProperties();
        long price = item.getRetailPrice();
        long quantity = item.getQuantity();

        productImageView.setImageURI(productImageUrl);
        helper.setText(R.id.tv_product_title, "" + skuName);
        helper.setText(R.id.tv_product_subtitle, "" + skuDesc);
        helper.setText(R.id.tv_price, "￥" + ConvertUtil.cent2yuanNoZero(price));
        helper.setText(R.id.tv_count, "x " + quantity);

        helper.setVisible(R.id.divider, getItemCount() - 1 != getIndexOfItem(item));
    }

    private int getIndexOfItem(TeamOrderSkuBean target) {
        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i) == target) {
                return i;
            }
        }
        return -1;
    }

}
