package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiling.R;
import com.xiling.ddui.adapter.CouponAdapter;
import com.xiling.ddui.bean.CouponBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 选择优惠券
 */
public class CouponSelectorDialog extends Dialog {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private CouponAdapter mAdapter;

    private List<CouponBean> mCouponBeanList;
    private OnCouponSelectListener mOnCouponSelectListener;
    CouponBean mCouponBean;
    Context mContext;

    public void setSelect(CouponBean couponBean) {
        this.mCouponBean = couponBean;
    }

    public CouponSelectorDialog(@NonNull Context context, List<CouponBean> mCouponBeanList) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.mCouponBeanList = mCouponBeanList;
    }


    public CouponSelectorDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public CouponSelectorDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_coupon_selector);
        ButterKnife.bind(this);

        initView();

    }

    public CouponSelectorDialog setOnCouponSelectListener(OnCouponSelectListener listener) {
        this.mOnCouponSelectListener = listener;
        return this;
    }

    private void initView() {
        initWindow();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CouponAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!mCouponBeanList.get(position).getId().equals(mAdapter.getSelectId())){
                    mAdapter.setSelectId(mCouponBeanList.get(position).getId());
                    mCouponBean = mCouponBeanList.get(position);
                }else{
                    mAdapter.setSelectId("");
                    mCouponBean = null;
                }

            }
        });
        mAdapter.replaceData(mCouponBeanList);
        if (mCouponBeanList.size() > 0) {

            if (mCouponBean != null) {
                mAdapter.setSelectId(mCouponBean.getId());
            } else {
                mCouponBean = mCouponBeanList.get(0);
                mAdapter.setSelectId(mCouponBean.getId());
            }
            tvTitle.setText("可用优惠券(" + mCouponBeanList.size() + ")");
        } else {
            tvTitle.setText("可用优惠券");
        }


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

    @OnClick({R.id.iv_close, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_ok:
                if (mOnCouponSelectListener != null) {
                    if (mCouponBean != null) {
                        mOnCouponSelectListener.onCouponSelected(mCouponBean);
                    } else {
                        mOnCouponSelectListener.onCancel();
                    }
                }
                dismiss();
                break;
        }

    }

    public interface OnCouponSelectListener {
        void onCouponSelected(CouponBean couponBean);

        void onCancel();
    }

}
