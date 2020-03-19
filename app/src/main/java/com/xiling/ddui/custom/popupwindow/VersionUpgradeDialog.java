package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.DDUpgradeBean;
import com.xiling.ddui.tools.UITools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * pt
 * 版本升级
 */
public class VersionUpgradeDialog extends Dialog {
    Context mContext;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_message)
    TextView tvMessage;

    DDUpgradeBean upgradeBean;
    @BindView(R.id.btn_close)
    ImageView btnClose;

    public VersionUpgradeDialog(@NonNull Context context, DDUpgradeBean upgradeBean) {
        super(context);
        mContext = context;
        this.upgradeBean = upgradeBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_version_upgrade);
        ButterKnife.bind(this);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (upgradeBean != null) {
            tvTitle.setText("喜领商城V" + upgradeBean.getVersion());
            tvMessage.setText(upgradeBean.getMsg());
            btnClose.setVisibility(upgradeBean.getUpgradeStatus() == 2 ? View.GONE : View.VISIBLE);
        }


    }

    @OnClick({R.id.btn_upgrade, R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_upgrade:
                if (upgradeBean != null) {
                    UITools.jumpSystemBrowser(mContext, upgradeBean.getUpUrl());
                }
                break;
            case R.id.btn_close:
                dismiss();
                break;
        }
    }
}
