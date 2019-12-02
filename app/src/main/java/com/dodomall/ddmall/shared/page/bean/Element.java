package com.dodomall.ddmall.shared.page.bean;

import android.graphics.Color;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.tencent.bugly.crashreport.CrashReport;
import com.dodomall.ddmall.R;

public class Element {

    @SerializedName("type")
    public String type;
    @SerializedName("height")
    public int height;
    @SerializedName("background")
    public String background;
    @SerializedName("columns")
    public int columns;
    @SerializedName("labelColor")
    public String labelColor;
    @SerializedName("data")
    public JsonElement data;
    @SerializedName("hasMore")
    public boolean hasMore;
    @SerializedName("moreLink")
    public Event moreLink;
    @SerializedName("title")
    public String title;

    public boolean hasBackground() {
        return !Strings.isNullOrEmpty(background);
    }

    public boolean isImageBackground() {
        return hasBackground() && (background.startsWith("http://") || background.startsWith("https://"));
    }

    public boolean isColorBackground() {
        return hasBackground() && background.startsWith("#");
    }

    public void setBackgroundInto(View view) {
        try {
            if (!hasBackground()) {
                return;
            }
            SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.eleBackgroundIv);
            if (imageView == null) {
                return;
            }
            if (isImageBackground()) {
                imageView.setImageURI(background);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
                view.setBackgroundColor(Color.parseColor(background));
            }
        }catch (Exception e){
            CrashReport.postCatchedException(e);
        }
    }

    public class Event {
        @SerializedName("event")
        public String event;
        @SerializedName("target")
        public String target;
    }
}
