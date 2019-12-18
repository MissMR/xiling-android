package com.xiling.ddmall.module.community;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigbyto on 19/03/2017.
 *
 */
public abstract class SimpleAdapter<T> extends BaseAdapter implements DataManager.DataSource<T>{
    private List<T> dataList = new ArrayList<>();
    private LayoutInflater inflater;

    public SimpleAdapter() {}

    public SimpleAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    protected abstract int getLayoutId(int position);

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T data = getItem(position);

        int viewType = getItemViewType(position);
        ViewDataBinding binding ;
        if (convertView == null) {
            binding = createViewBinding(parent,viewType);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ViewDataBinding) convertView.getTag();
        }

        onBindData(binding,data, position);

        return convertView;
    }

    private ViewDataBinding createViewBinding(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return DataBindingUtil.inflate(inflater,viewType,parent,false);
    }

    protected void onBindData(ViewDataBinding binding, T data, int position) {
        binding.setVariable(BR.item,data);
        binding.executePendingBindings();
    }

    @Override
    public List<T> getDataList() {
        return dataList;
    }

    @Override
    public void appendToDataList(List<T> dataList) {
        this.dataList.addAll(dataList);
    }

    @Override
    public void setDataList(List<T> dataList) {
        this.dataList.clear();
        this.dataList = dataList ;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataChange() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutId(position);
    }
}

