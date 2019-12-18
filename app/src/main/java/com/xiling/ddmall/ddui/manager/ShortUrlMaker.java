package com.xiling.ddmall.ddui.manager;

import com.xiling.ddmall.ddui.bean.DDNUrlBean;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUrlService;

public class ShortUrlMaker {

    private static ShortUrlMaker self = null;

    public static ShortUrlMaker share() {
        if (self == null) {
            self = new ShortUrlMaker();
        }
        return self;
    }

    IUrlService iUrl = null;

    private ShortUrlMaker() {
        iUrl = ServiceManager.getInstance().createService(IUrlService.class);
    }

    public interface ShortUrlListener {
        void onUrlCreate(String url);

        void onUrlCreateError(String error);
    }

    /**
     * 生成短链接
     *
     * @param url      长链接地址
     * @param listener 毁掉协议
     */
    public void create(String url, final ShortUrlListener listener) {
        APIManager.startRequest(iUrl.shortUrl(url), new RequestListener<DDNUrlBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDNUrlBean result) {
                super.onSuccess(result);
                if (listener != null) {
                    listener.onUrlCreate(result.getDDShortURL());
                } else {
                    DLog.w("create short url success no listener :" + result.getShortUrl()
                            + ",dd shart URL :" + result.getDDShortURL());
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onUrlCreateError(e.getMessage());
                } else {
                    DLog.w("create short url error no listener :" + e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 还原短链接为长连接
     *
     * @param url
     * @param listener
     */
    public void restore(String url, final ShortUrlListener listener) {
        APIManager.startRequest(iUrl.restore(url), new RequestListener<DDNUrlBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDNUrlBean result) {
                super.onSuccess(result);
                if (listener != null) {
                    listener.onUrlCreate(result.getoUrl());
                } else {
                    DLog.w("create short url success no listener :" + result.getoUrl());
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onUrlCreateError(e.getMessage());
                } else {
                    DLog.w("create short url error no listener :" + e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
