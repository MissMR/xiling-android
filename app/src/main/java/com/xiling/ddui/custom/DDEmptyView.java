package com.xiling.ddui.custom;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;

/**
 * created by Jigsaw at 2019/3/25
 */
public class DDEmptyView extends LinearLayout {

    private ImageView mIvEmpty;
    private TextView mTvEmptyDesc;
    private TextView mTvBtnEmpty;

    private OnClickListener mOnEmptyBtnClickListener;

    public DDEmptyView(Context context) {
        super(context);
        init();
    }

    public DDEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DDEmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.dd_empty_view, this);
        mIvEmpty = findViewById(R.id.iv_empty);
        mTvEmptyDesc = findViewById(R.id.tv_empty_desc);
        mTvBtnEmpty = findViewById(R.id.tv_empty_btn);

        mTvBtnEmpty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEmptyBtnClickListener != null) {
                    mOnEmptyBtnClickListener.onClick(v);
                }
            }
        });

    }

    public void setEmptyImage(@DrawableRes int drawableRes) {
        mIvEmpty.setImageResource(drawableRes);
    }

    public void setEmptyDesc(String emptyDesc) {
        if (TextUtils.isEmpty(emptyDesc)) {
            mTvEmptyDesc.setVisibility(GONE);
            return;
        }

        mTvEmptyDesc.setVisibility(VISIBLE);
        mTvEmptyDesc.setText(emptyDesc);
    }

    public void setEmptyBtn(String text) {
        if (TextUtils.isEmpty(text)) {
            mTvBtnEmpty.setVisibility(GONE);
            return;
        }
        mTvBtnEmpty.setVisibility(VISIBLE);
        mTvBtnEmpty.setText(text);
    }

    public void setEmptyBtnEnable(boolean enable) {
        mTvBtnEmpty.setEnabled(enable);
    }

    public void setOnEmptyBtnClickListener(OnClickListener listener) {
        this.mOnEmptyBtnClickListener = listener;
    }


}
