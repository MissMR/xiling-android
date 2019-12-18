package com.xiling.ddmall.module.store;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.Store;
import com.xiling.ddmall.shared.component.StoreInfoView;
import com.xiling.ddmall.shared.component.dialog.WJDialog;
import com.xiling.ddmall.shared.util.FrescoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreCard extends LinearLayout {

    @BindView(R.id.storeNameTv)
    protected TextView mStoreNameTv;
    @BindView(R.id.storeDescTv)
    protected TextView mStoreDescTv;
    @BindView(R.id.storeLogoIv)
    protected SimpleDraweeView mStoreLogoTv;
    private Store mStore;

    public StoreCard(Context context) {
        super(context);
        initView();
    }

    public StoreCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.merge_store_layout, this);
        ButterKnife.bind(this, view);
    }

    public void setStore(Store store) {
        mStore = store;
        mStoreNameTv.setText(store.storeName);
        mStoreDescTv.setText(String.format("共有 %s 个商品", store.saleProductCount));
        FrescoUtil.setImage(mStoreLogoTv, store.thumbUrl);
    }

    @OnClick(R.id.viewStoreBtn)
    protected void viewStore() {
        final WJDialog wjDialog = new WJDialog(getContext());
        StoreInfoView storeInfoView = new StoreInfoView(getContext());
        wjDialog.show();
        storeInfoView.setCloseClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wjDialog.dismiss();
            }
        });
        storeInfoView.setData(mStore);
        wjDialog.setContentView(storeInfoView);
    }

    @OnClick(R.id.viewStoreProductBtn)
    protected void viewStoreProducts() {
//

    }
}
