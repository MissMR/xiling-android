package com.xiling.ddui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.AccountManagerBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.user.LoginActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.net.HttpRequest;

/**
 * @auth 逄涛
 * 账户管理
 */
public class XLAccountManagerActivity extends BaseActivity {

    INewUserService newUserService;

    @BindView(R.id.recycler_account)
    RecyclerView recyclerAccount;
    AccountManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlaccount_manager);
        ButterKnife.bind(this);
        setTitle("账户管理");
        setLeftBlack();
       /* showHeaderRightText("管理", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        newUserService = ServiceManager.getInstance().createService(INewUserService.class);
        initView();
    }

    private void initView() {
        recyclerAccount.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AccountManagerAdapter();
        recyclerAccount.setAdapter(adapter);
        getAccountList();
    }

    private void getAccountList() {
        APIManager.startRequest(newUserService.getAcountList(), new BaseRequestListener<List<AccountManagerBean>>() {

            @Override
            public void onSuccess(List<AccountManagerBean> result) {
                super.onSuccess(result);
                if (result != null) {
                    adapter.setNewData(result);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }

        });
    }

    @OnClick({R.id.btn_account_out, R.id.btn_account_add})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        switch (view.getId()) {
            case R.id.btn_account_out:
                D3ialogTools.showAlertDialog(context, "确定退出当前账户?", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserManager.getInstance().loginOut(context);
                        startActivity(new Intent(context, LoginActivity.class));
                        finish();
                    }
                }, "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
            case R.id.btn_account_add:
                break;
        }
    }

    class AccountManagerAdapter extends BaseQuickAdapter<AccountManagerBean, BaseViewHolder> {

        public AccountManagerAdapter() {
            super(R.layout.item_account_manager);
        }

        @Override
        protected void convert(BaseViewHolder helper, AccountManagerBean item) {
            GlideUtils.loadHead(context, (ImageView) helper.getView(R.id.iv_head), item.getHeadImage());
            helper.setText(R.id.tv_name, item.getNickName());
            helper.setText(R.id.tv_phone, PhoneNumberUtil.getSecretPhoneNumber(item.getPhone()));
            if (item.getAuthStatus() == 2) {
                helper.setText(R.id.tv_auth, "已账户认证");
                helper.setTextColor(R.id.tv_auth, Color.parseColor("#C6C6C6"));
            } else {
                helper.setText(R.id.tv_auth, "未账户认证");
                helper.setTextColor(R.id.tv_auth, Color.parseColor("#DCB982"));
            }

            helper.setVisible(R.id.tv_ing, helper.getAdapterPosition() == 0);


        }
    }

}
