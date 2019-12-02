package com.dodomall.ddmall.shared.basic;


import android.app.Dialog;
import android.support.v4.widget.SwipeRefreshLayout;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/16.
 */
public abstract class BaseSubscriber<T> implements Observer<T> {
    private Disposable mD;
    private SwipeRefreshLayout mLayoutRefresh;
    private Dialog mProgressDialog;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        mD = d;
        if (mLayoutRefresh != null) {
            mLayoutRefresh.setRefreshing(true);
        }
        if (null != mProgressDialog && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (null != mProgressDialog) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void onComplete() {
        if (mLayoutRefresh != null) {
            mLayoutRefresh.setRefreshing(false);
        }
        if (null != mProgressDialog) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public Disposable getDisposable() {
        return mD;
    }


    public void setLayoutRefresh(SwipeRefreshLayout layoutRefresh) {
        mLayoutRefresh = layoutRefresh;
    }

    public void setProgressDialog(Dialog progressDialog) {
        mProgressDialog = progressDialog;
    }
}
