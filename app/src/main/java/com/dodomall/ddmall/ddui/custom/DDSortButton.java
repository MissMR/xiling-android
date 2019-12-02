package com.dodomall.ddmall.ddui.custom;

import android.graphics.Color;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dodomall.ddmall.R;

public class DDSortButton implements View.OnClickListener {

    public interface DDSortStatusListener {
        void onDDSortStatusChanged(SortStatus status);
    }

    public enum SortStatus {
        /*未选择*/
        UnSelect,
        /*由低到高*/
        DownToUp,
        /*由高到低*/
        UpToDown
    }

    DDSortStatusListener sortStatusListener = null;

    public void setSortStatusListener(DDSortStatusListener sortStatusListener) {
        this.sortStatusListener = sortStatusListener;
    }

    View parentView = null;
    TextView statusTextView = null;
    ImageView statusImageView = null;

    public DDSortButton(View view) {
        parentView = view;
        initView();
    }

    private void initView() {
        statusTextView = parentView.findViewById(R.id.sortTitleView);
        statusImageView = parentView.findViewById(R.id.sortStatusView);
        statusImageView.setImageResource(R.mipmap.icon_price_unselect);
        parentView.setOnClickListener(this);
    }

    SortStatus status = SortStatus.UnSelect;

    @Override
    public void onClick(View v) {
        switch (status) {
            case UnSelect:
                status = SortStatus.DownToUp;
                statusImageView.setImageResource(R.mipmap.icon_price_up);
                break;
            case DownToUp:
                status = SortStatus.UpToDown;
                statusImageView.setImageResource(R.mipmap.icon_price_down);
                break;
            case UpToDown:
                status = SortStatus.DownToUp;
                statusImageView.setImageResource(R.mipmap.icon_price_up);
                break;
        }
        statusTextView.setTextColor(Color.parseColor("#FF4647"));
        onSelect();
    }

    private void onSelect() {
        callback();
    }

    public void unSelect() {
        status = SortStatus.UnSelect;
        statusImageView.setImageResource(R.mipmap.icon_price_unselect);
        statusTextView.setTextColor(Color.parseColor("#333333"));
        if (status != SortStatus.UnSelect) {
            callback();
        }
    }

    public void downToUp(boolean isCallback) {
        status = SortStatus.DownToUp;
        statusImageView.setImageResource(R.mipmap.icon_price_up);
        statusTextView.setTextColor(Color.parseColor("#FF4647"));
        if (status != SortStatus.UnSelect && isCallback) {
            callback();
        }
    }

    public void upToDown(boolean isCallback) {
        status = SortStatus.UpToDown;
        statusImageView.setImageResource(R.mipmap.icon_price_down);
        statusTextView.setTextColor(Color.parseColor("#FF4647"));
        if (status != SortStatus.UnSelect && isCallback) {
            callback();
        }
    }

    private void callback() {
        if (sortStatusListener != null) {
            sortStatusListener.onDDSortStatusChanged(status);
        }
    }
}
