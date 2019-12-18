package com.xiling.ddmall.shared.util;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/2/27.
 * 异常报告工具类
 */
public class CarshReportUtils {

    /**
     * 将异常上报给 bugly
     *
     * @param exception
     */
    public static void post(Exception exception) {
        CrashReport.postCatchedException(exception);
    }

    /**
     * 将消息封装成异常，上报给 bugly
     * @param msg 消息
     */
    public static void post(String msg) {
        RuntimeException exception = new RuntimeException(msg);
        post(exception);
    }
}
