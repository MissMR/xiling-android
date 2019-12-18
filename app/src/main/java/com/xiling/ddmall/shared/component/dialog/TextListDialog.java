package com.xiling.ddmall.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.service.OrderService;
import com.xiling.ddmall.shared.util.CommonUtil;
import com.xiling.ddmall.shared.util.ConvertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/1.
 */
public class TextListDialog extends Dialog {

    @BindView(R.id.layoutList)
    LinearLayout mLayoutList;
    @BindView(R.id.confirmBtn)
    TextView mConfirmBtn;
    private String mCompanyCode;

    public TextListDialog(@NonNull Context context) {
        super(context, R.style.Theme_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_text_list);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.CENTER);
        setCanceledOnTouchOutside(true);
    }

    public void setData(String[] strs) {
        for (String str : strs) {
            mLayoutList.addView(getTextView(str));
        }
    }

    public TextView getTextView(final String str) {
        TextView textView = new TextView(getContext());
        textView.setTextSize(16);
        textView.setTextColor(Color.parseColor("#3333ff"));
        textView.setText(str);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtil.dip2px(30)));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderService.checkExpress(getContext(),str,mCompanyCode);
            }
        });
        return textView;
    }

    @OnClick(R.id.confirmBtn)
    public void onViewClicked() {
        dismiss();
    }


    public void setCompanyCode(String companyCode) {
        mCompanyCode = companyCode;
    }
}
