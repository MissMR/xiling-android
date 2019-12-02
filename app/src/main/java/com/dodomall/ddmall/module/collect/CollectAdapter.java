package com.dodomall.ddmall.module.collect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.DDProductDetailActivity;
import com.dodomall.ddmall.ddui.custom.DDUnLikeDialog;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICollectService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.TextViewUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan on 2017/6/15.
 */

public class CollectAdapter extends BaseAdapter<SkuInfo, CollectAdapter.ViewHolder> {

    private ICollectService mService;

    public CollectAdapter(Context context) {
        super(context);
    }

    @Override
    public CollectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_collect, parent, false));
    }

    @Override
    public void onBindViewHolder(final CollectAdapter.ViewHolder holder, final int position) {
        final SkuInfo skuInfo = items.get(position);
        holder.setCollect(skuInfo);

        holder.itemView.findViewById(R.id.itemTrashBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消喜欢确认对话框
                DDUnLikeDialog unLikeDialog = new DDUnLikeDialog(context);
                unLikeDialog.setCancelLikeEvent(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delCollect(holder.itemView.getContext(), items.get(position).productId, position);
                    }
                });
                unLikeDialog.show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDProductDetailActivity.start(holder.itemView.getContext(), skuInfo.productId);
            }
        });
    }

    private void delCollect(Context context, String skuId, final int position) {
        if (mService == null) {
            mService = ServiceManager.getInstance().createService(ICollectService.class);
        }
        APIManager.startRequest(mService.changeCollect("collect/delCollect", skuId), new BaseRequestListener<PaginationEntity<SkuInfo, Object>>() {
            @Override
            public void onSuccess(PaginationEntity<SkuInfo, Object> result, String msg) {
                items.remove(position);
                notifyDataSetChanged();
                if (TextUtils.isEmpty(msg)) {
                    msg = "取消喜欢成功~";
                }
                ToastUtil.success("" + msg);
            }
        });
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView mItemThumbIv;
        @BindView(R.id.itemTitleTv)
        protected TextView mItemTitleTv;
        @BindView(R.id.itemMoneyTv)
        protected TextView mItemMoneyTv;
        @BindView(R.id.itemMoneyTv2)
        protected TextView mItemMoneyTv2;
        @BindView(R.id.tvCount)
        protected TextView mtvCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            float r = ConvertUtil.dip2px(8);
            GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                    //分别设置左上角、右上角、左下角、右下角的圆角半径
                    .setRoundingParams(RoundingParams.fromCornersRadii(r, r, r, r))
                    //设置淡入淡出动画持续时间(单位：毫秒ms)
//                    .setFadeDuration(500)
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    //构建
                    .build();
            mItemThumbIv.setHierarchy(hierarchy);
        }

        protected void setCollect(final SkuInfo skuInfo) {
            mItemTitleTv.setText(skuInfo.name);
            mItemMoneyTv.setText(String.valueOf(ConvertUtil.centToCurrency(mItemMoneyTv.getContext(), skuInfo)));
//            if (SessionUtil.getInstance().isLogin() && SessionUtil.getInstance().getLoginUser().isShopkeeper()) {
            mItemMoneyTv2.setText(ConvertUtil.centToCurrency(mItemMoneyTv.getContext(), skuInfo.marketPrice));
            TextViewUtil.addThroughLine(mItemMoneyTv2);
//            mItemMoneyTv2.setVisibility(View.VISIBLE);
//            }else {
//                mItemMoneyTv2.setVisibility(View.GONE);
//            }
            mtvCount.setText(skuInfo.likeCount + "人喜欢");

//            FrescoUtil.setImageSmall(mItemThumbIv, skuInfo.thumbURL);
            mItemThumbIv.setImageURI(skuInfo.thumbURL);
        }
    }
}
