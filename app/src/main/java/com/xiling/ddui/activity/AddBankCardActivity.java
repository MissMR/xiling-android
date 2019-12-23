package com.xiling.ddui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.xiling.R;
import com.xiling.ddui.bean.UIEvent;
import com.xiling.ddui.bean.UserAuthBean;
import com.xiling.ddui.tools.DLog;
import com.xiling.module.auth.model.CardItemModel;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.dialog.DDMDialog;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.OptionsPickerDialogManage;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBankCardActivity extends BaseActivity {

    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.et_bank_card_number)
    EditText etBankCardNumber;
    @BindView(R.id.et_bank_address)
    EditText etBankAddress;
    @BindView(R.id.tv_btn_next)
    TextView tvBtnNext;


    private IUserService mIUserService;
    private UserAuthBean mUserAuthBean;
    private List<CardItemModel> mList;

    private String mSelectBankId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mUserAuthBean = (UserAuthBean) getIntent().getSerializableExtra(Constants.Extras.USER_AUTH);
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
        mList = new ArrayList<>();
        getBankList();
    }

    private void getBankList() {
        APIManager.startRequest(mIUserService.getBankList(), new BaseRequestListener<List<CardItemModel>>(this) {
            @Override
            public void onSuccess(List<CardItemModel> result) {
                super.onSuccess(result);
                mList.clear();
                mList.addAll(result);
            }
        });
    }

    private void initView() {
        tvUserName.setText(mUserAuthBean.getSecretUserName());
        if (mUserAuthBean.isBindedBankCard()) {
            showHeader("修改银行卡", true);
            mSelectBankId = String.valueOf(mUserAuthBean.getBankId());
            tvBankName.setText(mUserAuthBean.getBankName());
            etBankCardNumber.setText(mUserAuthBean.getBankAccount());
            etBankCardNumber.setSelection(etBankCardNumber.getText().length());
            etBankAddress.setText(mUserAuthBean.getBankcardAddress());
        } else {
            showHeader("添加银行卡", true);
        }
    }

    private void showBankSelector() {
        OptionsPickerView<CardItemModel> optionView = OptionsPickerDialogManage.getOptionsDialog(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                DLog.i("options1:" + options1 + ",options2:" + options2 + ",options3：" + options3);
                mSelectBankId = mList.get(options1).bankId;
                tvBankName.setText(mList.get(options1).bankName);
            }
        });
        optionView.setPicker(mList);
        optionView.show();
    }

    @OnClick({R.id.tv_bank_name, R.id.iv_clear, R.id.tv_btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bank_name:
                showBankSelector();
                break;
            case R.id.iv_clear:
                etBankCardNumber.setText("");
                break;
            case R.id.tv_btn_next:
                if (etBankCardNumber.getText().length() < 16) {
                    ToastUtil.error("请核实您的银行卡卡号");
                    break;
                }
                new DDMDialog(this)
                        .setTitle("请确认您的填写信息")
                        .setContent(getEditedBankCardInfo())
                        .setContextGravity(Gravity.CENTER)
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateBankCardInfo();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .enableClose(false)
                        .show();
                break;
        }
    }

    private void updateBankCardInfo() {
        APIManager.startRequest(mIUserService.bindBankCard(mSelectBankId, etBankCardNumber.getText().toString(), etBankAddress.getText().toString()),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        if (mUserAuthBean.isBindedBankCard()) {
                            ToastUtil.success("修改成功");
                        } else {
                            ToastUtil.success("您已成功绑定银行卡");
                        }

                        UIEvent event = new UIEvent();
                        event.setType(UIEvent.Type.CloseBindCardActivity);
                        EventBus.getDefault().post(event);

                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    private String getEditedBankCardInfo() {
        return new StringBuffer(tvBankName.getText().toString())
                .append("\n")
                .append(etBankCardNumber.getText().toString())
                .append("\n")
                .append(etBankAddress.getText().toString())
                .toString();
    }

}
