package com.xiling.ddui.adapter;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.ProductEvaluateBean;
import com.xiling.ddui.custom.DDAvatarContainerView;
import com.xiling.ddui.custom.DDExpandTextView;
import com.xiling.ddui.custom.DDStarView;
import com.xiling.dduis.base.AvatarDemoMaker;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.TextViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collection;
import java.util.Iterator;

/**
 * created by Jigsaw at 2019/3/20
 * 品控师列表
 */
public class MeasurementAdapter extends BaseQuickAdapter<ProductEvaluateBean, BaseViewHolder> {
    boolean mIsMaster;

    public MeasurementAdapter() {
        super(R.layout.item_measurement);
        mIsMaster = SessionUtil.getInstance().isMaster();
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductEvaluateBean item) {

        // 品控师相关
        SimpleDraweeView sdvAvatar = helper.getView(R.id.sdv_avatar);
        FrescoUtil.setImage(sdvAvatar, item.getHeadImage());

        helper.setText(R.id.tv_measurement_nickname, item.getNikeName());
        helper.setText(R.id.tv_measurement_desc, item.getUpdateTime());

        // 测评相关
        DDStarView starView = helper.getView(R.id.dd_star_view);
        starView.setValue(item.getScore());

        DDExpandTextView textView = helper.getView(R.id.dd_expand_text_view);
        textView.initWidth(ScreenUtils.getScreenWidth());
        textView.setCloseText(item.getContent());

        // 商品相关
        FrescoUtil.setImage((SimpleDraweeView) helper.getView(R.id.sdv_product), item.getThumbUrlForShopNow());
        helper.setText(R.id.tv_product_title, item.getProductName());

        helper.setVisible(R.id.tv_reward_price, mIsMaster);
        helper.setVisible(R.id.tv_market_price, !mIsMaster);
        helper.setText(R.id.tv_retail_price, ConvertUtil.centToCurrency(mContext, item.getMinPrice()));
        helper.setText(R.id.tv_reward_price, "赚" + ConvertUtil.cent2yuan(item.getMaxRewardPrice()));
        helper.setText(R.id.tv_market_price, ConvertUtil.centToCurrency(mContext, item.getMaxMarketPrice()));
        TextViewUtil.addThroughLine((TextView) helper.getView(R.id.tv_market_price));

        int saleCount = item.getSaleCount();
        boolean isShowSaleInfo = mIsMaster ? item.getShareCount() > 0 : saleCount > 0;
        helper.setVisible(R.id.dd_avatar_container, isShowSaleInfo);
        helper.setVisible(R.id.tv_sale_count, isShowSaleInfo);

        String hint = mIsMaster ? "已推%s件" : "已抢%s件";
        helper.setText(R.id.tv_sale_count, String.format(hint, mIsMaster ?
                item.getShareCountStr() : item.getSaleCountStr()));
        DDAvatarContainerView ddAvatarContainerView = helper.getView(R.id.dd_avatar_container);
        ddAvatarContainerView.setData(item.getFakeAvatars());

        helper.addOnClickListener(R.id.rl_product);

    }

    private void setFakeAvatarData(Collection<? extends ProductEvaluateBean> list) {
        Iterator<? extends ProductEvaluateBean> it = list.iterator();
        while (it.hasNext()) {
            ProductEvaluateBean productEvaluateBean = it.next();
            int fakeAvatarSize = mIsMaster ? productEvaluateBean.getShareCount()
                    : productEvaluateBean.getSaleCount();
            productEvaluateBean.setFakeAvatars(AvatarDemoMaker.randomAvatar(fakeAvatarSize > 3 ? 3 : fakeAvatarSize));
        }
    }

    @Override
    public void replaceData(@NonNull Collection<? extends ProductEvaluateBean> data) {
        setFakeAvatarData(data);
        super.replaceData(data);
    }

    @Override
    public void addData(@NonNull Collection<? extends ProductEvaluateBean> newData) {
        setFakeAvatarData(newData);
        super.addData(newData);
    }
}
