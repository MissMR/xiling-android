package com.xiling.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.ProductAuth;
import com.xiling.shared.util.CommonUtil;
import com.xiling.shared.util.FrescoUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-07-10
 */
public class ProductVerifyDialog extends Dialog {

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecycleView;
    private ProductAuthAdapter mProductAuthAdapter;

    public ProductVerifyDialog(Context context, List<ProductAuth> list) {
        this(context, 0);
        mProductAuthAdapter = new ProductAuthAdapter(context);
        mProductAuthAdapter.setItems(list);
    }

    private ProductVerifyDialog(Context context, int themeResId) {
        super(context, R.style.Theme_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_product_verify);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);

        mRecycleView.setAdapter(mProductAuthAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @OnClick(R.id.bottomBtn)
    protected void onClose() {
        dismiss();
    }

    private class ProductAuthAdapter extends BaseAdapter<ProductAuth, ViewHolder> {
        public ProductAuthAdapter(Context context) {
            super(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(layoutInflater.inflate(R.layout.item_product_auth, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ProductAuth productAuth = items.get(position);
            FrescoUtil.setImage(holder.mThumbIv, productAuth.icon);
            holder.mTitleTv.setText(productAuth.title);
            holder.mDescTv.setText(productAuth.content);
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemThumbIv)
        public SimpleDraweeView mThumbIv;
        @BindView(R.id.itemTitleTv)
        public TextView mTitleTv;
        @BindView(R.id.itemDescTv)
        public TextView mDescTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
