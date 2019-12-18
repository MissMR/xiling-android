package com.xiling.ddmall.module.community;


import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

public class TextAdapter {
    @BindingAdapter({"selected"})
    public static void selected(View view, boolean isSelected) {
        view.setSelected(isSelected);
    }

    @BindingAdapter({"color"})
    public static void setColor(TextView view, int res) {
        int color = ContextCompat.getColor(view.getContext(),res);
        view.setTextColor(color);
    }
}
