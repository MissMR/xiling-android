package com.xiling.module.community;

import android.text.TextUtils;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bigbyto on 30/06/2017.
 */

public class DateUtils {

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date, String format) {
        try {
            if (format == null || format.isEmpty()) {
                format = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStampLong(String date, String format) {
        try {
            if (format == null || format.isEmpty()) {
                format = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 时间戳转换日期格式字符串
     *
     * @param time
     * @param format
     * @return
     */
    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }


    /**
     * 倒计时专用
     */
    public static void setCountDownTimeStrng(long time, TextView tvHour, TextView tvMinute, TextView tvSecond) {
       long hours = time/1000/60/60;
       long seconds = (time - (hours*60*60*1000))/1000/60;
       long min = (time - (hours*60*60*1000)-(seconds*60*1000))/1000;
        DecimalFormat decimalFormat =new DecimalFormat("00");
        tvHour.setText( decimalFormat.format(hours));
        tvMinute.setText(decimalFormat.format(seconds));
        tvSecond.setText(decimalFormat.format(min));

    }

    /**
     * 计算订单完成时间
     *
     * @return
     */
    public static long getOrderFinishTime(String shipDate) {
        long shipStamp = date2TimeStampLong(shipDate, "");
        long curTime = System.currentTimeMillis();
        return shipStamp + 3600 * 24 * 10*1000 -curTime;
    }

    /**
     * 计算当前时间戳是几天几小时
     *
     * @param countDownTimne
     * @return
     */
    public static String getOrderFinishTimeString(long countDownTimne) {
        int day = (int) (countDownTimne / (1000 * 60 * 60 * 24));
        int hour = (int) ((countDownTimne - day * 1000 * 60 * 60 * 24) / (1000 * 60 * 60));
        return "订单将在" + day + "天" + hour + "小时后自动完成";
    }


    public static String getYM(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        return String.format("%s%s", year, month);
    }

    public static String getYMR(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return String.format("%s-%s-%s", year, month, day);
    }

    public static String parseYM(int ym) {
        String str = String.valueOf(ym);
        String year = str.substring(0, 4);
        String month = str.substring(4, str.length());

        return String.format("%s年%s月", year, month);
    }

    public static String getVoiceTime(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) {
            return "00:00";
        }

        int time = 0;
        try {
            time = Integer.parseInt(timeStr);
        } catch (Exception e) {
            e.printStackTrace();
            return "00:00";
        }

        int ONE_MINUTE = 60;
        int minute = time / ONE_MINUTE;
        int sec = (time - minute * ONE_MINUTE) % ONE_MINUTE;

        String minuteStr = "";
        String secStr = "";
        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr += minute;
        }

        if (sec < 10) {
            secStr = "0" + sec;
        } else {
            secStr += sec;
        }

        return minuteStr + ':' + secStr;
    }

    public static String getFormatTextFromDate(Date date, String formatText) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatText, Locale.getDefault());
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateString(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "1980-01-01";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Date date2 = new Date();

        long delta = System.currentTimeMillis() - date.getTime();
        int ONE_DAY = 86400000;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.setTime(date2);
        int today = calendar.get(Calendar.DAY_OF_MONTH);

        if (delta < ONE_DAY && day == today) {
            return String.format(Locale.getDefault(), "%02d:%02d", hours, minute);
        } else if (delta <= ONE_DAY * 2) {
            return "昨天";
        } else if (delta > ONE_DAY * 2 && delta <= ONE_DAY * 3) {
            return "前天";
        } else {
            return String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day);
        }
    }

    /**
     * 抢购专用的事件转换
     */
    public static String getTimeString(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "1980-01-01";
        }

        Date date2 = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date2);

        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowYear = calendar.get(Calendar.YEAR);

        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String response = String.format(Locale.getDefault(), "%02d:%02d", hours, minute);
        if (nowYear == year && nowMonth == month) {
            if (nowDay == day) {
                //今天
//                return response;
            } else if (nowDay == day + 1) {
                //昨天
//                return "昨天" + response;
            } else if (nowDay == day - 1) {
                //明天
//                return "明天" + response;
            }
        }
//            return String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day);
        //其他情况默认返回
        return response;
    }

    /**
     * 抢购专用的事件转换
     *
     * @return 2 明天 ； 1 正常字符串 ； 0 昨天
     */
    public static int getTimeStatus(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return 1;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return 1;
        }

        Date date2 = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date2);

        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowYear = calendar.get(Calendar.YEAR);

        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int response = 1;
        if (nowYear == year && nowMonth == month) {
            if (nowDay == day) {
                //今天
                return 1;
            } else if (nowDay == day + 1) {
                //昨天
                return 0;
            } else if (nowDay == day - 1) {
                //明天
                return 2;
            }
        }
//            return String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day);
        //其他情况默认返回
        return response;
    }

    public static Date parseDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date;
        try {
            date = format.parse(dateString);
        } catch (Exception e) {
            date = new Date();
            e.printStackTrace();
        }

        return date;
    }

    public static int getYearFromDateString(String dateString) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDateString(dateString));

        return calendar.get(Calendar.YEAR);
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    public static boolean isThisYear(Date date) {
        int year = getYear(date);
        int thisYear = getYear(new Date());

        return year == thisYear;
    }

    public static String getYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

    public static String getCurrentTime() {
        Calendar time = Calendar.getInstance();
        time.setTime(new Date());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time.getTime());
    }

    public static String getDateBefore(int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day + 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(now.getTime());
    }

    public static String getDayDateBefore(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day + 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(now.getTime());
    }

    public static String getDayDateAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day - 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(now.getTime());
    }

    public static Date parseDateString2(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date;
        try {
            date = format.parse(dateString);
        } catch (Exception e) {
            date = new Date();
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 计算当前日期
     *
     * @return
     */
    public static int[] getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)};
    }

    public static int longOfTwoDate(Date first, Date second) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(first);
        int cnt = 0;
        while (calendar.getTime().compareTo(second) != 0) {
            calendar.add(Calendar.DATE, 1);
            cnt++;
        }
        cnt++;
        return cnt;
    }

    public static String getStandDate(Date dt) {
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dFormat.format(dt);
    }
}
