package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.adapter.CouponAdapter;
import com.xiling.ddui.bean.CouponBean;
import com.xiling.ddui.bean.DDCouponBean;
import com.xiling.ddui.bean.SkuListBean;
import com.xiling.ddui.custom.DDCouponView;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICouponService;

import java.util.ArrayList;
import java.util.HashMap;
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

    private CouponAdapter mAdapter;

    private List<CouponBean> mCouponBeanList;
    private OnCouponSelectListener mOnCouponSelectListener;
    ArrayList<SkuListBean> skuList;
    ICouponService mCouponService;
    Context mContext;

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

    String selectId = "";

    public CouponSelectorDialog(@NonNull Context context, ArrayList<SkuListBean> skuList) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.skuList = skuList;
    }


    public CouponSelectorDialog(@NonNull Context context, ArrayList<SkuListBean> skuList, String selectId) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.skuList = skuList;
        this.selectId = selectId;
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
        mCouponService = ServiceManager.getInstance().createService(ICouponService.class);
        initView();
        getCouponList();
    }

    public CouponSelectorDialog setOnCouponSelectListener(OnCouponSelectListener listener) {
        this.mOnCouponSelectListener = listener;
        return this;
    }

    private void initView() {
        initWindow();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CouponAdapter();
        if (TextUtils.isEmpty(selectId)) {
            mAdapter.setSelectId(selectId);
        }
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setSelectId(mCouponBeanList.get(position).getId());
                if (mOnCouponSelectListener != null) {
                    mOnCouponSelectListener.onCouponSelected(mCouponBeanList.get(position));
                }
                dismiss();
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


    public interface OnCouponSelectListener {
        void onCouponSelected(CouponBean couponBean);
    }

    private void getCouponList() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("products", skuList);

        APIManager.startRequest(mCouponService.getCouponList(APIManager.buildJsonBody(params)), new BaseRequestListener<List<CouponBean>>(mContext) {
            @Override
            public void onSuccess(List<CouponBean> result) {
                mCouponBeanList = result;
                mAdapter.replaceData(mCouponBeanList);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

}
