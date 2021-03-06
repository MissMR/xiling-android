package com.xiling.shared.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.xiling.MyApplication;
import com.xiling.R;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.bean.PopupOrderList;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

/**
 * 自定义Toast工具类
 * Created by JayChan on 2016/10/13.
 */
public class ToastUtil {

    public static final String ERR_DATA = "数据错误,请刷新重试";
    public static final String ERR_VERSION = "应用版本过低,请升级";

    public static void toastDataError() {
        error(ERR_DATA);
    }

    public static void toastVersionLess() {
        error(ERR_VERSION);
    }

    private static Dialog mProgressDialog;

    public static void error(String message) {
        show(message, R.color.error_text_color, R.drawable.toast_error_bg);
        Logger.i("Toast Error Message: " + message);
    }

    public static void success(String message) {
        show(message, R.color.success_text_color, R.drawable.toast_success_bg);
        Logger.i("Toast Success Message: " + message);
    }

    public static void show(String message, @ColorRes int textColor, @DrawableRes int backgroundRes) {
        Context context = MyApplication.getInstance().getApplicationContext();
//        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
//        layout.setBackgroundResource(backgroundRes);
//        TextView messageTv = (TextView) layout.findViewById(R.id.toastMessageTv);
//        messageTv.setTextColor(context.getResources().getColor(textColor));
//        messageTv.setText(message);
//        Toast toast = new Toast(context);
//        toast.setGravity(Gravity.TOP, 0, 6);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(layout);
//        toast.show();
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void show(String message,int duration) {
        Context context = MyApplication.getInstance().getApplicationContext();
//        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
//        layout.setBackgroundResource(backgroundRes);
//        TextView messageTv = (TextView) layout.findViewById(R.id.toastMessageTv);
//        messageTv.setTextColor(context.getResources().getColor(textColor));
//        messageTv.setText(message);
//        Toast toast = new Toast(context);
//        toast.setGravity(Gravity.TOP, 0, 6);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(layout);
//        toast.show();
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @SuppressLint("InflateParams")
    public static void showLoading(Context context) {
        if (context == null) {
            return;
        }
        if (context instanceof Activity && (((Activity) context).isFinishing() || ((Activity) context).isDestroyed())) {
            return;
        }

        if (null == mProgressDialog && null != context) {
            View view = LayoutInflater.from(context).inflate(R.layout.loading_layout, null);
            mProgressDialog = new Dialog(context, R.style.LoadingDialog);
            mProgressDialog.setContentView(view, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (null != mProgressDialog && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public static void hideLoading() {
        if (null != mProgressDialog) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
    }

    /**
     * 成功
     *
     * @param msg
     */
    public static void showSuccessToast(Context context, String msg) {
        Toast toast = new Toast(context);
        View inflate = View.inflate(context, R.layout.toast_success_or_fail, null);
        TextView tvMsg = inflate.findViewById(R.id.tv_msg);
        ImageView ivIcon = inflate.findViewById(R.id.iv_icon);
        if (!TextUtils.isEmpty(msg)) {
            tvMsg.setText(msg);
        }
        ivIcon.setBackgroundResource(R.mipmap.icon_dialog_top_success);
        toast.setView(inflate);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 失败
     *
     * @param msg
     */
    public static void showFailToast(Context context, String msg) {
        Toast toast = new Toast(context);
        View inflate = View.inflate(context, R.layout.toast_success_or_fail, null);
        TextView tvMsg = inflate.findViewById(R.id.tv_msg);
        ImageView ivIcon = inflate.findViewById(R.id.iv_icon);
        if (!TextUtils.isEmpty(msg)) {
            tvMsg.setText(msg);
        }
        ivIcon.setBackgroundResource(R.mipmap.icon_dialog_top_failure);
        toast.setView(inflate);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static Dialog getProgressDialog() {
        return mProgressDialog;
    }


}
