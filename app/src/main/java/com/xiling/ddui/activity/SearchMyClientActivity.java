package com.xiling.ddui.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.fragment.MyClientFragment;
import com.xiling.shared.basic.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchMyClientActivity extends BaseActivity {

    @BindView(R.id.keywordEt)
    TextView keywordEt;
    @BindView(R.id.layout_frame)
    FrameLayout layoutFrame;
    @BindView(R.id.cleanBtn)
    ImageView cleanBtn;

    private String searchString = "";
    MyClientFragment myClientFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_my_client);
        ButterKnife.bind(this);
        myClientFragment = new MyClientFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.layout_frame, myClientFragment);
        transaction.commit();


        keywordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() > 0) {
                    cleanBtn.setVisibility(View.VISIBLE);
                } else {
                    cleanBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        keywordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    searchString = textView.getText().toString();
                    myClientFragment.getCustomerList("",searchString);
                    return true;

                }
                return false;
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.cleanBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.cleanBtn:
                keywordEt.setText("");
                break;
        }
    }
}
