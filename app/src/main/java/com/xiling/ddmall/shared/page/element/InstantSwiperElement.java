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

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseCallback;
import com.xiling.ddmall.shared.bean.InstantData;
import com.xiling.ddmall.shared.component.CountDown;
import com.xiling.ddmall.shared.decoration.SpacesItemHorizontalDecoration;
import com.xiling.ddmall.shared.page.bean.Element;
import com.xiling.ddmall.shared.service.ProductService;
import com.xiling.ddmall.shared.util.CarshReportUtils;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.EventUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ViewConstructor")
public class InstantSwiperElement extends LinearLayout {

    private CountDown mCountDown;
    private TextView mTitleTv;
    private SwiperAdapter mSwiperAdapter;
    private TextView mMoreBtn;
    private TextView mTvTag;

    public InstantSwiperElement(final Context context, final Element element) {
        super(context);
        try {
            View view = inflate(getContext(), R.layout.el_instant_swiper_layout, this);
            element.setBackgroundInto(view);
            mMoreBtn = (TextView) view.findViewById(R.id.eleMoreTv);
            mTvTag = (TextView) view.findViewById(R.id.tvTag);
//            if (element.hasMore) {
            mMoreBtn.setVisibility(VISIBLE);
//            } else {
//                mMoreBtn.setVisibility(GONE);
//            }

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            mSwiperAdapter = new SwiperAdapter();
            recyclerView.setAdapter(mSwiperAdapter);
            recyclerView.addItemDecoration(new SpacesItemHorizontalDecoration(ConvertUtil.dip2px(10), true));
            mCountDown = (CountDown) view.findViewById(R.id.eleCountDown);
            mTitleTv = (TextView) view.findViewById(R.id.eleTitleTv);
            reloadData();
        } catch (Exception e) {
            CarshReportUtils.post(e);
        }
    }

    private void reloadData() {
        ProductService.getInstantComponentData(new BaseCallback<InstantData>() {
            @Override
            public void callback(final InstantData data) {
                if (data.secondKill == null) {
                    mTitleTv.setText("暂无秒杀");
                    mCountDown.setVisibility(View.GONE);
                    mTvTag.setVisibility(GONE);

                    mMoreBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EventUtil.viewInstant(getContext(), "");
                        }
                    });
                    return;
                }
                mMoreBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventUtil.viewInstant(getContext(), data.secondKill.id);
                    }
                });
                mTitleTv.setText(data.secondKill.title);
                mSwiperAdapter.setItems(data.secondKillProducts);
                mSwiperAdapter.notifyDataSetChanged();
                mCountDown.setTimeLeft(getTimeLeft(data.secondKill.endDate), new CountDown.OnFinishListener() {
                    @Override
                    public void onFinish() {
                        reloadData();
                    }
                });
            }
        });
    }

    private long getTimeLeft(String endTime) {
        if (StringUtils.isEmpty(endTime)) {
            return 0;
        }
        long endTimeMillis = TimeUtils.string2Millis(endTime);
        long startTimeMillis = TimeUtils.getNowTimeDate().getTime();
        return endTimeMillis <= startTimeMillis ? 0 : endTimeMillis - startTimeMillis;
    }


    private class SwiperAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<InstantData.Product> items = new ArrayList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.el_swiper_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final InstantData.Product skuInfo = items.get(position);
            holder.mThumbIv.setImageURI(skuInfo.thumbUrl);
            holder.mTitleTv.setText(skuInfo.skuName);
            holder.mPriceTv.setText(ConvertUtil.centToCurrency(getContext(), (long) skuInfo.retailPrice));
            holder.itemView.setOnClickListener(new OnClickListener() {
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

//        public List<InstantData.Product> getItems() {
//            return items;
//        }

        void setItems(@NonNull List<InstantData.Product> items) {
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
