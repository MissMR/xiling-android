package com.xiling.ddmall.dduis.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.CategoryBean;
import com.xiling.ddmall.dduis.bean.FilterDataBean;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    public static final String kID_FREE_SHIP = "FilterAdapter.FREE_SHIP";
    public static final String kID_RUSH = "FilterAdapter.RUSH";

    public String getSelects() {
        StringBuffer stringBuffer = new StringBuffer();
        for (FilterDataBean item : data) {
            if (item.isSelect()) {
                stringBuffer.append("" + item.getSelectId() + ",");
            }
        }
        String response = stringBuffer.toString();
        int length = response.length();
        if (length > 1) {
            response = response.substring(0, length - 1);
        }
        return response;
    }

    public boolean isFreeShip() {
        String ids = getSelects();
        if (!TextUtils.isEmpty(ids)) {
            return ids.contains(kID_FREE_SHIP);
        } else {
            return false;
        }
    }

    public boolean isRush() {
        String ids = getSelects();
        if (!TextUtils.isEmpty(ids)) {
            return ids.contains(kID_RUSH);
        } else {
            return false;
        }
    }

    boolean isMultipleSelect = false;

    public void canMultipleSelect(boolean isMultipleSelect) {
        this.isMultipleSelect = isMultipleSelect;
    }

    public void showCategory(ArrayList<CategoryBean> data) {

        //TIPS 这里改为false就可以只能选中一项
        isMultipleSelect = true;

        this.data.clear();

        //分类数据
        for (CategoryBean bean : data) {
            this.data.add(new FilterDataBean(bean.getCategoryId(), bean.getCategoryName(), isCategorySelect(bean.getCategoryId())));
        }

        notifyDataSetChanged();
    }

    public void showServiceData() {

        isMultipleSelect = true;

        data.clear();

        //服务数据
        data.add(new FilterDataBean(kID_FREE_SHIP, "包邮", isFreeship));
        data.add(new FilterDataBean(kID_RUSH, "限时抢购", isRush));

        notifyDataSetChanged();
    }

    boolean isCategorySelect(String cid) {
        return categoryStack.containsKey(cid);
    }


    Context context = null;
    ArrayList<FilterDataBean> data = new ArrayList<>();

    HashMap<String, String> categoryStack = new HashMap<>();
    boolean isRush = false;
    boolean isFreeship = false;

    public void select(ArrayList<String> subcategorys) {
        categoryStack.clear();
        for (String key : subcategorys) {
            categoryStack.put(key, key);
        }
    }

    public void select(boolean isRush, boolean isFreeship) {
        this.isRush = isRush;
        this.isFreeship = isFreeship;
    }

    public FilterAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_filter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilterAdapter.ViewHolder holder, int position) {
        FilterDataBean bean = data.get(position);
        holder.setData(position, bean);
        holder.render();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_main)
        RelativeLayout mainLayout;

        @BindView(R.id.tv_title)
        TextView titleTextView;

        @BindView(R.id.iv_status)
        ImageView statusImageView;

        int index = -1;
        FilterDataBean item = null;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int index, FilterDataBean item) {
            this.index = index;
            this.item = item;
        }

        public void render() {
            if (item != null) {
                titleTextView.setText(item.getTitle());
                titleTextView.setTextColor(item.isSelect() ? Color.parseColor("#FF4647") : Color.parseColor("#000000"));
                statusImageView.setVisibility(item.isSelect() ? View.VISIBLE : View.INVISIBLE);
                mainLayout.setBackgroundResource(item.isSelect() ? R.drawable.item_bg_red_select_r8 : R.drawable.item_bg_gray_unselect_r8);
            }
        }

        @OnClick(R.id.layout_main)
        void onMainPressed() {
            if (!isMultipleSelect) {
                clear();
            }
            item.setSelect(!item.isSelect());
//            notifyItemChanged(index);
            notifyDataSetChanged();
        }
    }

    public void clearSelect() {
        clear();
        notifyDataSetChanged();
    }

    void clear() {
        for (FilterDataBean item : data) {
            item.setSelect(false);
        }
    }

}
