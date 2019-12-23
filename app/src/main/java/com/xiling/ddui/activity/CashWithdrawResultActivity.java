package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.module.MainActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/12
 * 提现结果页
 */
public class CashWithdrawResultActivity extends BaseActivity {

    @BindView(R.id.tv_data)
    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_withdraw_result);
        ButterKnife.bind(this);
        showHeader("提现");
        tvData.setText(getIntent().getStringExtra(Constants.Extras.DATA));

        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackMasterCenter();
            }
        });
    }

    @OnClick({R.id.tv_btn_withdraw_record, R.id.tv_btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_withdraw_record:
                startActivity(new Intent(this, CashWithdrawRecordActivity.class));
                break;
            case R.id.tv_btn_back:
                goBackMasterCenter();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBackMasterCenter();
    }

    private void goBackMasterCenter() {
        startActivity(new Intent(this, MainActivity.class));
    }

}
