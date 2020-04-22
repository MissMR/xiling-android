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
import android.widget.LinearLayout;

import com.xiling.R;
import com.xiling.ddui.adapter.SpuParameterAdapter;
import com.xiling.ddui.bean.ProductParameterBean;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 商品详情-商品参数
 */
public class SpuParameterPopupWindow extends Dialog {


    Context mContext;
    @BindView(R.id.recycler_platform)
    RecyclerView recyclerPlatform;

    IProductService mProductService;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    private String spuId;
    SpuParameterAdapter spuParameterAdapter;

    public SpuParameterPopupWindow(@NonNull Context context, String spuId) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.spuId = spuId;
    }


    public SpuParameterPopupWindow(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public SpuParameterPopupWindow(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_spu_parameter);
        ButterKnife.bind(this);
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
        initView();
        requestData();
    }

    private void requestData() {
        APIManager.startRequest(mProductService.getSpuParameter(spuId), new BaseRequestListener<List<ProductParameterBean>>() {
            @Override
            public void onSuccess(List<ProductParameterBean> result) {
                super.onSuccess(result);
                if (result != null && result.size() > 0) {
                    recyclerPlatform.setVisibility(View.VISIBLE);
                    llEmpty.setVisibility(View.GONE);
                    spuParameterAdapter.setNewData(result);
                } else {
                    recyclerPlatform.setVisibility(View.GONE);
                    llEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                recyclerPlatform.setVisibility(View.GONE);
                llEmpty.setVisibility(View.VISIBLE);
            }
        });
    }


    private void initView() {
        initWindow();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerPlatform.setLayoutManager(linearLayoutManager);
        spuParameterAdapter = new SpuParameterAdapter();
        recyclerPlatform.setAdapter(spuParameterAdapter);
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

    @OnClick({R.id.btn_ok, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
}
