package com.dodomall.ddmall.ddui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.shared.basic.BaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectMapAddressAdapter extends BaseAdapter<PoiItem, SelectMapAddressAdapter.AddressViewHolder> {

    public interface EventListener {
        void onPoiItemClicked(int position, PoiItem item);
    }

    private EventListener listener = null;

    public EventListener getListener() {
        return listener;
    }

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public SelectMapAddressAdapter(Context context) {
        super(context);
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_map_address_select, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        PoiItem item = items.get(position);
        holder.setData(item, position);
        holder.render();
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mainPanel)
        RelativeLayout mainPanel;

        @BindView(R.id.statusImageView)
        ImageView statusImageView;

        @BindView(R.id.poiName)
        TextView poiNameView;

        @BindView(R.id.poiAddress)
        TextView poiAddressView;

        int position;
        PoiItem data = null;

        @OnClick(R.id.mainPanel)
        void onItemClicked() {
            if (listener != null) {
                listener.onPoiItemClicked(position, data);
            } else {
                DLog.w("can't find listener for poi item clicked!(" + position + "," + data + ")");
            }
        }

        public AddressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(PoiItem data, int position) {
            this.position = position;
            this.data = data;
        }

        public void render() {
            String title = data.getTitle();
            poiNameView.setText(title);

            String fullAddress = "" + data.getProvinceName() + " " + data.getCityName() + " " + data.getSnippet();
            poiAddressView.setText(fullAddress);

            if (position == 0) {
                int color = context.getResources().getColor(R.color.mainColor);
                poiNameView.setTextColor(color);
                statusImageView.setImageResource(R.mipmap.icon_list_current);
            } else {
                poiNameView.setTextColor(Color.parseColor("#333333"));
                statusImageView.setImageResource(R.mipmap.icon_list_other);
            }
        }

    }
}
