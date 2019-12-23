package com.xiling.ddui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;

import java.util.List;

/**
 * created by Jigsaw at 2018/12/20
 */
public class DDTopMenuPopupWindow<T extends DDTopMenuPopupWindow.MenuItemText> extends PopupWindow {
    private Context mContext;
    private OnItemClickListener<T> mOnItemClickListener;
    private BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    public DDTopMenuPopupWindow(Context context, List<T> list) {
        super(context);
        mContext = context;
        init();
        mAdapter.addData(list);
    }


    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_window_top, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0));//new ColorDrawable(0)即为透明背景
        setContentView(view);

        view.findViewById(R.id.fl_bg_alpha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = getContentView().findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_popup_window_top_menu) {
            @Override
            protected void convert(BaseViewHolder helper, T item) {
                helper.setText(R.id.tv_item, item.getMenuItemText());
            }

        };
        recyclerView.setAdapter(mAdapter);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        if (mAdapter.getOnItemClickListener() == null) {
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    mOnItemClickListener.onItemClick(view, mAdapter.getItem(position), position);
                    dismiss();
                }
            });
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            // 7.0 以上 适配
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T item, int position);
    }

    public static abstract class MenuItemText {
        public abstract String getMenuItemText();
    }
}
