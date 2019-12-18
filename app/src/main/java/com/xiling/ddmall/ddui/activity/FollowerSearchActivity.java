package com.xiling.ddmall.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.fragment.FollowerSearchFragment;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/12/21
 * 粉丝搜索
 */
public class FollowerSearchActivity extends BaseActivity {

    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.fl_content)
    FrameLayout flContent;

    private FollowerSearchFragment mFollowerSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_search);
        ButterKnife.bind(this);
        KeyboardUtils.showSoftInput(etSearch);
        initView();
    }

    private void initView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mFollowerSearchFragment = new FollowerSearchFragment();
        transaction.add(R.id.fl_content, mFollowerSearchFragment);
        transaction.commit();
    }

    @OnClick(R.id.tv_search)
    public void onSearchClicked() {
        if (TextUtils.isEmpty(etSearch.getText())) {
            ToastUtil.error("请先输入要搜索的用户昵称");
            return;
        }
        KeyboardUtils.hideSoftInput(this);
        mFollowerSearchFragment.search(etSearch.getText().toString());
    }

    @OnClick(R.id.iv_back)
    public void onFinishClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hideSoftInput(this);
    }
}
