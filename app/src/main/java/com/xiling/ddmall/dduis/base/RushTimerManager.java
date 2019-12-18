package com.xiling.ddmall.dduis.base;

import android.os.Handler;
import android.os.Message;

import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.module.community.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class RushTimerManager {

    public interface RushTimerListener {

        /**
         * @param status 状态 1 为距离开始,2为距离结束,3为已结束，自动停止倒计时
         * @param hour   小时数
         * @param minute 分钟数
         * @param second 秒数
         */
        void onRushTimer(int status, String hour, String minute, String second);
    }

    long startTime = 0;
    long endTime = 0;
    RushTimerListener listener = null;
    boolean isStop = true;

    public void setListener(RushTimerListener listener) {
        this.listener = listener;
    }

    public void update(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void update(String startTime, String endTime) {
        DLog.d("startTime:" + startTime);
        DLog.d("endTime:" + endTime);
        Date sDate = DateUtils.parseDateString(startTime);
        Date eDate = DateUtils.parseDateString(endTime);

        this.startTime = sDate.getTime();
        this.endTime = eDate.getTime();
    }

    public void fire() {
        if (isStop) {
            isStop = false;
            //如果有重复处理，移除
            timer.removeMessages(0);
            timer.sendEmptyMessage(0);
        } else {

        }
    }

    public void cancel() {
        isStop = true;
        timer.removeMessages(0);
    }

    Handler timer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //如果有重复处理，移除
            timer.removeMessages(0);

            //计时处理
            clock();

            //发出下一次的计时
            if (!isStop) {
                timer.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    /**
     * - 雷光电火的计算
     * - 忽略处理数据的时间
     * - 原则上理解为处理是瞬时完成
     * - 一切以最完美状态运行
     * <p>
     * - 不存在CPU调度和计算耗时
     * - 不存在夏令时偏差
     * - 更不存在元素衰减或射线造成的晶振偏移
     * <p>
     * - 如果存在偏差那就怪地球引擎出现了BUG!
     */
    private void clock() {

        int status = 0;
        String hour = "00";
        String minute = "00";
        String second = "00";

        long now = new Date().getTime();

        if (now < startTime) {
            //未开始
            status = 1;

            long diff = startTime - now;
            if (diff > 0) {
                TimeDiffTool diffTool = new TimeDiffTool(diff);
                hour = diffTool.getHour();
                minute = diffTool.getMinute();
                second = diffTool.getSecond();
            } else {
                hour = "00";
                minute = "00";
                second = "00";
                status = 2;
            }
        } else if (now > endTime) {
            //已结束
            hour = "00";
            minute = "00";
            second = "00";
            status = 3;
            isStop = true;
        } else {
            //抢购中
            long diff = endTime - now;
            if (diff > 0) {
                TimeDiffTool diffTool = new TimeDiffTool(diff);
                hour = diffTool.getHour();
                minute = diffTool.getMinute();
                second = diffTool.getSecond();
                status = 2;
            } else {
                hour = "00";
                minute = "00";
                second = "00";
                status = 3;
                isStop = true;
            }
        }

        if (listener != null) {
//            DLog.i("status:" + status + "," + hour + ":" + minute + ":" + second);
            listener.onRushTimer(status, hour, minute, second);
        }

    }

    public static boolean isRush(String startTime, String endTime) {
        DLog.d("startTime:" + startTime);
        DLog.d("endTime:" + endTime);
        Date sDate = DateUtils.parseDateString(startTime);
        Date eDate = DateUtils.parseDateString(endTime);

        long now = new Date().getTime();

        if (sDate.getTime() <= now) {
            return true;
        } else {
            return false;
        }
    }

    private class TimeDiffTool implements Serializable {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;

        private String hour = "00";
        private String minute = "00";
        private String second = "00";

        public TimeDiffTool(long diff) {

            // 计算差多少天
            long day = diff / nd;
            // 计算差多少小时
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;
            // 计算差多少秒//输出结果
            long sec = diff % nd % nh % nm / ns;

            //给小时补偿天数
            hour += day * 24;

            setHour(hour > 9 ? hour + "" : "0" + hour);
            setMinute(min > 9 ? min + "" : "0" + min);
            setSecond(sec > 9 ? sec + "" : "0" + sec);
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public String getMinute() {
            return minute;
        }

        public void setMinute(String minute) {
            this.minute = minute;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }
    }
}
