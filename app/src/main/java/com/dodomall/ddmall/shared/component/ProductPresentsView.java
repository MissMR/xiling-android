package com.dodomall.ddmall.shared.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.Presents;
import com.dodomall.ddmall.shared.component.dialog.PresentsBottomDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/8.
 */
public class ProductPresentsView extends FrameLayout {

    @BindView(R.id.rvProduct)
    RecyclerView mRvProduct;
    private Context mContext;

    public ProductPresentsView(@NonNull Context context) {
        this(context, null);
    }

    public ProductPresentsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate(context, R.layout.view_product_presents, this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mRvProduct.setLayoutManager(new LinearLayoutManager(mContext));
        mRvProduct.setNestedScrollingEnabled(false);
        setVisibility(GONE);

    }

    public void setData(final List<Presents> presents) {
        if (presents != null && presents.size() > 0) {
            PresentsAdapter presentsAdapter = new PresentsAdapter(presents);
            mRvProduct.setAdapter(presentsAdapter);
            setVisibility(VISIBLE);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PresentsBottomDialog dialog = new PresentsBottomDialog(mContext);
                    dialog.show();
                    dialog.setData(presents);
                }
            });
        }
    }

    class PresentsAdapter extends RecyclerView.Adapter<PresentsViewHoder> {

        private List<Presents> mPresents;

        public PresentsAdapter(List<Presents> presents) {
            mPresents = presents;
        }

        @Override
        public PresentsViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_product_presents, parent,false);
            inflate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PresentsBottomDialog dialog = new PresentsBottomDialog(mContext);
                    dialog.show();
                    dialog.setData(mPresents);
                }
            });
            return new PresentsViewHoder(inflate);
        }

        @Override
        public void onBindViewHolder(PresentsViewHoder holder, int position) {
            holder.setData(mPresents.get(position));
        }


        @Override
        public int getItemCount() {
            return mPresents.size();
        }
    }

    class PresentsViewHoder extends RecyclerView.ViewHolder {

        public PresentsViewHoder(View itemView) {
            super(itemView);
        }

        public void setData(Presents presents) {
            TextView tvName = (TextView) itemView.findViewById(R.id.tvName);
            TextView tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            tvName.setText(presents.skuName);
            tvCount.setText("x" + presents.quantity);
        }
    }
}
