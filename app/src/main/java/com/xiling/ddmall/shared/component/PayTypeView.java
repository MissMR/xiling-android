package com.xiling.ddmall.shared.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.PayTypeModel;
import com.xiling.ddmall.shared.constant.AppTypes;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/1/23.
 */
public class PayTypeView extends FrameLayout {

    @BindView(R.id.ivLogo)
    SimpleDraweeView mIvLogo;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.ivSelect)
    ImageView mIvSelect;

    private boolean mIsSelect;
    private PayTypeModel mModel;
    private ItemClickListener mItemClickListener;

    public PayTypeView(@NonNull Context context) {
        super(context);
        View view = inflate(context, R.layout.view_pay_type, this);
        ButterKnife.bind(this, view);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onSelectItem(getType());
                }
            }
        });
    }

    public void setData(PayTypeModel model, long availableMoney) {
        mModel = model;
        FrescoUtil.setImage(mIvLogo, model.iconUrl);
        if (model.payType == AppTypes.PAY_TYPE.BALANCE) {
            mTvName.setText(String.format("%s（当前可用%.2f）", model.payTypeStr, ConvertUtil.cent2yuan(availableMoney)));
        } else {
            mTvName.setText(model.payTypeStr);
        }
    }

    public int getType() {
        return mModel.payType;
    }

    public void select(boolean isSelect) {
        mIsSelect = isSelect;
        mIvSelect.setSelected(mIsSelect);
    }

    public boolean isSelect() {
        return mIsSelect;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onSelectItem(int payType);
    }
}
