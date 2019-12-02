package com.dodomall.ddmall.shared.basic;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.util.ToastUtil;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.basic
 * @since 2017-06-11
 */
abstract public class BaseRequestListener<T> extends RequestListener<T> {

    private Activity mContext;
    private SwipeRefreshLayout mRefreshLayout;

    public BaseRequestListener() {

    }

    public BaseRequestListener(Activity context) {
        mContext = context;
    }

    public BaseRequestListener(SwipeRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        mRefreshLayout.setColorSchemeResources(R.color.red);
    }

    @Override
    public void onStart() {
        if (checkActivityFinished()) {
            return;
        }
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(true);
        } else if (mContext != null) {
            ToastUtil.showLoading(mContext);
        } else {

        }
    }

    @Override
    public void onError(Throwable e) {
        ToastUtil.error(e.getMessage());
        if (checkActivityFinished()) {
            return;
        }

        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        } else if (mContext != null) {
            ToastUtil.hideLoading();
        } else {

        }
    }

    @Override
    public void onSuccess(T result) {
        if (checkActivityFinished()) {
            return;
        }
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        } else if (mContext != null) {
            ToastUtil.hideLoading();
        } else {

        }
    }

    @Override
    public void onComplete() {
        if (checkActivityFinished()) {
            return;
        }
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        } else if (mContext != null) {
            ToastUtil.hideLoading();
        } else {

        }
    }

    private boolean checkActivityFinished() {
        return mContext != null && (mContext.isFinishing() || mContext.isDestroyed());
    }

}
