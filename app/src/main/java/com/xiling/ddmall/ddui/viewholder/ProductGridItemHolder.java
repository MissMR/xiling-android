package com.xiling.ddmall.ddui.viewholder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.activity.DDProductDetailActivity;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.ddui.tools.UITools;
import com.xiling.ddmall.dduis.base.AvatarDemoMaker;
import com.xiling.ddmall.dduis.bean.DDProductBean;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by Jigsaw at 2019/1/22
 * 购物车 猜你喜欢数据 支付结果页列表
 */
public class ProductGridItemHolder extends BaseViewHolder {

    @BindView(R.id.sd_thumb)
    SimpleDraweeView sdThumb;

    @BindView(R.id.tv_title)
    TextView titleTextView;

    @BindView(R.id.tv_sale_count)
    TextView saleCountTextView;

    @BindView(R.id.tv_share_count)
    TextView shareCountTextView;

    @BindViews({R.id.sd_avatar_01, R.id.sd_avatar_02, R.id.sd_avatar_03})
    SimpleDraweeView[] avatars;

    @BindView(R.id.layout_user)
    RelativeLayout userLayout;

    @BindView(R.id.layout_vip)
    RelativeLayout vipLayout;

    @BindView(R.id.layout_user_price)
    RelativeLayout userPriceLayout;

    @BindView(R.id.layout_vip_price)
    RelativeLayout vipPriceLayout;

    @BindView(R.id.tv_price_now)
    TextView userPriceTextView;

    @BindView(R.id.tv_price_market)
    TextView marketPriceTextView;

    @BindView(R.id.tv_vip_price)
    TextView vipPriceTextView;

    @BindView(R.id.tv_price_reward)
    TextView rewardPriceTextView;

    @OnClick(R.id.btn_store)
    void onStorePressed() {
        DLog.i("onStorePressed:上/下架");
    }

    @OnClick(R.id.btn_share)
    void onSharePressed() {
        DLog.i("onSharePressed:推广");
    }

    @OnClick(R.id.btn_buy)
    void onBuyPressed() {
        DLog.i("onBuyPressed:购买");
        onRowPressed();
    }

    @OnClick(R.id.layout_row)
    void onRowPressed() {
        if (data != null) {
            DDProductDetailActivity.start(itemView.getContext(), data.getProductId());
        } else {
            ToastUtil.error(ToastUtil.ERR_DATA);
        }
    }

    DDProductBean data = null;

    public ProductGridItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //添加删除线
        UITools.addTextViewDeleteLine(marketPriceTextView);
    }

    public void setData(DDProductBean data) {
        this.data = data;
    }

    public void render() {
        if (data != null) {
            //设置商品缩略图
            sdThumb.setImageURI(data.getThumbUrlForShopNow());
            //设置商品标题
            titleTextView.setText("" + data.getProductName());

            shareCountTextView.setText("已推" + ConvertUtil.formatWan(data.getExtendTime()) + "次");
            saleCountTextView.setText("已抢" + ConvertUtil.formatWan(data.getSaleCount()) + "件");

            vipPriceTextView.setText("￥" + data.getMinRetailPrice());
            rewardPriceTextView.setText("赚" + data.getMaxRewardPrice());

            userPriceTextView.setText("￥" + data.getMinRetailPrice());
            marketPriceTextView.setText("￥" + data.getMinMarketPrice());

            if (isVip()) {
                vipLayout.setVisibility(View.VISIBLE);
                vipPriceLayout.setVisibility(View.VISIBLE);
                userLayout.setVisibility(View.GONE);
                userPriceLayout.setVisibility(View.GONE);
            } else {
                userLayout.setVisibility(View.VISIBLE);
                userPriceLayout.setVisibility(View.VISIBLE);
                vipLayout.setVisibility(View.GONE);
                vipPriceLayout.setVisibility(View.GONE);
            }

            //设置随机头像
            AvatarDemoMaker.setDemoAvatar(avatars);
        } else {
            DLog.e("" + this.getClass().getSimpleName() + " data is null.");
        }
    }

    public boolean isVip() {
        return SessionUtil.getInstance().isMaster();
    }

}