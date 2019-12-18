package com.xiling.ddmall.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xiling.ddmall.ddui.activity.AuthActivity;
import com.xiling.ddmall.shared.component.dialog.DDMDialog;

public class D3ialogTools {

    /**
     * 显示实名认证通用对话框
     *
     * @param context    上下文对象
     * @param authStatus 审核状态,0:未提交;1:等待审核;2:审核通过;3:审核失败,int,默认为0
     * @auth hanQ
     */
    public static void showAuthDialog(Context context, final int authStatus) {

        String positiveText = "确定";
        String content = "";
        switch (authStatus) {
            case 0:
                content = "您还未进行实名认证哦~";
                positiveText = "去认证";
                break;
            case 1:
                content = "您的实名认证还在审核哦~";
                positiveText = "继续等待";
                break;
            case 3:
            case 4:
                content = "您的实名认证审核失败~";
                positiveText = "重新提交";
                break;
        }

        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                if (authStatus == 0 || authStatus == 3 || authStatus == 4) {
                    context.startActivity(new Intent(context, AuthActivity.class));
                }

            }
        };

        Dialog dialog = new DDMDialog(context)
                .setTitle("提示")
                .setContent(content)
                .setPositiveButton(positiveText, ocl);
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
