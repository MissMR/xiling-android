package com.dodomall.ddmall.shared.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.utils.LogUtils;
import com.dodomall.ddmall.ddui.manager.MessageManager;
import com.dodomall.ddmall.shared.bean.Message;
import com.dodomall.ddmall.shared.factory.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cn.jpush.android.api.JPushInterface;

/**
 * @author Jigsaw
 * @date 2018/11/13
 * 点击通知直接进入消息列表
 */
public class JPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()) && intent.hasExtra(JPushInterface.EXTRA_EXTRA)) {
            String json = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
            LogUtils.e("推送通知" + json);
            Message message = getFormatMessage(json);
            MessageManager.newInstance(context, message).openMessageDetail(true);
        }
    }

    private Message getFormatMessage(String json) {
        Gson gson = GsonFactory.make();
        Message message = null;
        try {
            message = gson.fromJson(json, Message.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return message;
    }

}
