package com.dodomall.ddmall.shared.service;

import android.support.annotation.NonNull;

import com.blankj.utilcode.utils.LogUtils;
import com.google.common.base.Joiner;
import com.dodomall.ddmall.shared.basic.BaseCallback;
import com.dodomall.ddmall.shared.bean.InstantData;
import com.dodomall.ddmall.shared.bean.Product;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IProductService;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ProductService {

    public static void getListBySkuIds(ArrayList<String> skuIds, final BaseCallback<ArrayList<SkuInfo>> callback) {
        IProductService iProductService = ServiceManager.getInstance().createService(IProductService.class);
        APIManager.startRequest(iProductService.getListBySkuIds(Joiner.on(",").join(skuIds)), new Observer<RequestResult<ArrayList<SkuInfo>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RequestResult<ArrayList<SkuInfo>> value) {
                if (value.code == 0) {
                    callback.callback(value.data);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static void getInstantComponentData(final BaseCallback<InstantData> callback) {
        IProductService service = ServiceManager.getInstance().createService(IProductService.class);
        APIManager.startRequest(service.getInstantComponentData(), new Observer<RequestResult<InstantData>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RequestResult<InstantData> value) {
                if (value.code == 0) {
                    callback.callback(value.data);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static void getSkuInfoById(String skuId, final BaseCallback<SkuInfo> callback) {
        IProductService service = ServiceManager.getInstance().createService(IProductService.class);

        APIManager.startRequest(service.getSkuById(skuId), new Observer<RequestResult<SkuInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RequestResult<SkuInfo> value) {
                if (value.code == 0) {
                    callback.callback(value.data);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static void getProductInfoById(String productId, final BaseCallback<Product> callback) {
        IProductService service = ServiceManager.getInstance().createService(IProductService.class);
        APIManager.startRequest(service.getDetailById(productId), new Observer<RequestResult<Product>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull RequestResult<Product> productRequestResult) {
                if (productRequestResult.code == 0) {
                    callback.callback(productRequestResult.data);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 添加浏览记录
     */
    public static void addViewRecord(String memberId, String skuId) {
        IProductService service = ServiceManager.getInstance().createService(IProductService.class);
        APIManager.startRequest(service.addViewRecord(memberId, skuId), new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Object o) {
                LogUtils.e("添加浏览记成功!!!!!!!!!!!!!!!!!!!");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtils.e("添加浏览记录失败!!!!!!!!!!!!!!!!!!!");
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
