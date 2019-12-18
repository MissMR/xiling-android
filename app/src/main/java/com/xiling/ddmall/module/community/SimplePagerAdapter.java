package com.xiling.ddmall.module.community;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigbyto on 27/03/2017.
 * simpe pager adapter
 */

public abstract class SimplePagerAdapter<T> extends PagerAdapter {
    private List<T> datalist = new ArrayList<>();
    private LayoutInflater inflater;

    public SimplePagerAdapter() {}

    public SimplePagerAdapter(List<T> datalist) {
        this.datalist = datalist;
    }

    protected abstract int getLayoutId(int postion);

    @Override
    public int getCount() {
        return datalist.size();
    }

    public T getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int layoutId = getLayoutId(position);
        if (inflater == null) {
            inflater = LayoutInflater.from(container.getContext());
        }

        ViewDataBinding binding = DataBindingUtil.inflate(inflater,layoutId,container,true);
        onBindData(binding,getItem(position),position);

        return binding.getRoot();
    }

    protected void onBindData(ViewDataBinding binding,T data, int position) {
//        binding.setVariable(BR.item,data);
        binding.executePendingBindings();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setDatalist(List<T> datalist) {
        this.datalist = datalist;
    }

    public List<T> getDataList() {
        return this.datalist;
    }
}
