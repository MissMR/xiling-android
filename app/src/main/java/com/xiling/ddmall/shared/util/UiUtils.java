package com.xiling.ddmall.shared.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.google.common.base.Strings;
import com.xiling.ddmall.module.user.LoginActivity;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/2.
 */
public class UiUtils {

    /**
     * 配置recycleview
     *
     * @param recyclerView
     * @param layoutManager
     */
    public static void configRecycleView(final RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager) {
        configRecycleView(recyclerView, layoutManager, true);
    }

    /**
     * 配置recycleview
     *
     * @param recyclerView
     * @param layoutManager
     */
    public static void configRecycleView(final RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager, boolean hasFixedSize) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(hasFixedSize);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public static boolean checkETPhone(EditText etPhone) {
        String phone = etPhone.getText().toString();
        if (Strings.isNullOrEmpty(phone)) {
            ToastUtil.error("请输入手机号");
            etPhone.requestFocus();
            return false;
        } else if (!ValidateUtil.isPhone(phone)) {
            ToastUtil.error("手机号格式不正确");
            etPhone.requestFocus();
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * 检查用户是否登录，如未登录，直接跳到注册页面
     * @param context
     * @return 是否已登录
     */
    public static boolean checkUserLogin(Context context){
        boolean isLogin = SessionUtil.getInstance().isLogin();
        if (!isLogin) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
        return isLogin;
    }
}
