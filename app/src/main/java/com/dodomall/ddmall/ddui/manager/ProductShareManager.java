package com.dodomall.ddmall.ddui.manager;

import android.app.Activity;

import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Product;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.component.dialog.DDMShareDialog;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IProductService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.ShareUtils;
import com.dodomall.ddmall.shared.util.ToastUtil;

public class ProductShareManager {

    Activity context = null;
    IProductService iProductService = null;
    SkuInfo mSkuInfo = null;
    String mSpuId = "";

    /**
     * 商品详情界面传入参数
     */
    public ProductShareManager(Activity context, SkuInfo skuInfo) {
        this.context = context;
        this.mSkuInfo = skuInfo;
    }

    /**
     * 首页H5调用分享赚 传入参数
     */
    public ProductShareManager(Activity context, String mSpuId) {
        this.context = context;
        this.mSpuId = mSpuId;
        iProductService = ServiceManager.getInstance().createService(IProductService.class);
        getProductDetail(mSpuId, null);
    }

    /**
     * 获取商品详情
     *
     * @param spuId 商品SPU ID
     */
    void getProductDetail(String spuId, final DataListener listener) {
        APIManager.startRequest(iProductService.getDetailById(spuId), new BaseRequestListener<Product>() {

            @Override
            public void onStart() {
                super.onStart();
                ToastUtil.showLoading(context);
            }

            @Override
            public void onSuccess(Product data) {
                mSkuInfo = SkuInfo.from(data);
                if (listener != null) {
                    listener.onLoadEnd();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                ToastUtil.hideLoading();
            }
        });
    }

    interface DataListener {
        void onLoadEnd();
    }

    public void show() {
        if (this.mSkuInfo == null) {
            getProductDetail(mSpuId, new DataListener() {
                @Override
                public void onLoadEnd() {
                    showDialog();
                }
            });
        } else {
            showDialog();
        }
    }

    void showDialog() {
        ToastUtil.hideLoading();
        String url = mSkuInfo.getProductUrl();
        DDMShareDialog.Share shareObject = new DDMShareDialog.Share(mSkuInfo.name, mSkuInfo.desc, mSkuInfo.thumb, url);
        //分享赚 - 利润
        shareObject.setCanMakeMoney("" + ConvertUtil.cent2yuanNoZero(mSkuInfo.rewardPrice));
        shareObject.setSkuInfo(mSkuInfo);

        ShareUtils.showDDMShareDialog(context, shareObject);
    }

}
