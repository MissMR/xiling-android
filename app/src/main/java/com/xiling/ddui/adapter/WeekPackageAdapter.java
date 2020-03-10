package com.xiling.ddui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class WeekPackageAdapter extends PagerAdapter{
    private Context context;
    private List<View> list;

    public WeekPackageAdapter(Context context, List<View> list) {
        this.context = context;
        this.list = list;
    }

    //返回长度
    @Override
    public int getCount() {
        return list.size();
    }

    //判断视图
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    //初始化视图
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //将对应view添加到容器中
        container.addView(list.get(position));
        //返回集合对应的View对象
        return list.get(position);
    }

    //销毁视图
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //容器中移除视图View
        container.removeView(list.get(position));
    }
}
