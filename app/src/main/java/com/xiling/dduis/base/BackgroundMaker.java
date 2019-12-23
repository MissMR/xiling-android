package com.xiling.dduis.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.text.TextUtils;
import android.view.View;

import com.xiling.shared.util.ConvertUtil;

public class BackgroundMaker {

    Context context = null;

    public BackgroundMaker(Context context) {
        this.context = context;
    }

    /**
     * 获取指定弧度的背景图
     */
    public Drawable getBackground(int boardWidth, int radius, int stokeColor, int fillColor) {
        int strokeWidth = ConvertUtil.dip2px(boardWidth);
        int roundRadius = ConvertUtil.dip2px(radius);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, stokeColor);

        return gd;
    }

    /**
     * 设置Drawable的颜色
     *
     * @param view  View对象
     * @param color 颜色
     */
    public static void setTriangleDrawableColor(View view, int color) {
        LayerDrawable layerDrawable = (LayerDrawable) view.getBackground();
        RotateDrawable rotate = (RotateDrawable) layerDrawable.getDrawable(0);
        GradientDrawable drawable = (GradientDrawable) rotate.getDrawable();
        drawable.setColor(color);
    }

    public Drawable getRadiusDrawable(int color, int radius) {
        return getBackground(0, radius, color, color);
    }

    /**
     * 首页搜索栏背景
     *
     * @param boardColor      边框颜色
     * @param backgroundColor 背景颜色
     */
    public Drawable getHomeSearchBarBackground(String boardColor, String backgroundColor) {

        int sFC = Color.parseColor("#f2f2f2");
        int sBgC = Color.parseColor("#f2f2f2");

        if (!TextUtils.isEmpty(boardColor)) {
            try {
                sFC = Color.parseColor(boardColor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(backgroundColor)) {
            try {
                sBgC = Color.parseColor(backgroundColor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return getBackground(1, 21, sFC, sBgC);
    }

    public Drawable getHomeTimeBackground(int backgroundColor) {
        return getBackground(1, 4, backgroundColor, backgroundColor);
    }


}
