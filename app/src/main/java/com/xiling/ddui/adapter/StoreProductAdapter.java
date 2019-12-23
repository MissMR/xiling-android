package com.xiling.ddui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SpannableStringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.MyApplication;
import com.xiling.R;
import com.xiling.ddui.bean.StoreBean;
import com.xiling.ddui.bean.StoreProductBean;
import com.xiling.ddui.custom.DDAvatarContainerView;
import com.xiling.ddui.tools.DLog;
import com.xiling.dduis.base.AvatarDemoMaker;
import com.xiling.dduis.custom.SaleProgressView;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.TextViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collection;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by Jigsaw at 2019/3/21
 */
public class StoreProductAdapter extends BaseQuickAdapter<StoreProductBean, BaseViewHolder> {
    private HeaderHolder mHeaderHolder;

    // 优化convert卡顿
    private boolean mIsMaster = SessionUtil.getInstance().isMaster();

    public StoreProductAdapter() {
        super(R.layout.item_store_product);
    }

    public void updateUserType() {
        this.mIsMaster = SessionUtil.getInstance().isMaster();
        DLog.i("mIsMaster:" + mIsMaster);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreProductBean item) {

        renderViewByUserType(helper, mIsMaster);

        renderViewByStock(helper, item.isSoldOut());

        SimpleDraweeView simpleDraweeView = helper.getView(R.id.sdv_product);
        simpleDraweeView.setImageURI(item.getProductLogoImageUrl());

        helper.setText(R.id.tv_title, item.getProductName());
        helper.setText(R.id.tv_price, ConvertUtil.centToCurrency(mContext, item.getMinRetailPrice()));
        helper.setText(R.id.tv_price_reward, "赚" + ConvertUtil.cent2yuan(item.getMaxRewardPrice()));

        TextView tvPriceMarket = helper.getView(R.id.tv_price_market);
        tvPriceMarket.setText(ConvertUtil.centToCurrency(mContext, item.getMaxMarketPrice()));
        TextViewUtil.addThroughLine(tvPriceMarket);

        if (item.hasProductSaleInfo(mIsMaster)) {
            // 在售小店或销量信息
            helper.setVisible(R.id.ll_sale_info, true);
            String productInfo = mIsMaster ? item.getOnPriceShopActualNumber() + item.getOnSaleShopDefaultNumber() + "个小店在售"
                    : "已抢" + item.getFormatSaleCount() + "件";
            helper.setText(R.id.tv_product_sale_info, productInfo);

            DDAvatarContainerView ddAvatarContainerView = helper.getView(R.id.dd_avatar_container);
            ddAvatarContainerView.setData(item.getFakeAvatars());

        } else {
            helper.setVisible(R.id.ll_sale_info, false);
        }


        if (item.isOnFlashSaleActive()) {
            helper.setVisible(R.id.fl_progress, true);
            helper.setVisible(R.id.tv_top_left_tag, true);

            SaleProgressView saleProgressView = helper.getView(R.id.pb_progress);
//            saleProgressView.setTotalAndCurrentCount(item.getFlashSaleSpu().getInventory() + item.getFlashSaleSpu().getSaleCount(),
//                    item.getFlashSaleSpu().getSaleCount());

        } else {
            helper.setVisible(R.id.fl_progress, false);
            helper.setVisible(R.id.tv_top_left_tag, false);
        }

        setClickListener(helper);
    }

    private void renderViewByStock(BaseViewHolder holder, boolean isSoldOut) {

        holder.setVisible(R.id.tv_foreground_sold_out, isSoldOut);

        boolean enable = !isSoldOut;
        holder.getView(R.id.tv_btn_product_top).setEnabled(enable);
        holder.getView(R.id.tv_btn_product_remove).setEnabled(enable);
        holder.getView(R.id.tv_btn_product_share).setEnabled(enable);
        holder.getView(R.id.tv_btn_product_buy).setEnabled(enable);

    }

    private void renderViewByUserType(BaseViewHolder holder, boolean isMaster) {

        boolean masterVisible = isMaster;

        holder.setVisible(R.id.tv_price_reward, masterVisible);
        holder.setVisible(R.id.tv_price_market, !masterVisible);

        holder.setVisible(R.id.tv_btn_product_share, masterVisible);
        holder.setVisible(R.id.tv_btn_product_top, masterVisible);
        holder.setVisible(R.id.tv_btn_product_remove, masterVisible);
        holder.setVisible(R.id.tv_btn_product_buy, !masterVisible);

        View llSaleInfo = holder.getView(R.id.ll_sale_info);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llSaleInfo.getLayoutParams();
        if (masterVisible) {
            layoutParams.removeRule(RelativeLayout.ALIGN_BOTTOM);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.tv_price);
        } else {
            layoutParams.removeRule(RelativeLayout.BELOW);
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.fl_image);
        }


    }

    public void makeItemTop(int position) {
        if (position <= 0 || position >= getItemCount()) {
            return;
        }

        StoreProductBean item = getItem(position);
        remove(position);
        addData(0, item);
    }

    private void setClickListener(BaseViewHolder holder) {
        if (mIsMaster) {
            holder.addOnClickListener(R.id.tv_btn_product_top);
            holder.addOnClickListener(R.id.tv_btn_product_remove);
            holder.addOnClickListener(R.id.tv_btn_product_share);
        } else {
            holder.addOnClickListener(R.id.tv_btn_product_buy);
        }
    }

    public void setHeaderData(StoreBean storeBean) {
        if (mHeaderHolder == null || storeBean == null) {
            DLog.e("mHeaderHolder == null || storeBean == null");
            return;
        }
        mHeaderHolder.render(storeBean);
    }

    private void setFakeAvatarData(Collection<? extends StoreProductBean> list) {
        Iterator<? extends StoreProductBean> it = list.iterator();
        while (it.hasNext()) {
            StoreProductBean storeProductBean = it.next();
            int fakeAvatarSize = mIsMaster ? storeProductBean.getOnPriceShopActualNumber() + storeProductBean.getOnSaleShopDefaultNumber()
                    : storeProductBean.getSaleCount();
            storeProductBean.setFakeAvatars(AvatarDemoMaker.randomAvatar(fakeAvatarSize > 3 ? 3 : fakeAvatarSize));
        }
    }

    @Override
    public void replaceData(@NonNull Collection<? extends StoreProductBean> data) {
        setFakeAvatarData(data);
        super.replaceData(data);
    }

    @Override
    public void addData(@NonNull Collection<? extends StoreProductBean> newData) {
        setFakeAvatarData(newData);
        super.addData(newData);
    }

    @Override
    public void remove(int position) {
        super.remove(position);
        StoreBean storeBean = getHeaderHolder().getStoreBean();
        storeBean.setMyShopNumber(storeBean.getMyShopNumber() - 1);
        getHeaderHolder().setStoreBean(storeBean);
        getHeaderHolder().setProductInfo(storeBean);
    }

    @Override
    public int setHeaderView(View header) {
        mHeaderHolder = new HeaderHolder(header);
        return super.setHeaderView(header);
    }

    @Override
    public void setEmptyView(View emptyView) {
        super.setEmptyView(emptyView);
        if (mHeaderHolder != null) {
            mHeaderHolder.rlStoreProductInfo.setVisibility(View.GONE);
        }
    }

    public HeaderHolder getHeaderHolder() {
        return mHeaderHolder;
    }

    public static class HeaderHolder {
        View headerView;

        @BindView(R.id.tv_share)
        public TextView tvShare;
        @BindView(R.id.tv_store_edit)
        public TextView tvStoreEdit;
        @BindView(R.id.sdv_avatar)
        public SimpleDraweeView sdvAvatar;
        @BindView(R.id.tv_store_name)
        public TextView tvStoreName;
        @BindView(R.id.tv_store_desc)
        public TextView tvStoreDesc;
        @BindView(R.id.ll_store_browse_history)
        public LinearLayout llStoreBrowseHistory;
        @BindView(R.id.ll_auth_container)
        public LinearLayout llAuthContainer;
        @BindView(R.id.tv_store_product_title)
        public TextView tvStoreProductTitle;
        @BindView(R.id.tv_store_product_count)
        public TextView tvStoreProductCount;
        @BindView(R.id.rl_store_product_info)
        public RelativeLayout rlStoreProductInfo;
        @BindView(R.id.tv_browser_today)
        public TextView tvBrowserToday;
        @BindView(R.id.tv_browser_total)
        public TextView tvBrowserTotal;

        private StoreBean mStoreBean;

        public HeaderHolder(View headerView) {
            this.headerView = headerView;
//            View header = LayoutInflater.from(getContext()).inflate(R.layout.layout_store_header, mRecyclerView, false);
            ButterKnife.bind(this, headerView);
        }

        public void render(StoreBean storeBean) {

            setStoreBean(storeBean);

            renderByUserType();

            this.sdvAvatar.setImageURI(storeBean.getHeadImage());
            this.tvStoreName.setText(storeBean.getNickName() + "的小店");
            this.tvStoreDesc.setText(storeBean.getAnnouncement());
            this.tvBrowserToday.setText(storeBean.getTodayVisitNumber());
            this.tvBrowserTotal.setText(storeBean.getTotalVisitNumber());

            setProductInfo(storeBean);

        }

        public void setProductInfo(StoreBean storeBean) {
            SpannableStringBuilder spannableStringBuilder;
            if (SessionUtil.getInstance().isMaster()) {
                spannableStringBuilder = SpannableStringUtils.getBuilder(String.valueOf(storeBean.getMyShopNumber()))
                        .append("/" + storeBean.getMaxShopNumber())
                        .setForegroundColor(ContextCompat.getColor(MyApplication.getInstance(), R.color.ddm_gray_dark))
                        .create();
            } else {
                spannableStringBuilder = SpannableStringUtils.getBuilder(String.valueOf(storeBean.getMyShopNumber()))
                        .setForegroundColor(ContextCompat.getColor(MyApplication.getInstance(), R.color.ddm_red))
                        .create();
            }

            this.tvStoreProductCount.setText(spannableStringBuilder);
        }

        private void renderByUserType() {
            boolean isMaster = SessionUtil.getInstance().isMaster();
            tvStoreEdit.setVisibility(isMaster ? View.VISIBLE : View.GONE);
            tvShare.setVisibility(isMaster ? View.GONE : View.VISIBLE);
            llStoreBrowseHistory.setVisibility(isMaster ? View.VISIBLE : View.GONE);
            llAuthContainer.setVisibility(isMaster ? View.GONE : View.VISIBLE);
            tvStoreProductTitle.setText(isMaster ? "我上架的商品" : "上架精选好货");
        }

        public StoreBean getStoreBean() {
            return mStoreBean;
        }

        public void setStoreBean(StoreBean storeBean) {
            mStoreBean = storeBean;
        }
    }

}
