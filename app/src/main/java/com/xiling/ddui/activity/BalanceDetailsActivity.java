package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.bean.BalanceTypeBean;
import com.xiling.ddui.fragment.BalanceDetailsFragment;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 逄涛
 * 余额明细
 */
public class BalanceDetailsActivity extends BaseActivity {
    INewUserService iNewUserService;

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_order)
    ViewPager viewpagerOrder;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> childNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_details);
        ButterKnife.bind(this);
        setTitle("余额明细");
        setLeftBlack();
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        getBalanceTypeList();
    }


    private void getBalanceTypeList() {
        APIManager.startRequest(iNewUserService.getBalanceType(), new BaseRequestListener<List<BalanceTypeBean>>() {

            @Override
            public void onSuccess(List<BalanceTypeBean> result) {
                super.onSuccess(result);
                if (result != null) {
                    childNames.clear();
                    fragments.clear();
                    for (BalanceTypeBean balanceTypeBean : result) {
                        childNames.add(balanceTypeBean.getValue());
                        fragments.add(BalanceDetailsFragment.newInstance(balanceTypeBean.getCode()));
                    }
                    slidingTab.setViewPager(viewpagerOrder, childNames.toArray(new String[childNames.size()]), BalanceDetailsActivity.this, fragments);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }

        });
    }

}
