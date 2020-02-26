package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.xiling.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * pt
 * 新人优惠弹框
 */
public class NewcomerDiscountDialog extends Dialog {
    Context mContext;

    public NewcomerDiscountDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_newcomer_discount);
        ButterKnife.bind(this);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @OnClick({R.id.btn_content, R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_content:
                break;
            case R.id.btn_close:
                dismiss();
                break;
        }
    }
}
