package com.xiling.shared.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
            mProgressDialog.setCancelable(false);
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

    public static void showOrderToast(Context context, PopupOrderList.DatasEntity datasEntity) {

        if (datasEntity == null) {
            return;
        }

        String time = TimeUtils.getFitTimeSpan(TimeUtils.string2Millis(datasEntity.createDate), TimeUtils.getNowTimeMills(), 4);
        if (time == null) {
            return;
        }

        Toast toast = new Toast(context);
        View inflate = View.inflate(context, R.layout.layout_main_toast, null);
        //设置头像
        SimpleDraweeView simpleDraweeView = inflate.findViewById(R.id.avatarImageView);
        FrescoUtil.setImageSmall(simpleDraweeView, datasEntity.headImage);
        //设置昵称
        TextView nameTextView = inflate.findViewById(R.id.nameTextView);
        nameTextView.setText("" + datasEntity.nickName);
        //设置时间
        TextView timeTextView = inflate.findViewById(R.id.timeTextView);
        timeTextView.setText("来自" + time + "之前的订单");

        toast.setView(inflate);
        toast.setDuration(Toast.LENGTH_LONG);

        DLog.e(datasEntity.createDate + "   " + datasEntity.nickName + "  差时间" + time);

        int x = SizeUtils.dp2px(22);
        int y = SizeUtils.dp2px(84);
        toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, x, y);
        toast.show();
    }

    public static Dialog getProgressDialog() {
        return mProgressDialog;
    }


}
