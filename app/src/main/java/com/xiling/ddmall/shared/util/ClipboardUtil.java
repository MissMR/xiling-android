package com.xiling.ddmall.shared.util;

import android.content.ClipData;
import android.content.ClipboardManager;

import com.xiling.ddmall.MyApplication;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * created by Jigsaw at 2018/9/15
 * 复制和粘贴功能
 */
public class ClipboardUtil {
    // 复制到粘贴板
    public static void setPrimaryClip(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) MyApplication.getInstance().getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", text);
        clipboardManager.setPrimaryClip(clipData);
//        ToastUtil.success("复制成功");
    }
}
