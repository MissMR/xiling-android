package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.adapter.BusinessCategorySelectAdapter;
import com.xiling.ddui.adapter.PlatFormSelectAdapter;
import com.xiling.ddui.bean.PlatformBean;
import com.xiling.ddui.bean.TopCategoryBean;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.contract.IProductService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 账户认证-经营类目选择
 */
public class BusinessCategorySelectPopWindow extends Dialog {


    Context mContext;
    @BindView(R.id.recycler_platform)
    RecyclerView recyclerPlatform;
    BusinessCategorySelectAdapter businessCategorySelectAdapter;
    IProductService iProductService;
    List<TopCategoryBean> selectList = new ArrayList<>();
    SelectListener platformSelectListener;


    public void setSelectListener(SelectListener platformSelectListener) {
        this.platformSelectListener = platformSelectListener;
    }


    public BusinessCategorySelectPopWindow(@NonNull Context context, List<TopCategoryBean> storeIdList) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.selectList = storeIdList;
    }


    public BusinessCategorySelectPopWindow(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public BusinessCategorySelectPopWindow(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_platform_select);
        ButterKnife.bind(this);
        iProductService = ServiceManager.getInstance().createService(IProductService.class);
        initView();
        getTopCategory();
    }

    /**
     * 获取一级分类列表（左侧）
     */
    private void getTopCategory() {
        APIManager.startRequest(iProductService.getTopCategory(), new BaseRequestListener<ArrayList<TopCategoryBean>>(mContext) {
            @Override
            public void onSuccess(ArrayList<TopCategoryBean> result) {
                super.onSuccess(result);
                if (selectList.size() > 0) {
                    for (TopCategoryBean platformBean : result) {
                        if (selectList.contains(platformBean)) {
                            platformBean.setSelect(true);
                        }
                    }
                }
                businessCategorySelectAdapter.setNewData(result);

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void initView() {
        initWindow();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        recyclerPlatform.setLayoutManager(gridLayoutManager);
        recyclerPlatform.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(mContext, 10), ScreenUtils.dip2px(mContext, 10)));
        businessCategorySelectAdapter = new BusinessCategorySelectAdapter();
        recyclerPlatform.setAdapter(businessCategorySelectAdapter);

        businessCategorySelectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TopCategoryBean topCategoryBean = businessCategorySelectAdapter.getItem(position);
                if (topCategoryBean.isSelect()) {
                    topCategoryBean.setSelect(false);
                    selectList.remove(topCategoryBean);
                } else {
                    topCategoryBean.setSelect(true);
                    selectList.add(topCategoryBean);
                }
                businessCategorySelectAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.btn_ok, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                dismiss();
                if (platformSelectListener != null) {
                    platformSelectListener.onPlatformSelect(selectList);
                }
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    public interface SelectListener {
        void onPlatformSelect(List<TopCategoryBean> topCategoryBeanList);
    }


}
