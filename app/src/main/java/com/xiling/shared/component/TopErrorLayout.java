package com.xiling.shared.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;

/**
 * 顶部错误组件
 * Created by JayChan on 2016/10/13.
 */
public class TopErrorLayout extends LinearLayout implements View.OnClickListener {

    private TextView mErrorTextTv;
    private ImageView mCloseBtn;

    public TopErrorLayout(Context context) {
        super(context);
        initViews();
    }

    public TopErrorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopErrorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.top_error_layout, (ViewGroup) getRootView(), true);
        getRootView().setBackgroundResource(R.drawable.base_error_background);
        mErrorTextTv = (TextView) findViewById(R.id.baseErrorTextTv);
        mCloseBtn = (ImageView) findViewById(R.id.baseErrorCloseBtnIv);
        mCloseBtn.setOnClickListener(this);
    }

    public void setErrorMessage(String message) {
        mErrorTextTv.setText(String.format("错误提示：%s", message));
    }

    @Override
    public void onClick(View v) {
        setVisibility(GONE);
    }
}
