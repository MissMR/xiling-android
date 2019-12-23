package com.xiling.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.DDCouponBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by Jigsaw at 2019/6/13
 * 选择优惠券
 */
public class DDCouponSelectorDialog extends Dialog {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Adapter mAdapter;

    private List<DDCouponBean> mDDCouponBeanList;
    private OnCouponSelectListener mOnCouponSelectListener;

    public DDCouponSelectorDialog(@NonNull Context context, List<DDCouponBean> couponBeanList) {
        this(context, R.style.DDMDialog);
        this.mDDCouponBeanList = couponBeanList;
    }

    public DDCouponSelectorDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDCouponSelectorDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_coupon_selector);
        ButterKnife.bind(this);
        initView();
    }

    public DDCouponSelectorDialog setOnCouponSelectListener(OnCouponSelectListener listener) {
        this.mOnCouponSelectListener = listener;
        return this;
    }

    private void initView() {
        initWindow();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.replaceData(mDDCouponBeanList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DDCouponBean couponBean = mAdapter.getItem(position);
                mAdapter.clearDataSelectStatusExcludeThat(couponBean);
                couponBean.setSelect(!couponBean.isSelect());
                mAdapter.notifyDataSetChanged();
                if (mOnCouponSelectListener != null) {
                    mOnCouponSelectListener.onCouponSelected(couponBean.isSelect() ? couponBean : null);
                    dismiss();
                }
            }
        });
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
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        dismiss();
    }

    public static class Adapter extends BaseQuickAdapter<DDCouponBean, BaseViewHolder> {

        public Adapter() {
            super(R.layout.item_coupon_selector);
        }

        @Override
        protected void convert(BaseViewHolder helper, DDCouponBean item) {
            DDCouponView ddCouponView = helper.getView(R.id.dd_coupon_view);
            ddCouponView.showBtnStatus(false);
            ddCouponView.setSelectable(true);
            ddCouponView.setDDCoupon(item);
        }


        public void clearDataSelectStatusExcludeThat(DDCouponBean couponBean) {
            for (DDCouponBean c : getData()) {
                if (c.isSelect() && c != couponBean) {
                    c.setSelect(false);
                }
            }
        }

    }

    public interface OnCouponSelectListener {
        void onCouponSelected(DDCouponBean couponBean);
    }

}
