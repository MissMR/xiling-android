package com.dodomall.ddmall.dduis.bean;

import com.dodomall.ddmall.dduis.base.RushTimerManager;
import com.dodomall.ddmall.module.community.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class DDRushHeaderBean implements Serializable {

    private String flashSaleId = "";
    private String name = "";
    private int status = 0;
    private String startTime = "";
    private String endTime = "";
    private boolean selected = false;

    public static boolean isRushEnable(int status) {
        if (status > 1) {
            //不管是抢购中还是已结束都认为是可以抢购
            return true;
        } else {
            return false;
        }
    }

    public void resetStatusTextWithTime() {
        long now = new Date().getTime();
        Date sDate = DateUtils.parseDateString(startTime);
        Date eDate = DateUtils.parseDateString(endTime);
        if (now < sDate.getTime()) {
            //未开始
            status = 1;
        } else if (now > eDate.getTime()) {
            //已结束
            status = 3;
        } else {
            //抢购中
            status = 2;
        }
    }

    public String getStatusText(boolean isSelect) {
        resetStatusTextWithTime();
        String statusText = "";

        int status = DateUtils.getTimeStatus(this.startTime);

        switch (getStatus()) {
            case 1:
                if (status == 2) {
                    statusText = "明天开抢";
                } else {
                    statusText = "即将开始";
                }
                break;
            case 2:
                if (isSelect) {
                    statusText = "抢购中";
                } else {
                    if (status == 0) {
                        statusText = "昨日已开抢";
                    } else {
                        statusText = "已开抢";
                    }

                }
                break;
            case 3:
                statusText = "已结束";
                break;
            default:
                statusText = "未知状态";
        }
        return statusText;
    }

    /**
     * 是否已开始抢购
     */
    public boolean isRushEnable() {
        resetStatusTextWithTime();
        return isRushEnable(status);
    }

    public String getFormatTimeText() {
        try {
            return DateUtils.getTimeString(this.startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getFlashSaleId() {
        return flashSaleId;
    }

    public void setFlashSaleId(String flashSaleId) {
        this.flashSaleId = flashSaleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
