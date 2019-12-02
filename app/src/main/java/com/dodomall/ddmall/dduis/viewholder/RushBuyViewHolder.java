package com.dodomall.ddmall.dduis.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.DDProductDetailActivity;
import com.dodomall.ddmall.ddui.config.UIConfig;
import com.dodomall.ddmall.ddui.custom.DDDeleteDialog;
import com.dodomall.ddmall.ddui.tools.AppTools;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.UITools;
import com.dodomall.ddmall.dduis.base.AvatarDemoMaker;
import com.dodomall.ddmall.dduis.base.BackgroundMaker;
import com.dodomall.ddmall.dduis.bean.DDProductBean;
import com.dodomall.ddmall.dduis.bean.DDRushSpuBean;
import com.dodomall.ddmall.dduis.custom.SaleProgressView;
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


public class RushBuyViewHolder extends RecyclerView.ViewHolder {

    private enum RushDataType {
        /**
         * 陈健写的抢购接口实体:DDRushSpuBean
         */
        CODER_CHEN_JIAN,
        /**
         * 张哲写的商品接口实体:DDProductBean
         */
        CODER_ZHANG_ZHE
    }

    public interface ReloadListener {
        void onReload(int position);
    }

    int status = 0;
    ReloadListener reloadListener = null;
    Context context = null;

    RushDataType apiType = RushDataType.CODER_CHEN_JIAN;
    DDRushSpuBean rushData = null;
    DDProductBean spuData = null;

    DDHomeService ddHomeService = null;

    public RushBuyViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        //添加删除线
        UITools.addTextViewDeleteLine(marketPriceTextView);

        ddHomeService = ServiceManager.getInstance().createService(DDHomeService.class);
    }

    /**
     * 使用抢购实体渲染界面
     */
    public void setData(DDRushSpuBean data) {
        this.rushData = data;
        this.apiType = RushDataType.CODER_CHEN_JIAN;
    }

    /**
     * 使用普通spu实体渲染界面
     */
    public void setData(DDProductBean data) {
        this.spuData = data;
        this.apiType = RushDataType.CODER_ZHANG_ZHE;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 是否已开始抢购
     */
    boolean isRushEnable() {
        //不管是抢购中还是已结束都认为是可以抢购
        if (apiType == RushDataType.CODER_ZHANG_ZHE) {
            return spuData.isRushEnable();
        } else {
            return status > 1;
        }
    }

    public void setReloadListener(ReloadListener reloadListener) {
        this.reloadListener = reloadListener;
    }

    @BindView(R.id.tv_rush_tag)
    TextView rushTagTextView;

    @BindView(R.id.sd_thumb)
    SimpleDraweeView sdThumb;

    @BindView(R.id.tv_spu)
    TextView nameTextView;

    @BindView(R.id.pb_progress)
    SaleProgressView progressBar;

    @BindView(R.id.layout_vip_price)
    RelativeLayout vipPriceLayout;

    @BindView(R.id.tv_vip_price)
    TextView vipPriceTextView;
    @BindView(R.id.tv_price_reward)
    TextView rewardPriceTextView;

    @BindView(R.id.layout_user_price)
    RelativeLayout userPriceLayout;

    @BindView(R.id.tv_price)
    TextView priceTextView;
    @BindView(R.id.tv_price_market)
    TextView marketPriceTextView;

    @BindView(R.id.tv_count)
    TextView countTextView;

    @BindView(R.id.btn_rush_buy)
    Button rushBuyButton;

    @BindView(R.id.btn_rush_notice)
    Button noticeButton;

    @BindView(R.id.layout_avatar)
    LinearLayout avatarLayout;

    @BindViews({R.id.sd_avatar_01, R.id.sd_avatar_02, R.id.sd_avatar_03})
    SimpleDraweeView[] avatars;

    @BindView(R.id.spuStatusView)
    TextView spuStatusView;

    @OnClick(R.id.layout_row)
    void onProductPressed() {
        if (apiType == RushDataType.CODER_ZHANG_ZHE) {
            if (spuData != null) {
                DDProductDetailActivity.start(context, spuData.getProductId());
            } else {
                ToastUtil.toastDataError();
            }
        } else {
            if (rushData != null) {
                DDProductDetailActivity.start(context, rushData.getSpuId());
            } else {
                ToastUtil.toastDataError();
            }
        }
    }

    @OnClick(R.id.btn_rush_buy)
    void onBuyPressed() {
        boolean isMaster = SessionUtil.getInstance().isMaster();
        //开抢以后
        if (isRushEnable()) {
            //店主是推广
            if (isMaster) {
                shareProduct();
            } else {
                //会员是抢购
                onProductPressed();
            }
        } else {
            //否则都是推广
            shareProduct();
        }
    }

    void shareProduct() {
        if (!SessionUtil.getInstance().isLogin()) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }

        UMShareListener shareListener = new UMShareListener() {
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
        };

        if (apiType == RushDataType.CODER_ZHANG_ZHE) {
            DDMProductQrCodeDialog dialog = new DDMProductQrCodeDialog((Activity) context, spuData, shareListener);
            dialog.show();
        } else {
            DDMProductQrCodeDialog dialog = new DDMProductQrCodeDialog((Activity) context, rushData, shareListener);
            dialog.show();
        }
    }


    void addShareCount() {

        String spuId = "";
        if (apiType == RushDataType.CODER_ZHANG_ZHE) {
            spuId = spuData.getProductId();
        } else {
            spuId = rushData.getSpuId();
        }

        DDHomeService homeService = ServiceManager.getInstance().createService(DDHomeService.class);
        APIManager.startRequest(homeService.addShareCount(spuId), new RequestListener<Object>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object result) {

                super.onSuccess(result);
                if (apiType == RushDataType.CODER_ZHANG_ZHE) {
                    spuData.addExtendTime();
                } else {
                    rushData.addExtendTime();
                }

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

    @OnClick(R.id.btn_rush_notice)
    void onRushNoticePressed() {
        if (!SessionUtil.getInstance().isLogin()) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }

        if (AppTools.isEnableNotification(context)) {
            //开启推送后直接显示一个间隔
            targetNotice();
        } else {
            //未开启推送提示用户开启
            DDDeleteDialog dialog = new DDDeleteDialog(context);
            dialog.setContent("打开推送通知才能设置提醒哦");
            dialog.setButtonName("去开启", "取消");
            dialog.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppTools.jumpToAppSettings(context);
                }
            });
            dialog.setOnNegativeClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.success("提醒开启失败，您将无法收到商品开抢提醒");
                }
            });
            dialog.show();
        }
    }

    void targetNotice() {

        ToastUtil.showLoading(context);

        String flashSaleSpuId = "";
        String flashSaleId = "";
        if (apiType == RushDataType.CODER_ZHANG_ZHE) {
            //活动商品SPU ID 不是productId! 不是productId! 不是productId!
            flashSaleSpuId = spuData.getFlashSpuId();
            //场次ID
            flashSaleId = spuData.getFstId();
        } else {
            flashSaleSpuId = rushData.getFlashSaleSpuId();
            flashSaleId = rushData.getFlashSaleId();
        }

        APIManager.startRequest(ddHomeService.targetNotice(flashSaleSpuId, flashSaleId), new RequestListener<Object>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(Object result, String msg) {
                super.onSuccess(result);

                if (apiType == RushDataType.CODER_ZHANG_ZHE) {
                    //处理本地数据
                    if (spuData.isNotice()) {
                        DLog.i("取消提醒");
                        spuData.setRemind(0);
                        spuData.setFocusTime(spuData.getFocusTime() - 1);
                    } else {
                        DLog.i("开启提醒");
                        spuData.setRemind(1);
                        spuData.setFocusTime(spuData.getFocusTime() + 1);
                    }
                } else {
                    //处理本地数据
                    if (rushData.isNotice()) {
                        DLog.i("取消提醒");
                        rushData.setFocus(0);
                        rushData.setFocusTime(rushData.getFocusTime() - 1);
                    } else {
                        DLog.i("开启提醒");
                        rushData.setFocus(1);
                        rushData.setFocusTime(rushData.getFocusTime() + 1);
                    }

                }

                ToastUtil.success(msg);
                requestReloadData();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error(e.getMessage());
            }

            @Override
            public void onComplete() {
                ToastUtil.hideLoading();
            }
        });

    }

    public void requestReloadData() {
        if (reloadListener != null) {
            reloadListener.onReload(this.getAdapterPosition());
        }
    }

    public void render() {
        if (apiType == RushDataType.CODER_ZHANG_ZHE) {
            renderSpuData();
        } else {
            renderRushData();
        }
    }

    private void renderRushData() {

        if (rushData == null) return;

        nameTextView.setText("" + rushData.getProductName());
        sdThumb.setImageURI(rushData.getThumbUrlForShopNow());

        if (apiType == RushDataType.CODER_ZHANG_ZHE) {
            rushTagTextView.setVisibility(View.VISIBLE);
        } else {
            rushTagTextView.setVisibility(View.INVISIBLE);
        }

        if (rushData.getInventory() > 0) {
            spuStatusView.setVisibility(View.INVISIBLE);
        } else {
            spuStatusView.setVisibility(View.VISIBLE);
        }

        long saleCount = rushData.getSaleCount();
        long totalCount = rushData.getInventory() + saleCount;
//        progressBar.setTotalAndCurrentCount((int) totalCount, (int) saleCount);
        //使用服务器下发的进度数据
        float progress = rushData.getFlashInventoryRate();
        progressBar.setProgress((int) totalCount, (int) saleCount, progress);

        if (isRushEnable()) {
            noticeButton.setVisibility(View.GONE);
            rushBuyButton.setVisibility(View.VISIBLE);

            progressBar.setStyle(UIConfig.COLOR_RUSH_PROGRESS_WRITE_TEXT, R.mipmap.bg_home_rush_progress, R.mipmap.bg_rush_progress, true);
            Drawable drawable = new BackgroundMaker(context).getRadiusDrawable(UIConfig.COLOR_RUSH_VIP, 16);
            rushBuyButton.setBackground(drawable);

            boolean isMaster = SessionUtil.getInstance().isMaster();
            if (isMaster) {

                rushBuyButton.setText("推广赚");
                avatarLayout.setVisibility(View.GONE);

                //店主
                userPriceLayout.setVisibility(View.GONE);
                vipPriceLayout.setVisibility(View.VISIBLE);

                //设置推广人次
                String saleText = "已推" + ConvertUtil.formatWan(rushData.getExtendTime()) + "次";
                countTextView.setText(saleText);

                vipPriceTextView.setTextColor(Color.BLACK);
                vipPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(rushData.getMinScorePrice()));

                rewardPriceTextView.setTextColor(UIConfig.COLOR_RUSH_VIP);
                rewardPriceTextView.setText("赚" + ConvertUtil.cent2yuanNoZero(rushData.getMaxBrokeragePrice()));

            } else {
                //非店主
                avatarLayout.setVisibility(View.VISIBLE);

                rushBuyButton.setText("去抢购");

                //设置购买人次
                String saleText = "已抢" + ConvertUtil.formatWan(saleCount) + "件";
                countTextView.setText(saleText);

                //非店主
                userPriceLayout.setVisibility(View.VISIBLE);
                vipPriceLayout.setVisibility(View.GONE);

                priceTextView.setTextColor(UIConfig.COLOR_RUSH_VIP);
                priceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(rushData.getMinScorePrice()));

                marketPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(rushData.getMaxSalePrice()));

                //设置随机头像
                AvatarDemoMaker.setDemoAvatar(avatars);
                //设置头像数量
                AvatarDemoMaker.setVisibilitys(avatars, (int) rushData.getSaleCount(), 3);
            }

        } else {

            noticeButton.setVisibility(View.VISIBLE);

            progressBar.setStyle(UIConfig.COLOR_RUSH_PROGRESS_GARY_TEXT, R.mipmap.bg_rush_progress_gray, R.mipmap.bg_rush_progress_gray, false);
            Drawable drawable = new BackgroundMaker(context).getRadiusDrawable(UIConfig.COLOR_RUSH_USER, 16);
            rushBuyButton.setBackground(drawable);
            rushBuyButton.setText("推广赚");

            if (rushData.isNotice()) {
                noticeButton.setBackgroundResource(R.mipmap.bg_btn_rush_notice_cancel);
            } else {
                noticeButton.setBackgroundResource(R.mipmap.bg_btn_rush_notice_submit);
            }

            boolean isMaster = SessionUtil.getInstance().isMaster();
            //判断是否是店主
            if (isMaster) {

                //店主显示推广按钮
                rushBuyButton.setVisibility(View.VISIBLE);
                avatarLayout.setVisibility(View.GONE);

                //设置推广人次
                String saleText = "已推" + ConvertUtil.formatWan(rushData.getExtendTime()) + "次";
                countTextView.setText(saleText);

                //店主
                userPriceLayout.setVisibility(View.GONE);
                vipPriceLayout.setVisibility(View.VISIBLE);

                vipPriceTextView.setTextColor(Color.BLACK);
                vipPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(rushData.getMinScorePrice()));

                rewardPriceTextView.setTextColor(UIConfig.COLOR_RUSH_USER);
                rewardPriceTextView.setText("赚" + ConvertUtil.cent2yuanNoZero(rushData.getMaxBrokeragePrice()));

            } else {

                //非店主不显示推广按钮
                rushBuyButton.setVisibility(View.GONE);
                avatarLayout.setVisibility(View.VISIBLE);

                //设置关注人次
                String saleText = "" + ConvertUtil.formatWan(rushData.getFocusTime()) + "人已关注";
                countTextView.setText(saleText);

                //非店主
                userPriceLayout.setVisibility(View.VISIBLE);
                vipPriceLayout.setVisibility(View.GONE);

                priceTextView.setTextColor(UIConfig.COLOR_RUSH_USER);
                priceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(rushData.getMinScorePrice()));

                marketPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(rushData.getMaxSalePrice()));

                //设置随机头像
                AvatarDemoMaker.setDemoAvatar(avatars);
                //设置头像数量
                AvatarDemoMaker.setVisibilitys(avatars, (int) rushData.getFocusTime(), 3);
            }

        }


    }

    private void renderSpuData() {

        if (spuData == null) return;

        nameTextView.setText("" + spuData.getProductName());
        sdThumb.setImageURI(spuData.getThumbUrlForShopNow());

        if (spuData.getFlashInventory() > 0) {
            spuStatusView.setVisibility(View.INVISIBLE);
        } else {
            spuStatusView.setVisibility(View.VISIBLE);
        }


        if (apiType == RushDataType.CODER_ZHANG_ZHE) {
            rushTagTextView.setVisibility(View.VISIBLE);
        } else {
            rushTagTextView.setVisibility(View.INVISIBLE);
        }

        long saleCount = spuData.getFlashSaleCount();
        long totalCount = spuData.getFlashInventory() + saleCount;
//        progressBar.setTotalAndCurrentCount((int) totalCount, (int) saleCount);
        //使用服务器下发的进度数据
        float progress = spuData.getFlashInventoryRate();
        progressBar.setProgress((int) totalCount, (int) saleCount, progress);

        if (isRushEnable()) {
            noticeButton.setVisibility(View.GONE);
            rushBuyButton.setVisibility(View.VISIBLE);

            progressBar.setStyle(UIConfig.COLOR_RUSH_PROGRESS_WRITE_TEXT, R.mipmap.bg_home_rush_progress, R.mipmap.bg_rush_progress, true);
            Drawable drawable = new BackgroundMaker(context).getRadiusDrawable(UIConfig.COLOR_RUSH_VIP, 16);
            rushBuyButton.setBackground(drawable);

            boolean isMaster = SessionUtil.getInstance().isMaster();
            if (isMaster) {

                rushBuyButton.setText("推广赚");
                avatarLayout.setVisibility(View.GONE);

                //店主
                userPriceLayout.setVisibility(View.GONE);
                vipPriceLayout.setVisibility(View.VISIBLE);

                //设置推广人次
                String saleText = "已推" + ConvertUtil.formatWan(spuData.getExtendTime()) + "次";
                countTextView.setText(saleText);

                vipPriceTextView.setTextColor(Color.BLACK);
                vipPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(spuData.getMinScorePrice()));

                rewardPriceTextView.setTextColor(UIConfig.COLOR_RUSH_VIP);
                rewardPriceTextView.setText("赚" + ConvertUtil.cent2yuanNoZero(spuData.getMaxBrokeragePrice()));

            } else {
                //非店主
                avatarLayout.setVisibility(View.VISIBLE);

                rushBuyButton.setText("去抢购");

                //设置购买人次
                String saleText = "已抢" + ConvertUtil.formatWan(saleCount) + "件";
                countTextView.setText(saleText);

                //非店主
                userPriceLayout.setVisibility(View.VISIBLE);
                vipPriceLayout.setVisibility(View.GONE);

                priceTextView.setTextColor(UIConfig.COLOR_RUSH_VIP);
                priceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(spuData.getMinScorePrice()));

                marketPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(spuData.getMaxSalePrice()));
            }

        } else {

            noticeButton.setVisibility(View.VISIBLE);
            avatarLayout.setVisibility(View.GONE);

            progressBar.setStyle(UIConfig.COLOR_RUSH_PROGRESS_GARY_TEXT, R.mipmap.bg_rush_progress_gray, R.mipmap.bg_rush_progress_gray, false);
            Drawable drawable = new BackgroundMaker(context).getRadiusDrawable(UIConfig.COLOR_RUSH_USER, 16);
            rushBuyButton.setBackground(drawable);
            rushBuyButton.setText("推广赚");

            if (spuData.isNotice()) {
                noticeButton.setBackgroundResource(R.mipmap.bg_btn_rush_notice_cancel);
            } else {
                noticeButton.setBackgroundResource(R.mipmap.bg_btn_rush_notice_submit);
            }

            boolean isMaster = SessionUtil.getInstance().isMaster();
            //判断是否是店主
            if (isMaster) {

                //店主显示推广按钮
                rushBuyButton.setVisibility(View.VISIBLE);

                //设置推广人次
                String saleText = "已推" + ConvertUtil.formatWan(spuData.getExtendTime()) + "次";
                countTextView.setText(saleText);

                //店主
                userPriceLayout.setVisibility(View.GONE);
                vipPriceLayout.setVisibility(View.VISIBLE);

                vipPriceTextView.setTextColor(Color.BLACK);
                vipPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(spuData.getMinScorePrice()));

                rewardPriceTextView.setTextColor(UIConfig.COLOR_RUSH_USER);
                rewardPriceTextView.setText("赚" + ConvertUtil.cent2yuanNoZero(spuData.getMaxBrokeragePrice()));

            } else {

                //非店主不显示推广按钮
                rushBuyButton.setVisibility(View.GONE);

                //设置关注人次
                String saleText = "" + ConvertUtil.formatWan(spuData.getFocusTime()) + "人已关注";
                countTextView.setText(saleText);

                //非店主
                userPriceLayout.setVisibility(View.VISIBLE);
                vipPriceLayout.setVisibility(View.GONE);

                priceTextView.setTextColor(UIConfig.COLOR_RUSH_USER);
                priceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(spuData.getMinScorePrice()));

                marketPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(spuData.getMaxSalePrice()));
            }

        }

        //设置随机头像
        AvatarDemoMaker.setDemoAvatar(avatars);
        //设置头像数量
        AvatarDemoMaker.setVisibilitys(avatars, (int) spuData.getSaleCount(), 3);
    }
}
