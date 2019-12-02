package com.dodomall.ddmall.shared.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blankj.utilcode.utils.ConvertUtils;
import com.dodomall.ddmall.R;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/1/20.
 */
public class PopupWindowManage {
    private static PopupWindowManage sInstance;
    private PopupWindow mPopupWindow;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private View mYinYing;
//    private int mBgRes = R.drawable.bg_shopping_spinner_bottom_selected;

    public static PopupWindowManage getInstance(Context context) {
        sInstance = new PopupWindowManage(context);
        return sInstance;
    }

    public void setWindowBackground(int res) {
//        this.mBgRes = res;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private PopupWindowManage(Context context) {
        mContext = context;
        initWindow();
    }

    private void initWindow() {
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_common_white_normal));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
    }

    public void showWindow(final View anchor, View windowView) {
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mYinYing.setVisibility(View.GONE);
                anchor.setSelected(false);
            }
        });
        mPopupWindow.setWidth(anchor.getWidth());
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        mPopupWindow.setContentView(windowView);
        mPopupWindow.showAsDropDown(anchor);
        anchor.setSelected(true);
        mYinYing.setVisibility(View.VISIBLE);

//        mPopupWindow.setFocusable(false);
    }

    public void showListWindow(View anchor, RecyclerView.Adapter adapter) {
        if (mRecyclerView == null) {
            mRecyclerView = new RecyclerView(mContext);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, ConvertUtils.dp2px(200f));
            mRecyclerView.setLayoutParams(layoutParams);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }
        mRecyclerView.setAdapter(adapter);
        showWindow(anchor, mRecyclerView);
    }

    public boolean isShowing(){
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public void setYinYing(View yinYing) {
        mYinYing = yinYing;
    }
}
