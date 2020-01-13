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
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.BankListBean;
import com.xiling.ddui.bean.SkuListBean;
import com.xiling.ddui.service.IBankService;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 取消订单
 */
public class BankSelectDialog extends Dialog {
    IBankService iBankService;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Context mContext;
    BankAdaper bankAdaper;

    public void setOnBankSelectListener(OnBankSelectListener onBankSelectListener) {
        this.onBankSelectListener = onBankSelectListener;
    }

    OnBankSelectListener onBankSelectListener;

    public BankSelectDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
        mContext = context;
    }


    public BankSelectDialog(@NonNull Context context, ArrayList<SkuListBean> skuList, String selectId) {
        this(context, R.style.DDMDialog);
        mContext = context;
    }


    public BankSelectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public BankSelectDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bank_select);
        ButterKnife.bind(this);
        iBankService = ServiceManager.getInstance().createService(IBankService.class);
        initView();
        getBankList();
    }


    private void initView() {
        initWindow();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getBankList() {
        APIManager.startRequest(iBankService.getBankList(), new BaseRequestListener<List<BankListBean>>() {
            @Override
            public void onSuccess(final List<BankListBean> result) {
                super.onSuccess(result);
                bankAdaper = new BankAdaper(result);
                recyclerView.setAdapter(bankAdaper);
                bankAdaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if (onBankSelectListener != null) {
                            onBankSelectListener.onBankSelect(result.get(position));
                        }
                        dismiss();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
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

    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }

        dismiss();
    }

    class BankAdaper extends BaseQuickAdapter<BankListBean, BaseViewHolder> {


        public BankAdaper(@Nullable List<BankListBean> data) {
            super(R.layout.item_bank_select, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BankListBean item) {
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getBankLogo());
            helper.setText(R.id.tv_bank_name, item.getBankName());
        }
    }


    public interface OnBankSelectListener {
        void onBankSelect(BankListBean bankListBean);
    }

}
