package com.xiling.ddui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.utils.StringUtils;
import com.xiling.R;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.bean.XLCardListBean;
import com.xiling.ddui.service.HtmlService;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ProductDetailUIHelper;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.dialog.DDMProductQrCodeDialog;
import com.xiling.shared.component.dialog.SkuSelectorDialog;
import com.xiling.shared.component.dialog.XLProductQrCodeDialog;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICartService;
import com.xiling.shared.service.contract.IProductService;
import com.xiling.shared.util.ToastUtil;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2018/10/31
 * 商品详情
 */
public class DDProductDetailActivity extends BaseActivity implements ProductDetailUIHelper.OnActionListener {

    public static final int ACTION_BUY = 0;
    public static final int ACTION_CART = 1;

    private ProductDetailUIHelper mProductDetailUIHelper;

    private String mSpuId;
    private IProductService mProductService;
    private ICartService mCartService;

    private ProductNewBean mSpuInfo;
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
        super.onDestroy();
        mProductDetailUIHelper.recyclerWebView();
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
        mCartService = ServiceManager.getInstance().createService(ICartService.class);
        getProductInfo(mSpuId);

    }

    private void getIntentData() {
        mSpuId = getIntent().getStringExtra(Key.SPU_ID);
        if (StringUtils.isEmpty(mSpuId)) {
            ToastUtil.error("参数错误");
            finish();
        }
    }

    private void getProductInfo(String spuId) {
        APIManager.startRequest(mProductService.getProductDetail(spuId), new BaseRequestListener<ProductNewBean>(this) {
            @Override
            public void onSuccess(ProductNewBean product) {
                super.onSuccess(product);
                mSpuInfo = product;
                mProductDetailUIHelper.updateSpuViews(mSpuInfo);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

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
        if (UserManager.getInstance().isLogin(context)) {
            showShareDialog();
        }
    }

    @Override
    public void onClickCart() {
        // 去购物车 业务逻辑

    }

    @Override
    public void onAddCart(String skuId,int size) {
        // 加入购物车 业务逻辑
        requestAddCart(skuId,size);
    }

    @Override
    public void onClickBuy() {
        //立即购买 业务逻辑
        com.sobot.chat.utils.ToastUtil.showToast(context, "立即购买");
    }

    /**
     * 添加购物车
     */
    private void requestAddCart(String skuId, int quantity) {
        APIManager.startRequest(mCartService.addShopCart(skuId, quantity), new BaseRequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                ToastUtil.success("添加成功");
            }
        });
    }


    private boolean checkNull(Object o) {
        return null == o;
    }

    @Override
    public void onClickBecomeMaster() {
        WebViewActivity.jumpService(this, HtmlService.BESHOPKEPPER);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage message) {
        switch (message.getEvent()) {
            case cartAmountUpdate:
                if (mSkuSelectorDialog != null) {
                    mSkuSelectorDialog.dismiss();
                }
                String total = (int) message.getData() > 99 ? "99+" : String.valueOf(message.getData());
                break;
            default:
        }
    }

    private void showShareDialog() {
        ToastUtil.hideLoading();
        XLProductQrCodeDialog dialog = new XLProductQrCodeDialog(this, mSpuInfo);
        dialog.show();
    }


}
