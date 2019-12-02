package com.dodomall.ddmall.shared.component.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.contracts.IRegion;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component.adapter
 * @since 2017-07-06
 */
public class RegionAdapter extends BaseAdapter<IRegion, RegionAdapter.ViewHolder> {

    public RegionAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.base_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final IRegion region = items.get(position);
        holder.mTitleTv.setText(region.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventMessage(Event.regionSelect, region));
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemTitleTv)
        protected TextView mTitleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
