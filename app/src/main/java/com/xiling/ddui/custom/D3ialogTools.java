package com.xiling.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xiling.shared.component.dialog.DDMDialog;

public class D3ialogTools {

    /**
     * 单个按钮
     *
     * @param context
     * @param title
     * @param message
     * @param btnName
     * @param okClickListener
     */
    public static void showSingleAlertDialog(Context context, String title, String message, String btnName, View.OnClickListener okClickListener) {
        Dialog dialog = new SingleAlertDialog(context)
                .setTitle(title)
                .setMessage(message)
                .setOnClickListener(btnName, okClickListener);

        dialog.show();

    }


    public static void showAlertDialog(Context context, String message, String posBtn, View.OnClickListener posClickListener, String negBtn, View.OnClickListener negListener) {
        Dialog dialog = new DDMDialog(context)
                .setContent(message)
                .setPositiveButton(posBtn, posClickListener)
                .setNegativeButton(negBtn, negListener);
        dialog.show();
    }

    public static void showAlertDialog(Context context,String title, String message, String posBtn, View.OnClickListener posClickListener, String negBtn, View.OnClickListener negListener) {
        Dialog dialog = new DDMDialog(context)
                .setTitle(title)
                .setContent(message)
                .setPositiveButton(posBtn, posClickListener)
                .setNegativeButton(negBtn, negListener);
        dialog.show();
    }


    /**
     * 显示成功对话框
     *
     * @param context  上下文对象
     * @param title    标题
     * @param content  内容
     * @param btnText  按钮文本
     * @param listener 按钮事件
     */
    public static void showSuccess(Context context, String title, String content, String btnText, View.OnClickListener listener) {
        showConfirm(context, true, title, content, btnText, listener);
    }

    /**
     * 显示失败对话框
     *
     * @param context  上下文对象
     * @param title    标题
     * @param content  内容
     * @param btnText  按钮文本
     * @param listener 按钮事件
     */
    public static void showFailure(Context context, String title, String content, String btnText, View.OnClickListener listener) {
        showConfirm(context, false, title, content, btnText, listener);
    }

    /**
     * 显示确认对话框
     *
     * @param context  上下文对象
     * @param status   成功失败标记
     * @param title    标题
     * @param content  内容
     * @param btnText  按钮文本
     * @param listener 按钮事件
     */
    public static void showConfirm(Context context, boolean status, String title, String content, String btnText, View.OnClickListener listener) {
        DDResultDialog dialog = new DDResultDialog(context);
        dialog.setStatus(status);
        dialog.setTitle(title);
        dialog.setContent(content);
        dialog.setConfirmText(btnText);
        dialog.setListener(listener);
        dialog.show();
    }

}
