package com.xiling.shared.page.element;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Strings;
import com.xiling.R;
import com.xiling.shared.page.bean.BasicData;
import com.xiling.shared.page.bean.Element;
import com.xiling.shared.util.CarshReportUtils;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.EventUtil;
import com.xiling.shared.util.FrescoUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinkElement extends LinearLayout {

    private Element mElement;

    public LinkElement(Context context, Element element) {
        super(context);
        try {
            this.mElement = element;
            View view = inflate(getContext(), R.layout.el_links_layout, this);
            element.setBackgroundInto(view);
            RecyclerView listRv = (RecyclerView) view.findViewById(R.id.eleListRv);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, element.columns);
            gridLayoutManager.setAutoMeasureEnabled(true);
            gridLayoutManager.setSmoothScrollbarEnabled(false);
            listRv.setLayoutManager(gridLayoutManager);
            int padding = ConvertUtil.dip2px(5f);
            listRv.setPadding(0, padding, 0, padding);
            listRv.setNestedScrollingEnabled(false);
            listRv.setScrollContainer(false);
            listRv.setAdapter(new ProductAdapter(ConvertUtil.json2DataList(element.data)));
        } catch (Exception e) {
            CarshReportUtils.post(e);
        }
    }


    private class ProductAdapter extends RecyclerView.Adapter<LinkViewHolder> {

        private final ArrayList<BasicData> items;

        ProductAdapter(ArrayList<BasicData> items) {
            this.items = items;
        }

        @Override
        public LinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LinkViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.el_link_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(LinkViewHolder holder, int position) {
            final BasicData data = items.get(position);
            holder.itemLabelTv.setText(data.label);
            if (!Strings.isNullOrEmpty(mElement.labelColor)) {
                holder.itemLabelTv.setTextColor(Color.parseColor(mElement.labelColor));
            }
            FrescoUtil.setImage(holder.itemThumbIv, data.icon);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventUtil.compileEvent(getContext(), data.event, data.target);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    class LinkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.eleLabelTv)
        protected TextView itemLabelTv;
        @BindView(R.id.eleIconIv)
        protected SimpleDraweeView itemThumbIv;


        LinkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
