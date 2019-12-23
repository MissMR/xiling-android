package com.xiling.shared.util;

import android.support.annotation.NonNull;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/6/8.
 */
public class CountDownRxUtils {

    /**
     * 给 textView 自动倒计时
     * text 需要显示倒计时的 textview
     * @param textView 需要倒计时的 text
     * @param timeCount 倒计时总时间(单位 秒)
     * @param initStr  默认状态下显示的文本
     */
    public static void textViewCountDown(final TextView textView, final int timeCount, final String initStr){
        Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .take(timeCount)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return timeCount - aLong;

                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        textView.setEnabled(false);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        textView.setText(aLong+"秒");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        textView.setEnabled(true);
                        textView.setText(initStr);
                    }
                });
    }

    /**
     * @param textView
     * @param timeCount 倒计时总时间，单位秒
     * @param format    显示格式（"还差1人，剩余%s"）
     * @param pattern   剩余时间转换格式（"HH:mm:ss"）
     */
    public static void textViewCountDown(final TextView textView, final int timeCount, final String format, final String pattern) {
        Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .take(timeCount)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return timeCount - aLong;

                    }
                })
                .subscribe(new Observer<Long>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        SimpleDateFormat sd = new SimpleDateFormat(pattern);
                        sd.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                        String time = sd.format(new Date(aLong * 1000));
                        textView.setText(String.format(format, time));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        textView.setText("拼团结束");
                    }
                });
    }
}
