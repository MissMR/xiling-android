package com.xiling.shared.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.utils.LogUtils;
import com.xiling.ddui.manager.MessageManager;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.shared.bean.Message;
import com.xiling.shared.factory.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;

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
            //通知刷新消息数量
            EventBus.getDefault().post(new MsgStatus(MsgStatus.ReloadMsgCount));
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
