package com.xiling.module.community;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.TextView;

/**
 * Created by bigbyto on 04/05/2017.
 * view binding adapter
 */

public class ViewAdapter {
    @BindingAdapter({"show"})
    public static void show(View view, boolean isShow) {
        view.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter({"isGone"})
    public static void isGone(View view, boolean isGone) {
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter({"isShow"})
    public static void isShow(View view, boolean isShow) {
        view.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter({"background"})
    public static void setBackgroundRes(View view, int resource){
        view.setBackgroundResource(resource);
    }

    @BindingAdapter({"bold"})
    public static void setBold(TextView view, boolean isBold) {
        view.getPaint().setFakeBoldText(isBold);
    }
}
