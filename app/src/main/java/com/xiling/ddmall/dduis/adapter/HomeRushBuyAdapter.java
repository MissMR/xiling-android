package com.xiling.ddmall.dduis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.dduis.bean.DDRushSpuBean;
import com.xiling.ddmall.dduis.viewholder.RushBuyViewHolder;

import java.util.ArrayList;

public class HomeRushBuyAdapter extends RecyclerView.Adapter<RushBuyViewHolder> {

    int status = 0;

    public void setStatus(int status) {
        this.status = status;
    }

    Context context = null;
    ArrayList<DDRushSpuBean> data = new ArrayList<>();

    public void setData(ArrayList<DDRushSpuBean> data) {
        this.data = data;
    }


    public void appendData(ArrayList<DDRushSpuBean> data) {
        this.data.addAll(data);
    }

    public HomeRushBuyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RushBuyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_home_rush, parent, false);
        return new RushBuyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RushBuyViewHolder holder, int position) {
        DDRushSpuBean item = data.get(position);
        holder.setStatus(this.status);
        holder.setData(item);
        holder.setReloadListener(new RushBuyViewHolder.ReloadListener() {
            @Override
            public void onReload(int position) {
                DLog.i("onReload:" + position);
                notifyItemChanged(position);
            }
        });
        holder.render();
    }

}
