package com.xiling.ddmall.ddui.bean;

import android.content.Context;
import android.net.Uri;

import com.xiling.ddmall.ddui.activity.DDProductDetailActivity;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.module.page.WebViewActivity;
import com.xiling.ddmall.shared.util.ToastUtil;

import java.io.Serializable;

public class DDHomeBanner implements Serializable {

    /***
     * 处理通用的跳转逻辑
     *
     * @param context 上下文对象
     * @param event  事件类型
     * @param target 事件数据
     **/
    public static void process(Context context, String event, String target) {
        if (DDHomeBanner.EVENT.LINK.equals(event)) {
            String url = target;
            DLog.w("跳转超链接:" + url);
            WebViewActivity.jumpUrl(context, url);
        } else if (DDHomeBanner.EVENT.NATIVE.equals(event)) {
            DLog.w("跳转原生");
            parseNative(context, target);
        } else {
            DLog.w("不响应跳转");
        }
    }

    /**
     * 跳转原生界面的处理
     *
     * @param target 目标
     */
    public static void parseNative(Context context, String target) {
        Uri parse = Uri.parse("app://" + target);
        switch (parse.getHost()) {
            case "product":
            case "product-instant":
//                viewProductDetail(context, parse.getQueryParameter("skuId"));
                ToastUtil.toastVersionLess();
                break;
            case "spuDetail":
                viewProductDetail(context, parse.getQueryParameter("spuId"));
                break;
        }
    }

    public static void viewProductDetail(Context context, String spuId) {
        if (spuId == null || spuId.isEmpty()) {
            return;
        }
        DDProductDetailActivity.start(context, spuId);
    }

    /**
     * 事件类型
     */
    public static final class EVENT {
        /*不响应*/
        public static final String NONE = "none";
        /*网页链接*/
        public static final String LINK = "link";
        /*原生逻辑*/
        public static final String NATIVE = "native";
    }

    //标题
    private String title = "";
    //轮播图片URL地址
    private String imgUrl = "";
    //点击响应类型(1:跳转H5;2:跳转原生)
//    @SerializedName("eventType")
    private String event = EVENT.NONE;
    //点击响应数据
//    @SerializedName("eventTarget")
    private String target = "";
    //背景颜色 需求已更改
    private String color = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "{ imgUrl : " + imgUrl + " , event : " + event + " , target : " + target + " }";
    }
}
