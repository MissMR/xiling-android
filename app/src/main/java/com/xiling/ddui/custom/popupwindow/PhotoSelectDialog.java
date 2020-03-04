package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xiling.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoSelectDialog extends Dialog {

    OnPhotoSelectListener onPhotoSelectListener;


    public PhotoSelectDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_photo_select);
        ButterKnife.bind(this);
        initView();
    }

    public void setOnPhotoSelectListener(OnPhotoSelectListener onPhotoSelectListener) {
        this.onPhotoSelectListener = onPhotoSelectListener;
    }

    private void initView() {
        initWindow();
    }

    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @OnClick({R.id.btn_photo, R.id.btn_album, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_photo:
                dismiss();
                if (onPhotoSelectListener != null) {
                    onPhotoSelectListener.onPhoto();
                }
                break;
            case R.id.btn_album:
                dismiss();
                if (onPhotoSelectListener != null) {
                    onPhotoSelectListener.onAlbum();
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    public interface OnPhotoSelectListener {
        void onPhoto();

        void onAlbum();
    }

}
