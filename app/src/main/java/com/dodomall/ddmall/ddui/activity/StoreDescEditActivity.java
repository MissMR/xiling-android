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
 * 店铺公告编辑页面
 */
public class StoreDescEditActivity extends BaseActivity {

    @BindView(R.id.et_store_desc)
    EditText etStoreDesc;
    @BindView(R.id.tv_btn_save)
    TextView tvBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_desc_edit);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        showHeader("设置店铺公告");
        tvBtnSave.setEnabled(false);
        etStoreDesc.addTextChangedListener(new TextWatcher() {
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
        etStoreDesc.setText(getIntent().getStringExtra(Constants.Extras.CONTENT));
    }

    @OnClick({R.id.iv_clear, R.id.tv_btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                etStoreDesc.setText("");
                break;
            case R.id.tv_btn_save:
                saveStoreDesc();
                break;
        }
    }

    private void saveStoreDesc() {
        APIManager.startRequest(ServiceManager.getInstance().createService(IStoreService.class).editStoreDesc(etStoreDesc.getText().toString()),
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
