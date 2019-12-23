package com.xiling.shared.component.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiling.R;
import com.xiling.module.product.adapter.PresentAdapter;
import com.xiling.shared.bean.Presents;
import com.xiling.shared.util.RvUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/30.
 */
public class PresentsBottomDialog extends BottomSheetDialog {

    @BindView(R.id.rvList)
    RecyclerView mRvList;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;

    private Context mContext;
    private ArrayList<Presents> mSkuInfos;
    private PresentAdapter mPresentAdapter;

    public PresentsBottomDialog(@NonNull Context context) {
        super(context);
        mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_presents_bottom);
        getWindow().setLayout(ScreenUtils.getScreenWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mSkuInfos = new ArrayList<>();
        mPresentAdapter = new PresentAdapter(mSkuInfos);
        RvUtils.configRecycleView(mContext, mRvList, mPresentAdapter);
        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                EventUtil.viewProductDetail(mContext, mSkuInfos.get(position).skuId);
            }
        });
    }

    @OnClick(R.id.tvSubmit)
    public void onViewClicked() {
        dismiss();
    }

    public void setData(List<Presents> data) {
        mSkuInfos.clear();
        if (data != null) {
            mSkuInfos.addAll(data);
            mPresentAdapter.notifyDataSetChanged();
        }
    }
}
