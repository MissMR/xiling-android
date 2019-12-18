package com.xiling.ddmall.module.category.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseAdapter;
import com.xiling.ddmall.shared.bean.Category;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.bean.event.EventMessage;

import org.greenrobot.eventbus.EventBus;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.category.adapter
 * @since 2017-06-17
 */
public class LeftCategoryAdapter extends BaseAdapter<Category, LeftCategoryAdapter.ViewHolder> {

    private String mId = "";

    public LeftCategoryAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_left_category, parent, false));
    }

    public void setSelect(String id){
        mId = id;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Category category = items.get(position);
        holder.mItemLabelTv.setText(category.name);
        holder.mItemLabelTv.setSelected(mId.equals(category.id));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mId = category.id;
                notifyDataSetChanged();
                EventBus.getDefault().post(new EventMessage(Event.changeCategory, category.id));
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mItemLabelTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemLabelTv = (TextView) itemView.findViewById(R.id.itemLabelTv);
        }
    }
}
