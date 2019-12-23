package com.xiling.ddui.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by Jigsaw at 2019/1/11
 * 社区 喜欢/分享/下载 按钮
 */

public class DDStatusButton extends FrameLayout {

    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_label)
    TextView mTvLabel;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.rl_root)
    RelativeLayout mRlRoot;

    private String mLabel;
    private int mCount;
    private Drawable mIconDrawable;
    private ColorStateList mTextColor;

    public DDStatusButton(@NonNull Context context) {
        super(context);
    }

    public DDStatusButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public DDStatusButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_status_button, this);
        ButterKnife.bind(this);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.DDStatusButton);
        try {
            mLabel = ta.getString(R.styleable.DDStatusButton_label);
            mCount = ta.getInt(R.styleable.DDStatusButton_count, 0);
            mIconDrawable = ta.getDrawable(R.styleable.DDStatusButton_icon);
            mTextColor = ta.getColorStateList(R.styleable.DDStatusButton_textColor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }

        mTvCount.setText(String.valueOf(mCount));

        if (mTextColor != null) {
            mTvCount.setTextColor(mTextColor);
            mTvLabel.setTextColor(mTextColor);
        }

        if (!TextUtils.isEmpty(mLabel)) {
            mTvLabel.setText(mLabel);
        }

        if (mIconDrawable != null) {
            mIvIcon.setImageDrawable(mIconDrawable);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mTvCount.setSelected(selected);
        mTvLabel.setSelected(selected);
    }

    public void setStatusCount(String count) {
        mTvCount.setText(count);
    }


}
