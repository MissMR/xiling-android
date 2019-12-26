package com.xiling.ddui.custom.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;


public class ScreeningPopupWindow extends PopupWindow implements View.OnClickListener {
    private Context mContext;

    public ScreeningPopupWindow(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public void setOnScreenListener(ScreeningPopupWindow.onScreenListener onScreenListener) {
        this.onScreenListener = onScreenListener;
    }

    private onScreenListener onScreenListener;

    private TextView tvFree;
    private View btnReset, btnOK;
    private EditText edMin, edMax;

    private boolean isfree = false;

    private String minPrice = "", maxPrice = "";

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_screen, null, false);
        setContentView(view);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ScreenUtils.dip2px(mContext, 300));
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationRight);
        // 设置点击popupwindow外屏幕其它地方消失
        setOutsideTouchable(true);
        setFocusable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(false);
                full(false);
            }
        });


        tvFree = view.findViewById(R.id.tv_free);
        btnReset = view.findViewById(R.id.btn_reset);
        btnOK = view.findViewById(R.id.btn_ok);
        edMin = view.findViewById(R.id.ed_min);
        edMax = view.findViewById(R.id.ed_max);

        tvFree.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnOK.setOnClickListener(this);
    }


    public void showForRight(View view) {
        setWindowAlpha(true);
        full(true);
        showAtLocation(view, Gravity.NO_GRAVITY, ScreenUtils.dip2px(mContext, 300), 0);
    }

    private void setWindowAlpha(boolean isAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        if (!isAlpha) {
            lp.alpha = 1f;
        } else {
            lp.alpha = 0.5f;
        }

        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    private void full(boolean enable) {
        if (enable) {//隐藏状态栏
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            ((Activity) mContext).getWindow().setAttributes(lp);
        } else {//显示状态栏
            WindowManager.LayoutParams attr = ((Activity) mContext).getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity) mContext).getWindow().setAttributes(attr);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_free:
                if (isfree) {
                    isfree = false;
                    tvFree.setBackgroundResource(R.drawable.bg_screen_unselect);
                } else {
                    isfree = true;
                    tvFree.setBackgroundResource(R.drawable.bg_screen_select);
                }

                break;
            case R.id.btn_reset:
                isfree = false;
                tvFree.setBackgroundResource(R.drawable.bg_screen_unselect);
                minPrice = "";
                maxPrice = "";
                edMin.setText("");
                edMax.setText("");
                break;
            case R.id.btn_ok:
                minPrice = edMin.getText().toString();
                maxPrice = edMax.getText().toString();

                if (onScreenListener != null){
                    onScreenListener.onScreenListener(isfree?1:0,minPrice,maxPrice);
                }

                this.dismiss();
                break;
            default:
                break;
        }
    }

    public interface onScreenListener {
        void onScreenListener(int isShippingFree, String minPrice, String maxPrice);
    }

}
