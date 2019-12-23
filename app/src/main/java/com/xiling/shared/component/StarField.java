package com.xiling.shared.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-07-19
 */
public class StarField extends LinearLayout {

    private String[] mNames = new String[]{"", "非常差", "差", "一般", "好", "非常好"};
    @BindView(R.id.labelTv)
    protected TextView mLabelTv;
    @BindView(R.id.starLayout)
    protected LinearLayout mStarLayout;
    @BindViews({R.id.star0Iv, R.id.star1Iv, R.id.star2Iv, R.id.star3Iv, R.id.star4Iv})
    protected List<ImageView> mStars;
    @BindView(R.id.nameTv)
    protected TextView mNameTv;
    private int mValue = 0;
    private String mLabel;

    public StarField(Context context) {
        this(context, null);
        initViews();
    }

    public StarField(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarField(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StarField);
        mLabel = typedArray.getString(R.styleable.StarField_sf_label);
        mValue = typedArray.getInt(R.styleable.StarField_sf_value, 0);
        typedArray.recycle();
        initViews();
    }

    private void initViews() {
        View view = inflate(getContext(), R.layout.cmp_star, this);
        ButterKnife.bind(this, view);
        setLabel(mLabel);
        setValue(mValue);
    }

    public void setLabel(String label) {
        mLabel = label;
        mLabelTv.setText(label);
    }

    public void setValue(int value) {
        mValue = value;
        if (mValue == 0) {
            changeValue(null);
        } else {
            ImageView imageView = mStars.get(value - 1);
            changeValue(imageView);
        }
    }

    public int getValue() {
        return mValue;
    }

    @OnClick({R.id.star0Iv, R.id.star1Iv, R.id.star2Iv, R.id.star3Iv, R.id.star4Iv})
    protected void changeValue(View view) {
        if (view != null) {
            mValue = mStarLayout.indexOfChild(view) + 1;
            for (int i = 0; i < mStarLayout.getChildCount(); i++) {
                mStarLayout.getChildAt(i).setSelected(i < mValue);
            }
        } else {
            mValue = 0;
        }
        mNameTv.setText(mNames[mValue]);
    }
}
