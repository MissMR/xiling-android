package com.xiling.ddui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.activity.DDCategoryActivity;
import com.xiling.ddui.bean.HomeShortcutBean;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DDHomeShortcutAdapter extends BaseAdapter<HomeShortcutBean, DDHomeShortcutAdapter.ViewHolder> {

    public DDHomeShortcutAdapter(Context context) {
        super(context);
    }

    @Override
    public DDHomeShortcutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_home_shortcut, parent, false);
        return new DDHomeShortcutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DDHomeShortcutAdapter.ViewHolder holder, int position) {
        HomeShortcutBean bean = items.get(position);
        //设置数据
        holder.setData(bean);
        //渲染数据
        holder.render();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleTextView)
        TextView titleTextView;

        @BindView(R.id.dividerLine)
        ImageView dividerLine;

        private HomeShortcutBean data = null;
        private int position = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setData(HomeShortcutBean data) {
            this.data = data;
        }

        public void render() {
            String title = data.getTitle();
            titleTextView.setText("" + title);
            dividerLine.setVisibility(position == getItemCount() - 1 ? View.INVISIBLE : View.VISIBLE);
        }

        @OnClick(R.id.llRowPanel)
        void onRowPressed() {
            String categoryId = data.getCategoryId();
            String categoryName = data.getTitle();
            if (TextUtils.isEmpty(categoryName)) {
                categoryName = "";
            }
//            String url = data.getUrl();
            if (!TextUtils.isEmpty(categoryId)) {
//                WebViewActivity.jumpUrl(context, 1, "#FF4647", data.getUrl());
                DDCategoryActivity.jumpTo(context, categoryId, categoryName);
            } else {
                DLog.i("data url is null.");
                ToastUtil.error("数据异常");
            }
        }
    }

}
