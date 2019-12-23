package com.xiling.shared.basic;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.xiling.shared.constant.Event;
import com.xiling.shared.Constants;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.event.EventMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.PrintWriter;
import java.io.StringWriter;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class BaseObserver<T> implements Observer<RequestResult<T>> {

    private Context mContext;
    private Disposable mDisposable;

    public BaseObserver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.mDisposable = d;
    }

    @Override
    public void onNext(RequestResult<T> value) {
        if (value.code == Constants.SUCCESS_CODE) {
            onHandleSuccess(value.data);
        } else if (value.code == Constants.NOT_LOGIN_CODE) {
            EventBus.getDefault().post(new EventMessage(Event.goToLogin, true));
        } else {
            EventBus.getDefault().post(new EventMessage(Event.toastErrorMessage, value.message));
        }
    }

    @Override
    public void onError(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        Logger.e("Request Error: " + sw.toString());
        Logger.e("Request Error: " + e.toString());
        this.hideLoading();
//        EventBus.getDefault().post(new EventMessage(Event.toastErrorMessage, "网络异常，请稍后再试"));
    }

    @Override
    public void onComplete() {
        this.hideLoading();
    }

    private void hideLoading() {
        mDisposable.dispose();
        EventBus.getDefault().post(new EventMessage(Event.hideLoading));
    }

    public abstract void onHandleSuccess(T t);
}
