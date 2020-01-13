package com.xiling.ddui.custom.popupwindow;

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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.CouponBean;
import com.xiling.ddui.bean.SkuListBean;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IOrderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 取消订单
 */
public class CancelOrderDialog extends Dialog {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    Context mContext;
    CancelAdapter cancelAdapter;
    public int selectPostion = 0;
    List<String> reasonList = new ArrayList<>();

    public void setOnReasonSelectListener(OnReasonSelectListener onReasonSelectListener) {
        this.onReasonSelectListener = onReasonSelectListener;
    }

    OnReasonSelectListener onReasonSelectListener;

    public CancelOrderDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
        mContext = context;
    }


    public CancelOrderDialog(@NonNull Context context, ArrayList<SkuListBean> skuList, String selectId) {
        this(context, R.style.DDMDialog);
        mContext = context;
    }


    public CancelOrderDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public CancelOrderDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cancel_order);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        reasonList.add("收获人信息有误");
        reasonList.add("商品数量或款式需要调整");
        reasonList.add("有更优惠的购买方案");
        reasonList.add("商品缺货");
        reasonList.add("我不想买了");
        reasonList.add("其他原因");
        cancelAdapter.setNewData(reasonList);
    }

    private void initView() {
        initWindow();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cancelAdapter = new CancelAdapter();
        recyclerView.setAdapter(cancelAdapter);
        cancelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                selectPostion = position;
                cancelAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.iv_close, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_submit:
                if (onReasonSelectListener != null) {
                    onReasonSelectListener.onReasonSelected(reasonList.get(selectPostion));
                }
                break;
        }

        dismiss();
    }


    public interface OnReasonSelectListener {
        void onReasonSelected(String reason);
    }

    class CancelAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


        public CancelAdapter() {
            super(R.layout.item_cancel_order);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            if (helper.getAdapterPosition() == selectPostion) {
                helper.setBackgroundRes(R.id.iv_select, R.drawable.icon_selected);
            } else {
                helper.setBackgroundRes(R.id.iv_select, R.drawable.icon_unselect);
            }
            helper.setText(R.id.tv_title, item);

        }
    }

}
