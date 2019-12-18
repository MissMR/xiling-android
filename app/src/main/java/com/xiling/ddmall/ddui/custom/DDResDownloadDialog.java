package com.xiling.ddmall.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.tools.AppTools;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.xiling.ddmall.shared.util.WechatUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDResDownloadDialog extends Dialog {

    Context activity = null;

    public DDResDownloadDialog(@NonNull Context context) {
        super(context, R.style.WXShareDialog);
        this.activity = context;
    }

    public DDResDownloadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDResDownloadDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @BindView(R.id.textCopyLayout)
    LinearLayout textCopyLayout;

    @BindView(R.id.resCopyStatusLayout)
    LinearLayout resCopyStatusLayout;

    @BindView(R.id.resImageView)
    ImageView resImageView;

    @BindView(R.id.resTextView)
    TextView resTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_res_download);
        ButterKnife.bind(this);
        initWindow();
        setCanceledOnTouchOutside(true);

        if (mode == DownloadMode.Image) {
            resTextView.setText("图片已保存到相册");
            resCopyStatusLayout.setVisibility(View.VISIBLE);
        } else if (mode == DownloadMode.Video) {
            if (status) {
                resTextView.setText("视频已保存到相册");
                resImageView.setBackgroundResource(R.mipmap.icon_status_ok);
            } else {
                resTextView.setText("因版权原因，视频不允许下载");
                resImageView.setBackgroundResource(R.mipmap.icon_status_fail);
            }
            resCopyStatusLayout.setVisibility(View.VISIBLE);
        } else {
            resCopyStatusLayout.setVisibility(View.GONE);
        }
    }

    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.y = ConvertUtil.dip2px(10); //设置居于底部的距离
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }

    @OnClick({R.id.closeImageView, R.id.emptyView})
    protected void onClosePressed() {
        dismiss();
    }

    public enum DownloadMode {
        Image,
        Video,
        Text
    }

    DownloadMode mode = DownloadMode.Text;
    boolean status = false;

    /**
     * 设置文案和提示语
     */
    public void setMode(DownloadMode mode, boolean status) {
        this.mode = mode;
        this.status = status;
    }

    @Override
    public void show() {
        super.show();
    }

    @OnClick(R.id.startWeChat)
    protected void onStartWeChatPressed() {
        if (WechatUtil.isWeChatAppInstalled(activity)) {
            AppTools.startMicroMsgApp(activity);
            dismiss();
        } else {
            ToastUtil.error("请先安装微信客户端");
        }
    }

}
