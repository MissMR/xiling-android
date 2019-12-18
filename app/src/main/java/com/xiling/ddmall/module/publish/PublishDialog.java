package com.xiling.ddmall.module.publish;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.util.CommonUtil;


/**
 * @author Stone
 * @time 2018/4/12  11:18
 * @desc ${TODD}
 */

public class PublishDialog extends Dialog {
    private onClickCallBack callBack;

    public PublishDialog(@NonNull Context context) {
        super(context, R.style.Theme_Light_Dialog);
    }

    public PublishDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PublishDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_publish_new);
        setCanceledOnTouchOutside(false);
        CommonUtil.initDialogWindow(getWindow(), Gravity.BOTTOM);
        findViewById(R.id.iv_cancel_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.take_pic_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(callBack!=null) {
                    callBack.onTakePic();
                }
            }
        });
        findViewById(R.id.take_video_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(callBack!=null) {
                    callBack.onTakeVideo();
                }
            }
        });
        findViewById(R.id.publish_history_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(callBack!=null) {
                    callBack.onHistoryClick();
                }
            }
        });
    }
    public void setOnClickCallBack(onClickCallBack callBack){
        this.callBack=callBack;
    }

    public interface onClickCallBack{
        void onTakePic();
        void onTakeVideo();
        void onHistoryClick();
    }
}
