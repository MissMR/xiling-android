package com.xiling.ddui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.custom.popupwindow.RechargeDialog;
import com.xiling.shared.basic.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author pt
 * 财务管理
 */
public class XLFinanceManangerActivity extends BaseActivity {

    @BindView(R.id.tv_balace)
    TextView tvBalace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlfinance_mananger);
        ButterKnife.bind(this);
        setTitle("财务管理");
        setLeftBlack();
    }

    @OnClick({R.id.btn_detailed, R.id.btn_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_detailed:

                break;
            case R.id.btn_recharge:
                new RechargeDialog(this).show();
                break;
        }
    }
}
