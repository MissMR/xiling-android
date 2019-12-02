package com.dodomall.ddmall.ddui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.service.IStoreService;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2019/3/26
 * 店铺昵称设置
 */
public class StoreNameEditActivity extends BaseActivity {

    @BindView(R.id.et_store_name)
    EditText etStoreName;
    @BindView(R.id.tv_btn_save)
    TextView tvBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_name_edit);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        showHeader("设置店铺昵称");
        tvBtnSave.setEnabled(false);
        etStoreName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvBtnSave.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etStoreName.setText(getIntent().getStringExtra(Constants.Extras.CONTENT));
    }

    @OnClick({R.id.iv_clear, R.id.tv_btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                etStoreName.setText("");
                break;
            case R.id.tv_btn_save:
                saveStoreName();
                break;
        }
    }

    private void saveStoreName() {
        APIManager.startRequest(ServiceManager.getInstance().createService(IStoreService.class)
                        .editStoreName(etStoreName.getText().toString()),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

}
