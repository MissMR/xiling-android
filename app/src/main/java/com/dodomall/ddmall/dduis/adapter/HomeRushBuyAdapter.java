package com.dodomall.ddmall.dduis.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.DDProductDetailActivity;
import com.dodomall.ddmall.ddui.config.UIConfig;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.UITools;
import com.dodomall.ddmall.dduis.base.AvatarDemoMaker;
import com.dodomall.ddmall.dduis.base.BackgroundMaker;
import com.dodomall.ddmall.dduis.base.RushTimerManager;
import com.dodomall.ddmall.dduis.bean.DDRushSpuBean;
import com.dodomall.ddmall.dduis.custom.SaleProgressView;
import com.dodomall.ddmall.dduis.viewholder.RushBuyViewHolder;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
