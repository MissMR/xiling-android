package com.xiling.ddmall.shared.util;

import android.app.Dialog;

import io.reactivex.ObservableTransformer;


/**
 * Created by jess on 11/10/2016 16:39
 * Contact with jess.yan.effort@gmail.com
 */

public class RxUtils {

    public static <T> ObservableTransformer<T, T> applySchedulers(final Dialog progressDialog) {
//        return upstream ->
//                upstream.subscribeOn(Schedulers.io())
//                        .doOnSubscribe(disposable -> progressDialog.show())
//                        .subscribeOn(AndroidSchedulers.mainThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .doAfterNext(disposable -> progressDialog.dismiss());
        return null;
    }
}
