package com.xiling.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.tools.AppTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDPermissionDialog extends Dialog {

    public DDPermissionDialog(@NonNull Context context) {
//        super(context);
//        super(context, R.style.DDMDialog);
        super(context, R.style.WXShareDialog);
    }

    public DDPermissionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDPermissionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.TOP);
            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.y = ConvertUtil.dip2px(10); //设置居于底部的距离
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvContent)
    TextView tvContent;

    @BindView(R.id.btn_jump_settings)
    Button jumpSettingsBtn;

    @OnClick(R.id.btn_jump_settings)
    void onJumpSettingPressed() {
        AppTools.jumpToAppSettings(getContext());
        dismiss();
    }

    @BindView(R.id.closeImageView)
    ImageView closeImageView;

    @OnClick(R.id.closeImageView)
    void onClosePressed() {
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip_permission);
        ButterKnife.bind(this);
        initWindow();
    }

    private String title = "开启消息推送";
    private String desc = "不再错过你的特别关心的重要通知，\n每天都有干货推荐";
    private String jumpText = "去开启";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getJumpText() {
        return jumpText;
    }

    public void setJumpText(String jumpText) {
        this.jumpText = jumpText;
    }
}
