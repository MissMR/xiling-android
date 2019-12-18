package com.xiling.ddmall.ddui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.utils.StringUtils;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.UIEvent;
import com.xiling.ddmall.ddui.custom.DDDeleteDialog;
import com.xiling.ddmall.ddui.manager.CSManager;
import com.xiling.ddmall.ddui.manager.CartAmountManager;
import com.xiling.ddmall.ddui.service.HtmlService;
import com.xiling.ddmall.ddui.tools.AppTools;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.ddui.tools.ProductDetailUIHelper;
import com.xiling.ddmall.dduis.activity.RushListActivity;
import com.xiling.ddmall.module.address.AddressListActivity;
import com.xiling.ddmall.module.cart.CartActivity;
import com.xiling.ddmall.module.page.WebViewActivity;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.Product;
import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.bean.SkuPvIds;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.bean.api.PaginationEntity;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.component.dialog.DDMProductQrCodeDialog;
import com.xiling.ddmall.shared.component.dialog.ProductVerifyDialog;
import com.xiling.ddmall.shared.component.dialog.SkuSelectorDialog;
import com.xiling.ddmall.shared.constant.Action;
import com.xiling.ddmall.shared.constant.AppTypes;
import com.xiling.ddmall.shared.constant.Key;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.CartManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.ProductService;
import com.xiling.ddmall.shared.service.contract.ICollectService;
import com.xiling.ddmall.shared.service.contract.IProductService;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.xiling.ddmall.shared.util.UiUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2018/10/31
 * 商品详情
 */
public class DDProductDetailActivity extends BaseActivity implements ProductDetailUIHelper.OnActionListener, SkuSelectorDialog.OnSelectListener {

    public static final int ACTION_BUY = 0;
    public static final int ACTION_CART = 1;

    private ProductDetailUIHelper mProductDetailUIHelper;

    private String mSpuId;
    private IProductService mProductService;
    private ICollectService mCollectService;

    private SkuInfo mSkuInfo;
    private Product mSpuInfo;
    private SkuSelectorDialog mSkuSelectorDialog;

    private boolean isDoingLike = false;

    public static void start(Context context, String spuId) {
        context.startActivity(new Intent(context, DDProductDetailActivity.class).putExtra(Key.SPU_ID, spuId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dd_product_detail);
        ButterKnife.bind(this);
        mProductDetailUIHelper = new ProductDetailUIHelper(this);
        mProductDetailUIHelper.setOnActionListener(this);

        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSpuInfo != null) {
            ToastUtil.hideLoading();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isDoingLike) {
            isDoingLike = false;
            ToastUtil.error("添加失败");
        }
    }

    @Override
    protected void onDestroy() {
        mProductDetailUIHelper.recyclerWebView();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }

    private void initData() {
        getIntentData();

        mProductService = ServiceManager.getInstance().createService(IProductService.class);
        mCollectService = ServiceManager.getInstance().createService(ICollectService.class);

        getProductInfo(mSpuId);

        if (SessionUtil.getInstance().isLogin()) {
            CartManager.getAmount();
            mProductDetailUIHelper.showBecomeMasterGuide(!SessionUtil.getInstance().getLoginUser().isStoreMaster());
        } else {
            mProductDetailUIHelper.showBecomeMasterGuide(true);
        }

        mProductDetailUIHelper.updateCartBadge(String.valueOf(CartAmountManager.share().getAmount()));

    }

    private void getIntentData() {
        mSpuId = getIntent().getStringExtra(Key.SPU_ID);
        if (StringUtils.isEmpty(mSpuId)) {
            ToastUtil.error("参数错误");
            finish();
        }
    }

    /**
     * 添加足迹的浏览记录
     */
    private void addProductViewRecord() {
        User user = SessionUtil.getInstance().getLoginUser();
        if (user != null) {
            ProductService.addViewRecord(user.id, mSpuId);
        }
    }

    private void getProductInfo(String spuId) {
        APIManager.startRequest(mProductService.getDetailById(spuId), new BaseRequestListener<Product>(this) {
            @Override
            public void onSuccess(Product product) {
                super.onSuccess(product);
                mSpuInfo = product;
                mProductDetailUIHelper.updateSpuViews(mSpuInfo);
                checkAddProductRecord();
            }
        });
    }

    private void checkAddProductRecord() {
        if (SessionUtil.getInstance().isLogin()) {
            addProductViewRecord();
        }
    }

    private void updateSkuLikeState(final boolean isActionLike) {

        if (checkNull(mSpuInfo)) {
            return;
        }
        String url = isActionLike ? ICollectService.URL_COLLECT : ICollectService.URL_UNCOLLECT;
        APIManager.startRequest(mCollectService.changeCollect(url, mSpuInfo.productId), new BaseRequestListener<PaginationEntity<SkuInfo, Object>>(this) {
            @Override
            public void onSuccess(PaginationEntity<SkuInfo, Object> result) {
                mProductDetailUIHelper.updateSpuLikeState(isActionLike);
                UIEvent event = new UIEvent();
                event.setType(UIEvent.Type.UnLikeProduct);
                EventBus.getDefault().post(event);

                if (isActionLike) {
                    ToastUtil.success("喜欢成功，您可以在“我的喜欢”中查看");
                }

            }
        });

    }

    @Override
    public void onClickFinish() {
        finish();
    }

    @Override
    public void onClickShare() {
        if (checkNull(mSpuInfo)) {
            return;
        }
        if (UiUtils.checkUserLogin(this)) {
            showShareDialog();
        }
    }

    @Override
    public void onClickProductLike(boolean isLikeProduct) {
        if (UiUtils.checkUserLogin(this)) {
            updateSkuLikeState(isLikeProduct);
        } else {
            isDoingLike = true;
        }
    }


    @Override
    public void onSelectAddress() {
        if (UiUtils.checkUserLogin(this)) {
            Intent intent = new Intent(this, AddressListActivity.class);
            intent.putExtra("action", Key.SELECT_ADDRESS);
            startActivityForResult(intent, Action.SELECT_ADDRESS);
        }
    }

    @Override
    public void onClickAuthInfo() {
        if (mSpuInfo != null && mSpuInfo.auths != null && !isFinishing() && !isFinished()) {
            new ProductVerifyDialog(this, mSpuInfo.auths).show();
        }
    }

    @Override
    public void onClickCart() {
        // 去购物车
        if (UiUtils.checkUserLogin(this)) {
            Intent intent = new Intent(this, CartActivity.class);
            intent.putExtra("tab", "cart");
            intent.putExtra("from", "mSpuInfo");
            startActivity(intent);
        }
    }

    @Override
    public void onAddCart() {
        // 加入购物车
        if (UiUtils.checkUserLogin(this)) {
            showSelectorDialog(ACTION_CART);
        }
    }

    @Override
    public void onSelectSkuInfo() {
        showSelectorDialog(ACTION_BUY);
    }

    @Override
    public void onClickBuy() {

        if (checkNull(mSpuInfo)) {
            return;
        }

        if (UiUtils.checkUserLogin(this)) {
            showSelectorDialog(ACTION_BUY);
        }
    }

    private boolean checkNull(Object o) {
        return null == o;
    }

    @Override
    public void onClickCustomService() {
        if (checkNull(mSpuInfo)) {
            return;
        }
        if (UiUtils.checkUserLogin(this)) {
            CSManager.share().jumpToChat(context, mSpuId);
        }
    }

    @Override
    public void onClickMaterialAll() {
        if (checkNull(mSpuInfo)) {
            return;
        }
        startActivity(new Intent(this, ProductMaterialActivity.class)
                .putExtra(Constants.Extras.SPU_ID, mSpuId));
    }

    @Override
    public void onClickBecomeMaster() {
        WebViewActivity.jumpService(this, HtmlService.BESHOPKEPPER);
    }

    @Override
    public void onClickMeasurement() {
        if (checkNull(mSpuInfo)) {
            return;
        }
        String engineerId = mSpuInfo.productEvaluate.getEngineerId();

        MeasurementActivity.start(this, engineerId, mSpuInfo.productEvaluate.getNikeName());

    }

    @Override
    public void onClickMeasurementDetail() {
        startActivity(new Intent(this, MeasurementDetailActivity.class)
                .putExtra(Constants.Extras.SPU_ID, mSpuId));
    }

    @Override
    public void onClickNotify() {
        if (checkNull(mSpuInfo)) {
            return;
        }
        if (UiUtils.checkUserLogin(this)) {
            if (AppTools.isEnableNotification(context)) {
                //开启推送后直接显示一个间隔
                notifyFlashSale();
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
    }

    @Override
    public void onClickFlashSale() {
        if (checkNull(mSpuInfo)) {
            return;
        }
        if (mSpuInfo.isFlashSaleActive()) {
            RushListActivity.jumpToFullList(this, mSpuInfo.flashSaleDetail.getFlashSaleId());
        }
    }

    private void notifyFlashSale() {
        APIManager.startRequest(mProductService.noticeFlashSale(mSpuInfo.flashSaleDetail.getFlashSpuId(),
                mSpuInfo.flashSaleDetail.getFlashSaleId()),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result, String message) {
                        super.onSuccess(result, message);
                        ToastUtil.success(message);
                        mProductDetailUIHelper.toggleNotifyStyle();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage message) {
        switch (message.getEvent()) {
            case cartAmountUpdate:
                if (mSkuSelectorDialog != null) {
                    mSkuSelectorDialog.dismiss();
                }
                String total = (int) message.getData() > 99 ? "99+" : String.valueOf(message.getData());
                mProductDetailUIHelper.updateCartBadge(total);
                break;
            default:
        }
    }

    public void showSelectorDialog(int action) {
        if (mSpuInfo == null) {
            return;
        }
        if (action < 0 || action > 1) {
            return;
        }

        int tag;
        if (action == ACTION_CART) {
            tag = AppTypes.SKU_SELECTOR_DIALOG.ACTION_CART;
        } else if (action == ACTION_BUY) {
            if (mSpuInfo.isProductFree()) {
                // 0元购
                tag = AppTypes.SKU_SELECTOR_DIALOG.ACTION_ACTIVITY_FREE;
            } else if (mSpuInfo.isStoreGift()) {
                // 店主礼包
                tag = AppTypes.SKU_SELECTOR_DIALOG.ACTION_SINGLE;
            } else if (SessionUtil.getInstance().isMaster()) {
                // 是店主两个按钮
                tag = AppTypes.SKU_SELECTOR_DIALOG.ACTION_DEFAULT;
            } else {
                // 普通会员 立即购买
                tag = AppTypes.SKU_SELECTOR_DIALOG.ACTION_BUY;
            }
        } else {
            return;
        }
        mSkuSelectorDialog = new SkuSelectorDialog(this, mSpuInfo, tag);
        mSkuSelectorDialog.setSelectListener(this);
        mSkuSelectorDialog.show();
    }

    @Override
    public void onSelectSku(SkuPvIds skuInfo) {
        mSpuInfo.setSelectedSkuPvIds(skuInfo);
        mProductDetailUIHelper.updateSkuViews(mSpuInfo.getSelectedSkuPvIds());
    }

    @Override
    public void onSelectCancel() {
        mSpuInfo.setSelectedSkuPvIds(null);
        mProductDetailUIHelper.updateSkuViews(null);
    }

    private void showShareDialog() {
        ToastUtil.hideLoading();
        DDMProductQrCodeDialog dialog = new DDMProductQrCodeDialog((Activity) context, mSpuInfo, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                DLog.i("onStart");
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                DLog.i("onResult");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                DLog.i("onError");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                DLog.i("onCancel");
            }
        });
        dialog.show();
    }


}
