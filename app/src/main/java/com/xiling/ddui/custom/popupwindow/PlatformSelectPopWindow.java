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
import com.xiling.ddui.adapter.EditAdapter;
import com.xiling.ddui.adapter.KeyAdapter;
import com.xiling.ddui.adapter.PlatFormSelectAdapter;
import com.xiling.ddui.bean.PlatformBean;
import com.xiling.ddui.bean.RealAuthBean;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.zhihu.matisse.internal.utils.Platform;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 账户认证-平台选择
 */
public class PlatformSelectPopWindow extends Dialog {


    Context mContext;
    @BindView(R.id.recycler_platform)
    RecyclerView recyclerPlatform;
    PlatFormSelectAdapter platFormSelectAdapter;
    INewUserService mUserService;
    List<PlatformBean> selectList = new ArrayList<>();
    PlatformSelectListener platformSelectListener;

    public void setPlatformSelectListener(PlatformSelectListener platformSelectListener) {
        this.platformSelectListener = platformSelectListener;
    }


    public PlatformSelectPopWindow(@NonNull Context context, List<PlatformBean> storeIdList) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.selectList = storeIdList;
    }


    public PlatformSelectPopWindow(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public PlatformSelectPopWindow(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_platform_select);
        ButterKnife.bind(this);
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        initView();
        requestData();
    }

    private void requestData() {
        APIManager.startRequest(mUserService.getAllStore(), new BaseRequestListener<List<PlatformBean>>() {
            @Override
            public void onSuccess(List<PlatformBean> result) {
                super.onSuccess(result);
                if (selectList.size() > 0) {
                    for (PlatformBean platformBean : result) {
                        if (selectList.contains(platformBean)) {
                            platformBean.setSelect(true);
                        }
                    }
                }
                platFormSelectAdapter.setNewData(result);
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
        platFormSelectAdapter = new PlatFormSelectAdapter();
        recyclerPlatform.setAdapter(platFormSelectAdapter);

        platFormSelectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PlatformBean platformBean = platFormSelectAdapter.getItem(position);
                if (platformBean.isSelect()) {
                    platformBean.setSelect(false);
                    selectList.remove(platformBean);
                } else {
                    platformBean.setSelect(true);
                    selectList.add(platformBean);
                }
                platFormSelectAdapter.notifyDataSetChanged();
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

    public interface PlatformSelectListener {
        void onPlatformSelect(List<PlatformBean> platformBeanList);
    }


}
