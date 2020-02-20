package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.xiling.R;
import com.xiling.ddui.activity.MyWeekCardPackageActivity;
import com.xiling.ddui.tools.ViewUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 周卡过期
 */
public class WeekBeOverdueDialog extends Dialog {
    Context mContext;

    public WeekBeOverdueDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public WeekBeOverdueDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected WeekBeOverdueDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_week_beoverdue);
        ButterKnife.bind(this);
        setCancelable(false);
    }

    @OnClick({R.id.btn_open, R.id.btn_know})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        switch (view.getId()) {
            case R.id.btn_open:
                Intent intent = new Intent(mContext, MyWeekCardPackageActivity.class);
                mContext.startActivity(intent);
                dismiss();
                break;
            case R.id.btn_know:
                dismiss();
                break;
        }
    }
}
