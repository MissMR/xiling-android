package com.xiling.ddui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by Jigsaw at 2019/1/18
 */
public class DDRewardView extends LinearLayout {

    @BindView(R.id.tv_prefix)
    TextView tvPrefix;
    @BindView(R.id.tv_money_prefix)
    TextView tvMoneyPrefix;
    @BindView(R.id.tv_reward)
    TextView tvReward;

    public DDRewardView(Context context) {
        super(context);
        initView();
    }

    public DDRewardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DDRewardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_reward, this, true);
        ButterKnife.bind(this);
    }

    public void setRewardMoney(String money) {
        tvReward.setText(money);
    }
}
