package com.xiling.ddui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.umeng.socialize.UMShareAPI;
import com.xiling.R;
import com.xiling.ddui.bean.CardExpandableBean;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.bean.RealAuthBean;
import com.xiling.ddui.bean.SkuListBean;
import com.xiling.ddui.bean.XLCardListBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.manager.ShopCardManager;
import com.xiling.ddui.service.HtmlService;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ProductDetailUIHelper;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.MainActivity;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.dialog.DDMProductQrCodeDialog;
import com.xiling.shared.component.dialog.XLProductQrCodeDialog;
import com.xiling.shared.constant.Event;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.contract.ICartService;
import com.xiling.shared.service.contract.IProductService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xiling.ddui.activity.ConfirmationOrderActivity.ORDER_SOURCE;
import static com.xiling.ddui.activity.ConfirmationOrderActivity.SKULIST;
import static com.xiling.shared.constant.Event.cartAmountUpdate;
import static com.xiling.shared.constant.Event.viewCart;

/**
 * @auth 逄涛
 * 商品详情
 */
public class DDProductDetailActivity extends BaseActivity implements ProductDetailUIHelper.OnActionListener {

    @BindView(R.id.tv_cart_badge)
    TextView tvCartBadge;
    private ProductDetailUIHelper mProductDetailUIHelper;
    private String mSpuId;
    private IProductService mProductService;
    private INewUserService iNewUserService;

    private ProductNewBean mSpuInfo;

    private boolean isDoingLike = false;

    public static void start(Context context, String spuId) {
        context.startActivity(new Intent(context, DDProductDetailActivity.class).putExtra(Key.SPU_ID, spuId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dd_product_detail);
        ButterKnife.bind(this);
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
        getProductInfo(mSpuId);
        ShopCardManager.getInstance().requestUpDataShopCardCount();
    }

    private void getIntentData() {
        mSpuId = getIntent().getStringExtra(Key.SPU_ID);
        if (StringUtils.isEmpty(mSpuId)) {
            ToastUtil.error("参数错误");
            finish();
        }
    }

    private void getProductInfo(final String spuId) {
        if (UserManager.getInstance().isLogin()) {
            UserManager.getInstance().checkUserInfo(new UserManager.OnCheckUserInfoLisense() {
                @Override
                public void onCheckUserInfoSucess(final NewUserBean newUserBean) {
                    APIManager.startRequest(mProductService.getProductDetail(spuId), new BaseRequestListener<ProductNewBean>(context) {
                        @Override
                        public void onSuccess(ProductNewBean product) {
                            super.onSuccess(product);
                            mSpuInfo = product;
                            mProductDetailUIHelper.updateSpuViews(mSpuInfo, newUserBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);

                        }
                    });
                }

                @Override
                public void onCheckUserInfoFail() {

                }
            });
        } else {
            APIManager.startRequest(mProductService.getProductDetail(spuId), new BaseRequestListener<ProductNewBean>(context) {
                @Override
                public void onSuccess(ProductNewBean product) {
                    super.onSuccess(product);
                    mSpuInfo = product;
                    mProductDetailUIHelper.updateSpuViews(mSpuInfo, null);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);

                }
            });

        }


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
        if (UserManager.getInstance().isLogin(context)) {
            startActivity(new Intent(this, MainActivity.class));
            EventBus.getDefault().post(new EventMessage(viewCart));
        }
    }

    @Override
    public void onAddCart(String skuId, int size) {
        // 加入购物车 业务逻辑
        if (UserManager.getInstance().isLogin(context)) {
            ShopCardManager.getInstance().requestAddCart(context, skuId, size, false, true);
        }
    }

    @Override
    public void onClickBuy(SkuListBean skuListBean) {
        //立即购买 业务逻辑
        if (UserManager.getInstance().isLogin(context)) {
            ArrayList<SkuListBean> skuList = new ArrayList<>();
            skuList.add(skuListBean);
            Intent intent = new Intent(context, ConfirmationOrderActivity.class);
            intent.putExtra(SKULIST, skuList);
            intent.putExtra(ORDER_SOURCE, 1);
            startActivity(intent);
        }
    }

    private boolean checkNull(Object o) {
        return null == o;
    }

    // 立即升级
    @Override
    public void onClickBecomeMaster() {
        if (UserManager.getInstance().isLogin(context)) {
            UserManager.getInstance().isRealAuth(context, new UserManager.RealAuthListener() {
                @Override
                public void onRealAuth() {
                    //跳转会员中心
                    startActivity(new Intent(context, XLMemberCenterActivity.class));
                }
            });
        }
    }

    
    private void showShareDialog() {
        if (UserManager.getInstance().isLogin(context)) {
            ToastUtil.hideLoading();
            XLProductQrCodeDialog dialog = new XLProductQrCodeDialog(this, mSpuInfo);
            dialog.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void imageUploadHandler(EventMessage message) {
        switch (message.getEvent()) {
            case cartAmountUpdate:
                int total = (int) message.getData();
                ViewUtil.setCartBadge(total, tvCartBadge);
                break;
            case LOGIN_SUCCESS:
            case LOGIN_OUT:
                ShopCardManager.getInstance().requestUpDataShopCardCount();
                getProductInfo(mSpuId);
                break;

        }

    }
}
