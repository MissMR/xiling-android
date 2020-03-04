package com.xiling.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.MyClientListBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyClientDetailsDialog extends Dialog {
    MyClientListBean.DataBean clientBean;
    INewUserService iNewUserService;
    Context mContext;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_zhishu)
    TextView tvZhishu;
    @BindView(R.id.tv_user_level1)
    TextView tvUserLevel1;
    @BindView(R.id.tv_user_level2)
    TextView tvUserLevel2;
    @BindView(R.id.tv_user_level3)
    TextView tvUserLevel3;
    @BindView(R.id.tv_role_name)
    TextView tvRoleName;

    public MyClientDetailsDialog(@NonNull Context context, MyClientListBean.DataBean clientBean) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.clientBean = clientBean;
    }

    public MyClientDetailsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_client_details);
        ButterKnife.bind(this);
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        getCustomerDetail();


    }


    private void getCustomerDetail(){
        APIManager.startRequest(iNewUserService.getCustomerDetail(clientBean.getMemberId()), new BaseRequestListener<MyClientListBean.DataBean>() {

            @Override
            public void onSuccess(MyClientListBean.DataBean result) {
                super.onSuccess(result);
                if (result != null){
                    clientBean = result;
                    GlideUtils.loadHead(mContext, ivHead, clientBean.getHeadImage());
                    tvName.setText(clientBean.getMemberName());
                    tvRoleName.setText(clientBean.getRoleName());
                    tvZhishu.setText(NumberHandler.reservedDecimalFor2(clientBean.getMonthlyConsumption()));
                    tvUserLevel1.setText(clientBean.getLevel1Count());
                    tvUserLevel2.setText(clientBean.getLevel2Count());
                    tvUserLevel3.setText(clientBean.getLevel3Count());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }


        });
    }


    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        dismiss();
    }
}
