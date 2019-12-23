package com.xiling.shared.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-06-07
 */
public class NoData extends LinearLayout {

    private ImageView mImageView;
    private TextView mTextView;
    private TextView mReloadView;

    public NoData(Context context) {
        super(context);
        initView();
    }

    public NoData(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NoData(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        inflate(getContext(), R.layout.cmp_no_data, this);
        mImageView = findViewById(R.id.noDataIcon);
        mTextView = findViewById(R.id.noDataLabel);
        mReloadView = findViewById(R.id.tvReRefresh);
    }

    public void interceptClick(boolean status) {
        if (status) {
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } else {
            this.setOnClickListener(null);
        }
    }

    public NoData setImgRes(int resId) {
        mImageView.setImageResource(resId);
        return this;
    }

    public NoData setTextView(String str) {
        mTextView.setText(str);
        return this;
    }

    public NoData setReload(String text, View.OnClickListener listener) {
        mReloadView.setText("" + text);
        mReloadView.setOnClickListener(listener);
        mReloadView.setVisibility(View.VISIBLE);
        return this;
    }

}
