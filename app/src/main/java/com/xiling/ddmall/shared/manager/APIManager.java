package com.xiling.ddmall.shared.manager;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.xiling.ddmall.MyApplication;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.util.SessionUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * APIManager
 * Created by JayChan on 2016/12/13.
 */
public class APIManager {

    public static <T> void startRequest(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public static <T> void startRequest(final Observable<RequestResult<T>> observable, final RequestListener<T> listener) {
        if (observable == null) {
            return;
        }
        listener.onStart();
        startRequest(observable, new Observer<RequestResult<T>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RequestResult<T> result) {
                String message = result.message;
                if (result.isSuccess()) {
                    listener.onSuccess(result.data);
                    listener.onSuccess(result.data, result.message);
                } else if (result.isNotLogin()) {
                    if (!MyApplication.isShowNoLogin) {
                        MyApplication.isShowNoLogin = true;
                        listener.onError(new Exception(Config.NET_MESSAGE.NO_LOGIN));
                        EventBus.getDefault().post(new EventMessage(Event.goToLogin));
                        SessionUtil.getInstance().logout();
                    }
                } else if (result.isFail()) {
                    listener.onError(new Exception(message));
                } else {
                    listener.onError(new Exception("未知错误"));
                }
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "API Error.");
                if (e instanceof SocketTimeoutException) {
                    listener.onError(new Exception("请求超时"));
                } else if (e instanceof IllegalStateException) {
                    listener.onError(new Exception("服务器数据异常"));
                } else if (e instanceof UnknownHostException) {
                    listener.onError(new Exception("网络状态异常"));
                } else {
                    listener.onError(e);
                }
            }

            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    @NonNull
    public static RequestBody buildJsonBody(Serializable object) {
        return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), new Gson().toJson(object));
    }

    public static RequestBody getRequestBody(String json) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), json);
        return body;
    }
}
