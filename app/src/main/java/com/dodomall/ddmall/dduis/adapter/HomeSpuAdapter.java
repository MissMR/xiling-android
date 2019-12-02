package com.dodomall.ddmall.dduis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.dduis.bean.DDProductBean;
import com.dodomall.ddmall.dduis.viewholder.RushBuyViewHolder;
import com.dodomall.ddmall.dduis.viewholder.SpuProductViewHolder;


import java.util.ArrayList;

public class HomeSpuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context = null;
    ArrayList<DDProductBean> data = new ArrayList<>();

    public void setData(ArrayList<DDProductBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void appendData(ArrayList<DDProductBean> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public HomeSpuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).isFlashSale()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_home_rush, parent, false);
            return new RushBuyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_category_data, parent, false);
            return new SpuProductViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DDProductBean bean = data.get(position);
        if (bean.isFlashSale()) {
            RushBuyViewHolder rushHolder = (RushBuyViewHolder) holder;
            rushHolder.setData(bean);
            rushHolder.setReloadListener(new RushBuyViewHolder.ReloadListener() {
                @Override
                public void onReload(int position) {
                    notifyItemChanged(position);
                }
            });
            rushHolder.render();
        } else {
            SpuProductViewHolder spuHolder = (SpuProductViewHolder) holder;
            spuHolder.setData(bean);
            spuHolder.setReloadListener(new SpuProductViewHolder.ReloadListener() {
                @Override
                public void onReload(int position) {
                    notifyItemChanged(position);
                }
            });
            spuHolder.render();
        }
    }

}
