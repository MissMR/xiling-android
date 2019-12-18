package com.xiling.ddmall.shared.page.element;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseCallback;
import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.decoration.SpacesItemHorizontalDecoration;
import com.xiling.ddmall.shared.page.bean.Element;
import com.xiling.ddmall.shared.service.ProductService;
import com.xiling.ddmall.shared.util.CarshReportUtils;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.EventUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ViewConstructor")
public class SwiperElement extends LinearLayout {

    private TextView mMoreBtn;

    public SwiperElement(final Context context, final Element element) {
        super(context);
        View view = inflate(getContext(), R.layout.el_swiper_layout, this);
        element.setBackgroundInto(view);

        mMoreBtn = (TextView) view.findViewById(R.id.eleMoreTv);
        if (element.hasMore && !StringUtils.isEmpty(element.title)) {
            mMoreBtn.setVisibility(VISIBLE);
            mMoreBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        EventUtil.compileEvent(context, element.moreLink.event, element.moreLink.target);
                    } catch (Exception e) {
                        CarshReportUtils.post(e);
                    }
                }
            });
        } else {
            mMoreBtn.setVisibility(GONE);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        if (!StringUtils.isEmpty(element.title)) {
            TextView tvTitle = (TextView) view.findViewById(R.id.eleTitleTv);
            tvTitle.setText(element.title);
        } else {
            LinearLayout layoutTop = (LinearLayout) view.findViewById(R.id.layoutTop);
            layoutTop.setVisibility(GONE);
            recyclerView.setPadding(0, SizeUtils.dp2px(15),0,0);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        final SwiperAdapter swiperAdapter = new SwiperAdapter();
        recyclerView.setAdapter(swiperAdapter);
        recyclerView.addItemDecoration(new SpacesItemHorizontalDecoration(ConvertUtil.dip2px(10), true));

        ArrayList<String> skuIds = ConvertUtil.json2StringList(element.data);
        ProductService.getListBySkuIds(skuIds, new BaseCallback<ArrayList<SkuInfo>>() {
            @Override
            public void callback(ArrayList<SkuInfo> data) {
                swiperAdapter.setItems(data);
                swiperAdapter.notifyDataSetChanged();
            }
        });
    }

    private class SwiperAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<SkuInfo> items = new ArrayList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.el_swiper_item, parent, false));
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final SkuInfo skuInfo = items.get(position);
            FrescoUtil.setImage(holder.mThumbIv, skuInfo.thumb);
            holder.mTitleTv.setText(skuInfo.name);
            holder.mPriceTv.setText(ConvertUtil.centToCurrency(getContext(), skuInfo.retailPrice));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventUtil.viewProductDetail(getContext(), skuInfo.productId);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        void setItems(@NonNull List<SkuInfo> items) {
            this.items = items;
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemThumbIv)
        SimpleDraweeView mThumbIv;
        @BindView(R.id.itemTitleTv)
        TextView mTitleTv;
        @BindView(R.id.itemPriceTv)
        TextView mPriceTv;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
