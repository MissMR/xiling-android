package com.dodomall.ddmall.dduis.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.DDProductDetailActivity;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.UITools;
import com.dodomall.ddmall.dduis.base.AvatarDemoMaker;
import com.dodomall.ddmall.dduis.bean.DDProductBean;
import com.dodomall.ddmall.module.user.LoginActivity;
import com.dodomall.ddmall.shared.component.dialog.DDMProductQrCodeDialog;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.DDHomeService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpuProductViewHolder extends BaseViewHolder {

    public interface ReloadListener {
        void onReload(int position);
    }

    ReloadListener reloadListener = null;

    public void setReloadListener(ReloadListener reloadListener) {
        this.reloadListener = reloadListener;
    }

    @BindView(R.id.spuStatusView)
    TextView spuStatusView;

    @BindView(R.id.sd_thumb)
    SimpleDraweeView sdThumb;

    @BindView(R.id.tv_rush_tag)
    TextView rushTagTextView;

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

    @BindView(R.id.btn_store)
    Button storeButton;

    @OnClick(R.id.btn_store)
    void onStorePressed() {
        DLog.i("onStorePressed:上/下架");
    }

    @OnClick(R.id.btn_share)
    void onSharePressed() {
        DLog.i("onSharePressed:推广");

        if (!SessionUtil.getInstance().isLogin()) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }

        DDMProductQrCodeDialog dialog = new DDMProductQrCodeDialog((Activity) context, data, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                addShareCount();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
        dialog.show();
    }

    void addShareCount() {
        DDHomeService homeService = ServiceManager.getInstance().createService(DDHomeService.class);
        APIManager.startRequest(homeService.addShareCount(data.getProductId()), new RequestListener<Object>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                data.addExtendTime();

                if (reloadListener != null) {
                    reloadListener.onReload(getAdapterPosition());
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @OnClick(R.id.btn_buy)
    void onBuyPressed() {
        DLog.i("onBuyPressed:购买");
        onRowPressed();
    }

    @OnClick(R.id.layout_row)
    void onRowPressed() {
        if (data != null) {
            DDProductDetailActivity.start(context, data.getProductId());
        } else {
            ToastUtil.error(ToastUtil.ERR_DATA);
        }
    }

    DDProductBean data = null;
    Context context = null;

    public SpuProductViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        //添加删除线
        UITools.addTextViewDeleteLine(marketPriceTextView);

        //TIPS 2.0版本暂时隐藏上架按钮
        storeButton.setVisibility(View.GONE);
    }

    public void setData(DDProductBean data) {
        this.data = data;
    }

    public void render() {
        if (data != null) {

            //是否显示抢购标签
            if (data.getIsFlashSale() == 1) {
                rushTagTextView.setVisibility(View.VISIBLE);
                if (data.getFlashInventory() > 0) {
                    spuStatusView.setVisibility(View.INVISIBLE);
                } else {
                    spuStatusView.setVisibility(View.VISIBLE);
                }
            } else {
                rushTagTextView.setVisibility(View.INVISIBLE);
                if (data.getStock() > 0) {
                    spuStatusView.setVisibility(View.INVISIBLE);
                } else {
                    spuStatusView.setVisibility(View.VISIBLE);
                }
            }

            //设置商品缩略图
            sdThumb.setImageURI(data.getThumbUrlForShopNow());
            //设置商品标题
            titleTextView.setText("" + data.getProductName());

            shareCountTextView.setText("已推" + ConvertUtil.formatWan(data.getExtendTime()) + "次");
            saleCountTextView.setText("已抢" + ConvertUtil.formatWan(data.getSaleCount()) + "件");

            if (data.isFlashSale()) {
                //抢购店主价格
                vipPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMinScorePrice()));
                rewardPriceTextView.setText("赚" + ConvertUtil.cent2yuanNoZero(data.getMaxBrokeragePrice()));

                //抢购会员价格
                userPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMinScorePrice()));
                marketPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMaxSalePrice()));
            } else {
                //非抢购店主价格
                vipPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMinPrice()));
                rewardPriceTextView.setText("赚" + ConvertUtil.cent2yuanNoZero(data.getMaxRewardPrice()));

                //非抢购会员价格
                userPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMinPrice()));
                marketPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMaxMarketPrice()));
            }


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
            //设置头像数量
            AvatarDemoMaker.setVisibilitys(avatars, (int) data.getSaleCount(), 3);//设置头像数量
        } else {
            DLog.e("" + this.getClass().getSimpleName() + " data is null.");
        }
    }

    public boolean isVip() {
        return SessionUtil.getInstance().isMaster();
    }

}
