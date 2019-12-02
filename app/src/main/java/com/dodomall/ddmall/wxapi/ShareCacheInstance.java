package com.dodomall.ddmall.wxapi;

/**
 * @author Stone
 * @time 2018/5/3  9:52
 * @desc ${TODD}
 */

public class ShareCacheInstance {
    //0代表的是朋友圈的，1代表的是素材的和小视频的

    private int callBackType;

    private boolean needCalBack;

    private String callBackParm;

    private ShareCacheInstance(){

   }

   private static class Singleton{
       static final ShareCacheInstance INSTANCE=new ShareCacheInstance();
   }

   public static ShareCacheInstance getInstance(){
       return Singleton.INSTANCE;
   }

    public int getCallBackType() {
        return callBackType;
    }

    public void setCallBackType(int callBackType) {
        this.callBackType = callBackType;
    }

    public boolean isNeedCalBack() {
        return needCalBack;
    }

    public void setNeedCalBack(boolean needCalBack) {
        this.needCalBack = needCalBack;
    }

    public String getCallBackParm() {
        return callBackParm;
    }

    public void setCallBackParm(String callBackParm) {
        this.callBackParm = callBackParm;
    }
}
