package com.xiling.ddui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xiling.R;
import com.xiling.ddui.tools.DLog;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Jigsaw at 2018/10/31
 */
public class DDSmartTab extends RadioGroup implements RadioGroup.OnCheckedChangeListener {

    private String TAG = "DDSmartTab";

    private List<String> mTabTextList = new ArrayList<>();
    private OnTabChangedListener mOnTabChangedListener;
    private OnDispatchTouchEventListener mOnDispatchTouchEventListener;


    public DDSmartTab(Context context) {
        super(context);
        initView();
    }

    public DDSmartTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DLog.i(TAG, "onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        DLog.i(TAG, "onCheckedChanged");
        if (mOnTabChangedListener != null) {
            mOnTabChangedListener.onTabChanged(getTabIndexById(checkedId));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        DLog.i(TAG, "dispatchTouchEvent");
        if (mOnDispatchTouchEventListener != null) {
            mOnDispatchTouchEventListener.onDispatchingTouchEvent();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnTabChangedListener(OnTabChangedListener onTabChangedListener) {
        this.mOnTabChangedListener = onTabChangedListener;
    }

    public void setOnDispatchTouchEventListener(OnDispatchTouchEventListener onDispatchTouchEventListener) {
        mOnDispatchTouchEventListener = onDispatchTouchEventListener;
    }

    private void initView() {
        removeAllViews();
        setOrientation(HORIZONTAL);
        setOnCheckedChangeListener(this);
    }

    public boolean checkTab(int tabIndex) {
        if (tabIndex < 0 || tabIndex >= getChildCount()) {
            DLog.w("tabIndex不合法，tabIndex:" + tabIndex + ",childCount:" + getChildCount());
            return false;
        }
        if (getCheckedRadioButtonId() == getChildAt(tabIndex).getId()) {
            return false;
        }
        check(getChildAt(tabIndex).getId());
        return true;

    }

    public void setTabTexts(List<String> list) {
        if (list == null || list.isEmpty()) {
            DLog.w("tab texts 为空");
            return;
        }
        mTabTextList.clear();
        mTabTextList.addAll(list);
        updateTabView();
        checkTab(0);
    }

    private void updateTabView() {
        removeAllViews();
        for (int i = 0; i < mTabTextList.size(); i++) {
            addView(getItemTabView(mTabTextList.get(i)));
        }
    }

    private RadioButton getItemTabView(String tabText) {
        RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.item_dd_smart_tab, this, false);
        radioButton.setText(tabText);
        return radioButton;
    }

    private int getTabIndexById(int tabId) {
        for (int i = 0; i < getChildCount(); i++) {
            if (tabId == getChildAt(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    public interface OnDispatchTouchEventListener {
        void onDispatchingTouchEvent();
    }

    public interface OnTabChangedListener {
        void onTabChanged(int tabIndex);
    }
}
